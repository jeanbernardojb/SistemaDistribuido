import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SimpleTCPServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();

    public void start(int port) throws IOException {
        System.out.println("[S1] Criando server socket para aguardar conexões de clientes em loop");
        serverSocket = new ServerSocket(port);

        while (true) {
            System.out.println("[S2] Aguardando conexão em: " + serverSocket.getLocalSocketAddress());
            Socket socket = serverSocket.accept();
            System.out.println("[S3] Conexão estabelecida com cliente: " + socket.getRemoteSocketAddress());

            ClientHandler clientHandler = new ClientHandler(socket);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
            for (ClientHandler client : clients) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream input;
        private DataOutputStream output;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        }

        public void run() {
            try {
                while (true) {
                    String msg = input.readUTF();
                    System.out.println("Cliente " + socket.getRemoteSocketAddress() + ": " + msg);

                    // Encaminha a mensagem para todos os outros clientes conectados
                    broadcastMessageToOthers("Cliente " + socket.getRemoteSocketAddress() + ": " + msg, this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        public void close() {
            try {
                input.close();
                output.close();
                socket.close();
                clients.remove(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String msg) throws IOException {
            output.writeUTF(msg);
        }
    }

    private void broadcastMessageToOthers(String msg, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                try {
                    client.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        int serverPort = 6666;
        try {
            // Inicia e roda o servidor
            SimpleTCPServer server = new SimpleTCPServer();
            server.start(serverPort);

            // Finaliza o servidor
            server.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
