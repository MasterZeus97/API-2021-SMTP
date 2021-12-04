package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

//Mettre le nom des fichier en dur
//Possibilité de faire un fichier par email
//Garder les choses simple

/**
 * Calculator client implementation
 */
public class Client {

    private static final Logger LOG = Logger.getLogger(Client.class.getName());


    /**
     * Main function to run client
     *
     * @param args no args required
     */
    public static void main(String[] args) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        BufferedReader stdin = null;



        Socket clientSocket = null;
        BufferedWriter out = null;
        BufferedReader in = null;

        stdin = new BufferedReader(new InputStreamReader(System.in));

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
                String sender = "thibault.seem@gmail.com";//"olivier.tissot-daguette@heig-vd.ch";
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

                String mailTo = "seemth1310@gmail.com";//"joel.dossantosmatias@heig-vd.ch";

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
                out.write("Subject: Prout\r\n");

                String message = "Contrairement à toi\r\n";
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



            /*while(true){
                String requete = stdin.readLine();
                out.write(requete + "\n");
                out.flush();
                if(requete.equals("Bye Bye"))
                    break;

                line = in.readLine();
                LOG.log(Level.INFO, line);
                System.out.println(line);
            }*/

            in.close();
            out.close();
            stdin.close();
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
