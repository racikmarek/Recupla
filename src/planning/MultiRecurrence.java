package planning;

import java.util.ArrayList;
import java.util.Calendar;

public class MultiRecurrence extends Recurrence {
	
	public MultiRecurrence(RecurrencePeriod period, int step, RecurrenceShiftHandling weekend_handling,
			Calendar begin, Calendar end, String name, String description) {
		super(period, step, weekend_handling, begin, end, name, description);
	}

	private ArrayList<Recurrence> recurrenceList;

	//TODO: this.period = RecurrencePeriod.none;
	
	@Override
	public ArrayList<Calendar> next_items(Calendar c, int count) {
		
		ArrayList<Calendar> result = new ArrayList<Calendar>();
		for (Recurrence r: recurrenceList) {
			result.addAll(r.next_items(c, count));
			result.sort(new CalendarComparator());
		}
		return result;
	}
	
	@Override
	protected void runCaching(Calendar c) {
		for (Recurrence r: recurrenceList) {
			r.runCaching(c);
		}
	}	
	
	public void addRecurrence(Recurrence r) {
		if (r != null && ! recurrenceList.contains(r)) {
			recurrenceList.add(r);
		}
	}

	public void removeRecurrence(Recurrence r) {
		if (r != null && ! recurrenceList.contains(r)) {
			recurrenceList.remove(r);
		}
	}	
	
	public ArrayList<Recurrence> getRecurrenceList() {
		return new ArrayList<Recurrence>(recurrenceList);
	}

	
}
