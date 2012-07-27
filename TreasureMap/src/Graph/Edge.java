/*
 * @author : Chan Nguyen 
 */
package Graph;

class Edge<T> {
	public Vertex<T> dest;
	public double cost;

	public Edge(Vertex<T> d, double c) {
		dest = d;
		cost = c;
	}
}