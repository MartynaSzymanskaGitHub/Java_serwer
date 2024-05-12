
import java.io.*;
import java.net.*;
import java.util.Scanner;


public class klient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (ConnectException e) {
            System.out.println("Nie mozna polaczyc sie z serwerem. Serwer nie jest uruchomiony");
            System.exit(1); 
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas nawiązywania połączenia: " + e.getMessage());
            return;
        }
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

    static int convertStringToTime(String text){
        int num = 0;
        int colonIndex = text.indexOf(':');

        if (colonIndex == -1) {
            return -1; 
        }
        String timePart = text.substring(colonIndex + 1).trim();
        if (timePart.isEmpty()) {
            return -1; 
        }

        try {
            num = Integer.parseInt(timePart);
        } catch (NumberFormatException e) {
            return -1; 
        }
        return num; 
        }

    public static void main(String[] args) throws IOException {
        klient client = new klient();
        client.startConnection("127.0.0.1", 6666);
        
         try (Scanner scanner = new Scanner(System.in)) {
            String inputLine;
            while (true) {
                System.out.print("Enter message for server (or 'exit' to end): ");
                inputLine = scanner.nextLine();
                if(inputLine.toLowerCase().startsWith("exit")){
                    client.stopConnection();
                    break;
                }
                if (!inputLine.contains(":")){
                    client.stopConnection();
                    throw new errorMessage("Given wrong format of message. Example: 'Message :seconds ' Breaking connections.");
                }
                if (inputLine.length() > 200 ) {
                    System.out.println("Given message is above 200 characters. Message not sent to the server");    
                    continue;
                } 
                int time = convertStringToTime(inputLine);
                if (time == -1 || time == 0) {
                    System.out.println("Given time is wrong. Message not sent to the server");
                    continue;
                } else if(time == -2) {
                    System.out.println("Given time is not intiger. Message not sent to the server");
                    continue;
                }

                else {
                String response = client.sendMessage(inputLine);
                System.out.println(response);
                }
            }
        } catch (IOException | errorMessage e) {
            System.out.println("Error: " + e.getMessage());
        }
}
}