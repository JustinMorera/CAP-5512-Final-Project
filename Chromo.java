import java.util.ArrayList;

/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

// import java.io.*;
// import java.util.*;
// import java.text.*;
import java.util.ArrayList;

public class Chromo
{
/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/

	public static int cumPop = 0; // Cumulative population

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	public int[] chromo;
	public double rawFitness;
	public double sclFitness;
	public double proFitness;
	public int id; // Tracks individual by ID# for phylogeny
    public ArrayList<Chromo> parents;
    public int startGen;
    public int endGen;

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	private static double randnum;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Chromo(){
		//  Set gene values to a randum allele in binary, based on available alleles of each gene
		chromo = new int[AdaptiCritters.genome.length];
		for (int i = 0; i < AdaptiCritters.genome.length; i++){
			int allele = Search.r.nextInt(AdaptiCritters.genome[i].length);
				this.chromo[i] = allele;
		}

        this.parents = new ArrayList<Chromo>();
		this.startGen = Search.G; // Current generation
		this.endGen = -1; // Set when fitness drops below 0 or threshold
		this.id = cumPop++; // ID = current cumPop then increments cumPop by 1
		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	//  Get Alpha Represenation of a Gene **************************************

	public String getGeneAlpha(int geneID){
		return (Integer.toString(this.chromo[geneID]));
	}

	//  Get Integer Value of a Gene (Positive or Negative, 2's Compliment) ****

	public int getIntGeneValue(int geneID){
		return (this.chromo[geneID]);
	}

	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){
	  switch (Parameters.mutationType){
	    case 1:     //  Replace with new random number
	 		for (int i = 0; i < this.chromo.length; i++) {
        		if (Search.r.nextDouble() < Parameters.mutationRate) {
            		int newGene = Search.r.nextInt(AdaptiCritters.genome.length);
            		this.chromo[i] = newGene;
        		}
    		}
	 	default:
 		  System.out.println("ERROR - No mutation method selected");
	 	}
	 }

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	//  Select a parent for crossover ******************************************

	public static int selectParent(ArrayList<Integer> chosen){

		// Random Selection
		randnum = Search.r.nextDouble();
		int parentIndex = -1;
		do {
			parentIndex = (int) (randnum * Search.member.size());
		} while (chosen.contains(parentIndex));

		return(parentIndex);
	}

	//  Produce a new child from two parents  **********************************
	// Simply sends child's phenotype to Search.java which runs Constructor

	public static int[] mateParents(Chromo parent1, Chromo parent2){

		// int xoverPoint1;
		// int xoverPoint2;
		int[] phenotype = new int[AdaptiCritters.genome.length];

		switch (Parameters.xoverType){

		case 1:     //  Single Point Crossover

			//  Select crossover point
			// xoverPoint1 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));

	// 		//  Create child chromosome from parental material
	// 		child1.chromo = parent1.chromo.substring(0,xoverPoint1) + parent2.chromo.substring(xoverPoint1);
	// 		child2.chromo = parent2.chromo.substring(0,xoverPoint1) + parent1.chromo.substring(xoverPoint1);
            
            // // Associate parents to children (move/modify this block as you see fit when crossover is implemented);
            // child1.parents.add(parent1);
            // child1.parents.add(parent2);
            // child2.parents.add(parent1);
            // child2.parents.add(parent2);

	// 		break;

		case 2:     //  Two Point Crossover

		case 3:     //  Uniform Crossover
			for (int i = 0; i < AdaptiCritters.genome.length; i++)
			{
				double parentChoice = Search.r.nextDouble();
				phenotype[i] = (parentChoice % 2) == 0 ? parent1.chromo[i] : parent2.chromo[i];
			}
			break;
		default:
			System.out.println("ERROR - Bad crossover method selected");
		}
		return phenotype;
	}

	//  Produce a new child from a single parent  ******************************

	public static void mateParents(int pnum, Chromo parent, Chromo child){

		//  Create child chromosome from parental material
		child.chromo = parent.chromo;

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromo targetA, Chromo sourceB){

		targetA.chromo = sourceB.chromo;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

}   // End of Chromo.java ******************************************************
