package programation_partager;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Approximates PI using the Monte Carlo method.  Demonstrates
 * use of Callables, Futures, and thread pools.
 */
public class Pi 
{
    public static void main(String[] args) throws Exception 
    {
	long total=0;
	int nbWorker = 1; // Oui c'est ça qu'il faut changer 1,2,3,...12
	int ntot = 1200000000*nbWorker;  // ça aussi ancienne valeur : 100000
	// 10 workers, 50000 iterations each
	total = new Master().doRun(ntot/nbWorker, nbWorker);
	System.out.println("total from programation_partager.Master = " + total);
    }
}

/**
 * Creates workers to run the Monte Carlo simulation
 * and aggregates the results.
 */
class Master {
    public long doRun(int totalCount, int numWorkers) throws InterruptedException, ExecutionException 
    {

	long startTime = System.currentTimeMillis();

	// Create a collection of tasks
	List<Callable<Long>> tasks = new ArrayList<Callable<Long>>(); // Collection de tache qui affiche des résultat
	for (int i = 0; i < numWorkers; ++i) 
	    {
		tasks.add(new Worker(totalCount));
	    }
    
	// Run them and receive a collection of Futures
	ExecutorService exec = Executors.newFixedThreadPool(numWorkers);
	List<Future<Long>> results = exec.invokeAll(tasks);
	long total = 0;
    
	// Assemble the results.
	for (Future<Long> f : results)
	    {
		// Call to get() is an implicit barrier.  This will block
		// until result from corresponding worker is ready.
		total += f.get();
	    }
	double pi = 4.0 * total / totalCount / numWorkers;

	long stopTime = System.currentTimeMillis();

	System.out.println("\nprogramation_partager.Pi : " + pi );
	System.out.println("Error: " + (Math.abs((pi - Math.PI)) / Math.PI)); //+ (value - Math.PI) / Math.PI * 100 + " %")
	System.out.println("Ntot: " + totalCount*numWorkers);
	System.out.println("Available processors: " + numWorkers);
	System.out.println("Time Duration (ms): " + (stopTime - startTime) + "\n");

	//System.out.println( (Math.abs((pi - Math.PI)) / Math.PI) +" "+ totalCount*numWorkers +" "+ numWorkers +" "+ (stopTime - startTime));
// Ajout
		try (FileWriter writer = new FileWriter("data/Pi_scal_faible_G26_8c.csv", true)) {
			boolean isFileEmpty = new java.io.File("data/Pi_scal_faible_G26_8c.csv").length() == 0;
			if (isFileEmpty) {
				writer.append("Error,Ntot,Threads,Duration\n");
			}
			writer.append((Math.abs((pi - Math.PI)) / Math.PI) + "," + (totalCount * numWorkers) + "," + numWorkers + "," + (stopTime - startTime) + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
// fin ajout
	exec.shutdown();
	return total;
    }
}

/**
 * Task for running the Monte Carlo simulation.
 */
class Worker implements Callable<Long> 
{   
    private int numIterations;
    public Worker(int num) 
	{ 
	    this.numIterations = num; 
	}

  @Override
      public Long call() 
      {
	  long circleCount = 0;
	  Random prng = new Random ();
	  for (int j = 0; j < numIterations; j++) 
	      {
		  double x = prng.nextDouble();
		  double y = prng.nextDouble();
		  if ((x * x + y * y) < 1)  ++circleCount;
	      }
	  return circleCount;
      }
}
