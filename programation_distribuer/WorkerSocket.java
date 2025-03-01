package programation_distribuer;

import programation_partager.Worker;
import java.io.*;
import java.net.*;

/** Worker is a server. It computes PI using the Monte Carlo method. */
public class WorkerSocket {
    static int port = 25545;
    private static boolean isRunning = true;

    public static void main(String[] args) throws Exception {
        // Vérifier si un port est fourni en argument
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number, using default: " + port);
            }
        }

        ServerSocket s = new ServerSocket(port);
        System.out.println("Worker started on port " + port);

        while (isRunning) {
            Socket soc = s.accept();
            System.out.println("Connected to Master");

            BufferedReader reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())), true);

            String message;
            while ((message = reader.readLine()) != null) {
                if (message.equals("END")) {
                    isRunning = false;
                    break;
                }

                try {
                    int totalCount = Integer.parseInt(message);
                    System.out.println("Received totalCount = " + totalCount);

                    Worker worker = new Worker(totalCount);
                    long insideCount = worker.call();

                    System.out.println("Computed " + insideCount + " points inside quarter-circle");
                    writer.println(insideCount);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + message);
                    writer.println("ERROR");
                }
            }

            reader.close();
            writer.close();
            soc.close();
        }

        s.close();
        System.out.println("Worker shutting down.");
    }
}
