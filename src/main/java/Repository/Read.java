package Repository;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Read {

    public static void readTable(String selectStmt, String schemaName) {
        selectStmt = selectStmt.toLowerCase();
        String[] splitStmt = selectStmt.replaceAll(";", "").replaceAll("'", "").replaceAll("\"", "").replaceAll(",","").split("\\s+");

        //Check if statement is select all or select by column name
        // A = select * from table_name
        // B = select * from table_name where primary_key = value;
        // C = select column_names from table_name;
        // D = select column_names from table_name where primary_key = value;
        String tableName = null;
        boolean hasCondition = selectStmt.contains("where");

        // Select all
        if (splitStmt[1].equals("*")) {
            // Get table name
            tableName = splitStmt[3];
            // Check for where condition
            if (hasCondition) {
                String whereColumnName = splitStmt[5];
                String whereValue = splitStmt[7];
                // Go to selectAllWithWhere
                selectAllWithWhere(tableName, whereColumnName, whereValue, schemaName);
            } else {
                // Go to selectAll
                selectAll(tableName, schemaName);
            }
        }
        // Select with column names
        else {
            // Check for where condition
            if (hasCondition) {
                // Get table name and column names
                tableName = splitStmt[splitStmt.length - 5];
                List<String> columnNames = new ArrayList<>(Arrays.asList(splitStmt).subList(1, splitStmt.length - 6));
                String whereColumnName = splitStmt[splitStmt.length-3];
                String whereValue = splitStmt[splitStmt.length-1];
                // Go to selectColumnsWithWhere
                selectColumnsWithWhere(tableName, columnNames, whereColumnName, whereValue, schemaName);
            } else {
                // Get table name and column names
                tableName = splitStmt[splitStmt.length - 1];
                List<String> columnNames = new ArrayList<>(Arrays.asList(splitStmt).subList(1, splitStmt.length - 2));
                // Go to selectColumns
                selectColumns(tableName,  columnNames, schemaName);
            }
        }
    }

    public static void selectAll(String tableName, String schemaName) {
        try {
            //Read existing JSON file
            String schemaFileName = "src/main/resources/Schemas/" + schemaName + ".json";
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(schemaFileName));
            JSONObject schemaData = (JSONObject) obj;

            // Check if the table exists in the schema
            if (!schemaData.containsKey(tableName)) {
                System.out.println("Table '" + tableName + "' does not exist in schema.");
                return;
            }

            JSONArray queryResult = (JSONArray) ((JSONObject) schemaData.get(tableName)).get("data");
            System.out.println(queryResult);


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void selectAllWithWhere(String tableName, String whereColumnName, String whereValue, String schemaName) {
        try {
            //Read existing JSON file
            String schemaFileName = "src/main/resources/Schemas/" + schemaName + ".json";
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(schemaFileName));
            JSONObject schemaData = (JSONObject) obj;

            // Check if the table exists in the schema
            if (!schemaData.containsKey(tableName)) {
                System.out.println("Table '" + tableName + "' does not exist in schema.");
                return;
            }

            // Get data
            JSONArray queryResult = (JSONArray) ((JSONObject) schemaData.get(tableName)).get("data");
            try{
                for (Object row : queryResult) {
                    JSONObject rowObj = (JSONObject) row;
                    if (((JSONObject) row).get(whereColumnName).equals(whereValue)) {
                        System.out.println(rowObj.toJSONString());
                    }

                }
            } catch (NullPointerException e){
                System.out.println("Incorrect column name.");
            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void selectColumns(String tableName, List<String> columnNames, String schemaName){
        try{

            // Read existing JSON file
            String schemaFileName = "src/main/resources/Schemas/" + schemaName + ".json";
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(schemaFileName));
            JSONObject schemaData = (JSONObject) obj;

            // Check if the table exists in the schema
            if (!schemaData.containsKey(tableName)) {
                System.out.println("Table '" + tableName + "' does not exist in schema.");
                return;
            }

            // Get data
            JSONArray query = (JSONArray) ((JSONObject) schemaData.get(tableName)).get("data");
            JSONObject queryResult = new JSONObject();

            try{
                for (Object rowObj : query) {
                    JSONObject dbRows = (JSONObject) rowObj;
                    for (String columnName : columnNames) {
                        // If column name is wrongly entered, throws exception
                        columnName.contains((CharSequence) dbRows.get(columnName));
                    if (dbRows.containsKey(columnName)) {
                        queryResult.put(columnName, dbRows.get(columnName));
                    }
                    }
                System.out.println(queryResult);
                }
            } catch (NullPointerException e){
                System.out.println("Incorrect column name.");
            }

        } catch (Exception e){
            System.out.println(e);
        }
    }

    public static void selectColumnsWithWhere(String tableName, List<String> columnNames, String whereColumnName, String whereValue, String schemaName){
        try {
            // Read existing JSON file
            String schemaFileName = "src/main/resources/Schemas/" + schemaName + ".json";
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(schemaFileName));
            JSONObject schemaData = (JSONObject) obj;

            // Check if the table exists in the schema
            if (!schemaData.containsKey(tableName)) {
                System.out.println("Table '" + tableName + "' does not exist in schema.");
                return;
            }

            // Get all data
            JSONArray query = (JSONArray) ((JSONObject) schemaData.get(tableName)).get("data");
            JSONObject queryResult = new JSONObject();

            try{
                for (Object rowObj : query) {
                    JSONObject dbRows = (JSONObject) rowObj;
                    //Get all columns data
                    for (String columnName : columnNames) {
                        // If column name is wrongly entered, throws exception
                        columnName.contains((CharSequence) dbRows.get(columnName));
                        if (dbRows.containsKey(columnName)) {
                            queryResult.put(columnName, dbRows.get(columnName));
                        }
                    }
                    //Fiter data based on where condition
                    if (((JSONObject) rowObj).get(whereColumnName).equals(whereValue)) {
                        System.out.println(((JSONObject) rowObj).toJSONString());
                    }
                }

            } catch (NullPointerException e){
                System.out.println("Incorrect column name.");
            }


        } catch (Exception e){
            System.out.println(e);
        }
    }

//    public static void main(String[] args) {
//        String stmt1 = "select * from student;";
//        String stmt2 = "select * from student where id = 1;";
//        String stmt3 = "select first_name from student;";
//        String stmt4 = "select id, first_name from student where last_names = 'doe';";
//        String schemaName = "Organization";
//        readTable(stmt4, schemaName);
//    }

}
