package Transaction;

import Repository.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * <h1>Insert Transaction</h1>
 * This class provides methods for record insertion in a transaction
 */
public class insertTransaction {

    /**
     * <h2>insertData</h2>
     * This method gets called if the user enters a sql to INSERT a record to the table.
     * The method accepts the following format only:
     *      insert into table_name (columnName1, columnName2, ... , columnNameN) values (value1, value2, ... value3)
     * Sample input statement:
     *      insert into students (id, first_name, last_name) values (1, John, doe)
     *
     *  Once the user inputs the insert statement, the method checks if the table exists. If it does, it then checks
     *  if the primary key value is already taken. Once these checks are passed the record is inserted.
     *
     *  Assumptions:
     *      The first key will always be the primary key
     *      During insert, only correct column names are provided by the user
     *
     * Note: this function does not perform automatic commits.
     *
     * @param insertStmt  the sql delete statement
     * @param schemaName  the schema name
     */
    public static void insertData(String insertStmt, String schemaName){
        insertStmt = insertStmt.toLowerCase();
        String fileName = "src/main/resources/Schemas/"+schemaName+"/metaData.txt";

        try{

            //Get table name
            String[] stmtArray = insertStmt.split("\\s|\\(+");
            String tableName = stmtArray[2];

            //Check if table already exists in metadata
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            boolean flag = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    String[] splitLine = line.split(";");
                    String dbTableName = splitLine[0].substring(1);
                    if(dbTableName.equals(tableName)){
                        flag = true;
                    }
                }
            }
            //Check of table file exists under schema name
            boolean tableFlag = checkTableFile(schemaName, tableName);
            if(!flag & !tableFlag){
                System.out.println("Table does not exist.");
                return;
            }else if(!flag){
                System.out.println("Table does not exist in metadata.");
                return;
            } else if (!tableFlag) {
                System.out.println("Table file does not exist in schema folder.");
                return;
            }
            reader.close();

            //Get column names
            String[] columnsArray = insertStmt.trim().split("\\(|\\)")[1].split(",");
            //Get values
            String[] splitStmt = insertStmt.replaceAll("\"","").replaceAll("'","").trim().split("values");
            String[] valuesArray = splitStmt[1].replaceAll("\\(","").replaceAll(",","").replaceAll("\\)","").replaceAll(";","").trim().split(" ");

            StringBuilder tableData = new StringBuilder();
            for(int i=0; i<columnsArray.length; i++){
                String colArray = columnsArray[i].trim();
                String valArray = valuesArray[i].trim();
                tableData.append(Constants.COL_NAME_START).append(colArray).append(Constants.TABLE_VALUE_START).append(valArray);
            }

            //Check if primary key already exists
            String tableFileName = "src/main/resources/Schemas/"+schemaName+"/"+tableName+".txt";
            String tableFileCopy = "src/main/resources/Schemas/"+schemaName+"/"+tableName+"Copy.txt";
            BufferedReader reader2 = new BufferedReader(new FileReader(tableFileName));
            String line2;
            while ((line2 = reader2.readLine()) != null) {
                if (line2.startsWith("#")) {
                    String[] splitLine = line2.replaceAll(",","").replaceAll("#","").split("[;|]");
                    String storedValue = splitLine[2];
                    if(storedValue.equals(valuesArray[0])){
                        System.out.println("Primary key already present. Cannot repeat primary key.");
                        return;
                    }
                }
            }
            reader2.close();

            //Create buffer for table
            CommitManager.createBufferWithFileName(tableFileName, tableFileCopy);

            //Insert data into file
            FileWriter writer = new FileWriter(tableFileCopy, true);
            String tableDataValue = Constants.ROW_START+tableData+"\n";
            writer.append(tableDataValue);
            writer.close();
            System.out.println("Record Inserted.");

        } catch (InputMismatchException e){
            System.out.println(e + " : Invalid input, try again");
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    /**
     * <h2>checkTableFile</h2>
     * This method is a private method called inside insertData() to check if table exists. It returns a boolean value
     * based on the result.
     *
     * @param  schemaName  the schema name
     * @param  tableName   the table name
     * @return             true if table exists otherwise false
     */
    private static boolean checkTableFile(String schemaName, String tableName){
        try{
            Scanner scanner = new Scanner(System.in);
            String tableFileName = "src/main/resources/Schemas/" + schemaName + "/" + tableName + ".txt";

            File tableFile = new File(tableFileName);

            //Check if table file exists or not
            if(tableFile.exists()){
                return true;
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return false;
    }

//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String stmt = "Insert into subjects (id, subject) values (1, maths);";
//        insertData(stmt, "School_a");
//    }
}
