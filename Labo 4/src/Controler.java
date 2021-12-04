import prank.Prank;
import prank.Group;

import java.io.*;
import java.net.Socket;
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
        System.out.println("Sender : " + prank.getGroup().getSender());
        System.out.println("\nVictims : ");
        for(int i = 0; i < prank.getGroup().getSizeGroup(); ++i)
            System.out.println(prank.getGroup().getVictims().get(0));
        System.out.println("\nMessage : \n" + prank.getMessage());
        /*

        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        Socket clientSocket = null;
        BufferedWriter out = null;
        BufferedReader in = null;

        try {
            clientSocket = new Socket("smtp.heig-vd.ch", 25);
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));

            LOG.log(Level.INFO, "*** Response sent by the server: ***");
            String line;

            line = in.readLine();
            System.out.println(line);
            String parts[] = line.split(" ", 2);

            if(line.startsWith("220 ")){
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
                String sender = prank.getGroup().getSender();//"olivier.tissot-daguette@heig-vd.ch";
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

                String mailTo = prank.getGroup().getVictims()[0];//"joel.dossantosmatias@heig-vd.ch";
                // A CHECK COMMENT INDIQUER PLUSIEURS EMAILS

                out.write("RCPT TO: " + mailTo + "\r\n");
                out.flush();

                //------------------------------------------------------------------------------------------------------

                line = in.readLine();
                if(line.startsWith("250 ")) {
                    System.out.println(line);
                }else{
                    throw new RuntimeException("Exception : Email d'envoi erreur");
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
                out.write("To: " + mailTo + "\r\n");
                //out.write("Cc: thibault.seem@heig-vd.ch\n");

                String message = prank.getMessage();
                out.write(message);

                out.write(".\r\n");
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

            }else{
                System.out.println("Erreur de connexion");
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
        }*/

    }
}
