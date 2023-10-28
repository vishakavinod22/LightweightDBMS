import Authentication.Login;
import Authentication.Registration;
import Repository.*;
import Transaction.PerformTransactions;

import java.util.Date;
import java.util.Scanner;

public class DbmsApplication {

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

    private static String getSchema(String username) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter schema name: ");
        String schemaName = scanner.nextLine();
        SchemaManager.accessSchema(schemaName, username);
        return schemaName + "_" + username;
    }

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
