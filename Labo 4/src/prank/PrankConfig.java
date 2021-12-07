package prank;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PrankConfig {
    private Emails emails;
    private Messages messages;
    private ArrayList<Group> groups;

    public PrankConfig(String[] configFiles) {
        try{
            FileInputStream fis = new FileInputStream("config/config.properties");
            Properties prop = new Properties();
            prop.load(fis);

            groups = new ArrayList<>();
            emails = new Emails(prop.getProperty("files.email"));
            messages = new Messages(prop.getProperty("files.message"));

            int totalGroup = Integer.parseInt(prop.getProperty("size.group"));
            int groupSize = Integer.parseInt(prop.getProperty("size.sizeGroup"));

            for(int i = 0; i < totalGroup; i++){
                groups.add(new Group(groupSize, emails));
            }

        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    String getMessage() {
        Random rand = new Random();
        return messages.getMessages().get(rand.nextInt(messages.getMessages().size()));
    }

    ArrayList<Group> getGroup() {
        return groups;
    }
}
