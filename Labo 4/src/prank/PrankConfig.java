package prank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class PrankConfig {
    private Emails emails;
    private Messages messages;
    private ArrayList<Group> groups;

    public PrankConfig(String[] configFiles) {
        groups = new ArrayList<>();
        emails = new Emails(configFiles[1]);
        messages = new Messages(configFiles[2]);

        BufferedReader in = null;
        try{
            in = new BufferedReader(new InputStreamReader(new FileInputStream(configFiles[0]), "UTF-8"));
            String line = null;
            int totalGroup = 0, n = 0;

            if(!in.readLine().equals("GROUP"))
                throw new IOException("The file format is not respected");
            line = in.readLine();
            System.out.println(line);

            totalGroup = Integer.parseInt(line);
            int[] groupSize = new int[totalGroup];

            if(!in.readLine().equals("SIZE"))
                throw new IOException("The file format is not respected");
            line = in.readLine();

            while(!line.equals("3ND")){
                groupSize[n] = Integer.parseInt(line);
                ++n;
                line = in.readLine();
            }

            for(int i = 0; i < totalGroup; i++){
                groups.add(new Group(groupSize[i], emails));
            }

            in.close();

        }
        catch(IOException ex) {
            System.out.println("sad");
        }
    }

    String getMessage() {
        Random rand = new Random();
        return messages.getMessages().get(rand.nextInt(messages.getMessages().size()));
    }

    Group getGroup() {
        Random rand = new Random();
        return groups.get(rand.nextInt(groups.size()));
    }
}
