/*
 * @author : Chan Nguyen 
 */
package GraphUtility;

public class Formula {
	public static int calculateHeightDifference(Point p1, Point p2) {
		return Math.abs(p1.getHeight() - p2.getHeight());
	}

	public static boolean isTwoPointConnected(Point p1, Point p2) {
		return (calculateHeightDifference(p1, p2) <= 20);
	}

	public static boolean isInRange(Point p, int lowerRange, int upperRange) {
		if (p.getX() < lowerRange || p.getX() > upperRange || p.getY() < lowerRange || p.getY() > upperRange)
			return false;
		else
			return true;
	}
}
