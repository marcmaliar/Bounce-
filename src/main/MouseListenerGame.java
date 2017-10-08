/**
 * 
 */
package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author marcmaliar
 *
 */
public class MouseListenerGame implements MouseListener{

	GameWorld gw;

	public MouseListenerGame(GameWorld gw) {
		this.gw = gw;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (gw.choosing) {
			gw.displayLine=true;
			gw.mouseX = e.getX();
			gw.mouseY = e.getY();
		}
		else {
			gw.displayLine = false;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (gw.balls.isEmpty() && gw.choosing) {
			gw.ballsLeft = Ball.maxBalls;
			gw.mouseX = e.getX();
			gw.mouseY = e.getY();
			gw.choosing = false;
			gw.displayLine = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
