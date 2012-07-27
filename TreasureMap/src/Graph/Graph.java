/*
 * @author : Chan Nguyen 
 */
package Graph;

import java.util.*;
import Exception.*;

public class Graph<T> {
	public static final double INFINITY = Double.MAX_VALUE;
	private Map<T, Vertex<T>> vertexMap = new HashMap<T, Vertex<T>>();

	public void addEdge(T sourceName, T destName, double cost) {
		Vertex<T> v = getVertex(sourceName);
		Vertex<T> w = getVertex(destName);
		v.adj.add(new Edge<T>(w, cost));
	}

	public void printPath(T destName) {
		Vertex<T> w = vertexMap.get(destName);
		if (w == null) {
			throw new NoSuchElementException("Destination vertex not found");
		}
		else if (w.dist == INFINITY) {
			System.out.println(destName + " is unreachable");
		}
		else {
			System.out.print("(Cost is: " + w.dist + ") ");
			printPath(w);
			System.out.println();
		}
	}

	public double getCost(T destName) {
		Vertex<T> w = vertexMap.get(destName);
		return w.dist;
	}

	public String printPathString(T destName) {
		Vertex<T> w = vertexMap.get(destName);
		if (w == null) {
			throw new NoSuchElementException("Destination vertex not found");
		}
		else if (w.dist == INFINITY) {
			return destName + " is unreachable";
		}
		else {
			return printPathString(w);
		}
	}

	private Vertex<T> getVertex(T vertexName) {
		Vertex<T> v = vertexMap.get(vertexName);
		if (v == null) {
			v = new Vertex<T>(vertexName);
			vertexMap.put(vertexName, v);
		}
		return v;
	}

	private void printPath(Vertex<T> dest) {
		if (dest.prev != null) {
			printPath(dest.prev);
			System.out.print(" to ");
		}
		System.out.print(dest.name);
	}

	private String printPathString(Vertex<T> dest) {
		StringBuffer sb = new StringBuffer();
		while (dest.prev != null) {
			sb.append(dest.name.toString());
			sb.append("-");
			dest = dest.prev;
		}

		sb.append(dest.name.toString());
		return sb.toString();
	}

	private void clearAll() {
		for (Vertex<T> v : vertexMap.values())
			v.reset();
	}

	public void dijkstra(T startName) {
		PriorityQueue<Path<T>> pq = new PriorityQueue<Path<T>>();

		Vertex<T> start = vertexMap.get(startName);
		if (start == null)
			throw new NoSuchElementException("Start vertex not found");

		clearAll();
		pq.add(new Path<T>(start, 0));
		start.dist = 0;

		int nodesSeen = 0;
		while (!pq.isEmpty() && nodesSeen < vertexMap.size()) {
			Path<T> vrec = pq.remove();
			Vertex<T> v = vrec.dest;
			if (v.occupied != 0) // already processed v
				continue;

			v.occupied = 1;
			nodesSeen++;

			for (int i = 0, iSize = v.adj.size(); i < iSize; ++i) {
				Edge<T> e = (Edge<T>) v.adj.get(i);
				Vertex<T> w = e.dest;
				double cvw = e.cost;

				if (cvw < 0)
					throw new GraphException("Graph has negative edges");

				if (w.dist > v.dist + cvw) {
					w.dist = v.dist + cvw;
					w.prev = v;
					pq.add(new Path<T>(w, w.dist));
				}
			}
		}
	}
}
