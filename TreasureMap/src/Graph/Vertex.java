/*
 * @author : Chan Nguyen 
 */
package Graph;

import java.util.LinkedList;
import java.util.List;

class Vertex<T> {
	public T name; // Vertex name
	public List<Edge<T>> adj; // Adjacency list of vertices
	public double dist; // The distance cost
	public Vertex<T> prev; // Previous vertex on shortest path
	public int occupied; // Mark the vertex as visited

	public Vertex(T nm) {
		name = nm;
		adj = new LinkedList<Edge<T>>();
		reset();
	}

	public void reset() {
		dist = Graph.INFINITY;
		prev = null;
		occupied = 0;
	}
}