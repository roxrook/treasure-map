/*
 * @author : Chan Nguyen 
 */
package Graph;

class Path<T> implements Comparable<Path<T>> {
	public Vertex<T> dest;
	public double cost;

	public Path(Vertex<T> d, double c) {
		dest = d;
		cost = c;
	}

	public int compareTo(Path<T> rhs) {
		double otherCost = rhs.cost;
		return cost < otherCost ? -1 : cost > otherCost ? 1 : 0;
	}
}