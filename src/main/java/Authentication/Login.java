package Authentication;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Login {
    private static final List<User> users = new ArrayList<>();

    public static void loginUser(){
        try {
            //Read the existing JSON file
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("src/main/resources/Users.json"));
            JSONObject jsonObject = (JSONObject) obj;

            //Parse JSON data to check if username and password are correct
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            String storedPassword = (String) jsonObject.get(username);
            boolean isPassword = Security.verifyPassword(password, storedPassword);

            if (jsonObject.containsKey(username) && isPassword == true) {
                //Get generated captcha
                String captcha = Captcha.generateCaptcha();
                System.out.println("Captcha: " + captcha);
                System.out.print("Enter captch for verification: ");

                //Verify if capcha is correct
                String verifyCaptcha = scanner.nextLine();
                if(captcha.equals(verifyCaptcha)){
                    System.out.println("Login successful!");
                }
                else{
                    System.out.println("Invalid captcha!");
                }
            } else {
                System.out.println("Invalid username or password.");
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
