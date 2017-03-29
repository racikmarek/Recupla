package planning;

import java.util.Calendar;

public class RecurrenceTestCase {
	public Recurrence recurrence;
	public HolidayCalendar holidayCalendar;
	public Calendar queryDate;
	public Calendar planDate;
	public int occurence;
	public String exception;
	
	public RecurrenceTestCase(Recurrence recurrence, HolidayCalendar holidayCalendar, Calendar queryDate,
			Calendar planDate, int occurence, String exception) {
		super();
		this.recurrence = recurrence;
		this.holidayCalendar = holidayCalendar;
		this.queryDate = queryDate;
		this.planDate = planDate;
		this.occurence = occurence;
		this.exception = exception;
	}
	
}
