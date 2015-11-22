package tools;

import java.util.ArrayList;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class MaxSizeArrayList<E> extends ArrayList<E> {

	private static final long serialVersionUID = 8932454309329992297L;

	private int maxLength;

	public MaxSizeArrayList(int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public boolean add(E e) {

		if (size() >= maxLength) {
			remove(0);
		}

		return super.add(e);
	}

}
