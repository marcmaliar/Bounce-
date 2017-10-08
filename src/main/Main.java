/**
 * 
 */
package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author marcmaliar
 *
 */
public class Main {

	/**
	 * @param args
	 */
	private final static int CANVASWIDTH = 500;
	private final static int CANVASLENGTH = 700;
	private final static int MENULENGTH = 80;
	private final static int BRICK_X = 8;
	private final static int BRICK_Y = 10;
	static JLabel title, score, highScore;
	static GameWorld gw;
	static JFrame frame;
	
	
	public static void main(String[] args) {
		frame = new JFrame();
		//frame.setPreferredSize(new Dimension(CANVASWIDTH,CANVASLENGTH+MENULENGTH));
		JPanel menu = initializeMenu(gw);
		GameWorld gw = new GameWorld(CANVASWIDTH, CANVASLENGTH, BRICK_X, BRICK_Y);
		gw.setPreferredSize(new Dimension(CANVASWIDTH, CANVASLENGTH));
		Main.gw = gw;
		frame.setLayout(new BorderLayout());
		frame.add(gw, BorderLayout.SOUTH);
		frame.add(menu, BorderLayout.NORTH);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		gw.startThread();
	}
	
	public static void updateLabels(int currentScore) {
		score.setText("Current Score: " + currentScore + " || High Score: " + getHighScore());
	}
	
	public static void setHighScore(int highScore) {
		if (highScore > getHighScore()) {
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter("HighScore"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				bw.write(highScore+"");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static JPanel initializeMenu(GameWorld gw) {
		JPanel menu = new JPanel();
		menu.setBackground(Color.GRAY);
		menu.setLayout(new BorderLayout());
		menu.setPreferredSize(new Dimension(CANVASWIDTH, MENULENGTH));
		
		title = new JLabel("Bounce!", SwingConstants.CENTER);
		//title.setPreferredSize(new Dimension(CANVASWIDTH, MENULENGTH/3));
		title.setVisible(true);
		menu.add(title, BorderLayout.NORTH);
		
		score = new JLabel("Current Score: 0 || High Score: " + getHighScore(), SwingConstants.CENTER);
		//score.setPreferredSize(new Dimension(CANVASWIDTH, MENULENGTH/3));
		score.setVisible(true);
		menu.add(score, BorderLayout.CENTER);
		
		JButton newGame = new JButton("New game");
		//newGame.setPreferredSize(new Dimension(CANVASWIDTH, MENULENGTH/3));
		newGame.setFocusable(true);
		newGame.setVisible(true);
		newGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Main.newGame();
			}
			
		});
		menu.add(newGame, BorderLayout.SOUTH);
		return menu;
	}
	public static void newGame() {
		frame.remove(gw);
		gw = new GameWorld(CANVASWIDTH, CANVASLENGTH, BRICK_X, BRICK_Y);
		gw.setPreferredSize(new Dimension(CANVASWIDTH, CANVASLENGTH));
		frame.add(gw, BorderLayout.SOUTH);
		gw.startThread();
		Ball.maxBalls = 1;
		frame.pack();
	}
	
	public static int getHighScore() {
		BufferedReader br = null;
		int result = 0;
		try {
			br = new BufferedReader(new FileReader("HighScore"));
			result = Integer.parseInt(br.readLine());
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {}
		}
		return result;
	}

}
