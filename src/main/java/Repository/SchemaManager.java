package Repository;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class SchemaManager {

    public static void accessSchema(String schemaName, String username){
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter schema name: ");
            String schemaFileName = "src/main/resources/Schemas/" + schemaName + "_" + username + ".json";

            File schemaFile = new File(schemaFileName);

            //If schema exists then get data or else create new schema
            if(schemaFile.exists()){
                System.out.println("Schema exists");
            } else {
                System.out.println("Created new schema.");
                schemaFile.createNewFile();
                //Write updated data back to JSON file
                try (FileWriter updatedFile = new FileWriter(schemaFileName)) {
                    updatedFile.write("{}");
//                    return true
                }
            }

        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}
