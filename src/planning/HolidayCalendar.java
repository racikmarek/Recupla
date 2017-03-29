package planning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

/**
 * Holiday calendar capable of keeping list of holidays and supports operations among them.
 *
 */
public class HolidayCalendar extends HashSet<Date> {
	private static final long serialVersionUID = -8044811132147776199L;
	public static final String file_encoding = "UTF-8";
	public static final String date_format = "dd.MM.yyyy";
	private SimpleDateFormat sdf;
	
	public HolidayCalendar() {
		super();
		sdf = new SimpleDateFormat(date_format);
	}
	
	public HolidayCalendar(String filepath) {
		this();
		load(filepath);
	}
	
	public void load(String filepath) {
		this.clear();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			String line = br.readLine();
			while (line != null) {
				Date d = sdf.parse(line);
				if (!this.contains((Date) d)) {
					this.add(d);
				}
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save(String filepath) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filepath, file_encoding);
		ArrayList<Date> outList = new ArrayList<Date>(this);
		Collections.sort(outList);
		for (Date d: outList) {
			writer.println(sdf.format(d));
		}
		writer.close();
	}
	
	public void add(int year, int month, int day) {
		Date d = createDate(year, month, day);
		if (!this.contains((Date) d)) {
			this.add(d);
		}
	}	
	
	public boolean contains(Date d) {
		for (Date item: this) {
			if (sdf.format(item).equals(sdf.format(d))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(Calendar c) {
		return contains((Date) c.getTime());
	}
	
	private Date createDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		return cal.getTime();
	}	
	
	public String toString() {
		String result = "";
		ArrayList<Date> outList = new ArrayList<Date>(this);
		Collections.sort(outList);
		for (Date d: outList) {
			if (result != "") {
				result += ", ";
			}
			result += sdf.format(d);
		}
		return result;
	}
	
	public boolean isWeekend(Calendar c) {
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		return (dayOfWeek == 7) || (dayOfWeek == 1);
	}
	
	public boolean isHoliday(Calendar c) {
		return this.contains(c);
	}
	
}
