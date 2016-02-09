import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for reading custom objects from a file into a unique, sorted array of strings.
 * @author jriley
 *
 */
public class RegexScanner {
	
	private static final String HELP_FILE = "help_file";
	
	//Argument Flags
	private static final String OUTPUT_FLAG = "-o";
	private static final String INPUT_FLAG = "-i";
	private static final String REGEX_FLAG ="-regex";
	private static final String HTML_FLAG = "-html";
	private static final String PREBUILT_FLAG = "-pre";
	private static final String VERBOSE_FLAG = "-v";
	private static final String QUIET_FLAG ="-q";
	private static final String ARG_ERROR_FLAG = "-ARGERROR";
	private static final String HELP_FLAG = "-help";
	
	private HashSet<String> object_set = new HashSet<String>();
	private Pattern regex;
	
	public RegexScanner(String rgx){
		regex = Pattern.compile(rgx);
	}
	
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
	
	//Sets Verbose mode to false, and Quiet mode to true by default.
	public void readFile(String filename) throws IOException{
		readFile(filename, false, true);
	}

	public void readFile(String filename, boolean isVerbose, boolean isQuiet) throws IOException{
		//Exits if filename is invalid
		if(filename == null || filename.length() < 1){
			if(!isQuiet){
				System.out.println("Invalid input file name provided.");
			}
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
			 if(isVerbose && !isQuiet){
				 System.out.println(Integer.toString(count) + ": " + line);
			 }
			 matcher = regex.matcher(line);
			 while(matcher.find()){
				 if(isVerbose && !isQuiet){
					 System.out.println("\t- Match Found!");
				 }
				 object_set.add(matcher.group(0));
			 }
		  }
		  
		  br.close();
	}
	
	//Safely processes arguments and retrieves the Input File, Output File, and Regex patterns provided
	public static HashMap<String, String> processArgs(String[] args){
		HashMap<String, String> map = new HashMap<String, String>();
		int size = args.length;
		
		//Invalid
		if(args == null || size < 1){
			map.put(ARG_ERROR_FLAG, "NULL");
			return map;
		}
		
		for(int i = 0; i < size; i++){
			//Arguments with secondary inputs
			if(args[i].equals(INPUT_FLAG) || args[i].equals(OUTPUT_FLAG) || args[i].equals(REGEX_FLAG) || args[i].equals(PREBUILT_FLAG)){
				if (i + 1 >= size){
					map.put(ARG_ERROR_FLAG, args[i]);
				}
				else{
					map.put(args[i], args[++i]);
				}
			}
			//Singular Arguments
			else if(args[i].equals(VERBOSE_FLAG) || args[i].equals(QUIET_FLAG) || args[i].equals(HTML_FLAG) || args[i].equals(HELP_FLAG)){
				map.put(args[i], "Y");
			}
			//Invalid Arguments
			else{
				map.put(ARG_ERROR_FLAG, args[i]);
			}
		}
		
		//Failsafe
		if(map.size() < 1){
			map.put(ARG_ERROR_FLAG, "NULL");
		}
		
		return map;
	}
	
	public static void printHelpFile() throws IOException {
		String file = Config.getStringValue(HELP_FILE);
		BufferedReader buffer = new BufferedReader(new FileReader(file));
		String line = buffer.readLine();
		
		while(line != null){
			System.out.println(line);
			line = buffer.readLine();
		}
		
		if(buffer != null){
			buffer.close();
		}
	}
	
	public static void main(String[] args){
		String entry = null;
		String regex = "";
		boolean output_flag = false, html_flag = false, isVerbose = false, isQuiet = false;
		
		boolean hasConfigFile = Config.checkInstance();
		if(!hasConfigFile){
			System.out.println("Config file not found. Exiting program.");
			return;
		}
		
		HashMap<String, String> map = processArgs(args);
		
		if(map.containsKey(HELP_FLAG)){
			try{
				printHelpFile();
			}
			catch(IOException e){
				System.out.println(e.getMessage());
				System.out.println("Exiting program.");
			}
			return;
		}
		
		//Level of output
		if(map.containsKey(VERBOSE_FLAG)){
			isVerbose = true;
		}
		if(map.containsKey(QUIET_FLAG)){
			isQuiet = true;
		}
		
		if(map.containsKey(ARG_ERROR_FLAG)){
			if(!isQuiet){
				System.out.println("Invalid argument provided: " + map.get(ARG_ERROR_FLAG));
			}
			return;
		}

		try {
			
			if(isVerbose && !isQuiet){
				System.out.println("Validating Provided Regex Pattern...");
			}
			//Validates Regex Pattern Entry
			if(!map.containsKey(REGEX_FLAG)){
				if(!map.containsKey(PREBUILT_FLAG)){
					if(!isQuiet){
						System.out.println("No regex pattern provided. Exiting program.");
					}
					return;
				}
				else{
					regex = StandardRegex.getRegexByName(map.get(PREBUILT_FLAG));
					if(regex.equals("") && !isQuiet){
						System.out.println("Invalid Prebuilt Regex Argument Provided: " + map.get(PREBUILT_FLAG));
					}
				}
			}
			else{
				regex = map.get(REGEX_FLAG);
				if(isVerbose && !isQuiet){
					System.out.println("Provided Regex Pattern: \n" + regex);
				}
			}
			
			//Initializes Input Stream
			InputStreamReader istream = new InputStreamReader(System.in);
			BufferedReader input = new BufferedReader(istream);
			RegexScanner reader = new RegexScanner(regex);
			
			//Validates Input File
			if(map.containsKey(INPUT_FLAG)){
				entry = map.get(INPUT_FLAG);
			}
			else{
				//Retrieves the input file name
				while(entry == null || entry.length() < 1){
					System.out.println("Enter the relative address of the file you'd like to scan: ");
					entry = input.readLine();
				}
			}
			
			if(isVerbose && !isQuiet){
				System.out.println("Reading input file...");
			}
			
			//Reads the input file
			reader.readFile(entry, isVerbose, isQuiet);
			
			if(isVerbose && !isQuiet){
				System.out.println("Input File Read Succesful.");
			}
			
			String[] output = reader.get_objects();
			if(!isQuiet){
				System.out.println(Integer.toString(output.length) + " unique custom objects found.");
			}
			
			for(String obj : output){
				System.out.println(obj);
			}
			
			if(map.containsKey(OUTPUT_FLAG)){
				entry = map.get(OUTPUT_FLAG);
				output_flag = true;
			}
			else{
				output_flag = false;
			}
			
			//Writes the output to a file if specified
			if(output_flag){
				String html_arg = null;
				
				if(isVerbose && !isQuiet){
					System.out.println("Writing Output File...");
				}
				
				//Determines if output should be an HTML list
				if(map.containsKey(HTML_FLAG)){
					html_arg = map.get(HTML_FLAG);
				}
				else if(!isQuiet){
					while(html_arg == null || html_arg.length() < 1){
						System.out.println("Would you like to output the file as an HTML list? (Y/N) ");
						html_arg = input.readLine();
					}
				}
				
				html_flag = html_arg.equalsIgnoreCase("Y");
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(entry));
				if(html_flag){
					writer.write("<ul>");
					if(isVerbose && !isQuiet){
						System.out.println("Output Format: HTML");
					}
				}
				else{
					if(isVerbose && !isQuiet){
						System.out.println("Output Format: Standard");
					}
				}
				
				for(String s : output){
					if(html_flag){
						writer.write("<li>"+s+"</li>");
					}
					else{
						writer.write(s);
					}
					writer.newLine();
				}
				
				if(html_flag){
					writer.write("</ul>");
				}
				writer.close();
			}
			
			input.close();
			istream.close();
			
			if(isVerbose && !isQuiet){
				System.out.println("Output File Write Successful. End of program.");
			}
		} 
		catch (IOException e) {
			if(!isQuiet){
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}
	}
	
}
