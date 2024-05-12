import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class klientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public klientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    int convertStringToTime(String text){
        int num = 0;
        int factor = 1;
        for (int i = text.length() - 1; i >= 0; i--) {
            char c = text.charAt(i);
            if (Character.isDigit(c)) {
                int digitValue = c - '0';
                num += factor * digitValue;
                factor *= 10;
            }
        }
        return num;
    }
    String changeString(String inputLine){
        String divide = ":";
        int firstIndexOfDivide = inputLine.indexOf(divide);
        return inputLine.substring(0, firstIndexOfDivide);
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while (true) { 
                try {
                    inputLine = in.readLine();
                    if(inputLine == null || inputLine.equalsIgnoreCase("exit")){
                        System.out.println("Connection broken. Disconnect client");
                        out.close();
                        in.close();
                        clientSocket.close();
                        break;
                    }
                    String newInput = changeString(inputLine);
                    System.out.println("Message from client: " + newInput);
                    int time = convertStringToTime(inputLine);
                    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
                    executorService.schedule(() -> {
                        out.println("Server response after delay: " + newInput);
                    }, time, TimeUnit.SECONDS);
                    executorService.shutdown();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;  
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
