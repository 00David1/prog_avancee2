// Estimate the value of Pi using Monte-Carlo Method, using parallel program
package programation_partager;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
class PiMonteCarlo {
	AtomicInteger nAtomSuccess;
	int nThrows;
	double value;
	class MonteCarlo implements Runnable {
		@Override
		public void run() {
			double x = Math.random();
			double y = Math.random();
			if (x * x + y * y <= 1)
				nAtomSuccess.incrementAndGet();
		}
	}
	public PiMonteCarlo(int i) {
		this.nAtomSuccess = new AtomicInteger(0);
		this.nThrows = i;   // c'est n_totale
		this.value = 0;    // c'est pi
	}
	public double getPi() {
		//int nProcessors = Runtime.getRuntime().availableProcessors();
		int nProcessors = 12;
		ExecutorService executor = Executors.newWorkStealingPool(nProcessors);
		for (int i = 1; i <= nThrows; i++) {
			Runnable worker = new MonteCarlo();
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		value = 4.0 * nAtomSuccess.get() / nThrows;
		return value;
	}
}
public class Assignment102 {
	public static void main(String[] args) throws Exception {
		int nProc = 12;
		int ntot = 12000000;
		PiMonteCarlo PiVal = new PiMonteCarlo(ntot);
		long startTime = System.currentTimeMillis();
		double value = PiVal.getPi();
		long stopTime = System.currentTimeMillis();
		System.out.println("Approx value:" + value);
		System.out.println("Difference to exact value of pi: " + (value - Math.PI));
		//System.out.println("Error: " + (value - Math.PI) / Math.PI * 100 + " %");
		System.out.println("Error: " + (Math.abs((value - Math.PI)) / Math.PI));
		System.out.println("Available processors: " + nProc);//+ Runtime.getRuntime().availableProcessors());
		System.out.println("Time Duration: " + (stopTime - startTime) + "ms");
		System.out.println("________________________________________________");
		try (FileWriter writer = new FileWriter("data/Assignment102_scal_faible_G26_8c.csv", true)) {
			boolean isFileEmpty = new java.io.File("data/Assignment102_scal_faible_G26_8c.csv").length() == 0;
			if (isFileEmpty) {
				writer.append("Error,Ntot,Threads,Duration\n");
			}
			writer.append( (Math.abs((value - Math.PI)) / Math.PI) + "," + (ntot) + "," + nProc + "," + (stopTime - startTime) + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Pi.main(args);
	}
}
