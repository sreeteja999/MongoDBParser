import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                Object parse = jsonParser.parse(s);
                if(parse instanceof JSONObject){
                    JSONObject jsonObject = (JSONObject) parse;
                    componentparse(jsonObject,printWriter);
                try {
                    //parse json converted document
                    Object parse = jsonParser.parse(s);
                    if (parse instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(s); //check type of JSONObject and typecast it
                        componentparse(jsonObject, printWriter);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            BufferedReader br = new BufferedReader(new FileReader("Mongofile.csv"));
            BufferedReader br1 = new BufferedReader(new FileReader("C:/Users/Dornala/Desktop/samplefile1.csv"));
            String line;
            String line1, qid;
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
            HashMap<String, String> qidToXpathMapMultiple = new HashMap<String, String>();

            while ((line = br1.readLine()) != null) {
                String[] split = line.split(",");
                if (!hashMap.containsKey(split[0])) {
                    hashMap.put(split[0], split[2]);
                } else {
                    Object o = hashMap.get(split[0]);
                    if (o instanceof ArrayList) {
                        ArrayList o1 = (ArrayList) o;
                        o1.add(split[2]);
                        hashMap.put(split[0], o1);
                    } else if (o instanceof String) {
                        ArrayList list = new ArrayList();
                        list.add(o);
                        list.add(split[2]);
                        hashMap.put(split[0], list);
                    }
                }
            }

            //Read every line in mongo file and keep qid & xpath in a separate hashmap - Good
            while ((line1 = br.readLine()) != null) {
                String[] split1 = line1.split(",");
                if (split1.length >= 3) {
                    qidToXpathMapMultiple.put(split1[0], split1[2].replace("[#n]", "#n"));
                }
            }

            // compare both hashmaps and find respective qid for xpath


            for (String key : hashMap.keySet()) {
                System.out.println(key);
                for (String key1 : qidToXpathMapMultiple.keySet()) {
                    if (qidToXpathMapMultiple.containsKey(key1)) {
                        String val = qidToXpathMapMultiple.get(key1);
                        if (key.equalsIgnoreCase(val)) {
                            hashMap1.put(key1, hashMap.get(key));
                        }
                    }

                }


            }
            System.out.println(hashMap1);
            br.close();
            br1.close();
            printWriter.close(); //close the file
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void componentparse(JSONObject component, PrintWriter printWriter) {
        if (component.containsKey("components")) {                                       //check if JSONobject contains key as 'components',if yes go inside loop
            Object componentobject = component.get("components");
            if (componentobject instanceof JSONArray) {                                   //check in components contain array of JSONobjects
                JSONArray componentobject1 = (JSONArray) componentobject;
                for (int i = 0; i < componentobject1.size(); i++) {                         //repeat loop for entire array of JSONobject
                    Object componentobj = componentobject1.get(i);
                    if (componentobj instanceof JSONObject) {
                        JSONObject componentobj1 = (JSONObject) componentobj;
                        String keyvalue = "", xpathvalue = "", typevalue = "";
                        if (componentobj1.containsKey("key")) {
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
                        if (componentobj1.containsKey("components")) {
                            //recursive function to repeat loop if we have components in components
                            componentparse(componentobj1, printWriter);
                        }

                    }
                }
            }
        }

    }
}

