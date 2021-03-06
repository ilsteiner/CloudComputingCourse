package hwk9_edu.edu.hu.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientSideLoadTester {
	private URL loadBalancer;
	private HashMap<String, Integer> ipAddresses;
	
	public ClientSideLoadTester(String loadBalancerURL) throws MalformedURLException {
		this.loadBalancer = new URL(loadBalancerURL);
		this.ipAddresses = new HashMap<String, Integer>();
	}
	
	public String testLoad() throws IOException{
			HttpURLConnection connection;
			String response = "";
			
			connection = (HttpURLConnection) loadBalancer.openConnection();
			connection.setRequestMethod("GET");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
//			Since we know the EXACT structure of the HTML page, we'll use a VERY naive parser
			for(int i=0;i<8;i++){
				reader.readLine();
			}
			
			response = reader.readLine().replaceAll("([<-?/][h][1][>])|[\t]|[<]", "");
			
			if(ipAddresses.containsKey(response)){
				int count = ipAddresses.get(response);
				ipAddresses.put(response, ++count);
			}
			else{
				ipAddresses.put(response, 1);
			}
			
			connection.disconnect();
			
			System.out.println("Contacted " + response);
			
			return response;
	}
	
	public List<String> testLoad(int count) throws IOException{
		List<String> ipStrings = new ArrayList<String>();
		
		for(int i=0;i<count;i++){
			ipStrings.add(testLoad());
		}
		
		return ipStrings;
	}

	public List<String> getIpCounts(){
		List<String> ipStrings = new ArrayList<String>();
		
		for(String ip:ipAddresses.keySet()){
			ipStrings.add(ip + " had " + ipAddresses.get(ip) + " contacts");
		}
		
		return ipStrings;
	}
	
	public void printIpCounts(){
		for(String ipString:getIpCounts()){
			System.out.println(ipString);
		}
	}
}
