package hu.cloud.edu;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.ListDomainsResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;


public class Problem2SimpleDB {

    public static void main(String[] args) throws Exception {
        AmazonSimpleDB sdb = new AmazonSimpleDBClient();
		Region usEast1 = Region.getRegion(Regions.US_EAST_1);
		sdb.setRegion(usEast1);

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SimpleDB");
        System.out.println("===========================================\n");

        try {
            // Create a domain
            String myDomain = "People";
            
            if(deleteDomainIfExists(sdb,myDomain)){
            	System.out.println(myDomain + " domain already exists, deleting...");
            }
            
            System.out.println("Creating domain called " + myDomain + ".\n");
            sdb.createDomain(new CreateDomainRequest(myDomain));

            // List domains
            System.out.println("Listing all domains in your account:\n");
            for (String domainName : sdb.listDomains().getDomainNames()) {
                System.out.println("  " + domainName);
            }
            System.out.println();

            // Put data into a domain
            System.out.println("Putting data into " + myDomain + " domain.\n");
            sdb.batchPutAttributes(new BatchPutAttributesRequest(myDomain, createSampleData()));

            // Select data from a domain
            // Notice the use of backticks around the domain name in our select expression.
            String selectExpression = "select * from `" + myDomain + "`";
            System.out.println("Selecting: " + selectExpression + "\n");
            SelectRequest selectRequest = new SelectRequest(selectExpression);
            List<Item> items = sdb.select(selectRequest).getItems();
            
            printItems(items);
                       
            //Change the year one of the Nobel prizes was awarded
            System.out.println("Changing Year Won attribute for Angus-Deaton");
            
            ArrayList<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>();
            
            attributes.add(new ReplaceableAttribute("Year Won", "2016", true));
          
            PutAttributesRequest request = new PutAttributesRequest(myDomain,"Angus-Deaton",attributes);
            
            sdb.putAttributes(request);
                                   
            printItems(sdb.select(new SelectRequest("select * from `" + myDomain + "` where `First Name` = 'Angus'")).getItems());

            // Delete an item and all of its attributes
            System.out.println("Deleting Hugh-Jackman");
            sdb.deleteAttributes(new DeleteAttributesRequest(myDomain, "Hugh-Jackman"));
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon SimpleDB, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with SimpleDB, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
    
    private static boolean deleteDomainIfExists(AmazonSimpleDB sdb,String domain){
    	ListDomainsResult domains = sdb.listDomains();
    	
    	if(domains.getDomainNames().contains(domain)){
    		sdb.deleteDomain(new DeleteDomainRequest(domain));
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    
    private static void printItems(List<Item> items){
    	for (Item item : items) {
            System.out.println("  Item");
            System.out.println("    Name: " + item.getName());
            for (Attribute attribute : item.getAttributes()) {
                System.out.println("      Attribute");
                System.out.println("        Name:  " + attribute.getName());
                System.out.println("        Value: " + attribute.getValue());
            }
        }
    }
    
    /* Creates an array of SimpleDB ReplaceableItems populated with sample data. */
    private static List<ReplaceableItem> createSampleData() {
        List<ReplaceableItem> sampleData = new ArrayList<ReplaceableItem>();

        //Create Person objects and immediately add them to the sample dataset
        sampleData.add(new Person("Hugh",
        							"Jackman",
        							"The Prestige",
        							"https://s3.amazonaws.com/e90-isteiner-people/stars/images/hughJackman.jpg",
        							"https://s3.amazonaws.com/e90-isteiner-people/stars/resumes/jackmanResume.docx")
        					.getReplaceableItem());
        
        sampleData.add(new Person("Ian",
				"Mckellen",
				"The Lord of the Rings: The Fellowship of the Ring",
				"https://s3.amazonaws.com/e90-isteiner-people/stars/images/ianMckellen.jpg",
				"https://s3.amazonaws.com/e90-isteiner-people/stars/resumes/mckellenResume.docx")
		.getReplaceableItem());
        
        sampleData.add(new Person("Tuppence",
				"Middleton",
				"The Imitation Game",
				"https://s3.amazonaws.com/e90-isteiner-people/stars/images/tuppenceMiddleton.jpg",
				"https://s3.amazonaws.com/e90-isteiner-people/stars/resumes/middletonResume.docx")
		.getReplaceableItem());
        
        sampleData.add(new NobelLaureate("Angus",
				"Deaton",
				"The Terminator",
				"https://s3.amazonaws.com/e90-isteiner-people/nobels/images/angusDeaton.jpg",
				"https://s3.amazonaws.com/e90-isteiner-people/nobels/resumes/deatonResume.docx",
				2015,
				"Economic Sciences")
		.getReplaceableItem());
        
        sampleData.add(new NobelLaureate("Kajita",
				"Takaaki",
				"The Sound of Music",
				"https://s3.amazonaws.com/e90-isteiner-people/nobels/images/kajitaTakaaki.jpg",
				"https://s3.amazonaws.com/e90-isteiner-people/nobels/resumes/takaakiResume.docx",
				2015,
				"Physics")
		.getReplaceableItem());
        
        sampleData.add(new NobelLaureate("Svetlana",
				"Alexievich",
				"The Great Escape",
				"https://s3.amazonaws.com/e90-isteiner-people/nobels/images/svetlanaAlexievich.jpg",
				"https://s3.amazonaws.com/e90-isteiner-people/nobels/resumes/alexievichResume.docx",
				2015,
				"Literature")
		.getReplaceableItem());
        
        return sampleData;
    }
}