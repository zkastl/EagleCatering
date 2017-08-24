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
	public int tableSize;
	public int emptySeats;
	
	public Layout(int tableSize, int emptySeats) {
		this.tableList = new ArrayList<Table>();
		this.tableSize = tableSize;
		this.emptySeats = emptySeats;
	}

	public Layout(List<Table> tableList) {
		this.tableList = tableList;
		fitnessScore = evaluateFitness();
		this.tableSize = tableList.get(0).capacity;
		this.emptySeats = tableList.get(0).numberOfEmptySeats;
	}
	
	public Layout(List<Guest> guestList, int tableCapacity, int emptySeats){
		this.tableList = new ArrayList<Table>();
		List<Integer> tableNumbers = new ArrayList<Integer>();
		for(Guest g : guestList) {
			if (g.tableNumber <= 0)
				try {
					throw new Exception("guest " + g.getName() + " has a invalid table number");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(!tableNumbers.contains(g.tableNumber))
				this.tableList.add(new Table(g.tableNumber, tableCapacity, emptySeats));
			else
				tableList.stream().filter(x -> x.tableNumber == g.guestNumber).findFirst().get().addGuest(g);			
		}
		this.fitnessScore = evaluateFitness();
		this.tableSize = tableCapacity;
		this.emptySeats = emptySeats;
	}
	
	public boolean addGuest(Guest g) {
		// Sort the tables by table number
		Collections.sort(this.tableList);
		
		// A guest can either be added to a random table, the first available table, or (if assigned) their table
		if(!this.addGuestToSpecificTable(g))
			return this.addGuestToFirstAvailableTable(g);
		return true;		
	}
	
	public boolean addGuestToFirstAvailableTable(Guest g) {
		// Sort tables by number
		Collections.sort(this.tableList);
		
		if(this.tableList.size() == 0)
			this.tableList.add(new Table(1, this.tableSize, this.emptySeats));
		
		for(Table t : this.tableList) {
			if(t.addGuest(g)) {
				return true;
			}			
		}
		
		return false;
	}
	
	public boolean addGuestToSpecificTable(Guest g) {
		// Sort tables by number
		Collections.sort(this.tableList);
		
		if(!this.containsTableNumber(g.tableNumber))
			this.tableList.add(new Table(g.tableNumber, this.tableSize, this.emptySeats));
		
		return this.tableList.stream().filter(x -> x.tableNumber == g.tableNumber).findFirst().get().addGuest(g);
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

	public String printLayout() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("***Table Layout***\n");
		System.out.print("***Table Layout***\n");
		
		sb.append("  Fitness Value: " + String.valueOf(this.fitnessScore + "\n"));
		System.out.print("  Fitness Value: " + String.valueOf(this.fitnessScore + "\n"));

		for (Table t : this.tableList) {
			sb.append("    Table " + String.valueOf(t.tableNumber) + ":\n");
			System.out.print("    Table " + String.valueOf(t.tableNumber) + ":\n");
			for (Guest g : t.seatedGuests) {
				sb.append("      " + g.firstName + " " + g.lastName + "\n");
				System.out.print("      " + g.firstName + " " + g.lastName + "\n");
			}
		}
		
		return sb.toString();
	}

	@Override
	public int compareTo(Layout o) {
		return this.fitnessScore.compareTo(o.fitnessScore);
	}
	
	// Returns a boolean containing whether or not the layout contains a table with the passed in number
	public boolean containsTableNumber(int tableNumber) {
		boolean containsTN = !(this.tableList == null || this.tableList.size() == 0) ? this.tableList.stream().anyMatch(x -> x.tableNumber == tableNumber) : false;
		return containsTN;
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
			for(Guest guest : table.seatedGuests){
				t += 100 * table.seatedGuests.stream().filter(x -> guest.sameTable.contains(x.guestNumber)).count();
				t += 1000 * table.seatedGuests.stream().filter(x -> x.lastName.equals(guest.lastName) && x.firstName.equals(guest.firstName)).count();
				t -= 10 * table.seatedGuests.stream().filter(x -> guest.notSameTable.contains(x.guestNumber)).count();
			}
			tableScores[index] = t;
			index++;
		}
		
		for(int i : tableScores)
			fitnessScore += i;
		fitnessScore = Math.floorDiv(fitnessScore, tableScores.length);
		
		return fitnessScore;
	}
}
