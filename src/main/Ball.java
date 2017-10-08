/**
 * 
 */
package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.List;

/**
 * @author marcmaliar
 *
 */
public class Ball {

	final static int WIDTH = 20;
	final static int LENGTH = 20;
	final static int SPEED = 2;
	static int maxBalls = 1;
	Color c = Color.BLUE;
	
	double x, y;
	double speedX, speedY;
	
	Brick lastBrick;
	
	public Ball(double x, double y, double speedX, double speedY) {
		this.x = x;
		this.y = y;
		this.speedX = speedX;
		this.speedY = speedY;
	}
	
	public boolean moveWithDetection(List<Brick> bricks, List<Star> stars, List<Ball> balls, int brickWidth, int brickLength, int canvasWidth, int canvasLength) {
		x += speedX * SPEED;
		y += speedY * SPEED;
		for (Brick b: bricks) {
			Area ball = (new Area(new Rectangle((int)x,(int)y,WIDTH,LENGTH)));
			ball.intersect(new Area(new Rectangle(b.x * brickWidth, b.y * brickLength, brickWidth, brickLength)));
			if (!ball.isEmpty() && b != lastBrick) {
				lastBrick = b;
				b.val--;
				if (b.val == 0) bricks.remove(b);
				int centerBallX = (int)x + WIDTH/2;
				int centerBallY = (int)y + LENGTH/2;
				int centerBrickX = b.x*brickWidth + brickWidth/2;
				int centerBrickY = b.y*brickLength + brickLength/2;
				int dx = centerBallX - centerBrickX;
				int dy = centerBallY - centerBrickY;
				double atan = Math.atan2(dy, dx);
				double brickTan = b.getTangent();
				if ((atan >= -brickTan && atan <= brickTan)||(atan >= Math.PI-brickTan && atan <= Math.PI)||(atan >= -Math.PI && atan <= -Math.PI+brickTan)) {
					speedX *= -1;
				}
				else if ((atan >= brickTan && atan <= Math.PI - brickTan)||(atan >= -Math.PI-brickTan && atan <= -brickTan)){
					speedY *= -1;
				}
				break;
			}
		}
		if (x < 0) {
			speedX *= -1;
			x = 0;
			lastBrick=null;
		}
		else if (x > canvasWidth - WIDTH) {
			speedX *= -1;
			x = canvasWidth - WIDTH;
			lastBrick=null;
		}
		else if (y < 0) {
			speedY *= -1;
			y = 0;
			lastBrick=null;
		}
		else if (y > canvasLength - LENGTH) {
			balls.remove(this);
		}
		for (Star s: stars) {
			if (new Area(new Rectangle((int)x,(int)y,WIDTH,LENGTH)).intersects(new Rectangle(s.x*brickWidth, s.y*brickLength, brickWidth, brickLength))) {
				stars.remove(s);
				return true;
			}
		}
		return false;
		
	}
	
	public void paintBall(Graphics g) {
		g.setColor(c);
		g.fillOval((int)x, (int)y, WIDTH, LENGTH);
	}
	
}
