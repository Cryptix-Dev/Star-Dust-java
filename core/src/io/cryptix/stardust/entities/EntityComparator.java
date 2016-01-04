package io.cryptix.stardust.entities;

import java.util.Comparator;

public class EntityComparator implements Comparator<Drawable> {

	@Override
	public int compare(Drawable o1, Drawable o2) {
		if (o2.drawPoint() != o1.drawPoint())
			return (o2.drawPoint() - o1.drawPoint()) > 0 ? 1 : -1;
		else
			return 0;
	}

}
