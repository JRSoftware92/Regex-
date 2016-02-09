import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for reading Config file
 * @author jriley
 *
 *
 */
public class ConfigFileReader {
	
	//Attribute Pattern
	private static final String KEY_VALUE = "^([a-zA-Z_]+)\\s*=\\s*([ -~]+)\\s*$";
	
	private Pattern regex;
	
	private HashMap<String, String> map;
	
	/**
	 * Default Constructor
	 */
	public ConfigFileReader(){
		regex = Pattern.compile(KEY_VALUE);
	}
	
	/**
	 * File Read Constructor
	 * @param file - String filepath of the file to be read
	 * @throws IOException
	 */
	public ConfigFileReader(String file) throws IOException {
		this();
		if(file != null)
			readFile(file);
	}
	
	public boolean hasKey(String key){
		return map != null && map.containsKey(key);
	}
	
	/**
	 * Returns the String value of the attribute at the provided key
	 * @param key - String key of the attribute value to be retrieved
	 * @return - String attribute value
	 */
	public String getValue(String key){
		if(hasKey(key))
			return map.get(key);
		else
			return "";
	}
	
	/**
	 * Returns the double quoted String value of the attribute at the provided key
	 * @param key - String key of the attribute value to be retrieved
	 * @return - String double quoted attribute value
	 */
	public String getStringValue(String key){
		if(hasKey(key))
			return CustomStringUtils.trimQuotes(map.get(key));
		else
			return "";
	}
	
	/**
	 * Reads the Configuration file at the provided path
	 * @param filename
	 * @throws IOException
	 */
	public void readFile(String filename) throws IOException {
		//Exits if the filename provided is null
		if(filename == null)
			return;
		
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new FileReader(filename));
			map = new HashMap<String, String>();
			
		    String line = buffer.readLine();
		    
		    //Searches for Attributes in the Configuration file
		    while (line != null) {
		    	Matcher matcher = regex.matcher(line);
		    	//If an attribute is found, extract the key and value from the file and store it in the map.
		    	if(matcher.find()){
		    		String key = matcher.group(1);
		    		String value = matcher.group(2);
		    		
		    		map.put(key, value);
		    	}
		        line = buffer.readLine();
		    }
		} 
		finally {
			if(buffer != null)
				buffer.close();
		}
	}
	
	//For Debugging Purposes Only
	public String debugString(){
		return CustomStringUtils.join(map.toString().split(","), "\n");
	}
	
	//Test Method
	public static void main(String[] args){
		if(args == null || args.length < 1)
			return;
		
		String file = args[0];
		try {
			ConfigFileReader config = new ConfigFileReader(file);
			
			System.out.println(config.debugString());
		} 
		catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}

