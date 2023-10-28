package Transaction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * <h1>Delete Transaction</h1>
 * This class provides methods for record deletion in a transaction
 */
public class deleteTransaction {

    /**
     * <h2>deleteData</h2>
     * This method gets called if the user enters a sql to DELETE a record from the table.
     * The method accepts the following format only:
     *      delete from table_name where columnName = columnValue;
     * Sample input statement:
     *      delete from students where id = 10;
     *      delete from students where first_name = "John";
     *
     * This function reads the table file, and if the record is found, it gets deleted.
     *
     * Note: this function does not perform automatic commits.
     *
     * @param deleteStmt  the sql delete statement
     * @param schemaName  the schema name
     */
    public static void deleteData(String deleteStmt, String schemaName){
        try{

            deleteStmt = deleteStmt.toLowerCase();
            String[] stmtArray = deleteStmt.replaceAll(";", "").split("\\s|\\(+");

            String tableName = stmtArray[2];
            String whereColumnName = stmtArray[4];
            String whereValue = stmtArray[stmtArray.length-1];

            String fileName = "src/main/resources/Schemas/" + schemaName + "/"+tableName+".txt";
            String fileNameCopy = "src/main/resources/Schemas/" + schemaName + "/"+tableName+"Copy.txt";

            //Create buffer for table
            CommitManager.createBufferWithFileName(fileName, fileNameCopy);

            BufferedReader reader = new BufferedReader(new FileReader(fileNameCopy));
            String line;
            List<String> updatedLine = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    String[] columns = line.split(";");
                    for (String column : columns) {
                        String[] keyValue = column.split("\\|");
                        String key = keyValue[0];
                        String value = "";
                        if(!key.equals("#")){
                            value = keyValue[1];
                        }

                        if (key.equals(whereColumnName) && !value.equals(whereValue)) {
                            updatedLine.add(line);
                            break;
                        }
                    }
                }
            }
            reader.close();

            if(!updatedLine.isEmpty()){
                FileWriter writer = new FileWriter(fileNameCopy);
                for (String l : updatedLine) {
                    writer.append(l).append("\n");
                }
                writer.close();
                System.out.println("Record deleted.");
            } else {
                System.out.println("Invalid column name.");
            }

        } catch (FileNotFoundException e){
            System.out.println("Table does not exist.");
        } catch (InputMismatchException e){
            System.out.println(e + " : Invalid input, try again");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String stmt = "delete from subjects where id = 3";
//        deleteData(stmt, "School_a");
//    }
}
