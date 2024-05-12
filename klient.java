
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

    static int convertStringToTime(String text){
        int num = 0;
        int factor = 1;  
        int numberlen = 0;
        boolean isIntiger = false;
        boolean isSpace = false;
        
        if (isNegative(text)) {
            return -1;
        }
        for (int i = text.length() - 1; i >= 0; i--) {
            char c = text.charAt(i);
            if (Character.isDigit(c)) {
                if(isSpace){
                    return -1;
                }
                isIntiger = true;
                int digitValue = c - '0';
                num += factor * digitValue;  
                factor *= 10;  
                numberlen++;
            }
            if((c == ',' || c =='.') && isIntiger == true){
                return -2;
            }
            if(c ==' ' && isIntiger == true ){
                isSpace = true;
            }
        }
        if (numberlen < 1) {
            return -1;
        }
        return num;
    }

    static boolean isNegative(String text){
        char character2 =' '; 
        for (int i = text.length() - 1; i >= 0; i--) {
            char character = text.charAt(i);
            if (i> 0) {
                character2 = text.charAt(i-1);
            }
            if (Character.isDigit(character) && character2 == '-') {
                return true; 
            }
        }
        return false;
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
                    throw new errorMessage("Given wrong format of message. Example: 'Message :seconds '");
                }
                if (inputLine.equals("") ) { 
                    System.out.println("Given empty message. Message not sent to the server");
                    continue;
                } else if (inputLine.length() > 200 ) {
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