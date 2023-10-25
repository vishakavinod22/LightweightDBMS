package Repository;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class SchemaManager {

    public static void accessSchema(String schemaName, String username){
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter schema name: ");
            String schemaFolderName = "src/main/resources/Schemas/" + schemaName + "_" + username;
            String schemaFileName = schemaFolderName + "/metaData.txt";

            File schemaFolder = new File(schemaFolderName);
            File schemaFile = new File(schemaFileName);

            //If schema exists then get data or else create new schema
            if(schemaFile.exists()){
                System.out.println("Schema exists");
            } else {
                System.out.println("Created new schema.");
                schemaFolder.mkdirs();
                schemaFile.createNewFile();
            }

        }
        catch (Exception e){
            System.out.println(e);
        }
    }

//    public static void main(String[] args) {
//        accessSchema("schemaName", "username");
//    }

}
