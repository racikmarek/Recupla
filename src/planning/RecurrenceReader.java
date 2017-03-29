package planning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecurrenceReader {
	private static final String ITEM_SEPARATOR = ";";
	private static final Map<Character, Character> ESCAPE_CHARS = initEscapeChars();
	private static Map<Character, Character> initEscapeChars() {
		Map<Character, Character> r = new HashMap<>();
		r.put(';', ',');
		return r;
	}
	protected HolidayCalendar calendar;
	
	public RecurrenceReader(HolidayCalendar calendar) {
		super();
		this.calendar = calendar;
	}

	public Recurrence getRecurrence(String config) {
		//TODO

		throw new UnsupportedOperationException();
	}
	
	public ArrayList<Recurrence> readFromFile(String filepath) {
		//TODO

		throw new UnsupportedOperationException();
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

	public HolidayCalendar getCalendar() {
		return calendar;
	}



	public void setCalendar(HolidayCalendar calendar) {
		this.calendar = calendar;
	}
	
}
