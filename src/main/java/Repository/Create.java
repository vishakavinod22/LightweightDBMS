package Repository;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Create {

    public static void createTable(String createStmt, String schemaName){
        try{
            createStmt = createStmt.toLowerCase();
            //Get table name and add to json object
            JSONObject tableData = new JSONObject();
            String[] stmtArray = createStmt.split("\\s|\\(+");
            String tableName = stmtArray[2];
            tableData.put("table_name", tableName);

            //Get table columns and add to JSON object
            JSONArray columnData = new JSONArray();
            String[] columnsArray = createStmt.split("\\(|\\)")[1].split(",");

            for (String column : columnsArray) {
                JSONObject data = new JSONObject();
                String[] columnDetails = column.trim().split("\\s+");
                //Check for table constraint like primary key
                if(columnDetails.length > 2){
                    String constraint = columnDetails[2]+" "+columnDetails[3];
                    data.put("constraint", constraint);
                }
                data.put("name",columnDetails[0]);
                data.put("type",columnDetails[1]);
                columnData.add(data);
            }

            tableData.put("columns",columnData);
            tableData.put("data",new JSONArray());

            //Read the existing JSON file
            JSONParser parser = new JSONParser();
            String schemaFileName = "src/main/resources/Schemas/" + schemaName + ".json";
            Object obj = parser.parse(new FileReader(schemaFileName));
            JSONObject schemaData = (JSONObject) obj;


            // Check if the table already exists in the schema
            if (schemaData.containsKey(tableName)) {
                System.out.println("Table '" + tableName + "' already exists in schema.");
                return;
            }

            //Add table to schema
            schemaData.put(tableName, tableData);

            //Write updated data back to JSON file
            try (FileWriter updatedFile = new FileWriter(schemaFileName)) {
                updatedFile.write(schemaData.toJSONString());
                System.out.println("Table created successfully!");
            }

        } catch (Exception e){
            System.out.println(e);
        }
    }

    public static void insertData(String insertStmt, String schemaName){
        try{
            insertStmt = insertStmt.toLowerCase();

            //Perform string manipulation to get table name, column name, and values
            String[] splitStmt = insertStmt.replaceAll("'","").replaceAll("\"","").split("values");
            String tableNameSet = splitStmt[0];
            String columnValueSet = splitStmt[1].replaceAll("\\(", "").replaceAll("\\)", "").trim();
            splitStmt = tableNameSet.replaceAll(",","").replaceAll("\\)","").split("\\(");

            //Table name
            String tableName = splitStmt[0].split(" ")[2];
            //Column names
            String[] columnNames = splitStmt[1].split(" ");
            //Values
            String[] values = columnValueSet.replaceAll(";","").split(",");

            JSONObject data = new JSONObject();
            for (int i = 0; i < columnNames.length; i++) {
                data.put(columnNames[i].trim(), values[i].trim());
            }

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

            JSONArray dataArray = (JSONArray) ((JSONObject) schemaData.get(tableName)).get("data");

            // Create a JSON object for the new row
            JSONObject newRow = new JSONObject();
            for (int i = 0; i < columnNames.length; i++) {
                newRow.put(columnNames[i].trim(), values[i].trim());
                // check if column names are correct
                for (Object row : dataArray) {
                    if(((JSONObject) row).get(columnNames[i]) == null){
                        System.out.println("Error: Wrong column name.");
                        return;
                    }
                }
            }

            // Check if there are any rows with the same id
            String idValue = newRow.get("id").toString();
            for (Object row : dataArray) {
                if (((JSONObject) row).get("id").toString().equals(idValue)) {
                    System.out.println("Error: Duplicate id found in the table.");
                    return;
                }
            }

            System.out.println(newRow);

            // Add the new row to the data array
            dataArray.add(newRow);

            // Write updated schema data back to JSON file
            try (FileWriter file = new FileWriter(schemaFileName)) {
                file.write(schemaData.toJSONString());
                System.out.println("Data inserted successfully.");
            }

        } catch (Exception e){
            System.out.println(e);
        }


    }

//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
////        String stmt = "create table student(id int primary key, first_name string, last_name string)";
////        String stmt = "insert into student (id, first_name, last_name) values (1, \"vishaka\", \"vinod\");"; //scanner.nextLine();
//        String stmt = scanner.nextLine();
//        insertData(stmt, "Organization");
//    }
}
