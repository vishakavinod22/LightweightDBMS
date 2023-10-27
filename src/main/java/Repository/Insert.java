package Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Insert {
    public static void insertData(String insertStmt, String schemaName){
        try{
            insertStmt = insertStmt.toLowerCase();
            String fileName = "src/main/resources/Schemas/"+schemaName+"/metaData.txt";

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

            //Insert data into file
            FileWriter writer = new FileWriter(tableFileName, true);
            String tableDataValue = Constants.ROW_START+tableData+"\n";
            writer.append(tableDataValue);
            writer.close();
            System.out.println("Record Inserted");

        } catch (InputMismatchException e){
            System.out.println(e + " : Invalid input, try again");
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

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
//        String stmt = scanner.nextLine();
//        insertData(stmt, "School_a");
//    }
}
