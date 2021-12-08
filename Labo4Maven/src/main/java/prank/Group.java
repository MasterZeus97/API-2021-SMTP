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
        if(sizeGroup < 3)
            throw new RuntimeException("La taille du groupe doit être supérieure à 3");
        this.sizeGroup = sizeGroup;
        defineGroup(emails);
    }

    private void defineGroup(Emails emails){
        Random rand = new Random();

        ArrayList<String> emailsClone = new ArrayList<>();
        emailsClone.addAll(emails.getEmails());

        sender = emailsClone.get(rand.nextInt(emailsClone.size()));
        emailsClone.remove(sender);

        for(int i = 0; i < sizeGroup-1; ++i){
            String email = emailsClone.get(rand.nextInt(emailsClone.size()));
            victims.add(email);
            emailsClone.remove(email);
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
