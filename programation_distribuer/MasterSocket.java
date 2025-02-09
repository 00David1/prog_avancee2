package programation_distribuer;

import java.io.*;
import java.net.*;
import java.util.Random;

/** Master is a client. It makes requests to multiple workers. */
public class MasterSocket {
	static int maxServer = 8;
	static int[] tab_port = new int[maxServer];
	static String[] tab_total_workers = new String[maxServer];
	static final String ip = "127.0.0.1";
	static BufferedReader[] reader = new BufferedReader[maxServer];
	static PrintWriter[] writer = new PrintWriter[maxServer];
	static Socket[] sockets = new Socket[maxServer];

	public static void main(String[] args) throws Exception {
		// Paramètres Monte Carlo
		int totalCount = 16000000; // Nombre de points par worker
		int total = 0;
		double pi;

		int numWorkers = maxServer;
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		String s;

		System.out.println("#########################################");
		System.out.println("# Computation of PI by MC method        #");
		System.out.println("#########################################");

		// Demande du nombre de workers
		System.out.println("\nHow many workers for computing PI (< maxServer = " + maxServer + "): ");
		try {
			s = bufferRead.readLine();
			numWorkers = Integer.parseInt(s);
			if (numWorkers > maxServer) numWorkers = maxServer;
		} catch (IOException | NumberFormatException e) {
			System.out.println("Invalid input, using default number of workers: " + numWorkers);
		}

		// Demande des ports des workers
		for (int i = 0; i < numWorkers; i++) {
			System.out.println("Enter worker " + i + " port: ");
			try {
				s = bufferRead.readLine();
				tab_port[i] = Integer.parseInt(s);
			} catch (IOException | NumberFormatException e) {
				System.out.println("Invalid port number, using default: " + (25545 + i));
				tab_port[i] = 25545 + i;
			}
		}

		// Création des connexions socket
		for (int i = 0; i < numWorkers; i++) {
			sockets[i] = new Socket(ip, tab_port[i]);
			System.out.println("Connected to worker on port " + tab_port[i]);

			reader[i] = new BufferedReader(new InputStreamReader(sockets[i].getInputStream()));
			writer[i] = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockets[i].getOutputStream())), true);
		}

		String message_repeat = "y";
		long stopTime, startTime;

		while (message_repeat.equalsIgnoreCase("y")) {
			total = 0; // Réinitialisation du total

			startTime = System.currentTimeMillis();

			// Envoi du nombre de points aux workers
			for (int i = 0; i < numWorkers; i++) {
				writer[i].println(totalCount);
			}

			// Réception des résultats
			for (int i = 0; i < numWorkers; i++) {
				tab_total_workers[i] = reader[i].readLine();
				total += Integer.parseInt(tab_total_workers[i]);
			}

			// Calcul de π
			pi = 4.0 * total / (totalCount * numWorkers);

			stopTime = System.currentTimeMillis();

			// Affichage des résultats
			System.out.println("\nEstimated programation_partager.Pi: " + pi);
			System.out.println("Error: " + (Math.abs((pi - Math.PI)) / Math.PI));
			System.out.println("Total points: " + (totalCount * numWorkers));
			System.out.println("Workers used: " + numWorkers);
			System.out.println("Time (ms): " + (stopTime - startTime) + "\n");

			// Demande de répétition
			System.out.println("Repeat computation? (y/N): ");
			try {
				message_repeat = bufferRead.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Fermeture des connexions
		for (int i = 0; i < numWorkers; i++) {
			writer[i].println("END");
			reader[i].close();
			writer[i].close();
			sockets[i].close();
		}
	}
}
