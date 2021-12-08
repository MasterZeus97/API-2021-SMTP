package prank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Emails {
    private ArrayList<String> emails;

    public Emails(String configFile) {
        emails = new ArrayList<>();
        parseEmails(configFile);
    }

    private void parseEmails(String configFile){
        BufferedReader in = null;
        try{
            in = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "UTF-8"));
            String line = null;

            line = in.readLine();
            while(!line.equals("3ND")){
                emails.add(line);
                line = in.readLine();
            }
            in.close();
        }
        catch(IOException ex) {
            System.out.println("sad");
        }

    }

    public ArrayList<String> getEmails() {
        return emails;
    }
}
