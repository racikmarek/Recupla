package planning;

import java.util.ArrayList;
import java.util.Calendar;

public class MultiplicativeRecurrence extends Recurrence {

	public MultiplicativeRecurrence(RecurrencePeriod period, int step, RecurrenceShiftHandling weekend_handling,
			Calendar begin, Calendar end, String name, String description) {
		super(period, step, weekend_handling, begin, end, name, description);
	}

	@Override
	public ArrayList<Calendar> next_items(Calendar c, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void runCaching(Calendar c) {
		// TODO 
		throw new UnsupportedOperationException();
	}
	
	

}
