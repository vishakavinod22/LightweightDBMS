package Authentication;

public class Captcha {

    private static String captchaString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static String generatedCaptcha = "";

    public static String generateCaptcha(){
        for(int i=0; i<5; i++){
            int captchaIndex = (int) (Math.random() * captchaString.length());
            generatedCaptcha += captchaString.charAt(captchaIndex);
        }
        return generatedCaptcha;
    }

}
