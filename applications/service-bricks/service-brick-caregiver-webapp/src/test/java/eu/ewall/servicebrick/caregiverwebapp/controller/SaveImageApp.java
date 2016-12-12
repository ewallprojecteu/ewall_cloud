package eu.ewall.servicebrick.caregiverwebapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
 
import org.bson.types.Binary;
 
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
  
/**
 * Java MongoDB : Save image example
 * 
 */
  
public class SaveImageApp {
 
    public static void main(String[] args) 
    {
        SaveImageApp o = new SaveImageApp();
                 
        /** Stores image in a collection **/
        o.withoutUsingGridFS();
    }
 
    void withoutUsingGridFS() 
    {
        try {
  
            Mongo mongo = new Mongo("localhost", 27017);
            DB db = mongo.getDB("imagedb");
            DBCollection collection = db.getCollection("dummyColl");
             
            String filename = "/Users/baronepa/Pictures/Capture.png";
            String empname ="ABC";
             
            /** Inserts a record with name = empname and photo 
              *  specified by the filepath 
              **/
            insert(empname,filename,collection);
             
            String destfilename = "/Users/baronepa/Pictures/retrieved.jpg";
            /** Retrieves record where name = empname, including his photo. 
              * Retrieved photo is stored at location filename 
              **/
            retrieve(empname, destfilename, collection);
             
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        } 
    }
     
    void insert(String empname, String filename, DBCollection collection)
    {
        try
        {
            File imageFile = new File(filename);
            FileInputStream f = new FileInputStream(imageFile);
 
            byte b[] = new byte[f.available()];
            f.read(b);
 
            Binary data = new Binary(b);
            BasicDBObject o = new BasicDBObject();
            o.append("name",empname).append("photo",data);
            collection.insert(o);
            System.out.println("Inserted record.");
 
            f.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    void retrieve(String name, String filename, DBCollection collection)
    {
        byte c[];
        try
        {
            DBObject obj = collection.findOne(new BasicDBObject("name", name));
            String n = (String)obj.get("name");
            c = (byte[])obj.get("photo");
            FileOutputStream fout = new FileOutputStream(filename);
            fout.write(c);
            fout.flush();
            System.out.println("Photo of "+name+" retrieved and stored at "+filename);
            fout.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}