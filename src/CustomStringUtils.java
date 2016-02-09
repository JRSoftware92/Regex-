/**
 * Utility class for standard string methods
 * @author jriley
 *
 */
public class CustomStringUtils {

	/**
	 * Joins an array of strings based on the provided delimiter
	 * @param arr - String array of values to be joined
	 * @param delim - String value of the delimiter to be used
	 * @return - String of joined values based on the provided array and delimiter
	 */
	public static String join(String[] arr, String delim){
		if(arr == null || delim == null || arr.length < 1)
			return "";
		
		StringBuilder buffer = new StringBuilder(arr[0]);
		if(arr.length > 1){
			for(int i = 1; i < arr.length; i++){
				buffer.append(delim);
				buffer.append(arr[i]);
			}
		}
		
		return buffer.toString();
	}
	
	/**
	 * Trims a double quoted string
	 * @param str - String value surrounded by double quotes
	 * @return - Value of str without surrounding double quotes
	 */
	public static String trimQuotes(String str){
		String temp = new StringBuilder(str)
				.reverse().toString()
				.replaceFirst("\"", "");
		
		temp = new StringBuilder(temp)
				.reverse().toString()
				.replaceFirst("\"", "");
		
		return temp;
	}
}
