package TableSorter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ProblemDomain.Guest;
import ProblemDomain.Table;

public class Layout implements Comparable<Layout> {

	public List<Table> tableList;
	public Integer fitnessScore = 0;

	public Layout(List<Table> tableList) {
		this.tableList = tableList;
		fitnessScore = evaluateFitness();
	}

	public List<Guest> getGuests() {
		List<Guest> guestList = new ArrayList<Guest>();

		for (Table t : tableList) {
			for (Guest g : t.seatedGuests) {
				guestList.add(g);
			}
		}

		return guestList;
	}

	public static Layout createRandomTableLayout(Collection<Guest> guestList, int capacity, int numEmptySeats)
			throws Exception {

		if (guestList == null || guestList.size() == 0) {
			throw new Exception("Invalid Parameters");
		}

		int numberCurrentTables = 1;
		ArrayList<Guest> tempGuests = new ArrayList<Guest>(guestList);
		ArrayList<Table> tableList = new ArrayList<Table>();
		tableList.add(new Table(numberCurrentTables, capacity, numEmptySeats));

		Collections.shuffle(tempGuests);

		for (Guest g : tempGuests) {
			if (!tableList.get(tableList.size() - 1).addGuest(g)) {				
				numberCurrentTables++;
				tableList.add(new Table(numberCurrentTables, capacity, numEmptySeats));
				tableList.get(tableList.size() - 1).addGuest(g);			
			}
			g.assignedTable = tableList.get(tableList.size() - 1);
			g.tableNumber = g.assignedTable.tableNumber;
		}

		Layout l = new Layout(tableList);
		l.evaluateFitness();
		return l;
	}	

	public void printLayout() {
		System.out.println("***Table Layout***");
		System.out.println("  Fitness Value: " + String.valueOf(this.fitnessScore));

		for (Table t : this.tableList) {
			System.out.println("    Table " + String.valueOf(t.tableNumber) + ":");
			for (Guest g : t.seatedGuests)
				System.out.println("      " + g.firstName + " " + g.lastName);
		}
	}

	@Override
	public int compareTo(Layout o) {
		return this.fitnessScore.compareTo(o.fitnessScore);
	}
	
	public int evaluateFitness() {
		if(tableList.size() == 0)
			return 0;
		
		// Starting fitness score
		fitnessScore = 0;
		int[] tableScores = new int[tableList.size()];
		int index = 0;
		
		// Calculate the fitness of each table, average them?
		//	A table's fitness is: 
		
		for(Table table : tableList) {
			int t = 0;
			for(Guest guest : table.seatedGuests) {
				t += (goodGuestAtTable(guest.sameTable, table) + badGuestAtTable(guest.sameTable, table));
			}
			
			tableScores[index] = t;
			index++;
		}
		
		for(int i : tableScores)
			fitnessScore += i;
		fitnessScore = Math.floorDiv(fitnessScore, tableScores.length); 
		
		return fitnessScore;
	}
	
	private int goodGuestAtTable(List<Integer> potentialNeighbors, Table table) {
		int guests = 0;
		for(int i : potentialNeighbors) {
			for(Guest g : table.seatedGuests) {
				if(g.guestNumber == i)
					guests += 1000;
			}
		}
		
		return guests;
	}
	
	private int badGuestAtTable(List<Integer> potentialNeighbors, Table table) {
		int guests = 0;
		for(int i : potentialNeighbors) {
			for(Guest g : table.seatedGuests) {
				if(g.guestNumber == i)
					guests += 1;
			}
		}
		
		return guests;
	}
}
