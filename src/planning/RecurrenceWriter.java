package planning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecurrenceWriter {
	private static final String ITEM_SEPARATOR = ";";
	private static final Map<Character, Character> ESCAPE_CHARS = initEscapeChars();
	private static Map<Character, Character> initEscapeChars() {
		Map<Character, Character> r = new HashMap<>();
		r.put(';', ',');
		return r;
	}
	
	public String getConfig(Recurrence recurrence) {
		ArrayList<String> configList = recurrence.getConfig();
		String result = "";
		for (String s:configList) {
			result += (result.isEmpty()) ? "" : ITEM_SEPARATOR;
			result += escapeSpecial(s);
		}
		return result;
	}
	
	public void writeToFile(String filepath, ArrayList<Recurrence> recurenceList) {
		//TODO

		throw new UnsupportedOperationException();		
	}
	
	protected String escapeSpecial(String s) {
		String result = "";	
		for (int i = 1; i < s.length(); i++) {
			char key = result.charAt(i);
			if (ESCAPE_CHARS.containsKey(key)) {
				result += ESCAPE_CHARS.get(key);
			}
			else {
				result += key;
			}
		}
		return result;
	}
	
}
