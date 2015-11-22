import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

public class EventLogger implements Observer {

	private long lastUpdate = System.currentTimeMillis();

	@Override
	public void update(Observable o, Object arg) {

		long currentMillis = System.currentTimeMillis();
		Date date = new Date(currentMillis);

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);

		if (currentMillis - lastUpdate > 750) {
			System.out.printf("\n%d:%d:%d.%d:\tSound dedected.", calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
		} else {
			System.out.print('.');
		}

		lastUpdate = currentMillis;
	}

}
