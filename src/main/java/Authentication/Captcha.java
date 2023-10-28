package Authentication;


/**
 * <h1>Captcha</h1>
 * This class provides a method to generate a captcha
 */
public class Captcha {

    private static String captchaString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static String generatedCaptcha = "";

    /**
     * <h2>generateCaptcha</h2>
     * Returns a captcha string
     * This method generates a 5 digit captcha code for user authentication and return a String
     *
     * @return      the generated captcha code
     */
    public static String generateCaptcha(){
        for(int i=0; i<5; i++){
            int captchaIndex = (int) (Math.random() * captchaString.length());
            generatedCaptcha += captchaString.charAt(captchaIndex);
        }
        return generatedCaptcha;
    }

}
