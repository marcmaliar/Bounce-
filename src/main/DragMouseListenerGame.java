/**
 * 
 */
package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * @author marcmaliar
 *
 */
public class DragMouseListenerGame implements MouseMotionListener{

	GameWorld gw;
	
	public DragMouseListenerGame(GameWorld gw) {
		this.gw = gw;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (gw.choosing) {
			gw.mouseX = e.getX();
			gw.mouseY = e.getY();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
