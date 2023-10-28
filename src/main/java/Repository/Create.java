package Repository;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * <h1>Create</h1>
 * This class provides methods for table creation
 */
public class Create {

    /**
     * <h2>createTable</h2>
     * This method gets called if the user enters a sql to CREATE a table.
     * The method accepts the following format only:
     *      create table table_name (columnName1 datatype, columnName2 datatype, .... , columnNameN datatype);
     * Sample input statement:
     *      create table students (id int, first_name string, last_name string);
     *
     * This function takes the CREATE statement and creates the table inside the Schema directory in the file metaData.txt
     * The table is just a text file with the table name.
     *      Example student.txt
     *
     *  The metaData.txt is the file that stores the table description details
     *
     * Note: this function performs automatic commits since it is a CREATE statement.
     *
     * @param  createStmt  the sql create statement
     * @param schemaName   the schema name
     */
    public static void createTable(String createStmt, String schemaName){
        try{
            createStmt = createStmt.toLowerCase();
            String fileName = "src/main/resources/Schemas/"+schemaName+"/metaData.txt";

            //Get table name
            String[] stmtArray = createStmt.split("\\s|\\(+");
            String tableName = stmtArray[2];

            //Get column names
            String[] columnsArray = createStmt.trim().split("\\(|\\)")[1].split(",");
            StringBuilder tableData = new StringBuilder();
            for (String array : columnsArray){
                String col = array.trim().split(" ")[0];
                String dataType = array.trim().split(" ")[1];
                tableData.append(Constants.COL_NAME_START).append(col).append(Constants.DATA_TYPE_START).append(dataType);
            }

            //Check if table already exists
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    String[] splitLine = line.split(";");
                    String dbTableName = splitLine[0].substring(1);
                    if(dbTableName.equals(tableName)){
                        System.out.println("Table already exists.");
                        return;
                    }
                }
            }
            reader.close();

            // Create table
            FileWriter writer = new FileWriter(fileName, true);
            String tableMetaData = Constants.ROW_START+tableName+tableData+"\n";
            writer.append(tableMetaData);
            writer.close();
            createTableFile(schemaName, tableName);
            System.out.println("Table created");

        } catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * <h2>createTableFile</h2>
     * This method is a private method called inside createTable() to generate the table file
     *
     * @param  schemaName  the schema name
     * @param  tableName   the table name
     */
    private static void createTableFile(String schemaName, String tableName){
        try{
            Scanner scanner = new Scanner(System.in);
            String tableFileName = "src/main/resources/Schemas/" + schemaName + "/" + tableName + ".txt";

            File tableFile = new File(tableFileName);

            //If table does not exist then create new schema
            if(!tableFile.exists()){
                tableFile.createNewFile();
            }

        } catch (InputMismatchException e){
            System.out.println(e + " : Invalid input, try again");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}
