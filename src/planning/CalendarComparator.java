package planning;

import java.util.Comparator;
import java.util.Calendar;

public class CalendarComparator implements Comparator<Calendar> {

	@Override
	public int compare(Calendar c1, Calendar c2) {
		if (c1.before(c2)) {
			return -1;
		} else if (c1.after(c2)) {
			return 1;
		}
		else {
			return 0;
		}
	}

}
