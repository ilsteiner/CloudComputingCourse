package hwk9_edu.edu.hu.client;

import java.io.IOException;
import java.net.MalformedURLException;

public class TestLoad {
	public static void main(String[] args) {
		try {
			ClientSideLoadTester tester = new ClientSideLoadTester("http://homework9-339199583.us-east-1.elb.amazonaws.com");
			
			tester.testLoad(1000);
			
			tester.printIpCounts();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
