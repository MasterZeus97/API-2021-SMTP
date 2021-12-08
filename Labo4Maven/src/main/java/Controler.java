import prank.Prank;
import prank.Group;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
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
        FileInputStream fis;

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

            String lineIn;
            String lineOut;

            lineIn = in.readLine();
            System.out.println(lineIn);
            try{

                if(!lineIn.startsWith("220 ")){
                    throw new RuntimeException("Erreur de connexion");
                }

                lineOut = "EHLO bonjour\r\n";
                System.out.print(lineOut);
                out.write(lineOut);
                out.flush();

                boolean testLecture = true;
                lineIn = in.readLine();

                //------------------------------------------------------------------------------------------------------
                while(testLecture){
                    System.out.println(lineIn);
                    if(lineIn.startsWith("250-")){
                        lineIn = in.readLine();
                    }else if(lineIn.startsWith("250 ")){
                        testLecture = false;
                    }else{
                        throw new RuntimeException("Exception : Connection Impossible");
                    }
                }

                //------------------------------------------------------------------------------------------------------
                String sender = group.getSender();//"olivier.tissot-daguette@heig-vd.ch";
                lineOut = "MAIL FROM: " + sender + "\r\n";
                System.out.print(lineOut);
                out.write(lineOut);
                out.flush();

                //------------------------------------------------------------------------------------------------------

                lineIn = in.readLine();
                if(lineIn.startsWith("250 ")){
                    System.out.println(lineIn);
                }else{
                    throw new RuntimeException("Exception : Email d'envoi erreur");
                }

                //------------------------------------------------------------------------------------------------------

                for(int i = 0; i < group.getVictims().size(); ++i){
                    String mailTo = group.getVictims().get(i);//"joel.dossantosmatias@heig-vd.ch";

                    mailRCPT(mailTo, out, in);
                }


                if(!copyCarbon.equals("0")){
                    mailRCPT(copyCarbon, out, in);
                }

                //------------------------------------------------------------------------------------------------------

                lineOut = "DATA\r\n";
                System.out.print(lineOut);
                out.write(lineOut);
                out.flush();

                //------------------------------------------------------------------------------------------------------

                lineIn = in.readLine();
                if(lineIn.startsWith("354 ")) {
                    System.out.println(lineIn);
                }else{
                    throw new RuntimeException("Exception : Email d'envoi erreur");
                }

                //------------------------------------------------------------------------------------------------------

                lineOut = "From: " + sender + "\r\n";
                System.out.print(lineOut);
                out.write(lineOut);

                String mailTo = "";
                for(int i = 0; i < group.getVictims().size(); ++i) {
                    mailTo += group.getVictims().get(i) + ", ";//"joel.dossantosmatias@heig-vd.ch";
                }
                mailTo = mailTo.substring(0, mailTo.length()-2);

                lineOut = "To: " + mailTo + "\r\n";
                System.out.print(lineOut);
                out.write(lineOut);

                if(!copyCarbon.equals("0")){
                    lineOut = "Cc: " + copyCarbon + "\r\n";
                    System.out.print(lineOut);
                    out.write(lineOut);
                }

                if(!subject.equals("0")){
                    lineOut = "Subject: =?utf-8?B?" + Base64.getEncoder().encodeToString(subject.getBytes(StandardCharsets.UTF_8)) + "?=\r\n";//"Subject: =?utf-8?Q?" + subject + "?=\r\n";
                    System.out.print(lineOut);
                    out.write(lineOut);
                }

                out.write("Content-Type: text/plain: charset=\"utf-8\"\r\n");
                String message = prank.getMessage();
                out.write("\r\n" + message);

                System.out.print(message);

                out.write("\r\n.\r\n");
                out.flush();

                //------------------------------------------------------------------------------------------------------

                lineIn = in.readLine();
                if(lineIn.startsWith("250 ")) {
                    System.out.println(lineIn);
                }else{
                    throw new RuntimeException("Exception : Envoi d'email prob");
                }

                //------------------------------------------------------------------------------------------------------
                lineOut = "QUIT\r\n";
                System.out.print(lineOut);
                out.write(lineOut);
                out.flush();

                //------------------------------------------------------------------------------------------------------

                lineIn = in.readLine();
                if(lineIn.startsWith("221 ")) {
                    System.out.println(lineIn);
                }else{
                    throw new RuntimeException("Exception : Problem de fermeture de connexion");
                }

                //------------------------------------------------------------------------------------------------------

            }catch (RuntimeException e){

                LOG.log(Level.SEVERE, e.toString(), e);

                lineOut = "RSET\r\n";
                System.out.print(lineOut);
                out.write(lineOut);
                out.flush();
                lineIn = in.readLine();
                boolean tmp;
                do{
                    if(lineIn.startsWith("250 ")) {
                        tmp = false;
                        System.out.println("Abort mission");
                    }else{
                        tmp = true;
                    }
                }while(tmp);

                do{
                    lineOut = "QUIT\r\n";
                    System.out.print(lineOut);
                    out.write("QUIT\r\n");
                    out.flush();

                    //------------------------------------------------------------------------------------------------------

                    lineIn = in.readLine();
                    if(lineIn.startsWith("221 ")) {
                        tmp = false;
                        System.out.println("Mission aborted");
                    }else{
                        tmp = true;
                    }
                }while(tmp);


            }finally{
                in.close();
                out.close();
                clientSocket.close();
            }

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

    private void mailRCPT(String mailTo, BufferedWriter out, BufferedReader in) throws RuntimeException, IOException {
        String line;
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
}
