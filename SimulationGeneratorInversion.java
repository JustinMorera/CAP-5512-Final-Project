import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Arrays;

public class SimulationGeneratorInversion
{
    public static int numGenes;
	public static int numGens;
	public static int numEvents;
    public static double lethalChance;
    public static void main(String[] args) 
    {
        numGenes = Integer.parseInt(args[0]);
        numEvents = Integer.parseInt(args[1]);
        numGens = Integer.parseInt(args[2]);
        lethalChance = Double.parseDouble(args[3]);
        int currentLine = 0;
        Random random = new Random();

        String[] template = new String[3 + numGenes + numEvents * (numGenes + 1)];
        Arrays.fill(template, "");

        template[currentLine++] = args[0]; // numGenes

        // Randomly assign number of alleles for each gene and fitness values for each allele
        for (int i = 0; i < numGenes; i++) 
        { // Number of alleles per gene: Range [2, 10]
            int numAlleles = random.nextInt(9) + 2; 
            template[currentLine] += numAlleles + " ";

            for (int j = 0; j < numAlleles; j++) 
            {
                if (random.nextDouble() <= lethalChance) 
                {
                    template[currentLine] += "x ";
                }
                else if (i % 2 == 0) // Even genes are beneficial
                { // Base fitness values per allele: Range [0, 50]
                    int allele = random.nextInt(51); 
                    if (allele >= 0)
                    {
                        template[currentLine] += "+";
                    }
                    template[currentLine] += allele + " ";
                }
                else // Odd genes are detrimental
                { // Base fitness values per allele: Range [-50, 0]
                    int allele = random.nextInt(51) - 50; 
                    if (allele >= 0)
                    {
                        template[currentLine] += "+";
                    }
                    template[currentLine] += allele + " ";
                }
            }
            currentLine += 1;
        }

        template[currentLine++] = args[1] + " " + args[2]; // numEvents numGens

        int[]eventGens = new int[numEvents];
        for (int i = 0; i < numEvents; i++)
        {
            eventGens[i] = random.nextInt(numGens);
        }
        Arrays.sort(eventGens);
        System.out.print("EventGens: ");
        for (int event : eventGens)
        {
            System.out.print(event + " ");
        }
        System.out.print("\n");

        for (int i = 0; i < numEvents; i++)
        {
            template[currentLine++] += eventGens[i];
            for (int j = 0; j < numGenes; j++)
            {
                String[] tempStrings = template[1 + j].split(" ");
                int numAlleles = Integer.parseInt(tempStrings[0]);

                for (int k = 0; k < numAlleles; k++) 
                {
                    if (tempStrings[k + 1].equals("x"))
                    {
                        template[currentLine] += "x ";
                    }
                    else if (j % 2 == 0) // Even genes become detrimental
                    { // Fitness modifiers from events: Range [-20, 0]
                        int allele = random.nextInt(21) - 20; 
                        if (allele >= 0)
                        {
                            template[currentLine] += "+";
                        }
                        template[currentLine] += allele + " ";
                    }
                    else // Odd genes become beneficial
                    { // Fitness modifiers from events: Range [0, 20]
                        int allele = random.nextInt(21); 
                        if (allele >= 0)
                        {
                            template[currentLine] += "+";
                        }
                        template[currentLine] += allele + " ";
                    }
                }
                currentLine += 1;
            }
        }


	//Make console output pretty and interpretable
	try {
	    int i = 0;
            while (i < template.length) {
            	//Don't parse empty lines
                if (template[i].trim().isEmpty()) {
                    i++;
                    continue;
                }
		
		//Number of Genes
                int numGenes = Integer.parseInt(template[i++].trim());
                System.out.println("Number of Genes: " + numGenes);
                
                //Initial Genes
                for (int gene = 0; gene < numGenes; gene++) {
                    String[] alleles = template[i++].trim().split(" ");
                    System.out.print("Gene " + (gene + 1) + " (" + alleles.length + " alleles):");
                    
                    for (String allele : alleles) {
                        System.out.print(allele + " ");
                    }
                    
                    System.out.println();
                }

		//Events and generations
                String[] eventAndGeneration = template[i++].trim().split(" ");
                int numEvents = Integer.parseInt(eventAndGeneration[0]);
                int totalGenerations = Integer.parseInt(eventAndGeneration[1]);
                System.out.println("Number of Events: " + numEvents + ", Total Generations: " + totalGenerations);

		//How event modifies genes
                while (i < template.length && numEvents-- > 0) {
                    int generation = Integer.parseInt(template[i++].trim());
                    System.out.println("Event at Generation: " + generation);
                    
                    for (int gene = 0; gene < numGenes; gene++) {
                        if (i >= template.length) break;
                        
                        String[] modifications = template[i++].trim().split(" ");
                        System.out.print("Gene " + (gene + 1) + ": ");
                        
                        for (String modification : modifications) {
                            System.out.print(modification + " ");
                        }
                        
                        System.out.println();
                        }
                }
            }
        }catch (NumberFormatException e) {
            System.out.println("Error with the number format");
            e.printStackTrace();
        }


        // Write the template to a file
        String filename = "Inversion_AdaptiCritters_Template_numGenes_" + numGenes + "_numEvents_" + numEvents + "_numGenerations_" + numGens + "lethalChance_" + lethalChance + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String line : template) {
                writer.println(line);
            }
            System.out.println("List successfully saved to list.txt");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to file.");
            e.printStackTrace();
        }
    }
}
