import Authentication.Login;
import Authentication.Registration;
import Repository.Create;
import Repository.Query;
import Repository.SchemaManager;

import java.util.Objects;
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

        System.out.println("Queries");
        System.out.println("--------");
        boolean isContinue = false;
        do {
            //Get user query
            String userQuery = Query.getUserQuery();
            String action = Query.getQueryAction(userQuery);
//            System.out.println(action);
            switch (action) {
                case "CREATE":
                    Create.createTable(userQuery, schemaName);
                    break;
                case "INSERT":
                    Create.insertData(userQuery, schemaName);
                    break;
            }

            System.out.println("Continue? [true/false]");
            isContinue = scanner.nextBoolean();
        } while (isContinue);

    }

    public static String getSchema(String username){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter schema name: ");
        String schemaName = scanner.nextLine();
        SchemaManager.accessSchema(schemaName, username);
        return schemaName+"_"+username;
    }
}
