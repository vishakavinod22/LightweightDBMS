package Repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class Delete {
    public static void deleteData(String deleteStmt, String schemaName){
        try{

            deleteStmt = deleteStmt.toLowerCase();
            String[] stmtArray = deleteStmt.replaceAll(";", "").split("\\s|\\(+");

            String tableName = stmtArray[2];
            String whereColumnName = stmtArray[4];
            String whereValue = stmtArray[stmtArray.length-1];

            String fileName = "src/main/resources/Schemas/" + schemaName + "/"+tableName+".txt";
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
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
                FileWriter writer = new FileWriter(fileName);
                for (String l : updatedLine) {
                    writer.append(l).append("\n");
                }
                writer.close();
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
//        String stmt = "delete from students where f_name = vishaka";
//        deleteData(stmt, "School_a");
//    }
}
