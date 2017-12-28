import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class UpdateMongoDB {

    public static void main(String[] args) {
        // Create instance of mongoclient
        MongoClient mongoClient = new MongoClient("localhost:27017") ;
        // Create instance of mongodatabase through mongoclient
        MongoDatabase mongoDatabase = mongoClient.getDatabase("sree");
        // Create instance of Collection we want to perform operation in mongoDatabase
        MongoCollection mongoCollection = mongoDatabase.getCollection("details");

        // Create mongoCursor to acces multiple records in mongoCollection
        MongoCursor<Document> mongoCursor = mongoCollection.find().iterator();
        // Repeat loop till last document
        while (mongoCursor.hasNext())
        {
            // Take next document
            Document document = mongoCursor.next();
            // Convert document to json and store in local variable
            String s = document.toJson();
            // Create instance of JSONParser
            JSONParser jsonParser = new JSONParser();
            /**/

            try {
                // Parse String
                Object parse = jsonParser.parse(s);
                if(parse instanceof JSONObject){   // Check parsed string is JSON or not
                    JSONObject jsonobject = (JSONObject) jsonParser.parse(s); // Typecast parsed string to JSON object
                   // readJSON(jsonobject);
                    updateJson(jsonobject);
                    readJSON(jsonobject);
                }

            } catch (Exception e){
                System.out.println(e);
            }

            String string = "name: \"Sreeteja\", fathername: \"ram\"";
           // System.out.println(string);
        }
    }
    // Read JSON
    public static JSONObject readJSON(JSONObject jsonObject){
        if(jsonObject.containsKey("father")){ // Search document based in key
            System.out.println(jsonObject.get("father")); // Print record based on Key
        }
        return jsonObject;
    }
    // Update JSON
    public static JSONObject updateJson(JSONObject jsonObject){
        if(jsonObject.containsKey("father")){
            jsonObject.put("father","Ramu");
        }
        if(jsonObject.containsKey("mother")){
            jsonObject.put("mother","Mouni");
        }
        return jsonObject;
    }
    // Update record in MongoDB
    public static void updateMongoDB(){

    }
}


