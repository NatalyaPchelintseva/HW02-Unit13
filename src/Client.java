import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Client implements Runnable {
    Socket socket;
    Scanner in;
    PrintStream out;
    AuthorizationServer server;

    Client(Socket socket, AuthorizationServer server) {
        this.socket = socket;
        this.server = server;
        new Thread(this).start();
    }

    public void receive(String message) {
        out.println(message);
    }

    public boolean logIn(String login, String password) {
        int size = server.autorizationData.size();
        String[] logins = server.autorizationData.keySet().toArray(new String[size]);
        String[] passwords = server.autorizationData.values().toArray(new String[0]);
        if (Arrays.asList(logins).contains(login)) {
            if (server.autorizationData.get(login).equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            in = new Scanner(is);
            out = new PrintStream(os);

            out.println("Log in, please, and enter password.");
            String inputLogin = in.nextLine();
            String inputPassword = in.nextLine();
            while (!inputLogin.equals("bye") && !inputPassword.equals("bye")) {
                if (logIn(inputLogin, inputPassword)) {
                    this.receive("you are connected to the server");
                }
                else {
                    this.receive("the login and/or password are wrong. Try again.");
                }
                inputLogin = in.nextLine();
                inputPassword = in.nextLine();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
