package Transaction;

import Authentication.Login;
import Authentication.Registration;
import Repository.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class PerformTransactions {

//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Menu");
//        System.out.println("1. Login");
//        System.out.println("2. Register");
//        System.out.print("Enter your choice: ");
//        int menu = scanner.nextInt();
//
//        String username = "";
//        String schemaName = "";
//
//        switch (menu) {
//            case 2:
//                boolean isSuccess = Registration.registerUser();
//                if (!isSuccess) {
//                    System.exit(0);
//                }
//                System.out.println("You can login now.");
//            case 1:
//                username = Login.loginUser();
//                schemaName = getSchema(username);
//                break;
//        }
//
//        char isContinue = 'n';
//        do {
//            performOperations(schemaName);
//
//            System.out.println("Continue? [y/n]");
//            isContinue = scanner.next().charAt(0);
//        } while (isContinue == 'y');
//    }
//
//    private static String getSchema(String username) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter schema name: ");
//        String schemaName = scanner.nextLine();
//        SchemaManager.accessSchema(schemaName, username);
//        return schemaName + "_" + username;
//    }
//
//    private static void performOperations(String schemaName) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Queries");
//        System.out.println("--------");
//        //Get user query
//        String userQuery = Query.getUserQuery();
//        String action = Query.getQueryAction(userQuery);
//        switch (action) {
//            case "CREATE":
//                Create.createTable(userQuery, schemaName);
//                break;
//            case "INSERT":
//                Insert.insertData(userQuery, schemaName);
//                break;
//            case "SELECT":
//                Select.selectData(userQuery, schemaName);
//                break;
//            case "DELETE":
//                Delete.deleteData(userQuery, schemaName);
//                break;
//            case "UPDATE":
//                Update.updateData(userQuery, schemaName);
//                break;
//            case "START":
//                startTransaction(schemaName);
//                break;
//            default:
//                System.out.println("Invalid SQL query. Try again");
//                break;
//        }
//
//
//    }

    public static void startTransaction(String schemaName) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your transaction block one below the other. \nOnce completed write commit / rollback.");
        System.out.println(Constants.DIVIDER);
        List<String> transactionBlock = new ArrayList<>();
        String tcl = "";

        while (true) {
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("commit") || userInput.equalsIgnoreCase("commit;")) {
                tcl = "commit";
                break;
            }
            else if (userInput.equalsIgnoreCase("rollback") || userInput.equalsIgnoreCase("rollback;")) {
                tcl = "rollback";
            }
            transactionBlock.add(userInput);
        }

        for (String query : transactionBlock){
            String action = Query.getQueryAction(query);
            switch (action) {
                case "INSERT":
                    insertTransaction.insertData(query, schemaName);
                    break;
                case "DELETE":
                    deleteTransaction.deleteData(query, schemaName);
                    break;
                case "UPDATE":
                    updateTransaction.updateData(query, schemaName);
                    break;
                default:
                    System.out.println("Invalid SQL query. Try again");
                    break;
            }
        }

        if(tcl.equals("commit")){
            CommitManager.commitChanges(schemaName);
        } else {
            CommitManager.rollbackChanges(schemaName);
        }
    }

//    public static void main(String[] args) {
//        startTransaction("school_a");
////        CommitManager.commitChanges("school_a");
//    }


}
