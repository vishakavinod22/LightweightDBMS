package Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * <h1>Select</h1>
 * This class provides methods for record selection
 */
public class Select {

    /**
     * <h2>selectData</h2>
     * This method gets called if the user enters a sql to SELECT a record from the table.
     * The method accepts the following formats only:
     *      select * from table_name;
     *      select * from table_name where columName = columnValue;
     *      select columnName1, columnName2, ... ,columnNameN from table_name;
     * Sample input statement:
     *      select * from students;
     *      select * from students where id = 1;
     *      select * from students where first_name = "John";
     *      select first_name, last_name from students;
     *
     *  Assumptions:
     *      The user uses the WHERE clause only with "SELECT *" statements
     *      During select, only correct column names are provided by the user
     *
     * @param selectStmt  the sql select statement
     * @param schemaName  the schema name
     */
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

    /**
     * <h2>checkTableFile</h2>
     * This method is a private method called inside selectData() to check if table exists. It returns a boolean value
     * based on the result.
     *
     * @param  schemaName  the schema name
     * @param  tableName   the table name
     * @return             true if table exists otherwise false
     */
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

    /**
     * <h2>selectAll</h2>
     * This method is a private method called inside selectData() when a user enters a simple SELECT statement
     *
     * The method accepts the following format only:
     *      select * from table_name;
     * Sample input statement:
     *      select * from students;
     *
     * @param tableName   the table name
     * @param schemaName  the schema name
     */
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

    /**
     * <h2>selectAllWithWhere</h2>
     * This method is a private method called inside selectData() when a user enters a simple SELECT statement with
     * a WHERE clause.
     *
     * The method accepts the following format only:
     *      select * from table_name where columName = columnValue;
     * Sample input statement:
     *      select * from students where id = 1;
     *      select * from students where first_name = "John";
     *
     * The where condition can search for any column name and is not limited to the primary key,
     * as long as it exists in the database.
     *
     * @param tableName   the table name
     * @param schemaName  the schema name
     */
    private static void selectAllWithWhere(String tableName, String schemaName, String whereColumnName, String whereValue){
        try{
            String fileName = "src/main/resources/Schemas/" + schemaName + "/"+tableName+".txt";
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
        } catch (InputMismatchException e){
            System.out.println(e + " : Invalid input, try again");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * <h2>selectColumns</h2>
     * This method is a private method called inside selectData() when a user enters a SELECT statement
     * with only a certain columnNames being selected.
     *
     * The method accepts the following format only:
     *      select columnName1, columnName2, ... ,columnNameN from table_name;
     * Sample input statement:
     *      select first_name, last_name from students;
     *
     * The where condition can search for any column name and is not limited to the primary key,
     * as long as it exists in the database.
     *
     * @param tableName   the table name
     * @param schemaName  the schema name
     */
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
        } catch (InputMismatchException e){
            System.out.println(e + " : Invalid input, try again");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * <h2>getSelectOutputAsString</h2>
     * This method is a public method called inside updateData() to get the output of the select statement as a string.
     * This method is used for getting data for update.
     *
     * @param tableName        the table name
     * @param schemaName       the schema name
     * @param whereColumnName  the columnName used in the WHERE condition
     * @param whereValue       the values assigned to the whereColumnName
     * @return                 the output of the select statement
     */
    public static List<String> getSelectOutputAsString(String tableName, String schemaName, String whereColumnName, String whereValue){
        try{
            String fileName = "src/main/resources/Schemas/" + schemaName + "/"+tableName+".txt";
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<String> selectOutput = new ArrayList<>();
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
                            selectOutput.add(result.substring(3));
                            break;
                        }
                    }
                }
            }
            reader.close();
            return selectOutput;

        } catch (FileNotFoundException e){
            return Collections.singletonList("Error : File not found");
        } catch (InputMismatchException e){
            return Collections.singletonList("Error : " + e + " : Invalid input, try again");
        }
        catch (Exception e){
            return Collections.singletonList("Error : " + e);
        }
    }

//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String stmt = scanner.nextLine();
//        selectData(stmt, "School_a");
//    }
}
