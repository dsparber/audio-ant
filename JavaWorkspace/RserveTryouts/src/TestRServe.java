import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

/**
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public class TestRServe {
	public static void main(String[] args) throws RserveException, REXPMismatchException {

		RConnection connection = new RConnection("localhost");
		REXP x = connection.eval("R.version.string");
		System.out.println(x.asString());
	}
}
