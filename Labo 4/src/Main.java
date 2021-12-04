public class Main {

    public static void main(String[] args) {

        Controler controler = new Controler(new String[]{"config/size.config","config/email.config", "config/message.config"});

        controler.sendEmail();

    }
}
