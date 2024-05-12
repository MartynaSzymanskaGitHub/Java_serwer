import java.io.*;
import java.net.*;

public class serwer {
private ServerSocket serverSocket;
public int port = 6666;
public int counterClients = 0;

 void start() throws IOException{
    serverSocket = new ServerSocket(port);
   
    System.out.println("Welcome to my server");
        while (true){
             Socket clientSocket = serverSocket.accept(); 
            new Thread(new klientHandler(clientSocket)).start();
            System.out.println("Client connected");
           
        }
}

 public static void main(String[] args) throws IOException {
        serwer server = new serwer();
        server.start();
    }
}

    
