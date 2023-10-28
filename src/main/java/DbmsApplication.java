import Authentication.Login;
import Authentication.Registration;
import Repository.*;
import Transaction.PerformTransactions;

import java.util.Scanner;

/**
 * <h1>DBMS Application</h1>
 * This class contains the main class.
 */
public class DbmsApplication {

    /**
     * <h2>main</h2>
     * The entry point of the program.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Menu");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("Enter your choice: ");
        int menu = scanner.nextInt();

        String username = "";
        String schemaName = "";

        switch (menu) {
            case 2:
                boolean isSuccess = Registration.registerUser();
                if (!isSuccess) {
                    System.exit(0);
                }
                System.out.println("You can login now.");
            case 1:
                username = Login.loginUser();
                schemaName = getSchema(username);
                break;
        }

        char isContinue = 'n';
        do {
            performOperations(schemaName);

            System.out.println("Continue? [y/n]");
            isContinue = scanner.next().charAt(0);
        } while (isContinue == 'y');
    }

    /**
     * <h2>getSchema</h2>
     * This method is a private method called inside main() to access the user Schema
     *
     * @param  username  the user's name
     * @return           schema name
     */
    private static String getSchema(String username) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter schema name: ");
        String schemaName = scanner.nextLine();
        SchemaManager.accessSchema(schemaName, username);
        return schemaName + "_" + username;
    }

    /**
     * <h2>performOperations</h2>
     * This method is a private method called inside main(). It allows the user to enter their sql queries and
     * calls the specific classes to perform the operations.
     *
     * @param  schemaName  the schema name
     */
    private static void performOperations(String schemaName) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Queries");
        System.out.println("--------");
        //Get user query
        String userQuery = Query.getUserQuery();
        String action = Query.getQueryAction(userQuery);
        switch (action) {
            case "CREATE":
                Create.createTable(userQuery, schemaName);
                break;
            case "INSERT":
                Insert.insertData(userQuery, schemaName);
                break;
            case "SELECT":
                Select.selectData(userQuery, schemaName);
                break;
            case "DELETE":
                Delete.deleteData(userQuery, schemaName);
                break;
            case "UPDATE":
                Update.updateData(userQuery, schemaName);
                break;
            case "START":
                PerformTransactions.startTransaction(schemaName);
                break;
            default:
                System.out.println("Invalid SQL query. Try again");
                break;
        }


    }
}
