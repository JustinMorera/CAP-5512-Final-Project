/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

// import java.io.*;
// import java.util.*;
// import java.text.*;

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
    public List<Chromo> parents;
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

	// //  Get Integer Value of a Gene (Positive only) ****************************

	// public int getPosIntGeneValue(int geneID){
	// 	return (this.chromo[geneID]);
	// }

	//  Mutate a Chromosome Based on Mutation Type *****************************

	// public void doMutation(){

	// 	String mutChromo = "";
	// 	char x;

	// 	switch (Parameters.mutationType){

	// 	case 1:     //  Replace with new random number

	// 		for (int j=0; j<(Parameters.geneSize * Parameters.numGenes); j++){
	// 			x = this.chromo.charAt(j);
	// 			randnum = Search.r.nextDouble();
	// 			if (randnum < Parameters.mutationRate){
	// 				if (x == '1') x = '0';
	// 				else x = '1';
	// 			}
	// 			mutChromo = mutChromo + x;
	// 		}
	// 		this.chromo = mutChromo;
	// 		break;

	// 	default:
	// 		System.out.println("ERROR - No mutation method selected");
	// 	}
	// }

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	//  Select a parent for crossover ******************************************

	public static int selectParent(){

		double rWheel = 0;
		int j = 0;
		int k = 0;

		switch (Parameters.selectType){

		case 1:     // Proportional Selection
			randnum = Search.r.nextDouble();
			for (j=0; j<Parameters.popSize; j++){
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel) return(j);
			}
			break;

		case 3:     // Random Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			return(j);

		case 2:     //  Tournament Selection

		default:
			System.out.println("ERROR - No selection method selected");
		}
	return(-1);
	}

	//  Produce a new child from two parents  **********************************

	// public static void mateParents(int pnum1, int pnum2, Chromo parent1, Chromo parent2, Chromo child1, Chromo child2){

	// 	int xoverPoint1;
	// 	int xoverPoint2;

	// 	switch (Parameters.xoverType){

	// 	case 1:     //  Single Point Crossover

	// 		//  Select crossover point
	// 		xoverPoint1 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));

	// 		//  Create child chromosome from parental material
	// 		child1.chromo = parent1.chromo.substring(0,xoverPoint1) + parent2.chromo.substring(xoverPoint1);
	// 		child2.chromo = parent2.chromo.substring(0,xoverPoint1) + parent1.chromo.substring(xoverPoint1);
            
            // // Associate parents to children (move/modify this block as you see fit when crossover is implemented);
            // child1.parents.add(parent1);
            // child1.parents.add(parent2);
            // child2.parents.add(parent1);
            // child2.parents.add(parent2);

	// 		break;

	// 	case 2:     //  Two Point Crossover

	// 	case 3:     //  Uniform Crossover

	// 	default:
	// 		System.out.println("ERROR - Bad crossover method selected");
	// 	}

	// 	//  Set fitness values back to zero
	// 	child1.rawFitness = -1;   //  Fitness not yet evaluated
	// 	child1.sclFitness = -1;   //  Fitness not yet scaled
	// 	child1.proFitness = -1;   //  Fitness not yet proportionalized
	// 	child2.rawFitness = -1;   //  Fitness not yet evaluated
	// 	child2.sclFitness = -1;   //  Fitness not yet scaled
	// 	child2.proFitness = -1;   //  Fitness not yet proportionalized
	// }

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
