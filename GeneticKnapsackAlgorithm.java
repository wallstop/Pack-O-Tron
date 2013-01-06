import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;


public class GeneticKnapsackAlgorithm {

	private static ArrayList<Specimen> genePool;
	private static ArrayList<Specimen> childrenPopulation;
	
	//Change these to tweak runtime
	private static final int genePoolSize = 50;
	private static final int numGenerations = 1000;
	private static final int numChildren = 50;
	
	private static int bestPossibleFitness;
	private static Random rGen;
	private static int numItems;
	private static int knapsackWidth;
	private static int knapsackLength;

	//Prints the most-fit specimen of the genePool to a file
	public static void printToFile()
	{
		Specimen mostFit;
		File outFile;
		BufferedWriter writer;
		
		mostFit = getMostFit(genePool);
		
		outFile = new File("Most fit Pack-O-Tron Specimen.txt");
		try 
		{
			writer = new BufferedWriter(new FileWriter(outFile));
			writer.write("Fitness: " + mostFit.getFitness());
			writer.newLine();
			for(KnapsackObject object : mostFit.getObjectArray())
			{
				writer.write(object.getWidth() + " " + object.getLength());
				writer.newLine();
			}
			for(int j = 0; j < knapsackLength; j++)
			{
				for(int i = 0; i < knapsackWidth; i++)
				{
					writer.write(mostFit.getKnapsack()[i][j] + " ");
				}
				writer.newLine();
			}
			
			writer.close();
		
		} catch (IOException e) {
			// Who cares
			e.printStackTrace();
		}	
		
	}
	
	/* Used for testing purposed */
	public static void printSpecimenToFile(Specimen specimen)
	{
		File outFile;
		BufferedWriter writer;
		
		
		outFile = new File("Pack-O-Tron Test Specimen.txt");
		try 
		{
			writer = new BufferedWriter(new FileWriter(outFile));
			writer.write("Fitness: " + specimen.getFitness());
			writer.newLine();
			for(KnapsackObject object : specimen.getObjectArray())
			{
				writer.write(object.getWidth() + " " + object.getLength());
				writer.newLine();
			}
			for(int j = 0; j < knapsackLength; j++)
			{
				for(int i = 0; i < knapsackWidth; i++)
				{
					writer.write(specimen.getKnapsack()[i][j] + " ");
				}
				writer.newLine();
			}
			
			writer.close();
		
		} catch (IOException e) {
			// Who cares
			e.printStackTrace();
		}	
		
	}
	
	/* Similar to writeToFile(), but prints to console instead */
	public static void printResult()
	{
		Specimen mostFit;
		
		mostFit = getMostFit(genePool);
		
		System.out.println("Fitness: " + mostFit.getFitness());
		
		for(KnapsackObject object : mostFit.getObjectArray())
			System.out.println(object.getWidth() + " " + object.getLength());
		
		for(int j = 0; j < mostFit.getKnapsack()[0].length; j++)
		{
			for(int i = 0; i < mostFit.getKnapsack().length; i++)
			{
				System.out.printf("%d ", mostFit.getKnapsack()[i][j]);
			}
			System.out.println();
		}
	}
	
	/* Returns the most-fit Specimen of some pool 
	 * 		Used for genePool improvement.
	*/
	public static Specimen getMostFit(ArrayList<Specimen> pool)
	{
		Specimen mostFit;
		
		mostFit = pool.get(0);
		
		for(Specimen specimen : pool)
		{
			if(specimen.getFitness() < mostFit.getFitness())
				mostFit = specimen;
		}
		
		return mostFit;		
	}
	
	/* Returns the second-most-fit Specimen of some pool 
	 * 		Used for genePool improvement.
	*/
	public static Specimen getSecondMostFit(ArrayList<Specimen> pool)
	{
		Specimen mostFit;
		Specimen secondMostFit;
		
		mostFit = getMostFit(pool);
		
		if(pool.get(0) == mostFit)
			secondMostFit = pool.get(1);
		else
			secondMostFit = pool.get(0);
		
		for(Specimen specimen : pool)
		{
			if(specimen.getFitness() < secondMostFit.getFitness() && specimen != mostFit)
				secondMostFit = specimen;
		}
		
		return secondMostFit;		
	}
	
	/* Returns the least-fit Specimen of some pool 
	 * 		Used for genePool improvement.
	*/
	public static Specimen getLeastFit(ArrayList<Specimen> pool)
	{
		Specimen leastFit;
		
		leastFit = pool.get(0);
		
		for(Specimen specimen : pool)
		{
			if(specimen.getFitness() > leastFit.getFitness())
				leastFit = specimen;
		}
		
		return leastFit;		
	}
	
	/* Returns the second-least-fit Specimen of some pool 
	 * 		Used for genePool improvement.
	*/
	public static Specimen getSecondLeastFit(ArrayList<Specimen> pool)
	{
		Specimen leastFit;
		Specimen secondLeastFit;
		
		leastFit = getLeastFit(pool);
		
		if(pool.get(0) == leastFit)
			secondLeastFit = pool.get(1);
		else
			secondLeastFit = pool.get(0);
		
		for(Specimen specimen : pool)
		{
			if(specimen.getFitness() > secondLeastFit.getFitness() && specimen != leastFit)
				secondLeastFit = specimen;
		}
		
		return secondLeastFit;		
	}
	
	/* Initializes the genePool with the array of objects */
	public static void initializeGenePool(ArrayList<KnapsackObject> objectArray)
	{
		genePool = new ArrayList<Specimen>();
		
		bestPossibleFitness = 0;
		
		//The best possible bounding box area is the sum of all of the object's areas. This might not be possible to achieve.
		for(KnapsackObject object : objectArray)
			bestPossibleFitness += object.getArea();
		
		for(int i = 0; i < genePoolSize; i++)
		{
			genePool.add(new Specimen(knapsackWidth, knapsackLength, numItems, objectArray, true));		
			//genePool.get(i).determineFitness() (Now called in constructor)
		}
	}
	
	/* Creates a new "genome", or ordered list, based off of two parents */
	public static Specimen mutateGenome(Specimen parent1, Specimen parent2)
	{
		Specimen child;
		Specimen tempSpecimen;
		boolean dominantParent;
		KnapsackObject chosenTrait;
		int indexOfTrait;
		int indexInOtherParent;
		ArrayList<KnapsackObject> tempList;
		int mutationChance;
		
		dominantParent = rGen.nextBoolean();
		mutationChance = rGen.nextInt(100) + 1;
		
		if(dominantParent)	//Randomly determines the "dominant" parent
		{
			tempSpecimen = parent1;
			parent1 = parent2;
			parent2 = parent1;
		}
		
		//Copies a "gene"'s, or KnapsackObject's, position from one parent to another, resulting in a new "genome"
		indexOfTrait = rGen.nextInt(numItems);
		chosenTrait = parent1.getObjectArray().get(indexOfTrait);
		
		tempList = new ArrayList<KnapsackObject>();
		//tempList.addAll(0, parent2.getObjectArray());
		tempList = parent2.getObjectArray();
		
		indexInOtherParent = tempList.indexOf(chosenTrait);
		
		tempList.remove(indexInOtherParent);
		tempList.add(indexOfTrait, chosenTrait);
		
		//Randomly swaps two object's positions
		if(mutationChance % 7 == 0)
		{
			int randomIndex1;
			int randomIndex2;
			KnapsackObject object1;
			KnapsackObject object2;
			
			randomIndex1 = rGen.nextInt(numItems);
			
			do
			{
				randomIndex2 = rGen.nextInt(numItems);
			} while(randomIndex1 == randomIndex2);
			
			object1 = tempList.get(randomIndex1);
			object2 = tempList.get(randomIndex2);
			
			tempList.set(randomIndex1, object2);
			tempList.set(randomIndex2, object1);		
		}
		//Randomly reverses the array
		if(mutationChance < 15)
		{
			Collections.reverse(tempList);
		}
		//Randomly swaps the sides of an object in the array
		if(mutationChance % 14 == 0)
		{
			KnapsackObject tempObject;
			int randomIndex;
			
			randomIndex = rGen.nextInt(numItems);
			
			tempObject = tempList.get(randomIndex);
			tempObject.swapSides();
			tempList.set(randomIndex, tempObject);
		}
		
		child = new Specimen(knapsackWidth, knapsackLength, numItems, tempList, false);
		
		return child;
	}
	
	public static void generateChildren(Specimen parent1, Specimen parent2)
	{		
		for(int i = 0; i < numChildren; i++)
			childrenPopulation.add(mutateGenome(parent1, parent2));
	}
	
	public static boolean compete()
	{
		boolean returnValue;
		Specimen genePoolLeastFit;
		Specimen genePoolSecondLeastFit;
		Specimen childStrongest;
		Specimen childSecondStrongest;

		returnValue = false;
		genePoolLeastFit = getLeastFit(genePool);
		genePoolSecondLeastFit = getSecondLeastFit(genePool);
		childStrongest = getMostFit(childrenPopulation);
		childSecondStrongest = getSecondMostFit(childrenPopulation);
		
		if(childSecondStrongest.getFitness() < genePoolSecondLeastFit.getFitness())	//If the "weakest" strong child is stronger than the strongest "weak" genePool member, swap both
		{
			//genePool.get(indexOfTwoWeakest[0]).getRoomAssignment().delete();
			genePool.set(genePool.indexOf(genePoolSecondLeastFit), childSecondStrongest);
				
			//genePool.get(indexOfTwoWeakest[1]).getRoomAssignment().delete();
			genePool.set(genePool.indexOf(genePoolLeastFit), childStrongest);
			returnValue = true;
		}
		//Otherwise, replace the weakest Specimen in the genePool with the strongest child.
		else if(childStrongest.getFitness() < genePoolLeastFit.getFitness())
		{	
			//children[1].getRoomAssignment().delete();	//Deletes the room assignment for the weaker of the two children
			//genePool.get(indexOfTwoWeakest[0]).getRoomAssignment().delete();
			genePool.set(genePool.indexOf(genePoolLeastFit), childStrongest);		
			returnValue = true;
		}
		else	//Deletes the room assignments
		{
			//children[1].getRoomAssignment().delete();
			//children[0].getRoomAssignment().delete();
		}
		
		return returnValue;
	}
	
	public static void evolve()
	{
		int matingType;
		boolean randomChoice;
		Specimen parent1;
		Specimen parent2;
		int parent1Index;
		int parent2Index;
		
		matingType = rGen.nextInt(99);
		childrenPopulation = new ArrayList<Specimen>();
		
		if(matingType < 33)	//Two random parents
		{
			parent1Index = rGen.nextInt(genePool.size());
			do
			{
				parent2Index = rGen.nextInt(genePool.size());
			} while(parent1Index == parent2Index);
			
			parent1 = genePool.get(parent1Index);
			parent2 = genePool.get(parent2Index);
		}
		else if(matingType < 67)	//Two most fit parents
		{
			parent1 = getMostFit(genePool);
			parent2 = getSecondMostFit(genePool);
		}
		else	//One of the most fit parents and a random parent
		{
			randomChoice = rGen.nextBoolean();
			
			if(randomChoice)
				parent1 = getMostFit(genePool);
			else
				parent1 = getSecondMostFit(genePool);
			
			parent1Index = genePool.indexOf(parent1);
			
			do
			{
				parent2Index = rGen.nextInt(genePool.size());
			} while(parent1Index == parent2Index);
			
			parent2 = genePool.get(parent2Index);
		}
		
		generateChildren(parent1, parent2);
	}
	
	public static void darwinize()
	{
		int currentGeneration;
		
		currentGeneration = 0;
		
		printToFile();
		
		while((getMostFit(genePool).getFitness() != bestPossibleFitness) && (currentGeneration < numGenerations))
		{
			currentGeneration++;
			System.out.println("Generation " + currentGeneration);
			evolve();
			if(compete())
			{
				System.out.println("Generation improved!");
			}
			
		}
		
		printToFile();
	}
	
	public static void testSpecimen(ArrayList<KnapsackObject> objectArray)
	{
		Specimen sample;
		
		sample = new Specimen(knapsackWidth, knapsackLength, numItems, objectArray, false);
		
		printSpecimenToFile(sample);		
	}
	
	
	public static void main(String [] args)
	{
		Scanner input;
		ArrayList<KnapsackObject> objectArray;	
		String tempInput;
		String [] tempInputArray;
		
		input = new Scanner(System.in);
		rGen = new Random();
		
		tempInput = input.nextLine();
		tempInputArray = tempInput.split(" ");
		knapsackWidth = Integer.parseInt(tempInputArray[0]);
		knapsackLength = Integer.parseInt(tempInputArray[1]);
		
		numItems = input.nextInt();
		objectArray = new ArrayList<KnapsackObject>();
		
		input.nextLine(); 	//Takes care of trailing /n
		for(int i = 0; i < numItems; i++)
		{
			tempInput = input.nextLine();
			tempInputArray = tempInput.split(" ");
			objectArray.add(new KnapsackObject(Integer.parseInt(tempInputArray[0]), Integer.parseInt(tempInputArray[1])));
		}
		
		//testSpecimen(objectArray);
		initializeGenePool(objectArray);
		darwinize();
		
		
	}
	
	
}
