package planning;

import java.util.ArrayList;
import java.util.Calendar;

public class IterativeRecurrence extends Recurrence {

	public IterativeRecurrence(HolidayCalendar holidayCalendar, ArrayList<String> configList) {
		super(holidayCalendar, configList);
	}

	public IterativeRecurrence(HolidayCalendar holidayCalendar, RecurrencePeriod period, int step,
			RecurrenceShiftHandling shiftHandling, Calendar begin, Calendar end, String name, String description,
			boolean[] daysOfWeekAllowed) 
	{
		super(holidayCalendar, period, step, shiftHandling, begin, end, name, description, daysOfWeekAllowed);
	}

	@Override
	public ArrayList<Calendar> next_items(Calendar c, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void runCaching(Calendar c) {
		// TODO Auto-generated method stub
	}

}
