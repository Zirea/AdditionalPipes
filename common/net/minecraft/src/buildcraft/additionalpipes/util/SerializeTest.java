package net.minecraft.src.buildcraft.additionalpipes.util;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SerializeTest {

	private FrequencyMap frequencyMap = new FrequencyMap();

	public SerializeTest() {
		
		frequencyMap.setFreqName(0, "test0");
		frequencyMap.setFreqName(1, "test1");
		frequencyMap.setFreqName(2, "test2");
		frequencyMap.setFreqName(3, "test3");
		frequencyMap.setFreqName(4, "test4");
		
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
		
		for (int freq : frequencyMap.keys()) {
			
			System.out.println(freq + ": " + frequencyMap.getFreqName(freq));
		}
	}

	public static void main(String[] args) {

		new SerializeTest();
	}

}
