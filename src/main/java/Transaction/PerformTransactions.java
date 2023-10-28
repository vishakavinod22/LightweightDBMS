package Transaction;

import Authentication.Login;
import Authentication.Registration;
import Repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * <h1>Perform Transaction</h1>
 * This class provides methods for performing transactions
 */
public class PerformTransactions {

    /**
     * <h2>startTransaction</h2>
     * This method gets called if the user enters a sql to perform transactions.
     * Once the user enters "START TRANSACTION" this method gets called. This user can then enter multiple queries to
     * perform INSERT, DELETE, or UPDATE.
     *
     * The method allows the user to enter N lines of queries until the user enters "COMMIT" or a "ROLLBACK".
     *
     * The method then executes all the queries one by one and stores it in the buffer. It then calls the CommitManager
     * methods to perform a commit or rollback.
     *
     * @param schemaName  the schema name
     */
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
