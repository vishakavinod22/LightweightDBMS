package Authentication;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * <h1>Login</h1>
 * This class provides a method to perform user login
 */
public class Login {
    private static final List<User> users = new ArrayList<>();

    /**
     * <h2>loginUser</h2>
     * This method gets the user inputs for username and password.
     *
     * It compares the user inputs with the Users.json file to check if the username and password are correct.
     * If the username and password are correct, the method calls the Captcha.generateCaptcha() to genrate a
     * five digit captcha sequence.
     * The user then inputs the captcha for authentication. Once the captchas are verified, the user has logged
     * in and can perform DB operations.
     *
     * This method returns a string that contains the username.
     *
     * @return      the username
     */
    public static String loginUser(){
        try {
            //Read the existing JSON file
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("src/main/resources/Users.json"));
            JSONObject jsonObject = (JSONObject) obj;

            //Parse JSON data to check if username and password are correct
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            //Converting username to lowercase to avoid case-insensitive checks
            username = username.toLowerCase();
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
                    return username;
                }
                else{
                    System.out.println("Invalid captcha!");
                    System.exit(0);
                }
            } else {
                System.out.println("Invalid username or password.");
                System.exit(0);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return " ";

    }
}
