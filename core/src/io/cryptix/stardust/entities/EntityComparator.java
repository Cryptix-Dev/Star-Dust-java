package io.cryptix.stardust.entities;

import java.util.Comparator;

public class EntityComparator implements Comparator<Entity> {

	@Override
	public int compare(Entity o1, Entity o2) {
		if (o2.drawPoint() != o1.drawPoint())
			return (o2.drawPoint() - o1.drawPoint()) > 0 ? 1 : -1;
		else
			return 0;
	}

}
