import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

public class EventLogger implements Observer {

	private boolean soundDetected = false;
	private int count = 0;

	@Override
	public void update(Observable o, Object arg) {

		long currentMillis = System.currentTimeMillis();
		Date date = new Date(currentMillis);

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);

		boolean soundDetected = (boolean) arg;

		if (this.soundDetected && !soundDetected) {

			if (count <= 2) {
				System.out.print(" \t\t(unlikely)");
			} else if (count <= 4) {
				System.out.print("\t\t(maybe)");
			} else if (count <= 6) {
				System.out.print("\t\t(likley)");
			} else {
				System.out.print("  \t(almost sure)");
			}

		} else if (!this.soundDetected && soundDetected) {

			System.out.printf("\n%d:%d:%d.%d:\tSound dedected.", calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
			count = 1;

		} else if (soundDetected) {
			System.out.print('.');
			count++;
		}

		this.soundDetected = soundDetected;
	}

}
