package eaglecatering.guestsorter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;

public class Sorter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
	/*private ArrayList<Guest> createGuests(Path file) {
		ArrayList<Guest> guests = new ArrayList<Guest>();
		
		try(InputStream in = Files.newInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				String line = null;
				ArrayList<String> g = new ArrayList<String>();
				g.addAll(reader.readLine().split(","));
				int numberSames = (x -> g)
				
				while((line = reader.readLine()) != null) {
					
					String[] guest = line.split(",");
					guests.add(new Guest(line[0], line[1], line[2],
							line[]))
				}
		} catch(IOException x) {
			System.err.println(x);
		}
		
		return guests;
	}*/

}
