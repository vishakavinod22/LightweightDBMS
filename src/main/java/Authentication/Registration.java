package Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Registration {

    private static final List<User> users = new ArrayList<>();

    public static boolean registerUser(){
        try {
            //Read the existing JSON file
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("src/main/resources/Users.json"));
            JSONObject jsonObject = (JSONObject) obj;

            //Parse JSON data to check if username is taken
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            //Converting username to lowercase to avoid case-insensitive checks
            username = username.toLowerCase();

            if (jsonObject.containsKey(username)) {
                System.out.println("Username is already taken.");
                return false;
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            //Hashing the password
            String hashedPassword = Security.HashMd5(password);

            //Add new user to JSON data
            jsonObject.put(username, hashedPassword);

            //Write updated data back to JSON file
            try (FileWriter updatedFile = new FileWriter("src/main/resources/Users.json")) {
                updatedFile.write(jsonObject.toJSONString());
                System.out.println("User registered successfully!");
                return true;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

}
