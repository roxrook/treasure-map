/*
 * @author : Chan Nguyen 
 */
package Media;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import java.awt.geom.*;

public class Picture implements PictureInterface {
	private String fileName;
	private String title;
	private BufferedImage bufferedImage;
	private PictureFrame pictureFrame;
	private String extension;

	public Picture() {
		this(200, 100);
	}

	public Picture(String fileName) {
		// load the picture into the buffered image
		load(fileName);
	}

	public Picture(int width, int height) {
		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		title = "None";
		fileName = "None";
		extension = "jpg";
		setAllPixelsToAColor(Color.white);
	}

	public Picture(int width, int height, Color theColor) {
		this(width, height);
		setAllPixelsToAColor(theColor);
	}

	public Picture(Picture copyPicture) {
		if (copyPicture.fileName != null) {
			this.fileName = new String(copyPicture.fileName);
			this.extension = copyPicture.extension;
		}

		if (copyPicture.title != null) {
			this.title = new String(copyPicture.title);
		}

		if (copyPicture.bufferedImage != null) {
			this.bufferedImage = new BufferedImage(copyPicture.getWidth(), copyPicture.getHeight(), BufferedImage.TYPE_INT_RGB);
			this.copyPicture(copyPicture);
		}
	}

	public Picture(BufferedImage image) {
		this.bufferedImage = image;
		title = "None";
		fileName = "None";
		extension = "jpg";
	}

	public String getExtension() {
		return extension;
	}

	public void copyPicture(Picture sourcePicture) {
		Pixel sourcePixel = null;
		Pixel targetPixel = null;

		// loop through the columns
		for (int sourceX = 0, targetX = 0; sourceX < sourcePicture.getWidth() && targetX < this.getWidth(); sourceX++, targetX++) {
			// loop through the rows
			for (int sourceY = 0, targetY = 0; sourceY < sourcePicture.getHeight() && targetY < this.getHeight(); sourceY++, targetY++) {
				sourcePixel = sourcePicture.getPixel(sourceX, sourceY);
				targetPixel = this.getPixel(targetX, targetY);
				targetPixel.setColor(sourcePixel.getColor());
			}
		}
	}

	public void setAllPixelsToAColor(Color color) {
		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getHeight(); y++) {
				getPixel(x, y).setColor(color);
			}
		}
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public Graphics getGraphics() {
		return bufferedImage.getGraphics();
	}

	public Graphics2D createGraphics() {
		return bufferedImage.createGraphics();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String name) {
		fileName = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if (pictureFrame != null)
			pictureFrame.setTitle(title);
	}

	public int getWidth() {
		return bufferedImage.getWidth();
	}

	public int getHeight() {
		return bufferedImage.getHeight();
	}

	public PictureFrame getPictureFrame() {
		return pictureFrame;
	}

	public void setPictureFrame(PictureFrame pictureFrame) {
		// set this picture objects' picture frame to the passed one
		this.pictureFrame = pictureFrame;
	}

	public Image getImage() {
		return bufferedImage;
	}

	public int getBasicPixel(int x, int y) {
		return bufferedImage.getRGB(x, y);
	}

	public void setBasicPixel(int x, int y, int rgb) {
		bufferedImage.setRGB(x, y, rgb);
	}

	public Pixel getPixel(int x, int y) {
		// create the pixel object for this picture and the given x and y
		// location
		Pixel pixel = new Pixel(this, x, y);
		return pixel;
	}

	public Pixel[] getPixels() {
		int width = getWidth();
		int height = getHeight();
		Pixel[] pixelArray = new Pixel[width * height];

		// loop through height rows from top to bottom
		for (int row = 0; row < height; row++)
			for (int col = 0; col < width; col++)
				pixelArray[row * width + col] = new Pixel(this, col, row);

		return pixelArray;
	}

	public void load(Image image) {
		// get a graphics context to use to draw on the buffered image
		Graphics2D graphics2d = bufferedImage.createGraphics();

		// draw the image on the buffered image starting at 0,0
		graphics2d.drawImage(image, 0, 0, null);

		// show the new image
		show();
	}

	public void show() {
		// if there is a current picture frame then use it
		if (pictureFrame != null)
			pictureFrame.updateImageAndShowIt();

		// else create a new picture frame with this picture
		else
			pictureFrame = new PictureFrame(this);
	}

	public void hide() {
		if (pictureFrame != null)
			pictureFrame.setVisible(false);
	}

	public void setVisible(boolean flag) {
		if (flag)
			this.show();
		else
			this.hide();
	}

	public void explore() {
		// create a copy of the current picture and explore it
		// new PictureExplorer( new SimplePicture( this ) );
	}

	public void repaint() {
		// if there is a picture frame tell it to repaint
		if (pictureFrame != null)
			pictureFrame.repaint();

		// else create a new picture frame
		else
			pictureFrame = new PictureFrame(this);
	}

	public void loadOrFail(String fileName) throws IOException {
		// set the current picture's file name
		this.fileName = fileName;

		// set the extension
		int posDot = fileName.indexOf('.');
		if (posDot >= 0)
			this.extension = fileName.substring(posDot + 1);

		// if the current title is null use the file name
		if (title == null)
			title = fileName;

		File file = new File(this.fileName);

		bufferedImage = ImageIO.read(file);
	}

	public boolean load(String fileName) {
		try {
			this.loadOrFail(fileName);
			return true;

		}
		catch (Exception ex) {
			System.out.println("There was an error trying to open " + fileName);
			bufferedImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
			addMessage("Couldn't load " + fileName, 5, 100);
			return false;
		}

	}

	public boolean loadImage(String fileName) {
		return load(fileName);
	}

	public void addMessage(String message, int xPos, int yPos) {
		// get a graphics context to use to draw on the buffered image
		Graphics2D graphics2d = bufferedImage.createGraphics();

		// set the color to white
		graphics2d.setPaint(Color.white);

		// set the font to Helvetica bold style and size 16
		graphics2d.setFont(new Font("Helvetica", Font.BOLD, 16));

		// draw the message
		graphics2d.drawString(message, xPos, yPos);
	}

	public void drawString(String text, int xPos, int yPos) {
		addMessage(text, xPos, yPos);
	}

	public Picture scale(double xFactor, double yFactor) {
		// set up the scale transform
		AffineTransform scaleTransform = new AffineTransform();
		scaleTransform.scale(xFactor, yFactor);

		// create a new picture object that is the right size
		Picture result = new Picture((int) (getWidth() * xFactor), (int) (getHeight() * yFactor));

		// get the graphics 2d object to draw on the result
		Graphics graphics = result.getGraphics();
		Graphics2D g2 = (Graphics2D) graphics;

		// draw the current image onto the result image scaled
		g2.drawImage(this.getImage(), scaleTransform, null);

		return result;
	}

	public Picture getPictureWithWidth(int width) {
		// set up the scale tranform
		double xFactor = (double) width / this.getWidth();
		Picture result = scale(xFactor, xFactor);
		return result;
	}

	public Picture getPictureWithHeight(int height) {
		// set up the scale tranform
		double yFactor = (double) height / this.getHeight();
		Picture result = scale(yFactor, yFactor);
		return result;
	}

	public boolean loadPictureAndShowIt(String fileName) {
		boolean result = true; // the default is that it worked

		// try to load the picture into the buffered image from the file name
		result = load(fileName);

		// show the picture in a picture frame
		show();

		return result;
	}

	public void writeOrFail(String fileName) throws IOException {
		String extension = this.extension; // the default is current

		// create the file object
		File file = new File(fileName);
		File fileLoc = file.getParentFile();

		// canWrite is true only when the file exists already! (alexr)
		if (!fileLoc.canWrite()) {
			// System.err.println("can't write the file but trying anyway? ...");
			throw new IOException(fileName + " could not be opened. Check to see if you can write to the directory.");
		}

		// get the extension
		int posDot = fileName.indexOf('.');
		if (posDot >= 0)
			extension = fileName.substring(posDot + 1);

		// write the contents of the buffered image to the file as jpeg
		ImageIO.write(bufferedImage, extension, file);

	}

	public boolean write(String fileName) {
		try {
			this.writeOrFail(fileName);
			return true;
		}
		catch (Exception ex) {
			System.out.println("There was an error trying to write " + fileName);
			return false;
		}

	}

	public Rectangle2D getTransformEnclosingRect(AffineTransform trans) {
		int width = getWidth();
		int height = getHeight();
		double maxX = width - 1;
		double maxY = height - 1;
		double minX, minY;
		Point2D.Double p1 = new Point2D.Double(0, 0);
		Point2D.Double p2 = new Point2D.Double(maxX, 0);
		Point2D.Double p3 = new Point2D.Double(maxX, maxY);
		Point2D.Double p4 = new Point2D.Double(0, maxY);
		Point2D.Double result = new Point2D.Double(0, 0);
		Rectangle2D.Double rect = null;

		// get the new points and min x and y and max x and y
		trans.deltaTransform(p1, result);
		minX = result.getX();
		maxX = result.getX();
		minY = result.getY();
		maxY = result.getY();
		trans.deltaTransform(p2, result);
		minX = Math.min(minX, result.getX());
		maxX = Math.max(maxX, result.getX());
		minY = Math.min(minY, result.getY());
		maxY = Math.max(maxY, result.getY());
		trans.deltaTransform(p3, result);
		minX = Math.min(minX, result.getX());
		maxX = Math.max(maxX, result.getX());
		minY = Math.min(minY, result.getY());
		maxY = Math.max(maxY, result.getY());
		trans.deltaTransform(p4, result);
		minX = Math.min(minX, result.getX());
		maxX = Math.max(maxX, result.getX());
		minY = Math.min(minY, result.getY());
		maxY = Math.max(maxY, result.getY());

		// create the bounding rectangle to return
		rect = new Rectangle2D.Double(minX, minY, maxX - minX + 1, maxY - minY + 1);
		return rect;
	}

	public Rectangle2D getTranslationEnclosingRect(AffineTransform trans) {
		return getTransformEnclosingRect(trans);
	}

	public String toString() {
		String output = "Simple Picture, filename " + fileName + " height " + getHeight() + " width " + getWidth();
		return output;
	}

} // end of SimplePicture class
