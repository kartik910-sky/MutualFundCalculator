package com.kartik.fund;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MutualFundProfitCalculator {
	
	private static final String API_BASE_URL = "https://api.mfapi.in/mf/";

    public static double calculateProfit(String schemeCode, String startDate, String endDate, double capital) {
        try {
            // Step 1: Get NAV on both purchase and redemption dates
            double navOnPurchaseDate = getNAV(schemeCode, startDate);
            double navOnRedemptionDate = getNAV(schemeCode, endDate);

            // Step 2: Calculate the number of units allotted on the purchase date
            double unitsAllotted = capital / navOnPurchaseDate;

            // Step 3: Calculate the value of units on the redemption date
            double valueOnRedemptionDate = unitsAllotted * navOnRedemptionDate;

            // Step 4: Calculate the net profit
            double netProfit = valueOnRedemptionDate - capital;

            // Print the results
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            System.out.println("Mutual Fund Scheme Code: " + schemeCode);
            System.out.println("Start Date: " + startDate);
            System.out.println("End Date: " + endDate);
            System.out.println("Initial Investment: " + capital);
            System.out.println("NAV on Purchase Date: " + decimalFormat.format(navOnPurchaseDate));
            System.out.println("NAV on Redemption Date: " + decimalFormat.format(navOnRedemptionDate));
            System.out.println("Number of Units Allotted: " + decimalFormat.format(unitsAllotted));
            System.out.println("Value on Redemption Date: " + decimalFormat.format(valueOnRedemptionDate));
            System.out.println("Net Profit: " + decimalFormat.format(netProfit));

            return netProfit;
        } catch (Exception e) {
            System.out.println("Error calculating mutual fund profit: " + e.getMessage());
            return 0.0;
        }
    }

    private static double getNAV(String schemeCode, String date) throws IOException, ParseException {
        URL url = new URL(API_BASE_URL + schemeCode);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        // Parse JSON response to get NAV on the specified date
        String navString = response.toString().split("\"date\":\"" + date + "\",\"nav\":\"")[1].split("\"")[0];
        return Double.parseDouble(navString);
    }
	    
	public static void main(String[] args) {
		
		calculateProfit("101206", "26-07-2023", "18-10-2023", 1000000.0);
		
		
	}

}
