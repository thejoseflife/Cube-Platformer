package com.cubeplatformer.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
	
	private Handler handler;
	private Player player;
	
	private boolean[] keyDown = new boolean[2];
	
	public KeyInput(Handler handler, GameCP game, Player player) {
		this.handler = handler;
		this.player = player;
		
		keyDown[0] = false;
		keyDown[1] = false;
	}
	
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
				if(tempObject.getID() == ID.Player) {
					if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) { tempObject.setVelX(4); keyDown[0] = true; }
					if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) { tempObject.setVelX(-4); keyDown[1] = true; }
					if((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && !tempObject.isJumping()) { tempObject.setVelY(-12); tempObject.setJumping(true); }
					if(key == KeyEvent.VK_SPACE && !Player.isShooting && player.velX != 0) { Player.isShooting = true; }
				}
			
			}
		
		
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getID() == ID.Player) {
				if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) { keyDown[0] = false; }
				if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) { keyDown[1] = false; }
				if(key == KeyEvent.VK_SPACE && Player.isShooting) { Player.isShooting = false; }
				
				if(!keyDown[0] && !keyDown[1]) {
					tempObject.setVelX(0);
				}
			}
		}
	}
}

