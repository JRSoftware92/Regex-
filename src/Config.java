import java.io.IOException;

/**
 * Singleton Config file
 * @author jriley
 *
 *
 */
public class Config {
	
	private static final String config_file = "/usr/local/etc/regex_parser.cfg";
	
	private static Config instance = null;
	
	private ConfigFileReader config = null;
	
	private Config(){
		try {
			config = new ConfigFileReader(config_file);
		} 
		catch (IOException e) {
			config = null;
		}
	}
	
	public static boolean checkInstance(){
		if(instance == null){
			instance = new Config();
		}
		return instance != null;
	}
	
	public static Config instance(){
		checkInstance();
		return instance;
	}

	/**
	 * Retrieves a value from the Config File corresponding to the provided key
	 * @param key - String key of the value to be retrieved
	 * @return - String value of the parameter being retrieved
	 */
	public static String getValue(String key){
		boolean exists = checkInstance();
		if(exists){
			ConfigFileReader cfg = instance.config;
			if(cfg != null){
				return instance.config.getValue(key);
			}
			else{
				return null;
			}
			
		}
		else{
			return null;
		}
	}
	
	/**
	 * Retrieves a String value from the Config File corresponding to the provided key
	 * @param key - String key of the value to be retrieved
	 * @return - String value of the parameter being retrieved
	 */
	public static String getStringValue(String key){
		boolean exists = checkInstance();
		if(exists){
			ConfigFileReader cfg = instance.config;
			if(cfg != null){
				return instance.config.getStringValue(key);
			}
			else{
				return null;
			}
			
		}
		else{
			return null;
		}
	}
}
