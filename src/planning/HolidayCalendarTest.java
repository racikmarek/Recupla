package planning;

import static org.junit.Assert.*;
import java.util.Calendar;
import org.junit.Test;

public class HolidayCalendarTest {

	private HolidayCalendar initBasic() {
		HolidayCalendar hc = new HolidayCalendar();
		//create sample of dates
		hc.add(2017, 1, 1);
		hc.add(2017, 1, 1);
		hc.add(2016, 12, 31);
		hc.add(2017, 3, 25);
		return hc;
	}
	
	@Test
	public void testDuplicateInsertion() throws Exception {
		HolidayCalendar hc = initBasic();
		//create sample of dates
		hc.add(2017, 1, 1);
		hc.add(2017, 1, 1);
		hc.add(2016, 12, 31);
		hc.add(2017, 3, 25);
		assertTrue(hc.size() == 3);
		//System.out.println(hc);
	}

	@Test
	public void testSaveCount() throws Exception {
		int checksum = 0;
		HolidayCalendar hc = initBasic();
		String filename = "holidays.txt";
		checksum += hc.size();
		hc.save(filename);
		checksum += hc.size();
		hc.load(filename);
		checksum += hc.size();
		//System.out.println(hc);
		hc.save(filename);
		checksum += hc.size();
		hc.load(filename);
		checksum += hc.size();
		//System.out.println(hc);
		hc.save(filename);
		checksum += hc.size();
		assertTrue(checksum == 18);
	}
	
	@Test
	public void testSaveLoadContent() throws Exception {
		HolidayCalendar hc = initBasic();
		String filename = "holidays.txt";
		boolean result = true;
		hc.save(filename);
		hc.load(filename);		
		Calendar c = Calendar.getInstance();
		c.set(2016, Calendar.DECEMBER, 31);
		result &= hc.contains(c);
		c.set(2017, Calendar.JANUARY, 1);
		result &= hc.contains(c);
		c.set(2017, Calendar.MARCH, 25);
		result &= hc.contains(c);
		c.set(2017, Calendar.JANUARY, 2);
		result &= ! hc.contains(c);
		c.set(2017, Calendar.MARCH, 25);
		result &= hc.isHoliday(c);
		assertTrue(result);
	}
	
	@Test
	public void testIsWeekend() {
		boolean result = true;
		HolidayCalendar hc = initBasic();
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.FEBRUARY, 18);	//regular Saturday	
		result &= hc.isWeekend(c);
		c.set(2017, Calendar.FEBRUARY, 19);	//regular Sunday	
		result &= hc.isWeekend(c);
		c.set(2017, Calendar.FEBRUARY, 17);	//negative: Friday
		result &= ! hc.isWeekend(c);
		c.set(2017, Calendar.FEBRUARY, 20); //negative: Monday
		result &= ! hc.isWeekend(c);
		c.set(2035, Calendar.JULY, 21); //regular weekend - random future
		result &= hc.isWeekend(c);
		hc.add(2017, 2, 20);            //define holiday-weekend collision
		c.set(2017, Calendar.FEBRUARY, 20); 
		result &= ! hc.isWeekend(c);    //holiday is not a weekend
		assertTrue(result);
	}

	@Test
	public void testIsHoliday() {
		boolean result = true;
		HolidayCalendar hc = initBasic();
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 25);	//positive case 1
		result &= hc.isHoliday(c);
		c.set(2017, Calendar.JANUARY, 1);	//positive case 2
		result &= hc.isHoliday(c);
		c.set(2017, Calendar.APRIL, 15);	//negative case 2
		result &= ! hc.isHoliday(c);
		assertTrue(result);
	}
	
}
