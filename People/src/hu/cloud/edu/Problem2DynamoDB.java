package hu.cloud.edu;

import java.util.ArrayList;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.Tables;

/**
 * This sample demonstrates how to perform a few simple operations with the
 * Amazon DynamoDB service.
 */
public class Problem2DynamoDB {

    /*
     * Before running the code:
     *      Fill in your AWS access credentials in the provided credentials
     *      file template, and be sure to move the file to the default location
     *      (C:\\Users\\073621\\.aws\\credentials) where the sample code will load the
     *      credentials from.
     *      https://console.aws.amazon.com/iam/home?#security_credential
     *
     * WARNING:
     *      To avoid accidental leakage of your credentials, DO NOT keep
     *      the credentials file in your source directory.
     */

    static AmazonDynamoDBClient dynamoDB;

    /**
     * The only information needed to create a client are security credentials
     * consisting of the AWS Access Key ID and Secret Access Key. All other
     * configuration, such as the service endpoints, are performed
     * automatically. Client parameters, such as proxies, can be specified in an
     * optional ClientConfiguration object when constructing a client.
     *
     * @see com.amazonaws.auth.BasicAWSCredentials
     * @see com.amazonaws.auth.ProfilesConfigFile
     * @see com.amazonaws.ClientConfiguration
     */
    private static void init() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [ZoranJavaDSDK]
         * credential profile by reading from the credentials file located at
         * (C:\\Users\\073621\\.aws\\credentials).
         */
        //AWSCredentials credentials = null;
        try {
            //credentials = new ProfileCredentialsProvider("ZoranJavaDSDK").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\073621\\.aws\\credentials), and is in valid format.",
                    e);
        }
        dynamoDB = new AmazonDynamoDBClient();
        Region usWest2 = Region.getRegion(Regions.US_EAST_1);
        dynamoDB.setRegion(usWest2);
    }

    public static void main(String[] args) throws Exception {
        init();

        try {
            String tableName = "CELEBRITIES_SDK";

            // Create table if it does not exist yet
            if (Tables.doesTableExist(dynamoDB, tableName)) {
                System.out.println("Table " + tableName + " is already ACTIVE");
            } else {            	
                // Create a table with a primary hash key named 'name', which holds a string
                /*CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
                    .withKeySchema(new KeySchemaElement().withAttributeName("name").withKeyType(KeyType.HASH))
                    .withAttributeDefinitions(new AttributeDefinition().withAttributeName("name").withAttributeType(ScalarAttributeType.S))
                    .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
                    TableDescription createdTableDescription = dynamoDB.createTable(createTableRequest).getTableDescription();
                System.out.println("Created Table: " + createdTableDescription);

                // Wait for it to become active
                System.out.println("Waiting for " + tableName + " to become ACTIVE...");
                Tables.awaitTableToBecomeActive(dynamoDB, tableName);*/
            }

            // Describe our new table
            DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
            TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
            System.out.println("Table Description: " + tableDescription);

            // Add the items
            for (Map<String, AttributeValue> item : createSampleDate()) {
            	System.out.println("Putting item: " + item.get("name").toString());
            	PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
                PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
                System.out.println("Result: " + putItemResult);
			}
            
            ScanRequest consoleScan = new ScanRequest("CELEBRITIES_CONSOLE");
            ScanRequest sdkScan = new ScanRequest("CELEBRITIES_SDK");
            
            System.out.println("See what's in the console table:");
            System.out.println(dynamoDB.scan(consoleScan));
            
            System.out.println("See what's in the SDK table:");
            System.out.println(dynamoDB.scan(sdkScan));
            
            System.out.println("Change the year for Angus Deaton");  
                        
            UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            		.withPrimaryKey("name","Angus-Deaton")
            		.withUpdateExpression("set #y = :val")
            		.withNameMap(new NameMap().with("#y", "Year Won"))
            		.withValueMap(new ValueMap().withNumber(":val", 2016));
            
            new DynamoDB(dynamoDB).getTable(tableName).updateItem(updateItemSpec);
            
            System.out.println("See the changes in the SDK table:");
            System.out.println(dynamoDB.scan(sdkScan));
            		
/*            // Scan items for movies with a year attribute greater than 1985
            HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
            Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.GT.toString())
                .withAttributeValueList(new AttributeValue().withN("1985"));
            scanFilter.put("year", condition);
            ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
            ScanResult scanResult = dynamoDB.scan(scanRequest);
            System.out.println("Result: " + scanResult);*/

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to AWS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with AWS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
    
    public static ArrayList<Map<String, AttributeValue>> createSampleDate(){
    	ArrayList<Person> people = new ArrayList<>();
    	ArrayList<Map<String, AttributeValue>> list = new ArrayList<>();
    	
    	people.add(new Person("Hugh",
				"Jackman",
				"The Prestige",
				"https://s3.amazonaws.com/e90-isteiner-people/stars/images/hughJackman.jpg",
				"https://s3.amazonaws.com/e90-isteiner-people/stars/resumes/jackmanResume.docx"));
    	
    	people.add(new Person("Ian",
				"Mckellen",
				"The Lord of the Rings: The Fellowship of the Ring",
				"https://s3.amazonaws.com/e90-isteiner-people/stars/images/ianMckellen.jpg",
				"https://s3.amazonaws.com/e90-isteiner-people/stars/resumes/mckellenResume.docx"));
    	
    	people.add(new Person("Tuppence",
				"Middleton",
				"The Imitation Game",
				"https://s3.amazonaws.com/e90-isteiner-people/stars/images/tuppenceMiddleton.jpg",
				"https://s3.amazonaws.com/e90-isteiner-people/stars/resumes/middletonResume.docx"));
    	
    	people.add(new NobelLaureate("Angus",
				"Deaton",
				"The Terminator",
				"https://s3.amazonaws.com/e90-isteiner-people/nobels/images/angusDeaton.jpg",
				"https://s3.amazonaws.com/e90-isteiner-people/nobels/resumes/deatonResume.docx",
				2015,
				"Economic Sciences"));
    	
    	people.add(new NobelLaureate("Kajita",
				"Takaaki",
				"The Sound of Music",
				"https://s3.amazonaws.com/e90-isteiner-people/nobels/images/kajitaTakaaki.jpg",
				"https://s3.amazonaws.com/e90-isteiner-people/nobels/resumes/takaakiResume.docx",
				2015,
				"Physics"));
    	
    	people.add(new NobelLaureate("Svetlana",
				"Alexievich",
				"The Great Escape",
				"https://s3.amazonaws.com/e90-isteiner-people/nobels/images/svetlanaAlexievich.jpg",
				"https://s3.amazonaws.com/e90-isteiner-people/nobels/resumes/alexievichResume.docx",
				2015,
				"Literature"));
    	
    	for (Person person : people) {
			list.add(person.getAsItem());
		}
    	
    	return list;
    }

}