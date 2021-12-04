package prank;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Group {
    private String sender;
    private ArrayList<String> victims;
    private int sizeGroup;

    public Group(int sizeGroup, Emails emails) {
        victims = new ArrayList<>();
        this.sizeGroup = sizeGroup;
        defineGroup(emails);
    }

    private void defineGroup(Emails emails){
        Random rand = new Random();

        ArrayList<String> emailsClone = new ArrayList<>();
        emailsClone.addAll(emails.getEmails());

        sender = emailsClone.get(rand.nextInt(emailsClone.size()));
        emailsClone.remove(sender);

        for(int i = 0; i < sizeGroup; ++i){
            victims.add(emailsClone.get(rand.nextInt(emailsClone.size())));
            emailsClone.remove(victims.get(i));
        }
    }

    public String getSender() {
        return sender;
    }

    public ArrayList<String> getVictims() {
        return victims;
    }

    public int getSizeGroup() {
        return sizeGroup;
    }
}
