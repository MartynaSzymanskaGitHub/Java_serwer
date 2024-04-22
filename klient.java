
import java.io.*;
import java.net.*;
import java.util.Scanner;


public class klient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws UnknownHostException, IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        return  in.readLine();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
    public static void main(String[] args) throws IOException {
        klient client = new klient();
        client.startConnection("127.0.0.1", 6666);
        
         try (Scanner scanner = new Scanner(System.in)) {
            String inputLine;
            while (true) {
                System.out.print("Enter message for server (or 'quit' to end): ");
                inputLine = scanner.nextLine();
                if ("quit".equalsIgnoreCase(inputLine)) {
                    break;
                }
                String response = client.sendMessage(inputLine);
                if(inputLine.equals("bye")){ 
                    client.stopConnection();
                    break;
                    }
                System.out.println(response);
            }
        }
}
}