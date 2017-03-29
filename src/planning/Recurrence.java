package planning;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public abstract class Recurrence {
	private static int maxID = 1;
	private static int randomShiftStep = 6;
	private Random rnd = new Random();
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

	public Recurrence(HolidayCalendar holidayCalendar, RecurrencePeriod period, int step, RecurrenceShiftHandling shiftHandling, 
			Calendar begin, Calendar end, String name, String description, boolean[] daysOfWeekAllowed) {
		super();
		this.holidayCalendar = holidayCalendar;
		this.id = maxID++; 
		this.period = period;
		this.step = step;
		this.shiftHandling = shiftHandling;
		this.begin = begin;
		this.end = end;
		this.name = name;
		this.description = description;
		this.daysOfWeekAllowed = daysOfWeekAllowed;
		
	}
	
	public Recurrence(HolidayCalendar holidayCalendar, String state) {
		super();
		this.holidayCalendar = holidayCalendar;
		setState(state);
	}

	public void setState(String state) {
		//TODO
		throw new UnsupportedOperationException();
	}
	
	public String getState() {
		//TODO
		throw new UnsupportedOperationException();
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
		switch (shiftHandling) {
			case Before: 
				result.add(Calendar.DAY_OF_MONTH, -1); break;
			case BeforeRNDWD: 
				result.add(Calendar.DAY_OF_MONTH, -(rnd.nextInt(randomShiftStep)+1)); break;
			case After: 
				result.add(Calendar.DAY_OF_MONTH, +1); break;
			case AfterRNDWD:
				result.add(Calendar.DAY_OF_MONTH, +(rnd.nextInt(randomShiftStep)+1)); break;
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
	
}
