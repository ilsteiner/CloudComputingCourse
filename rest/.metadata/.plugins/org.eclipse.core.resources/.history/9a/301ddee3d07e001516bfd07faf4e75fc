package com.rest.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomerResourceClient {
	public static void main(String[] args) {
		try {
			int numCustomers = 4;
			ArrayList<String> names = new ArrayList<>(Arrays.asList("Bob","Susan","Joe","Sam","Peter"));
			
			System.out.println("*** Create " + numCustomers + " new Customers ***");
				
			for(int i=0;i<numCustomers;i++){
				URL postUrl = new URL("http://10.0.2.34:8080/rest/customers");
				HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
				System.out.println("opened connection...");
				connection.setDoOutput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/xml");
				
				System.out.println("*** Create a new Customer named " + names.get(i) + "***");
				String newCustomer = asXML(names.get(i), "Smith", i * 11 + " Main Street", "Boston", "MA", "02115");
				OutputStream os = connection.getOutputStream();
				os.write(newCustomer.getBytes());
				os.flush();
				System.out.println(HttpURLConnection.HTTP_CREATED);
				System.out.println(connection.getResponseCode());
				System.out.println("Location: " + connection.getHeaderField("Location"));
				
				connection.disconnect();
			}
			
			System.out.println("Starting with customer id 2, print 2 customers");
			
			URL getUrl = new URL("http://10.0.2.34:8080/rest/customers/2?count=2");
			HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
			connection.setRequestMethod("GET");
			System.out.println("Content-Type: " + connection.getContentType());

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String line = reader.readLine();
			while (line != null) {
				System.out.println(line);
				line = reader.readLine();
			}
			System.out.println(HttpURLConnection.HTTP_OK);
			System.out.println(connection.getResponseCode());
			connection.disconnect();
			
			System.out.println("Starting with customer id 1, print 4 customers");
			
			getUrl = new URL("http://10.0.2.34:8080/rest/customers/1?count=4");
			connection = (HttpURLConnection) getUrl.openConnection();
			connection.setRequestMethod("GET");
			System.out.println("Content-Type: " + connection.getContentType());

			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			line = reader.readLine();
			while (line != null) {
				System.out.println(line);
				line = reader.readLine();
			}
			System.out.println(HttpURLConnection.HTTP_OK);
			System.out.println(connection.getResponseCode());
			connection.disconnect();
			
			System.out.println("Delete customer 3");
			
			URL deleteURL = new URL("http://10.0.2.34:8080/rest/customers/3");
			connection = (HttpURLConnection) deleteURL.openConnection();
			connection.setRequestMethod("DELETE");
			System.out.println(HttpURLConnection.HTTP_OK);
			System.out.println(connection.getResponseCode());
			System.out.println("Content-Type: " + connection.getContentType());
			connection.disconnect();
			
			System.out.println("Show that customer 3 is now gone");
			
			try {
				getUrl = new URL("http://10.0.2.34:8080/rest/customers/3");
				connection = (HttpURLConnection) getUrl.openConnection();
				connection.setRequestMethod("GET");
				System.out.println("Content-Type: " + connection.getContentType());

				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				line = reader.readLine();
				while (line != null) {
					System.out.println(line);
					line = reader.readLine();
				}
				System.out.println(HttpURLConnection.HTTP_OK);
				System.out.println(connection.getResponseCode());
				connection.disconnect();
			} catch (FileNotFoundException e) {
				System.out.println("It doesn't exist anymore, so the get threw an error.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String asXML(String firstName,String lastName,String street,String city,String state,String zip){
		return "<customer>" + 
				"<first-name>" +
					firstName +
				"</first-name>" +
				"<last-name>" +
					lastName +
				"</last-name>" +
				"<street>" +
					street +
				 "</street>" +
				 "<city>" +
				 	city +
				 "</city>" +
				 "<state>" +
				 	state +
				 "</state>" +
				 "<zip>" +
				 	zip +
				 "</zip>" +
				 "<country>USA</country>" +
				 "</customer>";
	}
}
