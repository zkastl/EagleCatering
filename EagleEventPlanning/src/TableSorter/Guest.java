package eaglecatering.guestsorter;

import java.util.List;

public class Guest {
	
	public String firstName;
	public String lastName;
	public int guestNumber;
	public List<Integer> sameTable;
	public List<Integer> notSameTable;
	public int tableNumber;
	
	public Guest(int guestNumber, String firstName, String lastName, List<Integer> sameTable, List<Integer> notSameTable, int tableNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.guestNumber = guestNumber;
		this.sameTable = sameTable;
		this.notSameTable = notSameTable;
		this.tableNumber = tableNumber;
	}
	
	public void assignTable(int tableNumber) {
		this.tableNumber = tableNumber;
	}
	
}
