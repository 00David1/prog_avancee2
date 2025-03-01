package programation_partager;


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

