package programation_distribuer;

import java.io.*;
import java.net.*;
import java.util.Random;

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

        while (true) {
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

                int totalCount = Integer.parseInt(message);
                int insideCount = computeMonteCarlo(totalCount);
                System.out.println("Computed " + insideCount + " points inside quarter-circle");
                writer.println(insideCount);
            }

            reader.close();
            writer.close();
            soc.close();

            if (!isRunning) break;
        }

        s.close();
        System.out.println("Worker shutting down.");
    }

    /** Monte Carlo simulation to estimate Pi */
    private static int computeMonteCarlo(int totalCount) {
        int inside = 0;
        Random rand = new Random();

        for (int i = 0; i < totalCount; i++) {
            double x = rand.nextDouble();
            double y = rand.nextDouble();
            if (x * x + y * y <= 1) inside++;
        }

        return inside;
    }
}
