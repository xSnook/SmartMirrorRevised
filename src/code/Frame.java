package code;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;

	public Frame() {
		// getContentPane().setBackground(Color.BLACK);
		// setTitle("SmartMirror 2.0");
		// setSize(Toolkit.getDefaultToolkit().getScreenSize());
		// setExtendedState(JFrame.MAXIMIZED_BOTH);
		// setResizable(false);
		// setUndecorated(true);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//
		// setLocationRelativeTo(null);
		// initComponents();
		// setVisible(true);
		//
		new WeatherComponent();
	}

	private void initComponents() {
		
		setBlankCursor();
	}
	
	private void setBlankCursor() {
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		getContentPane().setCursor(blankCursor);
	}

	public static void main(String[] args) {
		new Frame();
	}

}
