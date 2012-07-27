/*
 * @author : Chan Nguyen 
 */
package GraphUtility;

import java.util.*;
import Media.*;

public class Converter {
	final private int lowerLimitHeight = 50;
	final private int upperLimitHeight = 140;
	final private int startingPoint = 20;
	final private int endingPoint = 500;
	final private int steppingPoint = 20;

	private Vector<Point> validNodes;
	private TreeMap<Point, Integer> pointWithHeight;
	private TreeMap<Point, Integer> mapPointToInt;
	private TreeMap<Integer, Point> mapIntToPoint;

	public TreeMap<Point, Integer> getMapPointToInt() {
		return mapPointToInt;
	}

	public TreeMap<Integer, Point> getMapIntToPoint() {
		return mapIntToPoint;
	}

	public Converter(String pictureFilePath) {
		validNodes = new Vector<Point>();
		pointWithHeight = new TreeMap<Point, Integer>();
		Picture pic = readPictureFromFileReturnPictureMap(pictureFilePath);
		readAllValidNodesToVectorOfValidNodesAndTreeMapOfPointWithHeight(pic);
		mapPointToInt = getTreeMapPointToInteger();
		mapIntToPoint = getTreeMapIntegerToPoint();
	}

	private TreeMap<Point, Integer> getTreeMapPointToInteger() {
		TreeMap<Point, Integer> temp = new TreeMap<Point, Integer>();
		for (int i = 0, iSize = validNodes.size(); i < iSize; ++i)
			temp.put(validNodes.elementAt(i), i);
		return temp;
	}

	private TreeMap<Integer, Point> getTreeMapIntegerToPoint() {
		TreeMap<Integer, Point> temp = new TreeMap<Integer, Point>();
		for (int i = 0; i < validNodes.size(); ++i)
			temp.put(i, validNodes.elementAt(i));
		return temp;
	}

	public Vector<Point> getVectorOfValidNodes() {
		return validNodes;
	}

	public TreeMap<Point, Integer> getTreeMapPointWithHeight() {
		return pointWithHeight;
	}

	private Picture readPictureFromFileReturnPictureMap(String pictureFilePath) {
		Picture heightMap = new Picture(pictureFilePath);
		return heightMap;
	}

	private boolean isPixelValidPoint(Pixel px) {
		if (px.getRed() > lowerLimitHeight && px.getRed() < upperLimitHeight)
			return true;
		else
			return false;
	}

	public boolean isNotConnectedPoint(Pixel p) {
		if ((p.getX() == 260 && p.getY() == 160) || (p.getX() == 280 && p.getY() == 180) || (p.getX() == 300 && p.getY() == 200)) {
			return true;
		}
		else {
			return false;
		}
	}

	private void readAllValidNodesToVectorOfValidNodesAndTreeMapOfPointWithHeight(Picture map) {
		for (int x = startingPoint; x <= endingPoint; x += steppingPoint) {
			for (int y = startingPoint; y <= endingPoint; y += steppingPoint) {
				Pixel px = map.getPixel(x, y);
				if (isPixelValidPoint(px) && !isNotConnectedPoint(px)) {
					validNodes.add(new Point(x, y, px.getRed()));
					pointWithHeight.put(new Point(x, y, px.getRed()), new Integer(px.getRed()));
				}
			}
		}
	}

	public Integer getIntegerFromPoint(Point p) {
		return mapPointToInt.get(p);
	}

	public Point getPointFromInteger(Integer i) {
		return mapIntToPoint.get(i);
	}
}
