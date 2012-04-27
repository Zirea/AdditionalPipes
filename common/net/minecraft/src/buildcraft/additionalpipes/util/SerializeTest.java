package net.minecraft.src.buildcraft.additionalpipes.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SerializeTest {

	private FrequencyMap frequencyMap = new FrequencyMap();
	private ArrayList<String> usernames = new ArrayList<String>();

	public SerializeTest() {
		
		usernames.add("Kyprus");
		usernames.add("Phantomfox32");
		usernames.add("Mercios");
		
		for (String username : usernames) {
			
			frequencyMap.setFreqName(username, 0, "test0");
			frequencyMap.setFreqName(username, 1, "test1");
			frequencyMap.setFreqName(username, 2, "test2");
			frequencyMap.setFreqName(username, 3, "test3");
			frequencyMap.setFreqName(username, 4, "test4");
		}
		
		writeMap();
		
		frequencyMap = new FrequencyMap();
		
		readMap();
		
		printMap();

	}

	public void writeMap() {

		try {

			FileOutputStream fileOut = new FileOutputStream("FrequencyMap.dat");
			GZIPOutputStream gzipOut = new GZIPOutputStream(fileOut);
			ObjectOutputStream out = new ObjectOutputStream(gzipOut);
			
			out.writeObject(frequencyMap);
			
			out.close();
			fileOut.close();

		} catch (IOException i) {
			i.printStackTrace();
		}

	}

	public void readMap() {

		try {
			
			FileInputStream fileIn = new FileInputStream("FrequencyMap.dat");
			GZIPInputStream gzipIn = new GZIPInputStream(fileIn);
			ObjectInputStream in = new ObjectInputStream(gzipIn);
			
			frequencyMap = (FrequencyMap) in.readObject();
			
			in.close();
			fileIn.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void printMap() {
		
		for (String username : usernames) {
			
			for (int freq : frequencyMap.keys("Kyprus")) {
				
				System.out.println(username + freq + ": " + frequencyMap.getFreqName("Kyprus", freq));
			}
		}
	}

	public static void main(String[] args) {

		new SerializeTest();
	}

}
