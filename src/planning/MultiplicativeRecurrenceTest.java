package planning;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.junit.Assert;
import org.junit.Test;

public class MultiplicativeRecurrenceTest {
	private static final String date_format = Recurrence.DATE_FORMAT;
	private static final String testFilePath = "tests/recurrence_tests.csv";
	private static final String csvDelimiter = ";";
	private static final String listItemDelimiter = ", ";
	private static final String exceptCreation = "C";
	private static final String exceptTest = "T";
	private static final String exceptNone = "";
	private static final String replanMethod = "Multiplicative";
	private static ArrayList<RecurrenceTestCase> testCases;
	
	//TODO change configuration of tests according to attributes of Recurrence class
	
	private ArrayList<String> loadRecurrenceConfig(String[] recDef) {				
		ArrayList<String> recurrenceConfig = new ArrayList<>();
		recurrenceConfig.add(id)
		
		Calendar begin = Calendar.getInstance();
		begin.setTime(sdf.parse(recDef[8]));
		Calendar end = Calendar.getInstance();
		end.setTime(sdf.parse(recDef[10]));
		
		RecurrencePeriod period = RecurrencePeriod.valueOf(recDef[3]);
		int step = Integer.parseInt(recDef[5]);
		
		RecurrenceShiftHandling weekend_handling = RecurrenceShiftHandling.valueOf(recDef[7]);
				
		
		return recurrenceConfig;
	}
	
	private ArrayList<RecurrenceTestCase> getTestCases() throws ParseException {
		ArrayList<RecurrenceTestCase> result = new ArrayList<RecurrenceTestCase>();
		BufferedReader br = null;
		String line;
		try {
			br = new BufferedReader(new FileReader(testFilePath));
			while ((line = br.readLine()) != null) {
				String[] recDef = line.split(csvDelimiter);
				if (!recDef[4].equals(replanMethod)) {
					continue;
				}
				SimpleDateFormat sdf = new SimpleDateFormat(date_format);
				
				//TODO call load recurrence config


				
				String name = recDef[1];
				String description = "";

				HolidayCalendar hc = new HolidayCalendar();
				Calendar queryDate = begin; 
				
				Calendar planDate = null;
				String exception = recDef[11];
				if (exception == exceptNone) {
					planDate = Calendar.getInstance();
					planDate.setTime(sdf.parse(recDef[11]));
				}
				
				int occurence = Integer.parseInt(recDef[6]);
				
				sdf = new SimpleDateFormat(date_format);
				String holidays = recDef[9];
				if (holidays.length() > 2) {
					holidays = holidays.substring(1, holidays.length() - 1);
					String[] holidayDates = holidays.split(listItemDelimiter);
					for (int i = 1; i < holidayDates.length; i++) {
						hc.add(sdf.parse(holidayDates[i]));
					}
				}			
				
				MultiplicativeRecurrence r = null;
				if (exception == exceptCreation) {
					try {
						new MultiplicativeRecurrence(period, step, weekend_handling, begin, end, name, description);
						fail(name + " " + "should have end with init exception");
					}
					catch (Exception e) {
					}
				}
				else {
					r = new MultiplicativeRecurrence(period, step, weekend_handling, begin, end, name, description);
				}
				result.add(new RecurrenceTestCase(r, hc, queryDate, planDate, occurence, exception));
			}
			
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	@Test
	public void runAll() throws ParseException {
		testCases = getTestCases();
		if (testCases.size() == 0) {
			fail("Test file not initialized correctly");
		}
		for (RecurrenceTestCase tc: testCases) {
			testTextCase(tc);
		}
	}	
	
	private void testTextCase(RecurrenceTestCase tc) {
		if (tc.exception == exceptTest) {
			try {
				tc.recurrence.next_items(tc.queryDate, tc.occurence + 1);
			}
			catch (Exception e) {
			}
		}
		else {
			try {	
				Calendar planDate = tc.recurrence.next_items(tc.queryDate, tc.occurence + 1).get(tc.occurence);
				if (planDate != tc.planDate) {
					Assert.fail(tc.recurrence.getId() + " " + tc.recurrence.getName() + "- plan date does not match test case.");
				}
			}
			catch (NullPointerException e) {
				Assert.fail(tc.recurrence.getId() + " " + tc.recurrence.getName() + "- failed on NullPointerException at next_items");
			}
		}
		assertTrue(true);
	}
	
}
