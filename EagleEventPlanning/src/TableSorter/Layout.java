package TableSorter;

import java.util.ArrayList;
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

	public static Layout createRandomTableLayout(ArrayList<Guest> guestList, int capacity, int numEmptySeats)
			throws Exception {

		if (guestList == null || guestList.size() == 0) {
			throw new Exception("Invalid Parameters");
		}

		int numberCurrentTables = 1;
		ArrayList<Guest> tempGuests = new ArrayList<Guest>(guestList);
		ArrayList<Table> tableList = new ArrayList<Table>();
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
		if(tableList.size() == 0)
			return 0;
		
		/*  '''
        evaluating the fitness of a layout means that each guest is examined for its neighbors at the same table
        a guest has people they can and cannot sit next to; the more times the rules are followed, the better the score
       
        HIGHER FITNESS SCORES ARE ALWAYS BETTER
        '''
        # Starting fitness score
        self.fitness_score = 0

        for guest in self.get_guests():

            #good_neighbors = [neighbor for neighbor in self.get_guests() if neighbor.guest_number in guest.same_table]
            #bad_neighbors = [neighbor for neighbor in self.get_guests() if neighbor.guest_number in guest.not_same_table]

            #for neighbor in good_neighbors:
            #    if guest.table_number == neighbor.table_number:
            #        self.fitness_score += 1

            #for neighbor in bad_neighbors:
            #    if guest.table_number != neighbor.table_number:
            #        self.fitness_score += 1

            guest_table = [table for table in self.table_list if table.table_number == guest.table_number][0]
            self.fitness_score += 1000 * len([neighbor for neighbor in guest_table.seated_guests if neighbor.guest_number in guest.same_table])
            self.fitness_score += len([neighbor for neighbor in guest_table.seated_guests if neighbor.guest_number not in guest.not_same_table])*/
		
		// Starting fitness score
		fitnessScore = 0;
		Table guestTable;
		
		for(Guest guest : getGuests()) {
			ArrayList<Guest> goodNeighbors = new ArrayList<Guest>();
			ArrayList<Guest> badNeighbors = new ArrayList<Guest>();
			
			for(Guest g : getGuests())
				if(guest.sameTable.contains(g.guestNumber))
					goodNeighbors.add(g);
			
			for(Guest g : getGuests())
				if(guest.notSameTable.contains(g.guestNumber))
					badNeighbors.add(g);
			
			for(Table t : tableList)
				if(t.tableNumber == guest.tableNumber)
					guestTable = t;
			
			
		}
		
		return 0;
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
}
