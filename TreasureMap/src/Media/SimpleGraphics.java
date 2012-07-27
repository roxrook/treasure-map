/*
 * @author : Chan Nguyen 
 */
package Media;

import java.awt.Color;
import java.awt.Graphics;
import GraphUtility.*;
import Game.*;

public class SimpleGraphics {
	public static void drawOneNodeOnPictureAt(Picture pic, Point p) {
		Graphics g = pic.getGraphics();
		g.setColor(Color.black);
		g.fillOval(p.getX() - 2, p.getY() - 2, 5, 5);
	}

	public static void drawGoal(Picture pic, Point p, Color c) {
		Graphics g = pic.getGraphics();
		g.setColor(c);
		g.draw3DRect(p.getX() - 5, p.getY() - 5, 10, 10, false);
	}

	public static void drawNodeHasTreasureMap(Picture pic, Point p) {
		Graphics g = pic.getGraphics();
		g.setColor(Color.red);
		g.fillOval(p.getX() - 2, p.getY() - 2, 5, 5);
	}

	public static void drawTreasureOnPictureAt(Picture pic, Point p) {
		Graphics g = pic.getGraphics();
		g.setColor(Color.yellow);
		g.drawRect(p.getX() - 2, p.getY() - 2, 5, 5);
	}

	public static void eraseTreasureOnPictureAt(Picture pic, Point p) {
		Graphics g = pic.getGraphics();
		g.setColor(Color.green);
		g.drawRect(p.getX() - 2, p.getY() - 2, 5, 5);
	}

	public static void hightlightPoint(Picture pic, Point p) {
		Graphics g = pic.getGraphics();
		g.setColor(Color.yellow);
		g.fillOval(p.getX() - 5, p.getY() - 5, 10, 10);
	}

	public static void drawOneLineBetweenTwoNodes(Picture pic, Point p1, Point p2) {
		Graphics g = pic.getGraphics();
		g.setColor(Color.black);
		g.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	public static void drawPath(Picture pic, Point p1, Point p2) {
		Graphics g = pic.getGraphics();
		g.setColor(Color.red);
		g.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	public static void drawText(Picture pic, String text, int x, int y) {
		Graphics g = pic.getGraphics();
		g.setColor(Color.red);
		g.drawString(text, x, y);
	}
}
