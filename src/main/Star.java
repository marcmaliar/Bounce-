/**
 * 
 */
package main;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author marcmaliar
 *
 */
public class Star {

	int x, y;
	
	private final Color c = Color.RED;
	
	public Star(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void paint(Graphics g, double brickWidth, double brickLength) {
		g.setColor(c);
		g.fillOval((int)(x*brickWidth), (int)(y*brickLength), (int)(brickWidth), (int)(brickLength));
	}
}
