import java.io.*;
import java.net.*;

public class serwer {
private ServerSocket serverSocket;
public int port = 6666;

 void start() throws IOException, ClassNotFoundException{
    serverSocket = new ServerSocket(port);
    int counterClients = 0;
    System.out.println("Welcome to my server");
        while (true){
             Socket clientSocket = serverSocket.accept();
            new Thread(new klientHandler(clientSocket)).start();
            counterClients++;
            System.out.println("Waiting for the client request. Current connected ("+counterClients+")\n");
        }
}

public void stop() throws IOException {
            serverSocket.close();
        }

 public static void main(String[] args) throws IOException, ClassNotFoundException {
        serwer server = new serwer();
        server.start();
    }


}

    
