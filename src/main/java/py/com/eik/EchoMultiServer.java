package py.com.eik;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoMultiServer {

    private static Logger LOG = LogManager.getLogger(EchoMultiServer.class);
    private ServerSocket serverSocket;

    public void start(int port) {
        LOG.info("Iniciando servidor");
        try {
            serverSocket = new ServerSocket(port);
            while (true)
                new EchoClientHandler(serverSocket.accept()).start();

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
                    LOG.info(inputLine);
                }

                in.close();
                out.close();
                clientSocket.close();
                LOG.info("Cerrando clientSocket");

            } catch (IOException e) {
                e.printStackTrace();
                LOG.debug(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        EchoMultiServer server = new EchoMultiServer();
        server.start(5555);
    }

}