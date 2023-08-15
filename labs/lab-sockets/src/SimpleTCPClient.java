import java.io.*;
import java.net.*;

public class SimpleTCPClient {
    public static void main(String[] args) {
        String serverIp = "127.0.0.1";
        int serverPort = 6666;

        try {
            Socket socket = new Socket(serverIp, serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

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
                System.out.println("Servidor: " + outputLine);

                if (outputLine.equals("bye")) {
                    break;
                }
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
