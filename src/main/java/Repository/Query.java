package Repository;

import java.util.Scanner;

public class Query {

    public static String getUserQuery(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your query:");
        String userQuery = scanner.nextLine();
        return userQuery;
    }

    public static String getQueryAction(String userQuery){
        String[] statement = userQuery.split(" ");
        return statement[0].toUpperCase();
    }
}
