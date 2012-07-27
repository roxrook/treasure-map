/*
 * @author : Chan Nguyen 
 */
package Game;

import java.awt.Color;
import java.util.*;

import GraphUtility.*;
import Graph.*;
import Media.*;
import Character.*;

public class TreasureMap {
	private Converter pictureUtility;
	private Graph<Integer> graph;
	private Picture picture;
	public World mapHandle;

	final private int[] rowMove = { -20, -20, -20, 0, 0, +20, +20, +20 };
	final private int[] columnMove = { -20, 0, +20, -20, +20, -20, 0, +20 };

	public TreasureMap() {
		// get info of all nodes
		pictureUtility = new Converter(PictureFilePath.heightMapFilePath);
		// create a graph
		graph = new Graph<Integer>();
		// create a picture
		picture = new Picture(PictureFilePath.colorMapFilePath);
		// draw basic map
		drawMapAndGetGraphInfo();
		// add to World handle
		mapHandle = new World(picture.getWidth(), picture.getHeight());
		mapHandle.setPicture(picture);
	}

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public World getMapHandle() {
		return mapHandle;
	}

	public Graph<Integer> getGraph() {
		return graph;
	}

	public Converter getPictureToPointOnGraphUtility() {
		return pictureUtility;
	}

	public void drawMapAndGetGraphInfo() {
		Vector<Point> vec = pictureUtility.getVectorOfValidNodes();
		TreeMap<Point, Integer> tm = pictureUtility.getTreeMapPointWithHeight();
		TreeMap<Point, Integer> piTm = pictureUtility.getMapPointToInt();

		for (int i = 0, totalNodes = vec.size(); i < totalNodes; ++i) {
			Point p = vec.elementAt(i);
			for (int m = 0; m < 8; ++m) {
				Integer height = tm.get(new Point(p.getX() + rowMove[m], p.getY() + columnMove[m], 0));
				if (height != null) {
					Point temp = new Point(p.getX() + rowMove[m], p.getY() + columnMove[m], height.intValue());
					if (Formula.isInRange(temp, 20, 500)) {
						if (Formula.isTwoPointConnected(p, temp)) {
							graph.addEdge(new Integer(i), piTm.get(temp), Formula.calculateHeightDifference(p, temp));
							SimpleGraphics.drawOneNodeOnPictureAt(picture, temp);
							SimpleGraphics.drawOneNodeOnPictureAt(picture, p);
							SimpleGraphics.drawOneLineBetweenTwoNodes(picture, p, temp);
						}
					}
				}
			}
		}
	}
}
