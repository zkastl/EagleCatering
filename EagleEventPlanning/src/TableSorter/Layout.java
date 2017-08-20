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
	
	public void addGuest(Guest g) {
		// Sort the tables by table number
		Collections.sort(this.tableList);
		
		// Get the table the guest should be assigned to or null if either the table doesn't exist 
		Table guestTable = this.tableList.stream().filter(x -> (x.tableNumber == g.tableNumber) && !x.isFull()).findFirst().orElse(null);
		
		// If the guest table is not null, then a valid table was found to match the guest,
		// Add the guest, unless the table is full; if it's full, add it to the first available table
		if (guestTable != null)
			guestTable.addGuest(g);
		else
			addGuestToFirstAvailableTable(g);
		
		
	}
	
	private void addGuestToFirstAvailableTable(Guest g) {
		// Sort tables by number
		Collections.sort(this.tableList);
		
		if(this.tableList.size() == 0)
			this.tableList.add(new Table(1, this.tableSize, this.emptySeats));
		
		try {
			for(Table t : this.tableList) {
				if(t.addGuest(g)) {
					g.assignedTable = t;
					g.tableNumber = t.tableNumber;
					return;
				}
				else {
					this.tableList.add(new Table(this.tableList.size() - 1, this.tableSize, this.emptySeats));
					continue;
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
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
