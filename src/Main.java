import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Run file for RegexScanner
 * @author jriley
 *
 */
public class Main {
	
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

	//Safely processes arguments and retrieves the Input File, Output File, and Regex patterns provided
	private static HashMap<String, String> processArgs(String[] args){
		HashMap<String, String> map = new HashMap<String, String>();
		int size = args.length;
		
		//Invalid
		if(args == null || size < 1){
			map.put(ARG_ERROR_FLAG, "NULL");
			return map;
		}
		
		for(int i = 0; i < size; i++){
			//Arguments with secondary inputs
			switch(args[i]){
			case INPUT_FLAG:
			case REGEX_FLAG:
			case PREBUILT_FLAG:
			case OUTPUT_FLAG:			//Handle Parameters
				if (i + 1 >= size)
					map.put(ARG_ERROR_FLAG, args[i]);
				else
					map.put(args[i], args[++i]);
				break;
			case VERBOSE_FLAG:
			case QUIET_FLAG:
			case HTML_FLAG:
			case HELP_FLAG:				//Set Argument Flag
				map.put(args[i], "Y");
				break;
			default:					//Invalid Argument Provided
				map.put(ARG_ERROR_FLAG, args[i]);
				break;
			}
		}
		
		//Failsafe
		if(map.size() < 1)
			map.put(ARG_ERROR_FLAG, "NULL");
		
		return map;
	}
	
	/**
	 * Prints the Help file to the console
	 * @throws IOException
	 */
	public static void printHelpFile() throws IOException {
		String file = Config.getStringValue(HELP_FILE);
		BufferedReader buffer = new BufferedReader(new FileReader(file));
		String line = buffer.readLine();
		
		while(line != null){
			System.out.println(line);
			line = buffer.readLine();
		}
		
		if(buffer != null)
			buffer.close();
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
		if(map.containsKey(QUIET_FLAG))
			isQuiet = true;
		else if(map.containsKey(VERBOSE_FLAG))
			isVerbose = true;
		
		if(map.containsKey(ARG_ERROR_FLAG)){
			if(!isQuiet)
				System.out.println("Invalid argument provided: " + map.get(ARG_ERROR_FLAG));
			return;
		}

		try {
			
			if(isVerbose)
				System.out.println("Validating Provided Regex Pattern...");
			//Validates Regex Pattern Entry
			if(!map.containsKey(REGEX_FLAG)){
				if(!map.containsKey(PREBUILT_FLAG)){
					if(!isQuiet)
						System.out.println("No regex pattern provided. Exiting program.");
					return;
				}
				else{
					regex = StandardRegex.getRegexByName(map.get(PREBUILT_FLAG));
					if(regex.equals("") && !isQuiet)
						System.out.println("Invalid Prebuilt Regex Argument Provided: " + map.get(PREBUILT_FLAG));
				}
			}
			else{
				regex = map.get(REGEX_FLAG);
				if(isVerbose)
					System.out.println("Provided Regex Pattern: \n" + regex);
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
			
			if(isVerbose)
				System.out.println("Reading input file...");
			
			//Reads the input file
			reader.readFile(entry, isVerbose, isQuiet);
			
			if(isVerbose)
				System.out.println("Input File Read Succesful.");
			
			String[] output = reader.get_objects();
			if(!isQuiet)
				System.out.println(Integer.toString(output.length) + " unique custom objects found.");
			
			for(String obj : output)
				System.out.println(obj);
			
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
				
				if(isVerbose)
					System.out.println("Writing Output File...");
				
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
					if(isVerbose)
						System.out.println("Output Format: HTML");
				}
				else{
					if(isVerbose)
						System.out.println("Output Format: Standard");
				}
				
				for(String s : output){
					if(html_flag)
						writer.write(String.format("<li>%s</li>", s));
					else
						writer.write(s);
					writer.newLine();
				}
				
				if(html_flag)
					writer.write("</ul>");
				writer.close();
			}
			
			input.close();
			istream.close();
			
			if(isVerbose)
				System.out.println("Output File Write Successful. End of program.");
		} 
		catch (IOException e) {
			if(!isQuiet){
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}
	}

}
