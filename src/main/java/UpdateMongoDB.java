import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.PrintWriter;
import java.lang.reflect.Array;

public class UpdateMongoDB {

    public static void main(String[] args) {
        // Create instance of mongoclient
        MongoClient mongoClient = new MongoClient("localhost:27017");
        // Create instance of mongodatabase through mongoclient
        MongoDatabase mongoDatabase = mongoClient.getDatabase("formioapp");
        // Create instance of Collection we want to perform operation in mongoDatabase
        MongoCollection mongoCollection = mongoDatabase.getCollection("forms");
        try {
            PrintWriter printWriter = new PrintWriter("Mongofile.csv");

        // Create mongoCursor to acces multiple records in mongoCollection
        MongoCursor<Document> mongoCursor = mongoCollection.find().iterator();
        // Repeat loop till last document
        while (mongoCursor.hasNext()) {
            // Take next document
            Document document = mongoCursor.next();
            // Convert document to json and store in local variable
            String s = document.toJson();
            // Create instance of JSONParser
            JSONParser jsonParser = new JSONParser();
            /**/
            try
            {
                //parse json converted document
                Object parse = jsonParser.parse(s);
                if(parse instanceof JSONObject){
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(s); //check type of JSONObject and typecast it
                    componentparse(jsonObject,printWriter);
                }
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        printWriter.close(); //close the file
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public static void componentparse(JSONObject component,PrintWriter printWriter) {
        if(component.containsKey("components")){                                       //check if JSONobject contains key as 'components',if yes go inside loop
            Object componentobject =  component.get("components");
            if(componentobject instanceof JSONArray){                                   //check in components contain array of JSONobjects
                JSONArray componentobject1 = (JSONArray) componentobject;
                for(int i=0 ; i < componentobject1.size();i++){                         //repeat loop for entire array of JSONobject
                    Object componentobj=componentobject1.get(i);
                    if(componentobj instanceof JSONObject){
                        JSONObject componentobj1 = (JSONObject) componentobj;
                        String keyvalue = "",xpathvalue = "",typevalue = "";
                        if(componentobj1.containsKey("key")) {
                            if (componentobj1.get("key").toString().contains("qid")) {
                                keyvalue = (String) (componentobj1.get("key"));         //get keyvalue of key 'key' and save it to local variable by changing type to string
                                if (componentobj1.containsKey("type")) {
                                    if (componentobj1.get("type").equals("select")) {   //check key 'type' and save corresponding keyvalue to local variable type value
                                        if (componentobj1.get("multiple").equals(true)) {
                                            typevalue = "Multiple";
                                        } else {
                                            typevalue = "Single";
                                        }
                                    } else if (componentobj1.get("type").equals("textfield")) {
                                        typevalue = componentobj1.get("type").toString();
                                    } else
                                        break;
                                }
                                if (componentobj1.containsKey("properties")) {
                                    Object properties = componentobj1.get("properties");
                                    if (properties instanceof JSONObject) {
                                        JSONObject properties1 = (JSONObject) properties;
                                        if (properties1.containsKey("xpath")) {
                                            xpathvalue = (String) (properties1.get("xpath"));
                                        }
                                    }
                                }
                                printWriter.println(keyvalue + "," + typevalue + "," + xpathvalue);
                            }
                        }
                        if(componentobj1.containsKey("components")){
                            //recursive function to repeat loop if we have components in components
                            componentparse(componentobj1, printWriter);
                        }

                    }
                }
            }
        }

    }
}

































//            try {
//                // Parse String
//                Object parse = jsonParser.parse(s);
//                if(parse instanceof JSONObject){   // Check parsed string is JSON or not
//                    JSONObject jsonobject = (JSONObject) jsonParser.parse(s); // Typecast parsed string to JSON object
//                   // readJSON(jsonobject);
//                    updateJson(jsonobject);
//                    readJSON(jsonobject);
//                }
//
//            } catch (Exception e){
//                System.out.println(e);
//            }
//
//            String string = "name: \"Sreeteja\", fathername: \"ram\"";
//           // System.out.println(string);
//        }
//    }
    // Read JSON
//    public static JSONObject readJSON(JSONObject jsonObject){
//        if(jsonObject.containsKey("date")){ // Search document based in key
//            System.out.println(jsonObject.get("date")); // Print record based on Key
//        }
//        return jsonObject;
//    }
//    // Update JSON
//    public static JSONObject updateJson(JSONObject jsonObject){
//        if(jsonObject.containsKey("father")){
//            jsonObject.put("father","Ramu");
//        }
//        if(jsonObject.containsKey("mother")){
//            jsonObject.put("mother","Mouni");
//        }
//        return jsonObject;
//    }
//    // Update record in MongoDB
//    public static void updateMongoDB(){
////
//  }
//}


