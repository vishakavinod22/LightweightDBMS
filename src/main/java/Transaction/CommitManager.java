package Transaction;

import java.io.*;

/**
 * <h1>Commit Manager</h1>
 * This class provides methods for managing commits and rollbacks by creating a buffer.
 */
public class CommitManager {

    /**
     * <h2>createBufferWithFileName</h2>
     * This method creates a buffer when transaction operations like INSERT, UPDATE and DELETE are queried.
     * This essentially is creating a copy of the table file and making changes to the data.
     * The changes will not be reflected to the actual table file until the user types commit.
     * The naming convention is
     *      original db table : students.txt
     *      buffer for table  : studentsCopy.txt
     *
     * @param fileName      the original db table name
     * @param fileNameCopy  the buffer name
     */
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

    /**
     * <h2>commitChanges</h2>
     * This method performs the COMMIT operation by pushing the changes to the actual table file.
     * Once all the transaction activities are done, if the user has entered commit and everything went correctly,
     * this method parses through the directory to find all the buffer tables and commit it.
     *
     * @param schemaName theSchemaName
     */
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

    /**
     * <h2>rollbackChanges</h2>
     * This method performs the ROLLBACK operation by not pushing the changes from the buffer to the actual table file.
     * Once all the transaction activities are done, if the user has entered rollback or transaction was interrupted,
     * the method parses through the directory to find all the buffer tables deletes them.
     *
     * @param schemaName theSchemaName
     */
    public static void rollbackChanges(String schemaName){
        String directoryPath = "src/main/resources/Schemas/" + schemaName;
        File directory = new File(directoryPath);

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            for (File file : files) {
                if (file.isFile() && file.getName().endsWith("Copy.txt")) {
                    if (file.delete()) {
                        System.out.println("Rollback Complete");
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
