import prank.Prank;
import prank.Group;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controler {
    private static final Logger LOG = Logger.getLogger(Controler.class.getName());

    private Prank prank;

    public Controler(String[] configFiles) {
        prank = new Prank(configFiles);
    }

    public void sendEmail(){
        prank.generatePrank();
        ArrayList<Group> group = prank.getGroup();
        for(Group g : group){
            formatAndSend(g);
        }
    }

    private void formatAndSend(Group group){
        prank.generatePrank();

        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        System.out.println("\n\n");

        Socket clientSocket = null;
        BufferedWriter out = null;
        BufferedReader in = null;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream("config/config.properties");
            Properties prop = new Properties();
            prop.load(fis);
            String address = prop.getProperty("smtp.serverAddress");
            String copyCarbon = prop.getProperty("witnessToCC");
            String subject = prop.getProperty("subject");

            int port = Integer.parseInt(prop.getProperty("smtp.serverPort"));
            clientSocket = new Socket(address, port);
            fis.close();

            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));

            String line;

            line = in.readLine();
            System.out.println(line);
            try{

                if(!line.startsWith("220 ")){
                    throw new RuntimeException("Erreur de connexion");
                }

                out.write("EHLO bonjour\r\n");
                out.flush();

                boolean testLecture = true;
                line = in.readLine();

                //------------------------------------------------------------------------------------------------------
                while(testLecture){
                    System.out.println(line);
                    if(line.startsWith("250-")){
                        line = in.readLine();
                    }else if(line.startsWith("250 ")){
                        testLecture = false;
                    }else{
                        throw new RuntimeException("Exception : Connection Impossible");
                    }
                }

                //------------------------------------------------------------------------------------------------------
                String sender = group.getSender();//"olivier.tissot-daguette@heig-vd.ch";
                out.write("MAIL FROM: " + sender + "\r\n");
                out.flush();

                //------------------------------------------------------------------------------------------------------

                line = in.readLine();
                if(line.startsWith("250 ")){
                    System.out.println(line);
                }else{
                    throw new RuntimeException("Exception : Email d'envoi erreur");
                }

                //------------------------------------------------------------------------------------------------------

                for(int i = 0; i < group.getVictims().size(); ++i){
                    String mailTo = group.getVictims().get(i);//"joel.dossantosmatias@heig-vd.ch";

                    if(mailTo.contains("@")){
                        out.write("RCPT TO: " + mailTo + "\r\n");
                        out.flush();
                    }else{
                        throw new RuntimeException("Exception : Email format not respected");
                    }

                    //--------------------------------------------------------------------------------------------------

                    line = in.readLine();
                    if(line.startsWith("250 ")) {
                        System.out.println(line);
                    }else{
                        throw new RuntimeException("Exception : Email d'envoi erreur");
                    }
                }

                //------------------------------------------------------------------------------------------------------

                out.write("DATA\r\n");
                out.flush();

                //------------------------------------------------------------------------------------------------------

                line = in.readLine();
                if(line.startsWith("354 ")) {
                    System.out.println(line);
                }else{
                    throw new RuntimeException("Exception : Email d'envoi erreur");
                }

                //------------------------------------------------------------------------------------------------------


                out.write("From: " + sender + "\r\n");
                String mailTo = "";
                for(int i = 0; i < group.getVictims().size(); ++i) {
                    mailTo += group.getVictims().get(i) + ", ";//"joel.dossantosmatias@heig-vd.ch";
                }

                mailTo = mailTo.substring(0, mailTo.length()-2);

                out.write("To: " + mailTo + "\r\n");

                if(!copyCarbon.equals("0")){
                    out.write("Cc: " + copyCarbon + "\r\n");
                }

                if(!subject.equals("0")){
                    out.write("Subject: =?utf-8?Q?" + subject + "?=\r\n");
                }

                out.write("Content-Type: text/plain: charset=\"utf-8\"\r\n");
                String message = prank.getMessage();
                out.write("\r\n" + message);

                System.out.print(message);

                out.write("\r\n.\r\n");
                out.flush();

                //------------------------------------------------------------------------------------------------------

                line = in.readLine();
                if(line.startsWith("250 ")) {
                    System.out.println(line);
                }else{
                    throw new RuntimeException("Exception : Envoi d'email prob");
                }

                //------------------------------------------------------------------------------------------------------

                out.write("QUIT\r\n");
                out.flush();

                //------------------------------------------------------------------------------------------------------

                line = in.readLine();
                if(line.startsWith("221 ")) {
                    System.out.println(line);
                }else{
                    throw new RuntimeException("Exception : Problem de fermeture de connexion");
                }

                //------------------------------------------------------------------------------------------------------






            }catch (RuntimeException e){

                LOG.log(Level.SEVERE, e.toString(), e);

                out.write("RSET\r\n");
                out.flush();
                line = in.readLine();
                boolean tmp;
                do{
                    if(line.startsWith("250 ")) {
                        tmp = false;
                        System.out.println(line);
                    }else{
                        tmp = true;
                    }
                }while(tmp);

                do{
                    out.write("QUIT\r\n");
                    out.flush();

                    //------------------------------------------------------------------------------------------------------

                    line = in.readLine();
                    if(line.startsWith("221 ")) {
                        tmp = false;
                        System.out.println(line);
                    }else{
                        tmp = true;
                    }
                }while(tmp);


            }
            in.close();
            out.close();
            clientSocket.close();
        }
        catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.toString(), ex);
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.toString(), ex);
            }
            try {
                if (in != null) in.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.toString(), ex);
            }
            try {
                if (clientSocket != null && ! clientSocket.isClosed()) clientSocket.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.toString(), ex);
            }
        }

    }
}
