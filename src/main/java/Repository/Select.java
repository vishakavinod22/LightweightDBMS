package Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Select {
    public static void selectData(String selectStmt, String schemaName) {
        try {
            selectStmt = selectStmt.toLowerCase();
            String fileName = "src/main/resources/Schemas/" + schemaName + "/metaData.txt";

            String tableName = null;
            String whereColumnName = "";
            String whereValue = "";
            String[] stmtArray = selectStmt.replaceAll(";", "").split("\\s|\\(+");
            boolean isSelectAll = selectStmt.contains("*");
            boolean hasWhereClause = selectStmt.contains("where");

            System.out.println("Table Data");
            System.out.println("-----------");

            //Select all
            if (isSelectAll) {
                //Has where clause
                if (hasWhereClause) {
                    tableName = stmtArray[stmtArray.length -5];
                    whereColumnName = stmtArray[stmtArray.length-3];
                    whereValue = stmtArray[stmtArray.length-1].replaceAll("\"","").replace("'","");
                    //Call selectAllWithWhere
                    selectAllWithWhere(tableName, schemaName, whereColumnName, whereValue);
                } else {
                    tableName = stmtArray[stmtArray.length-1];
                    //Call selectAll
                    selectAll(tableName, schemaName);
                }
            }
            //Select by columns
            else {
                //Has where clause
                if (hasWhereClause) {
                    System.out.println("Error: where clause only applicable for \"select *\" statements.");
                    return;
                } else {
                    tableName = stmtArray[stmtArray.length-1];
                    List<String> cols = new ArrayList<>(Arrays.asList(stmtArray).subList(1, stmtArray.length - 2));
                    List<String> columnNames = new ArrayList<>();
                    for(String i : cols){
                        columnNames.add(i.replaceAll(",",""));
                    }
                    //Call selectAll
                    selectColumns(tableName, schemaName, columnNames);
                }
            }

            //Check if table already exists in metadata
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            boolean flag = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    String[] splitLine = line.split(";");
                    String dbTableName = splitLine[0].substring(1);
                    if (dbTableName.equals(tableName)) {
                        flag = true;
                    }
                }
            }
            //Check of table file exists under schema name
            boolean tableFlag = checkTableFile(schemaName, tableName);
            if (!flag & !tableFlag) {
                System.out.println("Table does not exist.");
                return;
            } else if (!flag) {
                System.out.println("Table does not exist in metadata.");
                return;
            } else if (!tableFlag) {
                System.out.println("Table file does not exist in schema folder.");
                return;
            }
            reader.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private static boolean checkTableFile(String schemaName, String tableName) {
        try {
            Scanner scanner = new Scanner(System.in);
            String tableFileName = "src/main/resources/Schemas/" + schemaName + "/" + tableName + ".txt";

            File tableFile = new File(tableFileName);

            //Check if table file exists or not
            if (tableFile.exists()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    private static void selectAll(String tableName, String schemaName){
        try{
            String fileName = "src/main/resources/Schemas/" + schemaName + "/"+tableName+".txt";
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    String[] splitLine = line.split(";");
                    String[] dataItemsArray = Arrays.toString(splitLine).replaceAll("\\|","=").replaceAll("\\[","").replaceAll("]","").trim().split("#");
                    String dataItems = dataItemsArray[1].replaceAll(",", "").replaceAll(" "," | ");
                    System.out.println(dataItems.substring(3));
                    System.out.println(Constants.DIVIDER);
                }
            }
            reader.close();
        } catch (FileNotFoundException e){
            System.out.println();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    private static void selectAllWithWhere(String tableName, String schemaName, String whereColumnName, String whereValue){
        try{
            String fileName = "src/main/resources/Schemas/" + schemaName + "/"+tableName+".txt";
            List<String> allData = new ArrayList<>();
            List<String> colValueData = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
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

                        if (key.equals(whereColumnName) && value.equals(whereValue)) {
                            String result = line.replaceAll("#","").replaceAll("\\|","=").replaceAll(";"," | ");
                            System.out.println(result.substring(3));
                            System.out.println(Constants.DIVIDER);
                            break;
                        }
                    }
                }
            }
            reader.close();

        } catch (FileNotFoundException e){
            System.out.println();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    private static void selectColumns(String tableName, String schemaName, List<String> columnNames){
        try{
            String fileName = "src/main/resources/Schemas/" + schemaName + "/"+tableName+".txt";
            String result = "";
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    String[] columns = line.split(";");
                    List<String> dataList = new ArrayList<>();
                    for (String columnData : columns) {
                        String[] keyValue = columnData.split("\\|");
                        String key = keyValue[0];
                        String value = "";
                        if(!key.equals("#")){
                            value = keyValue[1];
                        }

                        for (String requestedColumn : columnNames) {
                            if (requestedColumn.trim().equals(key)) {
                                dataList.add(key + "|" + value);
                            }
                        }
                    }
                    result = dataList.toString().replaceAll("\\[","").replaceAll("]","").replaceAll("\\|","=").replaceAll(","," |");
                    System.out.println(result.trim());
                    System.out.println(Constants.DIVIDER);
                }
            }
            reader.close();

        } catch (FileNotFoundException e){
            System.out.println();
        } catch (Exception e){
            System.out.println(e);
        }
    }

//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String stmt = scanner.nextLine();
//        selectData(stmt, "School_a");
//    }
}
