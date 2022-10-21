package pathfinding;

import static helps.Constants.Tiles.ROAD;
import static main.Game.GRID_HEIGHT;
import static main.Game.GRID_WIDTH;

import java.awt.Point;
import java.util.ArrayList;

import objects.Tile;

public class AStar {

	private static ArrayList<Node> openList, closedList;

	public static ArrayList<Point> pathFind(Tile[][] tileData, Point start, Point goal) {

		openList = new ArrayList<Node>();
		closedList = new ArrayList<Node>();

		Node current = new Node(start);
		openList.add(current);

		while (openList.size() > 0) {

			current = getBestNode();
			openList.remove(current);
			closedList.add(current);

			if (current.getPoint().equals(goal)) {
				break;
			}

			ArrayList<Point> neighbors = getNeighbors(tileData, current.getPoint());
			for (Point point : neighbors) {
				double gCost = current.getgCost() + getDistance(point, current.getPoint());
				double hCost = getDistance(point, goal);
				if (listContains(closedList, point)) {
					continue;

				} else if (listContains(openList, point)) {
					Node node = getNode(openList, point);
					if (gCost < node.getgCost()) {
						int index = getIndex(openList, node);
						openList.get(index).setgCost(gCost);
						openList.get(index).setfCost(gCost + hCost);
						openList.get(index).setParent(current);
					}

				} else
					openList.add(new Node(point, current, gCost, hCost));
			}
		}

		if (!current.getPoint().equals(goal)) {
			return null;
		}

		ArrayList<Point> path = new ArrayList<Point>();

		while (!current.getPoint().equals(start)) {
			path.add(current.getPoint());
			current = current.getParent();
		}

		path.add(start);

		return reverse(path);

	}

	private static Node getBestNode() {

		Node best = openList.get(0);
		for (Node node : openList) {
			if (node.getfCost() < best.getfCost())
				best = node;
		}

		return best;

	}

	private static ArrayList<Point> getNeighbors(Tile[][] tileData, Point parent) {

		ArrayList<Point> neighbors = new ArrayList<Point>();

		// Above
		if (parent.y > 0) {
			Point above = new Point(parent.x, parent.y - 1);
			if (isPointWalkable(tileData, above))
				neighbors.add(above);
		}

		// Right
		if (parent.x < GRID_WIDTH - 1) {
			Point right = new Point(parent.x + 1, parent.y);
			if (isPointWalkable(tileData, right))
				neighbors.add(right);
		}

		// Below
		if (parent.y < GRID_HEIGHT - 1) {
			Point below = new Point(parent.x, parent.y + 1);
			if (isPointWalkable(tileData, below))
				neighbors.add(below);
		}

		// Left
		if (parent.x > 0) {
			Point left = new Point(parent.x - 1, parent.y);
			if (isPointWalkable(tileData, left))
				neighbors.add(left);
		}

		return neighbors;

	}

	private static boolean isPointWalkable(Tile[][] tileData, Point point) {
		return tileData[point.y][point.x].getTileType() == ROAD;
	}

	private static double getDistance(Point from, Point to) {

		double xDist = from.getX() - to.getX();
		double yDist = from.getY() - to.getY();

		double cSquared = (xDist * xDist) + (yDist * yDist);

		return Math.sqrt(cSquared);

	}

	private static boolean listContains(ArrayList<Node> list, Point point) {

		for (Node node : list)
			if (node.getPoint().getX() == point.getX() && node.getPoint().getY() == point.getY())
				return true;
		return false;

	}

	private static Node getNode(ArrayList<Node> list, Point point) {

		for (Node node : list)
			if (node.getPoint().getX() == point.getX() && node.getPoint().getY() == point.getY())
				return node;
		return null;

	}

	private static int getIndex(ArrayList<Node> list, Node node) {

		int index = 0;
		for (Node listNode : list) {
			if (listNode.getPoint().equals(node.getPoint()))
				return index;
			index++;
		}

		return 0;

	}

	private static ArrayList<Point> reverse(ArrayList<Point> path) {

		ArrayList<Point> reverse = new ArrayList<Point>();
		for (int i = path.size() - 1; i >= 0; i--)
			reverse.add(path.get(i));
		return reverse;

	}

}
