import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationServer {
    ArrayList<Client> clients = new ArrayList<>();
    Map<String, String> autorizationData = new HashMap<>();
    ServerSocket serverSocket;

    AuthorizationServer() throws IOException {
        serverSocket = new ServerSocket(1234);
        autorizationData.put("qwerty", "123");
        autorizationData.put("asdfgh", "456");
        autorizationData.put("zxcvbn", "789");
    }

    public void run() throws IOException {
        while (true) {
            System.out.println("Waiting...");
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                clients.add(new Client(socket, this));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        AuthorizationServer server = new AuthorizationServer();
        server.run();
    }
}
