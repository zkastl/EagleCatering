package TableSorter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import ProblemDomain.Guest;

public class GA {

	private static int POPULATION_SIZE = 100;
	private static double MUTATION_RATE = 0.1;
	private static double DEATH_RATE = 0.15;
	private static int MAX_GENERATIONS = 10000;
	private static Random random = new Random();
	private static Comparator<Layout> descendCompare = new Comparator<Layout>() {
				
				@Override
				public int compare(Layout l1, Layout l2) {
					return -(l1.fitnessScore.compareTo(l2.fitnessScore));
				}
			};

	private GA() {
	}

	private static ArrayList<Layout> breed(List<Layout> population, double deathRate) {

		ArrayList<Layout> selected = new ArrayList<Layout>();
		ArrayList<Layout> children = new ArrayList<Layout>();

		int membersToSelect = (int) Math.ceil(population.size() * deathRate);
		if (membersToSelect % 2 != 0)
			membersToSelect++;

		for (int c = 0; c < membersToSelect; c++) {
			Layout select = null;

			while (select == null || selected.contains(select))
				select = rouletteSelection(population);

			selected.add(select);
		}

		for (int i = 0; i < selected.size(); i += 2)
			children.add(crossover(selected.get(i), selected.get(i + 1)));

		return children;
	}

	private static Layout crossover(Layout mother, Layout father) {
		// Use permutation encoding to find the sequence of guests
		
		// Select a single point for crossover and split the lists
		int crossoverPoint = random.nextInt(mother.getGuests().size());
		List<Guest> mothersPart = mother.getGuests().subList(0, crossoverPoint);
		
		// Create a new layout, add the mothers guests to the layout.
		Layout child = new Layout(mother.tableSize, mother.emptySeats);
		for(Guest g : mothersPart) {
			child.addGuest(g);
		}
		
		for(Guest g: father.getGuests()) {
			if(!child.getGuests().contains(g))
				child.addGuest(g);
		}
		
		child.evaluateFitness();
		return child;
	}

	private static Layout rouletteSelection(List<Layout> population) {

		ArrayList<Integer> fitnessValues = new ArrayList<Integer>();

		for (Layout p : population)
			fitnessValues.add(p.fitnessScore);

		int fitnessSum = 0;

		for (int i : fitnessValues)
			fitnessSum += i;

		int pick = new Random().nextInt(fitnessSum);
		int current = 0;

		for (Layout p : population) {
			current += p.fitnessScore;
			if (current > pick)
				return p;
		}

		return population.get(population.size() - 1);
	}

	public static Layout runGA(Collection<Guest> guests, int tableCapacity, int emptySeats) throws Exception {
		
		if(guests == null || guests.size() == 0) {
			System.out.println("invalid parameters: empty guest list");
			throw new Exception();
		}
				
		ArrayList<Layout> population = new ArrayList<Layout>();
		int generation = 1;
		//int maxFit = 0;

		for(int genome = 0; genome < POPULATION_SIZE; genome++) {			
			Layout layout = Layout.createRandomTableLayout(guests, tableCapacity, emptySeats);
			population.add(layout);
		}
		
		while(generation < MAX_GENERATIONS) {
			ArrayList<Layout> children = breed(population, DEATH_RATE);
			mutate(children, MUTATION_RATE);
			
			// Sort by descending fitness values
			population.sort(descendCompare);
			
			// kill the last X elements of the population, where x is the size of the array of children
			population.removeIf(x -> population.indexOf(x) >= (population.size() - children.size()));
			
			// Add the children to the population
			population.addAll(children);
			
			// Sort the new population by descending fitness scores
			population.sort(descendCompare);
			
			if (generation % 100 == 0) {
				System.out.println("Generation "+ generation + " - Max Fitness: " + population.get(0).fitnessScore );
			}
			
			generation++;			
		}
		
		population.get(0).printLayout();
		return population.get(0);
	}

	private static void mutate(List<Layout> children, double mutationRate) {
		if (new Random().nextDouble() > mutationRate)
			return;

		Collections.shuffle(children);
		mutateGenome(children.get(children.size() - 1));

	}

	private static void mutateGenome(Layout layout) {
		
		Guest a = null, b = null;
		while (a == b) {
			a = layout.getGuests().get(random.nextInt(layout.getGuests().size()));
			b = layout.getGuests().get(random.nextInt(layout.getGuests().size()));
		}
				
		Guest backupGuest = a;
		a = b;
		b = backupGuest;
		
		int backupNumber = a.tableNumber;
		a.tableNumber = b.tableNumber;
		b.tableNumber = backupNumber;	
	}

}
