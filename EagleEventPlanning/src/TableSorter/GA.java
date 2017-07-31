package TableSorter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import ProblemDomain.Guest;

public class GA {

	private static int POPULATION_SIZE;
	private static double MUTATION_RATE;
	private static double DEATH_RATE;
	private static int FITNESS_THRESHOLD;

	private GA() {
		POPULATION_SIZE = 100;
		MUTATION_RATE = 0.05;
		DEATH_RATE = 0.1;
		FITNESS_THRESHOLD = 100000;
	}

	private static List<Layout> breed(List<Layout> population, double deathRate) {

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
		return mother;
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

	public static void runGA(ArrayList<Guest> guests, int tableCapacity, int emptySeats) throws Exception {

		ArrayList<Layout> population = new ArrayList<Layout>();
		Layout maxFit = null;

		for (Layout l : population)
			if (maxFit == null || l.fitnessScore > maxFit.fitnessScore)
				maxFit = l;

		while (population.size() == 0 || maxFit.fitnessScore < FITNESS_THRESHOLD) {
			for (int genome = 0; genome < POPULATION_SIZE; genome++) {

				Layout layout = Layout.createRandomTableLayout(guests, tableCapacity, emptySeats);
				population.add(layout);
			}

			List<Layout> children = breed(population, DEATH_RATE);
			mutate(children, MUTATION_RATE);

			// Sort ascending
			population.sort(Comparator.comparing(Layout::evaluateFitness));

			// This SHOULD remove the first n elements of the population,
			// where n is one of the lowest fitness scores.
			for (int i = 0; i < children.size(); i++)
				population.remove(i);

			population.addAll(children);
			population.get(population.size() - 1).printLayout();
		}
	}

	private static void mutate(List<Layout> children, double mutationRate) {
		if (new Random().nextDouble() > mutationRate)
			return;

		Collections.shuffle(children);
		mutateGenome(children.get(children.size() - 1));

	}

	private static void mutateGenome(Layout layout) {
		// TODO Auto-generated method stub

	}

}
