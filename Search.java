/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
// import java.text.*;

public class Search {

/*******************************************************************************
*                           INSTANCE VARIABLES                                 *
*******************************************************************************/

/*******************************************************************************
*                           STATIC VARIABLES                                   *
*******************************************************************************/

	public static FitnessFunction problem;

	public static ArrayList<Chromo> member;
	public static ArrayList<Chromo> child;

	public static Chromo bestOfGenChromo;
	public static int bestOfGenR;
	public static int bestOfGenG;
	public static Chromo bestOfRunChromo;
	public static int bestOfRunR;
	public static int bestOfRunG;
	public static Chromo bestOverAllChromo;
	public static int bestOverAllR;
	public static int bestOverAllG;

	public static double sumRawFitness;
	public static double sumRawFitness2;	// sum of squares of fitness
	public static double sumSclFitness;
	public static double sumProFitness;
	public static double defaultBest;
	public static double defaultWorst;

	public static double averageRawFitness;
	public static double stdevRawFitness;

	public static int G;
	public static int R;
	public static Random r = new Random();
	private static double randnum;

	private static int memberIndex[];
	private static double memberFitness[];
	private static int TmemberIndex;
	private static double TmemberFitness;

	private static double fitnessStats[][];  // 0=Avg, 1=Best

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/


/*******************************************************************************
*                             MEMBER METHODS                                   *
*******************************************************************************/


/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	public static void main(String[] args) throws java.io.IOException{

		Calendar dateAndTime = Calendar.getInstance(); 
		Date startTime = dateAndTime.getTime();

	//  Read Parameter File
		System.out.println("\nParameter File Name is: " + args[0] + "\n");
		Parameters parmValues = new Parameters(args[0]);

	//  Write Parameters To Summary Output File
		String timeString = startTime.toString().replace(' ', '_').replace(':', '-');
		System.out.println(timeString);
		String summaryFileName = Parameters.expID + "_" + timeString + "_summary.txt";
		FileWriter summaryOutput = new FileWriter(summaryFileName);
		parmValues.outputParameters(summaryOutput);

	//	Set up Fitness Statistics matrix
		fitnessStats = new double[2][Parameters.generations];
		for (int i=0; i<Parameters.generations; i++){
			fitnessStats[0][i] = 0;
			fitnessStats[1][i] = 0;
		}

	//	Problem Specific Setup - For new new fitness function problems, create
	//	the appropriate class file (extending FitnessFunction.java) and add
	//	an else_if block below to instantiate the problem.
 
		if (Parameters.problemType.equals("AC")){
				problem = new AdaptiCritters();
		}
		else System.out.println("Invalid Problem Type");

		System.out.println(problem.name);

	//	Initialize RNG, array sizes and other objects
		r.setSeed(Parameters.seed);
		memberIndex = new int[Parameters.popSize];
		memberFitness = new double[Parameters.popSize];
		member = new ArrayList<Chromo>();
		child = new ArrayList<Chromo>();
		bestOfGenChromo = new Chromo();
		bestOfRunChromo = new Chromo();
		bestOverAllChromo = new Chromo();
		Chromo.cumPop -= 3;

		if (Parameters.minORmax.equals("max")){
			defaultBest = 0;
			defaultWorst = 999999999999999999999.0;
		}
		else{
			defaultBest = 999999999999999999999.0;
			defaultWorst = 0;
		}

		bestOverAllChromo.rawFitness = defaultBest;

		//  Start program for multiple runs
		for (R = 1; R <= Parameters.numRuns; R++){

			bestOfRunChromo.rawFitness = defaultBest;
			System.out.println();

            // Holds history of all individuals and their parent-child relationships
            List<Chromo> phylo = new ArrayList<Chromo>();

			//	Initialize First Generation
			for (int i = 0; i < Parameters.popSize; i++){
				member.add(new Chromo());
				phylo.add(member.get(member.size() - 1));
				// child[i] = new Chromo();
			}

			//	Begin Each Run
			for (G = 0; G < Parameters.generations; G++){

				sumProFitness = 0;
				sumSclFitness = 0;
				sumRawFitness = 0;
				sumRawFitness2 = 0;
				bestOfGenChromo.rawFitness = defaultBest;

				//	Test Fitness of Each Member
				for (int i = 0; i < member.size(); i++){

					member.get(i).rawFitness = 0;
					member.get(i).sclFitness = 0;
					member.get(i).proFitness = 0;

					problem.doRawFitness(member.get(i));

					sumRawFitness = sumRawFitness + member.get(i).rawFitness;
					sumRawFitness2 = sumRawFitness2 +
					member.get(i).rawFitness * member.get(i).rawFitness;

					if (Parameters.minORmax.equals("max")){
						if (member.get(i).rawFitness > bestOfGenChromo.rawFitness){
							Chromo.copyB2A(bestOfGenChromo, member.get(i));
							bestOfGenR = R;
							bestOfGenG = G;
						}
						if (member.get(i).rawFitness > bestOfRunChromo.rawFitness){
							Chromo.copyB2A(bestOfRunChromo, member.get(i));
							bestOfRunR = R;
							bestOfRunG = G;
						}
						if (member.get(i).rawFitness > bestOverAllChromo.rawFitness){
							Chromo.copyB2A(bestOverAllChromo, member.get(i));
							bestOverAllR = R;
							bestOverAllG = G;
						}
					}
					else {
						if (member.get(i).rawFitness < bestOfGenChromo.rawFitness){
							Chromo.copyB2A(bestOfGenChromo, member.get(i));
							bestOfGenR = R;
							bestOfGenG = G;
						}
						if (member.get(i).rawFitness < bestOfRunChromo.rawFitness){
							Chromo.copyB2A(bestOfRunChromo, member.get(i));
							bestOfRunR = R;
							bestOfRunG = G;
						}
						if (member.get(i).rawFitness < bestOverAllChromo.rawFitness){
							Chromo.copyB2A(bestOverAllChromo, member.get(i));
							bestOverAllR = R;
							bestOverAllG = G;
						}
					}
				}

				// Accumulate fitness statistics
				fitnessStats[0][G] += sumRawFitness / Parameters.popSize;
				fitnessStats[1][G] += bestOfGenChromo.rawFitness;

				averageRawFitness = sumRawFitness / Parameters.popSize;
				stdevRawFitness = Math.sqrt(
							Math.abs(sumRawFitness2 - 
							sumRawFitness*sumRawFitness/Parameters.popSize)
							/
							(Parameters.popSize-1)
							);

				// Output generation statistics to screen
				// System.out.println("Testing");
				// System.out.println("Number of individuals: " + member.size());
				// for (Chromo indiv : member)
				// {
				// 	System.out.println(indiv.id + " Phenotype: ");
				// 	for (int gene : indiv.chromo)
				// 	{
				// 		System.out.print(gene + " ");
				// 	}
				// 	System.out.println("");
				// 	System.out.println(" Fitness: " + indiv.rawFitness);
				// }
				System.out.println("Run: " + R + "\t" + " Gen: " + G + " Cum Pop Size: " + Chromo.cumPop + " Current Pop: " + member.size() +  "\t" + " Best Fit: " + (int)bestOfGenChromo.rawFitness + "\t" + " Avg Fit: " + averageRawFitness + "\t" + " Std Dev: " + stdevRawFitness);
				// Output generation statistics to summary file
				summaryOutput.write(" R ");
				Hwrite.right(R, 3, summaryOutput);
				summaryOutput.write(" G ");
				Hwrite.right(G, 3, summaryOutput);
				Hwrite.right((int)bestOfGenChromo.rawFitness, 7, summaryOutput);
				Hwrite.right(averageRawFitness, 11, 3, summaryOutput);
				Hwrite.right(stdevRawFitness, 11, 3, summaryOutput);
				summaryOutput.write("\n");


		// *********************************************************************
		// **************** SCALE FITNESS OF EACH MEMBER AND SUM ***************
		// *********************************************************************

				switch(Parameters.scaleType){

				case 0:     // No change to raw fitness
					for (int i=0; i<Parameters.popSize; i++){
						member.get(i).sclFitness = member.get(i).rawFitness + .000001;
						sumSclFitness += member.get(i).sclFitness;
					}
					break;

				case 1:     // Fitness not scaled.  Only inverted.
					for (int i = 0; i < member.size(); i++){
						member.get(i).sclFitness = 1/(member.get(i).rawFitness + .000001);
						sumSclFitness += member.get(i).sclFitness;
					}
					break;

				case 2:     // Fitness scaled by Rank (Maximizing fitness)

					//  Copy genetic data to temp array
					for (int i=0; i<Parameters.popSize; i++){
						memberIndex[i] = i;
						memberFitness[i] = member.get(i).rawFitness;
					}
					//  Bubble Sort the array by floating point number
					for (int i=Parameters.popSize-1; i>0; i--){
						for (int j=0; j<i; j++){
							if (memberFitness[j] > memberFitness[j+1]){
								TmemberIndex = memberIndex[j];
								TmemberFitness = memberFitness[j];
								memberIndex[j] = memberIndex[j+1];
								memberFitness[j] = memberFitness[j+1];
								memberIndex[j+1] = TmemberIndex;
								memberFitness[j+1] = TmemberFitness;
							}
						}
					}
					//  Copy ordered array to scale fitness fields
					for (int i=0; i<Parameters.popSize; i++){
						member.get(memberIndex[i]).sclFitness = i;
						sumSclFitness += member.get(memberIndex[i]).sclFitness;
					}

					break;

				case 3:     // Fitness scaled by Rank (minimizing fitness)

					//  Copy genetic data to temp array
					for (int i=0; i<Parameters.popSize; i++){
						memberIndex[i] = i;
						memberFitness[i] = member.get(i).rawFitness;
					}
					//  Bubble Sort the array by floating point number
					for (int i=1; i<Parameters.popSize; i++){
						for (int j=(Parameters.popSize - 1); j>=i; j--){
							if (memberFitness[j-i] < memberFitness[j]){
								TmemberIndex = memberIndex[j-1];
								TmemberFitness = memberFitness[j-1];
								memberIndex[j-1] = memberIndex[j];
								memberFitness[j-1] = memberFitness[j];
								memberIndex[j] = TmemberIndex;
								memberFitness[j] = TmemberFitness;
							}
						}
					}
					//  Copy array order to scale fitness fields
					for (int i=0; i<Parameters.popSize; i++){
						member.get(memberIndex[i]).sclFitness = i;
						sumSclFitness += member.get(memberIndex[i]).sclFitness;
					}

					break;

				default:
					System.out.println("ERROR - No scaling method selected");
				}


		// *********************************************************************
		// ****** PROPORTIONALIZE SCALED FITNESS FOR EACH MEMBER AND SUM *******
		// *********************************************************************

				for (int i = 0; i < member.size(); i++){
					member.get(i).proFitness = member.get(i).sclFitness/sumSclFitness;
					sumProFitness = sumProFitness + member.get(i).proFitness;
				}

		// *********************************************************************
		// ************ CROSSOVER AND CREATE NEXT GENERATION *******************
		// *********************************************************************

				Chromo parent1 = null;
				Chromo parent2 = null;
				ArrayList<Integer> chosen = new ArrayList<Integer>();

				// Remove unfit members from population
				for (int i = 0; i < member.size(); i++)
				{
					Chromo individual = member.get(i);
					if (individual.rawFitness <= Parameters.fitnessThreshold) {
						individual.endGen = G;
						member.remove(i);
						i--;
					}
				}
				// System.out.println("After death: ");
				// for (Chromo indiv : member)
				// {
				// 	System.out.println(indiv.id + " Phenotype: ");
				// 	for (int gene : indiv.chromo)
				// 	{
				// 		System.out.print(gene + " ");
				// 	}
				// 	System.out.println("");
				// 	System.out.println(" Fitness: " + indiv.rawFitness);
				// }
				for (int i = 0; i < member.size(); i++){
					if ((member.size() % 2 == 1) && (chosen.size() == member.size() - 1))
						{
							break;
						}
					if (!chosen.contains(i))
					{
						//	Select Two Parents
						parent1 = member.get(i);
						chosen.add(i);
						do {
							parent2 = member.get(Chromo.selectParent(chosen));
						} while (parent2 == parent1);
						chosen.add(member.indexOf(parent2));

						if (Parameters.fecundity > 1)
						{
							for (int j = 0; j < Parameters.fecundity; j++)
							{
								//	Crossover Two Parents to Create new child
								child.add(new Chromo());
								Chromo newChild = child.get(child.size() - 1);
								newChild.chromo = Chromo.mateParents(parent1, parent2);
								
								// Not necessary due to constructor
								// //  Set fitness values back to zero
								// newChild.rawFitness = -1;   //  Fitness not yet evaluated
								// newChild.sclFitness = -1;   //  Fitness not yet scaled
								// newChild.proFitness = -1;   //  Fitness not yet proportionalized

								// Record child's parents
								parent1.children.add(newChild);
								parent2.children.add(newChild);
								newChild.parents.add(parent1);
								newChild.parents.add(parent2);

								// Add child to phylogenetic tree
								phylo.add(newChild);
							}
						}
						else
						{
							//	Crossover Two Parents to Create new child
							child.add(new Chromo());
							Chromo newChild = child.get(child.size() - 1);
							newChild.chromo = Chromo.mateParents(parent1, parent2);

							parent1.children.add(newChild);
							parent2.children.add(newChild);
							newChild.parents.add(parent1);
							newChild.parents.add(parent2);
							
							// Not necessary due to constructor
							// //  Set fitness values back to zero
							// newChild.rawFitness = -1;   //  Fitness not yet evaluated
							// newChild.sclFitness = -1;   //  Fitness not yet scaled
							// newChild.proFitness = -1;   //  Fitness not yet proportionalized

							// Add child to phylo
							phylo.add(newChild);
						}
					}					
				} // End Crossover

				//	Mutate Children
				for (int i=0; i<Parameters.popSize; i++){
					// child[i].doMutation();
				}

				//	ADD Children to Last Generation
				for (int i = 0; i < child.size(); i++){
					member.add(child.get(i));
				}
				// Clear children
				child.clear();

			} //  Repeat the above loop for each generation

			Hwrite.left(bestOfRunR, 4, summaryOutput);
			Hwrite.right(bestOfRunG, 4, summaryOutput);

			problem.doPrintGenes(bestOfRunChromo, summaryOutput);

			System.out.println(R + "\t" + "B" + "\t"+ (int)bestOfRunChromo.rawFitness);

		} //End of a Run

		Hwrite.left("B", 8, summaryOutput);

		problem.doPrintGenes(bestOverAllChromo, summaryOutput);

		//	Output Fitness Statistics matrix
		summaryOutput.write("Gen                 AvgFit              BestFit \n");
		for (int i=0; i<Parameters.generations; i++){
			Hwrite.left(i, 15, summaryOutput);
			Hwrite.left(fitnessStats[0][i]/Parameters.numRuns, 20, 2, summaryOutput);
			Hwrite.left(fitnessStats[1][i]/Parameters.numRuns, 20, 2, summaryOutput);
			summaryOutput.write("\n");
		}

		summaryOutput.write("\n");
		summaryOutput.close();

		System.out.println();
		System.out.println("Start:  " + startTime);
		dateAndTime = Calendar.getInstance(); 
		Date endTime = dateAndTime.getTime();
		System.out.println("End  :  " + endTime);

	} // End of Main Class

}   // End of Search.Java ******************************************************

