package Repository;

import java.util.Scanner;

/**
 * <h1>Query</h1>
 * This class provides methods for activities related to query identification
 */
public class Query {

    /**
     * <h2>getUserQuery</h2>
     * This method gets called to get the query input from the user. It then returns the query as a string.
     *
     * @return    user query input
     */
    public static String getUserQuery(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your query:");
        String userQuery = scanner.nextLine();
        return userQuery;
    }

    /**
     * <h2>getQueryAction</h2>
     * This method gets called to identify the type of query: CREATE, INSERT, SELECT, UPDATE, DELETE, TRANSACTIONS.
     * It then returns a String with the operation name.
     *
     * @return    query type
     */
    public static String getQueryAction(String userQuery){
        String[] statement = userQuery.split(" ");
        return statement[0].toUpperCase();
    }
}
