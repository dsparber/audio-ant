package io.logger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

public class EventLogger implements Observer {

	@Override
	public void update(Observable o, Object arg) {

		long currentMillis = System.currentTimeMillis();
		Date date = new Date(currentMillis);

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);

		double percent = (double) arg;

		percent *= 100;

		System.out.printf("%d:%d:%d.%d:\t%.2f%%\n", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND), percent);

	}

}
