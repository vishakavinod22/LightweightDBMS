package Transaction;

import Repository.Select;

import java.io.*;
import java.util.*;

public class updateTransaction {
    public static void updateData(String updateStmt, String schemaName){
        try{
            updateStmt = updateStmt.toLowerCase();
            String fileName = "src/main/resources/Schemas/" + schemaName + "/metaData.txt";

            String[] stmtArray = updateStmt.replaceAll(";", "").replaceAll("'","").replaceAll("\"","").split("\\s|\\(+");
            String tableName = stmtArray[1];
            String whereColumnName = stmtArray[7];
            String whereValue = stmtArray[9];
            String setColumnName = stmtArray[3];
            String setValue = stmtArray[5];
            String updatedRecordValue = "";

            //Getting row data for update
            List<String> rowData = Select.getSelectOutputAsString(tableName, schemaName, whereColumnName, whereValue);

            //Check if record exists or not
            try{
                String a = rowData.get(0);
            }
            catch (IndexOutOfBoundsException e){
                System.out.println(rowData + " Record does not exist.");
                return;
            }

            String[] rowDataArray = rowData.get(0).replaceAll("\\[","").replaceAll("]","").split("\\|");
            List<String> dataBeforeUpdate = new ArrayList<>();

            for (String data : rowDataArray){
                dataBeforeUpdate.add(data.trim());
            }

            //Check if where column is a primary key
            String tableFileName = "src/main/resources/Schemas/"+schemaName+"/"+tableName+".txt";
            String tableFileCopy = "src/main/resources/Schemas/"+schemaName+"/"+tableName+"Copy.txt";

            //Create buffer for table
            CommitManager.createBufferWithFileName(tableFileName, tableFileCopy);

            BufferedReader reader = new BufferedReader(new FileReader(tableFileCopy));
            String line;
            String primaryKey = "";
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    String[] splitLine = line.replaceAll(",","").replaceAll("#","").split("[;|]");
                    primaryKey = splitLine[1];
                    if(!primaryKey.equals(whereColumnName)){
                        System.out.println("Update statement requires primary key in the where condition.");
                        return;
                    }

                    //Check if column names are correct
                    if(!line.contains(setColumnName) || !line.contains(whereColumnName)){
                        System.out.println("Invalid column name.");
                        return;
                    }

                    break;

                }

            }
            reader.close();

            //Set updated record value.
            List<String> dataAfterUpdate = new ArrayList<>();
            if (dataBeforeUpdate.get(0).equals(whereColumnName+"="+whereValue)) {
                for (String data : dataBeforeUpdate){
                    String[] keyValue = data.split("=");
                    if(keyValue[0].equals(setColumnName)){
                        keyValue[1] = setValue;
                    }
                    dataAfterUpdate.add(keyValue[0]+"="+keyValue[1]);
                }
                updatedRecordValue = "#;" + dataAfterUpdate.toString().replaceAll("=", "\\|").replaceAll("\\[", "").replaceAll("]", "").replaceAll(", ", ";");
            } else {
                System.out.println("Error in updating record.");
                return;
            }

            String tempFileName = "src/main/resources/Schemas/temp.txt";
            File tempFile = new File(tempFileName);
            File tableFile = new File(tableFileCopy);

            BufferedReader r = new BufferedReader(new FileReader(tableFileCopy));
            BufferedWriter w = new BufferedWriter(new FileWriter(tempFile));
            String line2;
            while ((line2 = r.readLine()) != null) {
                if (line2.startsWith("#;"+whereColumnName+"|"+whereValue)) {
                    w.write(updatedRecordValue + "\n");
                } else {
                    w.write(line2+"\n");
                }
            }
            w.close();
            r.close();

            boolean isUpdated = tempFile.renameTo(tableFile);
            if (!isUpdated) {
                System.out.println("Record not updated.");
            } else {
                System.out.println("Record updated.");
            }


        }catch (InputMismatchException e){
            System.out.println(e + " : Invalid input, try again");
        }
        catch (Exception e){
            System.out.println("Invalid input.");
        }
    }

//    public static void main(String[] args) {
//        Scanner s = new Scanner(System.in);
//        String stmt = "update subjects set id = 200 where id = 2";
//        updateData(stmt, "school_a");
//        //#;id|3;name|truck;color|grey;make|aaa;years|13
//    }
}
