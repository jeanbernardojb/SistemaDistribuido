import java.io.*;
import java.net.*;

public class SimpleTCPServer {
    public static void main(String args[]) {
        try {
            int serverPort = 6666;
            ServerSocket serverSocket = new ServerSocket(serverPort);

            System.out.println("Servidor esperando por conexões na porta " + serverPort);

            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

            String inputLine, outputLine;

            while (true) {
                System.out.print("Você: ");
                inputLine = consoleIn.readLine();
                out.println(inputLine);

                if (inputLine.equals("bye")) {
                    break;
                }

                outputLine = in.readLine();
                System.out.println("Cliente: " + outputLine);

                if (outputLine.equals("bye")) {
                    break;
                }
            }

            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
