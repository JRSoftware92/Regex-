
/**
 * Enum Class contains values for Standard Regular Expressions
 * @author jriley
 *
 */
public enum StandardRegex {
	
	phone("(\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b)"), 
	phone_us("((1?(?: |\\-|\\.)?(?:\\(\\d{3}\\)|\\d{3})(?: |\\-|\\.)?\\d{3}(?: |\\-|\\.)?\\d{4}))"),
	phone_all("((\\d{0,3})(-| ?)(\\()?([0-9]{3})(\\)|-| |\\)-|\\) )?([0-9]{3})(-| )?([0-9]{4}|[0-9]{4}))"),
	email("([\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4})"), 
	url("((?:http|ftp|https):\\/\\/[\\w\\-_]+(?:\\.[\\w\\-_]+)+(?:[\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?)"),
	ip("(([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3})");
	
	private String pattern;
	
	StandardRegex(String regex){
		this.pattern = regex;
	}
	
	public static String getRegexByName(String name){
		if(name == null || name.length() < 1)
			return "";
		
		String result = name;
		StandardRegex[] arr = StandardRegex.values();
		for(StandardRegex regex : arr){
			if(regex.name().equals(name)){
				result = regex.pattern;
				break;
			}
		}
		
		return result;
	}
}
