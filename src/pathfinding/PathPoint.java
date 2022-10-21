package pathfinding;

import java.awt.Point;
import java.io.Serializable;

public class PathPoint implements Serializable {

	private static final long serialVersionUID = 1392938359266330520L;

	private Point point;

	private int id, pointType;

	public PathPoint(int id, int pointType) {

		this.point = null;
		this.id = id;
		this.pointType = pointType;

	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public int getId() {
		return id;
	}

	public int getPointType() {
		return pointType;
	}

}
