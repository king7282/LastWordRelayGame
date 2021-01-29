package Domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Dictionary {
	private static ArrayList<String> dictionary;
	
	public static void openDictionary(String fileName) throws IOException {
		File file = new File(fileName);
		FileReader fileReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(fileReader);
		
		String line;
		dictionary = new ArrayList<String>();
		while((line = reader.readLine()) != null) {
			dictionary.add(line);
		}
		
		reader.close();
	}
	
	public static boolean isExistString(String str) {
		boolean flag = false;
		
		for(int i = 0; i < dictionary.size(); i++) {
			if(dictionary.get(i).equals(str)) {
				flag = true;
			}
		}
		
		return flag;
	}
}
