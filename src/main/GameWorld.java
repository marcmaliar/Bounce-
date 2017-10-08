package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
public class GameWorld extends JLayeredPane{

	/**
	 * 
	 */
	private static final long UPDATE_RATE = 300;
	private static final long serialVersionUID = 1990014910245385114L;
	private static final long BALL_RELEASE_INTERVAL = 100;
	private static final int BOTTOM_SPACE_PIXEL = 200;
	private static final double MIN_ANGLE = Math.PI/9;
	long currentTime;
	int canvasWidth;
	int canvasLength;
	int canvasLengthBricks;
	int brickX, brickY;
	int currentLevel = 1;
	int ballsLeft;
	int mouseX;
	int mouseY;
	boolean choosing = true;
	boolean displayLine = false;
	boolean gameOver = false;

	List<Brick> bricks = new CopyOnWriteArrayList<Brick>();
	List<Star> stars = new CopyOnWriteArrayList<Star>();
	List<Ball> balls = new CopyOnWriteArrayList<Ball>();

	GameWorld gw = this;

	public GameWorld(int canvasWidth, int canvasLength, int brickX, int brickY) {
		this.setLayout(null);
		this.canvasWidth = canvasWidth;
		this.canvasLength = canvasLength;
		this.brickX = brickX;
		this.brickY = brickY;
		ballsLeft = 1;
		mouseX = canvasWidth/2;
		mouseY = canvasLength;
		choosing = true;
		bricks.addAll(Brick.newLayer(gw, brickX, currentLevel, stars));
		canvasLengthBricks = canvasLength - BOTTOM_SPACE_PIXEL;

		this.setFocusable(true);
		MouseListener ml = new MouseListenerGame(this);
		this.addMouseListener(ml);
		MouseMotionListener dml = new DragMouseListenerGame(this);
		this.addMouseMotionListener(dml);
	}

	public void startThread() {
		Thread thread = new Thread() {
			public void run() {
				currentTime = System.currentTimeMillis();
				currentLevel = 1;
				Main.updateLabels(currentLevel);
				while (true) {
					//System.out.println(balls.size());
					
					if (ballsLeft == 0 && balls.size() == 0 && !choosing) {
						currentLevel++;
						Main.updateLabels(currentLevel);
						if (nextLayer()) break;
						bricks.addAll(Brick.newLayer(gw, brickX, currentLevel, stars));
						choosing = true;
					}
					int brickWidth = canvasWidth / brickX;
					int brickLength = canvasLengthBricks / brickY;
					for (Ball b: balls) {
						if (b.moveWithDetection(bricks, stars, balls, brickWidth, brickLength, canvasWidth, canvasLength)) {
							Ball.maxBalls++;
						}
					}
					if (ballsLeft != 0 && !choosing && System.currentTimeMillis()-currentTime>BALL_RELEASE_INTERVAL) {
						currentTime = System.currentTimeMillis();
						ballsLeft--;
						int newX = canvasWidth/2 - Ball.WIDTH/2;
						int newY = canvasLength - Ball.LENGTH;
						int dx = mouseX - newX;
						int dy = mouseY - newY;
						double hypot = Math.sqrt(dx*dx + dy*dy);
						double speedX = dx/hypot;
						double speedY = dy/hypot;
						if (Math.abs(Math.atan(Math.abs(speedY)/Math.abs(speedX))) < MIN_ANGLE) {
							
							speedX = speedX < 0 ? -Math.cos(MIN_ANGLE): Math.cos(MIN_ANGLE);
							speedY = -Math.sin(MIN_ANGLE);
						}
						
						balls.add(new Ball(newX, newY, speedX,speedY));
					}
					// Refresh the display
					repaint();
					// Makes the program slow down so things can be drawn properly
					try {
						Thread.sleep(1000 / UPDATE_RATE);
					} catch (InterruptedException ex) {}
				}
				gameOver = true;
				Main.setHighScore(currentLevel);
				Main.updateLabels(currentLevel);
				repaint();
			}
		};
		thread.start();  // start the thread for graphics
	}

	public boolean nextLayer() {
		for (Brick b: bricks) {
			b.y = b.y + 1;
			if (b.y >= brickY) return true;
		}
		for (Star s: stars) {
			s.y = s.y + 1;
			if (s.y > brickY) stars.remove(s);
		}
		return false;
	}


	@Override
	public void paintComponent(Graphics g) {


		g.drawLine(0, canvasLengthBricks, canvasWidth, canvasLengthBricks);
		double brickWidth = canvasWidth / brickX;
		double brickLength = canvasLengthBricks / brickY;
		drawBricks(g, brickWidth, brickLength);
		drawStars(g, brickWidth, brickLength);
		drawLabels(g, brickWidth, brickLength);
		if (displayLine) drawDirectionalString(g);
		drawNumBalls(g);
		drawBalls(g);
		if (gameOver) {
			try {
				g.drawImage(ImageIO.read(new File("GameOver.jpg")), 100, canvasLength/4, null);
			} catch (IOException e) {
				
			}
		}

	}
	public void drawBalls(Graphics g) {
		for (Ball b: balls) {
			b.paintBall(g);
		}
	}
	public void drawNumBalls(Graphics g) {
		g.setColor(Color.orange);
		g.drawString(Ball.maxBalls+"", (canvasWidth/2)-5, canvasLength-10);
	}
	public void drawDirectionalString(Graphics g) {
		g.setColor(Color.green);
		g.drawLine(canvasWidth/2, canvasLength, mouseX, mouseY);
	}

	public void drawStars(Graphics g, double brickWidth, double brickLength) {
		for (Star s: stars) {
			s.paint(g, brickWidth, brickLength);
		}
	}

	public void drawBricks(Graphics g, double brickWidth, double brickLength) {
		double arcWidth = brickWidth / 4;
		double arcLength = brickLength / 4;
		int maxVal = 0;
		for (Brick b: bricks) {
			if (b.val > maxVal) maxVal = b.val;
		}

		for (Brick b: bricks) {
			b.paintBrick(g, (int)brickWidth, (int)brickLength, (int)arcWidth, (int)arcLength, maxVal);
		}
	}
	public void drawLabels(Graphics g, double brickWidth, double brickLength) {
		for (Brick b: bricks) {
			g.setColor(Color.GREEN);
			g.drawString(b.val + "", (int)((b.x * ((int)brickWidth))+(int)brickWidth/2), (int)((b.y * ((int)brickLength))+(int)brickLength/2));
		}
	}
}
