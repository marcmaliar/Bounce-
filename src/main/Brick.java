package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;

public class Brick {
	
	
	int x, y,  val;
	int brickWidth, brickLength;
	JLabel j;
	static GameWorld gw;
	
	public Brick(GameWorld gw, int x, int y, int val) {
		this.x = x;
		this.y = y;
		this.val = val;
		brickWidth = 0;
		brickLength = 0;
	}

	public static List<Brick> newLayer(GameWorld gw, int brickX, int val, List<Star> stars) {
		Brick.gw = gw;
		List<Brick> layer = new LinkedList<Brick>();
		int starOpening = (new Random()).nextInt(brickX);
		int openingEmpty1 = 0;
		do {
			openingEmpty1 = (new Random()).nextInt(brickX);
		}while(starOpening == openingEmpty1);
		int openingEmpty2 = 0;
		do {
			openingEmpty2 = (new Random()).nextInt(brickX);
		}while(starOpening == openingEmpty2);
		for (int i = 0; i < brickX; i++) {
			if (i != starOpening && i != openingEmpty1 && i != openingEmpty2) layer.add(new Brick(gw, i,0,val));
			
		}
		stars.add(new Star(starOpening, 0));
		return layer;
	}
	
	public double getTangent() {
		return Math.atan((brickLength/2.0)/(brickWidth/2.0));
	}
	
	public void paintBrick(Graphics g, int brickWidth, int brickLength, int arcWidth, int arcLength, int maxBrickVal) {
		this.brickWidth = brickWidth;
		this.brickLength = brickLength;
		int colorVal = (int)(255-255*(((double)val) / ((double)maxBrickVal)));
		g.setColor(new Color(colorVal, colorVal, colorVal));
		g.fillRoundRect(x*brickWidth, y*brickLength, brickWidth, brickLength, arcWidth, arcLength);
	}
	
}
