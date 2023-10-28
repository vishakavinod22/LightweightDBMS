package Repository;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * <h1>Schema Manager</h1>
 * This class provides methods for managing user Schemas
 *
 * How Schema works:
 *      In a real database, the user only has access to their schemas and the tables within that schema.
 *      In this program, when a user logs in, they have to enter the schema name. If the schema does not exist,
 *      a new schema gets created. Here, a schema is a directory which has the following naming convention:
 *              schemaName_username
 *      So this way, different users can create different schemas and tables.
 *      Example: A user Sarah has created a schema called Hobbies and has a table called Books.
 *               The file structure would be as follows:
 *                      -- Hobbies_Sarah (directory)
 *                              -- Books.txt (text file)
 */
public class SchemaManager {

    /**
     * <h2>accessSchema</h2>
     * This method id called to access the user schema and the data within it. If the user enters a schema name that
     * does not exist, then a new schema gets created for that user and an empty metaData.txt file is created.
     *
     * @param schemaName  the schema name
     * @param username    the user's name
     */
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
