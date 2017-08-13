package TableSorter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ProblemDomain.Guest;
import ProblemDomain.Table;

public class Layout {

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

	public static Layout createRandomTableLayout(ArrayList<Guest> guestList, int capacity, int numEmptySeats)
			throws Exception {

		if (guestList == null || guestList.size() == 0) {
			throw new Exception("Invalid Parameters");
		}

		int numberCurrentTables = 1;
		List<Table> tableList = new ArrayList<Table>();
		tableList.add(new Table(numberCurrentTables, capacity, numEmptySeats));

		Collections.shuffle(tableList);

		for (Guest g : guestList) {
			if (!tableList.get(tableList.size() - 1).addGuest(g)) {

				numberCurrentTables++;
				tableList.add(new Table(numberCurrentTables, capacity, numEmptySeats));
				tableList.get(tableList.size() - 1).addGuest(g);
			}
		}

		Layout l = new Layout(tableList);
		l.evaluateFitness();
		return l;
	}

	public int evaluateFitness() {

		if (tableList.size() == 0)
			return 0;

		this.fitnessScore = 0;
		Table guestTable = null;
		ArrayList<Guest> goodNeighbors = new ArrayList<Guest>();
		ArrayList<Guest> badNeighbors = new ArrayList<Guest>();
		
		for (Guest guest : getGuests()) {
			for(Table t : this.tableList) {
				if (t.tableNumber == guest.tableNumber){
					guestTable = t;
					break;
				}			
			}
			
			for(Guest neighbor : guestTable.seatedGuests)
				if(guest.sameTable.contains(neighbor))
					goodNeighbors.add(neighbor);
			this.fitnessScore += 1000 * goodNeighbors.size();
			
			for(Guest neighbor : guestTable.seatedGuests)
				if(guest.notSameTable.contains(neighbor))
					badNeighbors.add(neighbor);
			this.fitnessScore -= badNeighbors.size();
		}
		return this.fitnessScore;
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
}
