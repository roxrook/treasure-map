/*
 * @author : Chan Nguyen 
 */
package Media;

import javax.swing.*;

public class PictureFrame {
	JFrame frame = new JFrame();
	ImageIcon imageIcon = new ImageIcon();
	private JLabel label = new JLabel(imageIcon);
	private PictureInterface picture;

	public PictureFrame() {
		initFrame();
	}

	public PictureFrame(PictureInterface picture) {
		this.picture = picture;
		initFrame();
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
		imageIcon.setImage(picture.getImage());
		frame.pack();
		frame.repaint();
	}

	public void updateImage() {
		if (picture != null) {
			imageIcon.setImage(picture.getImage());
			frame.setTitle(picture.getTitle());
		}
	}

	public void updateImageAndShowIt() {
		updateImage();
		frame.setVisible(true);
	}

	public void displayImage() {
		frame.setVisible(true);
	}

	public void hide() {
		frame.setVisible(false);
	}

	public void setVisible(boolean flag) {
		frame.setVisible(flag);
	}

	public void close() {
		frame.setVisible(false);
		frame.dispose();
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	public void repaint() {
		frame.setVisible(true);
		updateImage();
		frame.repaint();
	}

	private void initFrame() {
		updateImage();
		frame.getContentPane().add(label);
		frame.pack();
		frame.setVisible(true);
	}
}