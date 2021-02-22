package py.com.eik;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoMultiServer {

    final static Level LOG_DATA = Level.forName("LOG_DATA", 550);

    private static Logger logger = LogManager.getLogger(EchoMultiServer.class);

    private ServerSocket serverSocket;

    public void start(String port) {
        int portNumber = Integer.parseInt(port);
        start(portNumber);
    }

    public void start(int port) {
        logger.info("Iniciando servidor en puerto " + port);

        try {
            serverSocket = new ServerSocket(port);

            while (true)
                new EchoClientHandler(serverSocket.accept()).start();
        } catch (BindException e) {
            System.out.println("Puerto ya se utilizaaa!!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }

    }

    public void stop() {
        try {

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    }
                    out.println(inputLine);
                    logger.info(inputLine);
                }

                in.close();
                out.close();
                clientSocket.close();
                logger.info("Cerrando clientSocket");

            } catch (IOException e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {

        logger.log(LOG_DATA, "Esto es LOG_DATA LOG_DATA LOG_DATA LOG_DATA");

        EchoMultiServer server = new EchoMultiServer();
        String serverPort = PropertiesLoader.getServerPort();
        server.start(serverPort);
    }

}