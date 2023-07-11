package telran.time.application;

import java.time.*;

import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Locale;

public class PrintCalendar {
	private static final int TITLE_OFFSET = 8;
	static DayOfWeek[] daysOfweek = DayOfWeek.values();

	public static void main(String[] args) {
		try {
			RecordArguments recordArguments = getRecordArguments(args);
			printCalendar(recordArguments);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void printCalendar(RecordArguments recordArguments) {
		printTitle(recordArguments.month(), recordArguments.year());
		printWeekDays();
		printDays(recordArguments.month(), recordArguments.year());
	}

	private static void printDays(int month, int year) {
		int nDays = getNumberOfDays(month, year);
		int currentWeekDay = getFirstWeekDay(month, year);
		printOffset(currentWeekDay);
		for (int day = 1; day <= nDays; day++) {
			System.out.printf("%4d", day);
			currentWeekDay++;
			if (currentWeekDay == 7) {
				currentWeekDay = 0;
				System.out.println();
			}
		}
	}

	private static void printOffset(int currentWeekDay) {
		System.out.printf("%s", " ".repeat(4 * currentWeekDay));
	}

	private static int getFirstWeekDay(int month, int year) {
		LocalDate firstDateMonth = LocalDate.of(year, month, 1);
		int firstWeekDay = firstDateMonth.getDayOfWeek().getValue();
		int firstValue = daysOfweek[0].getValue();
		int delta = firstWeekDay - firstValue;
		return delta >= 0 ? delta : delta + daysOfweek.length;
	}

	private static int getNumberOfDays(int month, int year) {
		YearMonth ym = YearMonth.of(year, month);
		return ym.lengthOfMonth();
	}

	private static void printWeekDays() {
		System.out.print("  ");
		Arrays.stream(daysOfweek)
				.forEach(dw -> System.out.printf("%s ", dw.getDisplayName(TextStyle.SHORT, Locale.getDefault())));
		System.out.println();
	}

	private static void printTitle(int monthNow, int year) {
		Month month = Month.of(monthNow);
		String monthName = month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
		System.out.printf("%s%s, %d\n", " ".repeat(TITLE_OFFSET), monthName, year);
	}

	private static RecordArguments getRecordArguments(String[] args) throws Exception {
		LocalDate ld = LocalDate.now();
		int month = args.length == 0 ? ld.get(ChronoField.MONTH_OF_YEAR) : getMonth(args[0]);
		int year = args.length > 1 ? getYear(args[1]) : ld.get(ChronoField.YEAR);
		DayOfWeek firstWeekDay = args.length >= 3 ? getDayOfWeek(args[2]) : DayOfWeek.MONDAY;
		return new RecordArguments(month, year, firstWeekDay);
	}

	private static DayOfWeek getDayOfWeek(String dayOfWeek) throws Exception {
		String message = "";
		DayOfWeek firstWeekDay = null;
		try {
			firstWeekDay = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
			if (!firstWeekDay.equals(DayOfWeek.MONDAY)) {
				firstWeekDay = getValidDay(firstWeekDay);
			}
		} catch (IllegalArgumentException e) {
			message = "Invalid day of week, must be [monday - sunday]";
		}
		if (!message.isEmpty()) {
			throw new Exception(message);
		}
		return firstWeekDay;
	}

	private static DayOfWeek getValidDay(DayOfWeek firstWeekDay) {
		int firstIndex = firstWeekDay.getValue() - 1;
		DayOfWeek[] newDaysOfweek = new DayOfWeek[7];
		for (int i = 0; i < 7; i++) {
			newDaysOfweek[i] = daysOfweek[(firstIndex + i) % 7];
		}
		daysOfweek = newDaysOfweek;
		return firstWeekDay;
	}

	private static int getYear(String yearStr) throws Exception {
		String message = "";
		int year = 0;
		try {
			year = Integer.parseInt(yearStr);
			if (year < 0) {
				message = "year must be a positive number";
			}
		} catch (NumberFormatException e) {
			message = "year must be a number";
		}
		if (!message.isEmpty()) {
			throw new Exception(message);
		}
		return year;
	}

	private static int getMonth(String monthStr) throws Exception {
		String message = "";
		int month = 0;
		try {
			month = Integer.parseInt(monthStr);
			if (month < 1 || month > 12) {
				message = "month must be in the range [1-12]";
			}
		} catch (NumberFormatException e) {
			message = "month must be a number";
		}
		if (!message.isEmpty()) {
			throw new Exception(message);
		}
		return month;
	}

}
