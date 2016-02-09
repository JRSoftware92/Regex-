import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for reading Congif 
 * @author jriley
 *
 */
public class ConfigFileReader {
	private static final String KEY_VALUE = "^([a-zA-Z_]+)\\s*=\\s*([ -~]+)\\s*$";
	
	private Pattern regex;
	
	private HashMap<String, String> map;
	
	public ConfigFileReader(){
		regex = Pattern.compile(KEY_VALUE);
	}
	
	public ConfigFileReader(String file) throws IOException{
		this();
		if(file != null){
			readFile(file);
		}
	}
	
	public boolean hasKey(String key){
		return map != null && map.containsKey(key);
	}
	
	public String getValue(String key){
		if(hasKey(key)){
			return map.get(key);
		}
		else{
			return "";
		}
	}
	
	public String getStringValue(String key){
		if(hasKey(key)){
			return trimQuotes(map.get(key));
		}
		else{
			return "";
		}
	}
	
	public void readFile(String filename) throws IOException {
		BufferedReader buffer = null;
		
		try {
			buffer = new BufferedReader(new FileReader(filename));
			map = new HashMap<String, String>();
			
		    String line = buffer.readLine();
		    
		    while (line != null) {
		    	Matcher matcher = regex.matcher(line);
		    	if(matcher.find()){
		    		String key = matcher.group(1);
		    		String value = matcher.group(2);
		    		
		    		map.put(key, value);
		    	}
		        line = buffer.readLine();
		    }
		} 
		finally {
			if(buffer != null){
				buffer.close();
			}
		}
	}
	
	public String debugString(){
		return join(map.toString().split(","), "\n");
	}
	
	public static String join(String[] arr, String delim){
		if(arr == null || delim == null || arr.length < 1){
			return "";
		}
		StringBuilder buffer = new StringBuilder(arr[0]);
		if(arr.length > 1){
			for(int i = 1; i < arr.length; i++){
				buffer.append(delim);
				buffer.append(arr[i]);
			}
		}
		
		return buffer.toString();
	}
	
	public static String trimQuotes(String str){
		String temp = new StringBuilder(str)
				.reverse().toString()
				.replaceFirst("\"", "");
		
		temp = new StringBuilder(temp)
				.reverse().toString()
				.replaceFirst("\"", "");
		
		return temp;
	}
	
	public static void main(String[] args){
		if(args == null || args.length < 1){
			return;
		}
		
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

