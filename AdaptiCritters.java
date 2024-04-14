/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
// import java.text.*;

public class AdaptiCritters extends FitnessFunction{

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/


/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/

	//  Assumes no more than 100 values in the data file
	public static int[] testValue = new int[100];
	public static int[][] genome; // Whole genome for entire scenario

	static { // Fill genome array with genes and their alleles
		try {
			File template = new File(Parameters.dataInputFileName);
			Scanner scanner = new Scanner(template);

			int numGenes = scanner.nextInt();
			genome = new int[numGenes][];

			for (int i = 0; i < numGenes; i++) {
				int numAlleles = scanner.nextInt();
				genome[i] = new int[numAlleles];
				
				for (int j = 0; j < numAlleles; j++) {
					if (scanner.hasNextInt()) {
						genome[i][j] = scanner.nextInt();
					} else if (scanner.hasNext()) {
						String allele = scanner.next();
						genome[i][j] = allele.equals("x") ? -1000000 : Integer.parseInt(allele);
					}
				}
			}
			scanner.close();

		} catch (FileNotFoundException e) {
			System.out.println(Parameters.dataInputFileName + " does not exist");
			e.printStackTrace();
		}
	}
/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public AdaptiCritters () throws java.io.IOException {

		name = "AdaptiCritters Simulation";

	}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

//  COMPUTE A CHROMOSOME'S RAW FITNESS *************************************

	public void doRawFitness(Chromo X){
		double prevFitness = X.rawFitness;

		X.rawFitness = 0;
    	for (int i = 0; i < X.chromo.length; i++) {
        	int allele = X.chromo[i];
        	if (allele > -1000000)
				{
					X.rawFitness += genome[i][allele];
				}
			else
			{
				X.rawFitness = -1000000;
				break;
			}
    	}

		// Update average fitness of this individual
		X.avgFitness = ((prevFitness * Search.G) + X.rawFitness) / (Search.G + 1);
	}

//  PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE *********************************

	public void doPrintGenes(Chromo X, FileWriter output) throws java.io.IOException{

		for (int i=0; i<Parameters.numGenes; i++){
			Hwrite.right(X.getGeneAlpha(i),11,output);
		}
		output.write("   RawFitness");
		output.write("\n        ");
		for (int i=0; i<Parameters.numGenes; i++){
			Hwrite.right(X.getIntGeneValue(i),11,output);
		}
		Hwrite.right((int) X.rawFitness,13,output);
		output.write("\n\n");
		return;
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

}   // End of NumberMatch.java *************************************************


