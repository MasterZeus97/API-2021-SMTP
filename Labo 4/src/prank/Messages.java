package prank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Messages {
    private ArrayList<String> messages;

    public Messages(String configFile) {
        messages = new ArrayList<>();
        parseMessages(configFile);
    }

    private void parseMessages(String configFile){
        BufferedReader in = null;
        try{
            in = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "UTF-8"));
            String line = null;

            line = in.readLine();
            while(line != null && !line.equals("3ND")){
                line = in.readLine();
                String message = "";
                while(!line.equals("3ND M3SSAG3")){
                    message += line + "\n";
                    line = in.readLine();
                }
                messages.add(message);

                line = in.readLine();

            }
            in.close();
        }
        catch(IOException ex) {
            System.out.println("sad");
        }

    }

    public ArrayList<String> getMessages() {
        return messages;
    }
}
