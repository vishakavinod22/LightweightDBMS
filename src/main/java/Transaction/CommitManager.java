package Transaction;

import java.io.*;

public class CommitManager {
    public static void createBufferWithFileName(String fileName, String fileNameCopy) throws IOException {
        File dbFile = new File(fileName);
        File buffer = new File(fileNameCopy);

        //Check if buffer exists
        if(!buffer.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(dbFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(buffer));

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
            }

            reader.close();
            writer.close();
        }
    }

    public static void commitChanges(String schemaName){
        String directoryPath = "src/main/resources/Schemas/" + schemaName;
        File directory = new File(directoryPath);

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            for (File file : files) {
                if (file.isFile() && file.getName().endsWith("Copy.txt")) {
                    String newFileName = file.getName().replace("Copy.txt", ".txt");
                    File newFile = new File(directoryPath, newFileName);

                    if (file.renameTo(newFile)) {
                        System.out.println("Commit complete");
                    } else {
                        System.out.println("Commit failed. Rollback completed.");
                    }
                }
            }
        } else {
            System.out.println("Invalid directory path.");
        }

    }

    public static void rollbackChanges(String schemaName){
        String directoryPath = "src/main/resources/Schemas/" + schemaName;
        File directory = new File(directoryPath);

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            for (File file : files) {
                if (file.isFile() && file.getName().endsWith("Copy.txt")) {
                    if (file.delete()) {
                        System.out.println("Rollback Complete");
                    } else {
                        System.out.println("Failed to delete " + file.getName());
                    }
                }
            }
        } else {
            System.out.println("Invalid directory path.");
        }

    }

//    public static void main(String[] args) throws IOException {
////        createBufferWithFileName("src/main/resources/Schemas/testing_a/test.txt", "src/main/resources/Schemas/testing_a/testCopy.txt");
//        rollbackChanges("testing_a");
//
//    }
}
