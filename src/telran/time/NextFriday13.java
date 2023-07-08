package telran.time;

import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.UnsupportedTemporalTypeException;

public class NextFriday13 implements TemporalAdjuster {

	@Override
	public Temporal adjustInto(Temporal temporal) {
		if (!temporal.isSupported(ChronoUnit.MONTHS)) {
			throw new UnsupportedTemporalTypeException("Temporal must support months");
		}
		temporal = adjust(temporal);
		while (temporal.get(ChronoField.DAY_OF_WEEK) != 5) {
			temporal = temporal.plus(1, ChronoUnit.MONTHS);
		}
		return temporal;
	}

	private Temporal adjust(Temporal temporal) {
		if (temporal.get(ChronoField.DAY_OF_MONTH) >= 13) {
			temporal = temporal.plus(1, ChronoUnit.MONTHS);
		}
		return temporal.with(ChronoField.DAY_OF_MONTH, 13);
	}
}
