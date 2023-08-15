import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SimpleTCPClient {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public void start(String serverIp, int serverPort) throws IOException {
        // Cria socket de comunicação com o servidor e obtém fluxos de entrada/saída
        System.out.println("[C1] Conectando ao servidor " + serverIp + ":" + serverPort);
        socket = new Socket(serverIp, serverPort);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());

        // Loop de chat
        Scanner scanner = new Scanner(System.in);
        String msg;
        while (true) {
            System.out.print("Você: ");
            msg = scanner.nextLine();
            if (msg.equalsIgnoreCase("sair")) {
                break;
            }
            
            // Envia mensagem para o servidor
            output.writeUTF(msg);

            // Recebe e exibe a resposta do servidor
            String resposta = input.readUTF();
            System.out.println("Servidor: " + resposta);
        }

        scanner.close();
    }

    public void stop() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverIp = "127.0.0.1"; // Altere para o IP real do servidor
        int serverPort = 6666;
        try {
            // Cria e executa o cliente
            SimpleTCPClient client = new SimpleTCPClient();
            client.start(serverIp, serverPort);

            // Encerra o cliente
            client.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
