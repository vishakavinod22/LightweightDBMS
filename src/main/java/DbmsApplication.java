import Authentication.Login;
import Authentication.Registration;

import java.util.Scanner;

public class DbmsApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Menu");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("Enter your choice: ");
        int menu = scanner.nextInt();

        switch (menu){
            case 1:
                Login.loginUser();
                break;
            case 2:
                Registration.registerUser();
                System.out.println("You can login now.");
                Login.loginUser();
                break;
        }


    }
}
