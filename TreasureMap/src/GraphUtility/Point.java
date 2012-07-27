/*
 * @author : Chan Nguyen 
 */
package GraphUtility;

public class Point implements Comparable<Point> {
	protected int x;
	protected int y;
	protected int height;

	public Point(int x, int y, int height) {
		this.x = x;
		this.y = y;
		this.height = height;
	}

	public Point(Point rhs) {
		this.x = rhs.x;
		this.y = rhs.y;
		this.height = rhs.height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHeight() {
		return height;
	}

	public String toString() {
		return x + "," + y + "," + height;
	}

	public int compareTo(Point rhs) {
		if (x == rhs.getX()) {
			if (y == rhs.getY())
				return 0;
			else if (y > rhs.getY())
				return 1;
			else
				return -1;
		}
		else if (x > rhs.getX())
			return 1;
		else
			return -1;
	}

	public static Point parse(String str) {
		String[] values = str.split(",");
		return new Point(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
	}
}
