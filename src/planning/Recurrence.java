package planning;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public abstract class Recurrence {
	//constants
	public static final String DATE_FORMAT = "dd.MM.yyyy";
	public static final String RECORD_DELIMITER = ",";
	private static final int RANDOM_SHIFT_STEP = 6;
	//initialization of constants from configuration file
	static {
		try {
			//TODO
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(Recurrence.class.getName() + " class could not be initialized.", e);
		}
	}
	//global configuration
	private static int maxID = 0;
	//recurrence attributes
	protected HolidayCalendar holidayCalendar;
	protected int id;
	protected boolean cacheEnabled = false; 
	protected Calendar cacheDate = null;
	protected RecurrencePeriod period = RecurrencePeriod.None;
	protected int step = 0;
	protected RecurrenceShiftHandling shiftHandling;
	protected Calendar begin;
	protected Calendar end;
	protected String name;
	protected String description;
	protected boolean[] daysOfWeekAllowed = {true, true, true, true, true, true, true}; //first = Sunday; last = Saturday
	protected boolean allowWeekend;
	protected boolean allowHoliday;
	protected ArrayList<Calendar> excludedDays;
	
	public Recurrence(HolidayCalendar holidayCalendar, ArrayList<String> configList) {
		super();
		this.holidayCalendar = holidayCalendar;
		this.excludedDays = new ArrayList<Calendar>();
		setConfig(configList);
	}

	public void setConfig(ArrayList<String> configList) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String inputId = configList.get(0);
		this.id = (inputId.isEmpty()) ? ++Recurrence.maxID : Integer.parseInt(inputId);
		Recurrence.maxID = Math.max(Recurrence.maxID, this.id);
		this.period = RecurrencePeriod.valueOf(configList.get(1));
		this.step = Integer.parseInt(configList.get(2));
		this.shiftHandling = RecurrenceShiftHandling.valueOf(configList.get(3));
		try {
			this.begin = Calendar.getInstance();
			this.begin.setTime(sdf.parse(configList.get(4)));
			this.end = Calendar.getInstance();
			this.end.setTime(sdf.parse(configList.get(5)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.name = configList.get(6);
		this.description = configList.get(7);
		String daysOfWeekAllowedStr = configList.get(8);
		for (int i = 0; i < daysOfWeekAllowedStr.length(); i++) {
			this.daysOfWeekAllowed[i] = (daysOfWeekAllowedStr.charAt(i) == '1');
		}
		this.allowWeekend = configList.get(9) == "1" ? true : false;
		this.allowHoliday = configList.get(10) == "1" ? true : false;
		
		String[] excludedDaysStrs = configList.get(11).split(RECORD_DELIMITER);
		for (int i = 0; i < excludedDaysStrs.length; i++) {
			Calendar cal = Calendar.getInstance();
			try {
				cal.setTime(sdf.parse(excludedDaysStrs[i]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.excludedDays.add(cal);
		}
	}
	
	public ArrayList<String> getConfig() {
		ArrayList<String> configList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		configList.add(Integer.toString(this.id));
		configList.add(this.period.name());
		configList.add(Integer.toString(this.step));
		configList.add(this.shiftHandling.name());
		configList.add(sdf.format(this.begin));
		configList.add(sdf.format(this.end));
		configList.add(this.name);
		configList.add(this.description);
		String daysOfWeekAllowedStr = "";
		for (int i = 0; i < this.daysOfWeekAllowed.length; i++) {
			daysOfWeekAllowedStr += (daysOfWeekAllowed[i] == true) ? '1' : '0';
		}
		configList.add(daysOfWeekAllowedStr);
		configList.add(allowWeekend ? "1" : "0");
		configList.add(allowHoliday ? "1" : "0");
		String excludedDaysStr = "";
		for (Calendar excludedDay: this.excludedDays) {
			excludedDaysStr = excludedDaysStr.isEmpty() ? "" : RECORD_DELIMITER;
			sdf.format(excludedDay);
		}
		configList.add(excludedDaysStr);
		return configList;
	}
	
	public abstract ArrayList<Calendar> next_items(Calendar c, int count);
	
	protected Calendar normalize(Calendar c) {
		Calendar result = Calendar.getInstance();
		result.setTime(c.getTime());
		int step = 0;
		while (!isNormalized(result)) {
			switch (step % 3) {
				case 0: result = normalizeWeekend(result); break;
				case 1: result = normalizeHolidays(result); break;
				case 2: result = normalizeAllowedDaysOfWeek(result); break;
			}
			step++;
		}
		return result;
	}
	
	private Calendar normalizeWeekend(Calendar c) {
		Calendar result = Calendar.getInstance();
		result.setTime(c.getTime());
		if (shiftHandling == RecurrenceShiftHandling.NoShift || isNormalizedWeekend(result)) {
			return result;
		}
		while (! isNormalizedWeekend(result)) {
			applyShiftStep(c);
		}
		return result;
	}
	
	private Calendar normalizeHolidays(Calendar c) {
		Calendar result = Calendar.getInstance();
		result.setTime(c.getTime());
		if (shiftHandling == RecurrenceShiftHandling.NoShift || isNormalizedHolidays(result)) {
			return result;
		}
		while (! isNormalizedHolidays(result)) {
			applyShiftStep(c);
		}
		return result;
	}
	
	private Calendar normalizeAllowedDaysOfWeek(Calendar c) {
		Calendar result = Calendar.getInstance();
		result.setTime(c.getTime());
		if (shiftHandling == RecurrenceShiftHandling.NoShift || isNormalizedAllowedDaysOfWeek(result)) {
			return result;
		}
		while (! isNormalizedAllowedDaysOfWeek(result)) {
			applyShiftStep(c);
		}
		return result;
	}	
	
	private Calendar applyShiftStep(Calendar c) {
		Calendar result = Calendar.getInstance();
		result.setTime(c.getTime());
		Random rnd;
		switch (shiftHandling) {
			case Before: 
				result.add(Calendar.DAY_OF_MONTH, -1); break;
			case BeforeRNDWD: 
				rnd = new Random();
				result.add(Calendar.DAY_OF_MONTH, -(rnd.nextInt(RANDOM_SHIFT_STEP)+1)); break;
			case After: 
				result.add(Calendar.DAY_OF_MONTH, +1); break;
			case AfterRNDWD:
				rnd = new Random();
				result.add(Calendar.DAY_OF_MONTH, +(rnd.nextInt(RANDOM_SHIFT_STEP)+1)); break;
			default: return result;
		}
		return result;
	}		
	
	protected boolean isNormalized(Calendar c) {
		return (isNormalizedAllowedDaysOfWeek(c) || isNormalizedHolidays(c) || isNormalizedWeekend(c));
	}
	
	private boolean isNormalizedAllowedDaysOfWeek(Calendar c) {
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		return daysOfWeekAllowed[dayOfWeek - 1];
	}
	
	private boolean isNormalizedHolidays(Calendar c) {
		return ! holidayCalendar.isHoliday(c);
	}
	
	private boolean isNormalizedWeekend(Calendar c) {
		return ! holidayCalendar.isWeekend(c);
	}
	
	protected abstract void runCaching(Calendar c);

	public void cacheToDate(Calendar c) {
		if (isCaching()) {
			runCaching(c);
		}
	}

	public boolean isCaching() {
		return cacheEnabled;
	}
	
	public void enableCaching(Calendar cacheDate) {
		this.cacheDate = cacheDate;
		this.cacheEnabled = true;
	}

	public void disableCaching() {
		this.cacheDate = null;
		this.cacheEnabled = false;
	}

	public RecurrencePeriod getPeriod() {
		return period;
	}
	
	public void setPeriod(RecurrencePeriod period) {
		this.period = period;
	}
	
	public int getStep() {
		return step;
	}
	
	public void setStep(int step) {
		this.step = step;
	}
	
	public RecurrenceShiftHandling getWeekend_handling() {
		return shiftHandling;
	}

	public void setWeekend_handling(RecurrenceShiftHandling weekend_handling) {
		this.shiftHandling = weekend_handling;
	}

	public Calendar getBegin() {
		return begin;
	}

	public void setBegin(Calendar begin) {
		this.begin = begin;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}
	
	public boolean[] getDaysOfWeekAllowed() {
		return daysOfWeekAllowed;
	}

	public void setDaysOfWeekAllowed(boolean[] daysOfWeekAllowed) {
		this.daysOfWeekAllowed = daysOfWeekAllowed;
	}

	public boolean isAllowWeekend() {
		return allowWeekend;
	}

	public void setAllowWeekend(boolean allowWeekend) {
		this.allowWeekend = allowWeekend;
	}

	public boolean isAllowHoliday() {
		return allowHoliday;
	}

	public void setAllowHoliday(boolean allowHoliday) {
		this.allowHoliday = allowHoliday;
	}

	public ArrayList<Calendar> getExcludedDays() {
		return excludedDays;
	}

	public void setExcludedDays(ArrayList<Calendar> excludedDays) {
		this.excludedDays = excludedDays;
	}
	
}
