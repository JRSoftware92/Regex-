import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for reading custom objects from a file into a unique, sorted array of strings.
 * @author jriley
 *
 *
 */
public class RegexScanner {
	
	private HashSet<String> object_set = new HashSet<String>();
	private Pattern regex;
	
	/**
	 * Regex Constructor
	 * @param rgx - String regex pattern to scan for
	 */
	public RegexScanner(String rgx){
		regex = Pattern.compile(rgx);
	}
	
	/**
	 * Returns the object set as a sorted array of String values
	 * @return - String array of sorted unique values
	 */
	public String[] get_objects(){
		if(object_set == null || object_set.size() < 1){
			return new String[0];
		}
		else{
			String[] arr = new String[object_set.size()];
			arr = object_set.toArray(arr);
			
			Arrays.sort(arr);
			
			return arr;
		}
	}
	
	/**
	 * Wrapper for readFile(filename, isVerbose, isQuiet
	 * Sets Verbose mode to false, and Quiet mode to true by default.
	 * @param filename - String absolute path of the file to read
	 * @throws IOException
	 */
	public void readFile(String filename) throws IOException{
		readFile(filename, false, true);
	}

	/**
	 * Scans the file at the provided path for values matching the initialized regex
	 * @param filename - String absolute path of the file to read
	 * @param isVerbose - true if the program should print more information at run time
	 * @param isQuiet - true if the program should print no extra information at run time
	 * @throws IOException
	 */
	public void readFile(String filename, boolean isVerbose, boolean isQuiet) throws IOException{
		//Exits if filename is invalid
		if(filename == null || filename.length() < 1){
			if(!isQuiet)
				System.out.println("Invalid input file name provided.");
			return;
		}
		
		FileInputStream stream = new FileInputStream(filename);
		DataInputStream input = new DataInputStream(stream);
		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		String line;
		Matcher matcher;
		int count = 0;
		  
		//Retrieves matches from each line
		while ((line = br.readLine()) != null)   {
			count++;
			 if(isVerbose)
				 System.out.println(Integer.toString(count) + ": " + line);
			 
			 matcher = regex.matcher(line);
			 while(matcher.find()){
				 if(isVerbose)
					 System.out.println("\t- Match Found!");
				 object_set.add(matcher.group(0));
			 }
		}
		  
		br.close();
	}
}
