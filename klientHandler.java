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

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if ("".equals(inputLine)) {
                    out.println("bye");
                    break;
                }
                String newInput = inputLine;
                System.out.println("Message from client: "+inputLine);
                int czas = inputLine.length()-1;
                ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
                executorService.schedule(() -> {
                    out.println("Server response after delay: " + newInput);
                }, czas, TimeUnit.SECONDS);
                executorService.shutdown(); // Zamknij executor po wykonaniu zadania
            }
             
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
