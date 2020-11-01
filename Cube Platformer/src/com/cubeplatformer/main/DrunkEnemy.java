package com.cubeplatformer.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class DrunkEnemy extends GameObject {

	private Handler handler;
	private Player player;
	
	public float health = 50;
	
	private float r = 0;
	private float g = 255;
	private float b = 0;
	private int timer = 120;
	
	Color hc = new Color((int)r, (int)g, (int)b);
	
	private BufferedImage e1;
	private BufferedImage e2;
	
	public DrunkEnemy(int x, int y, ID id, SpriteSheet ss, Handler handler, Player player) {
		super(x, y, id);
		this.handler = handler;
		this.player = player;
		
		height = 64;
		
		e1 = ss.grabImage(4, 1, 32, 32);
		e2 = ss.grabImage(5, 1, 32, 32);
	}

	@Override
	public void tick(LinkedList<GameObject> object) {
		
		health = GameCP.clamp((int)health, 0, 50);
		
		timer--;
		if(timer <= 0) {
			timer = 120;
		}
		
		x += velX;
		y += velY;
		
		Collision(handler.object);
		
		if(health == 0) {
			GameCP.score++;
			handler.removeObject(this);
		}
		
		if(player.beerp != null) {
			if(getBounds().contains(player.beerp)) {
				decreaseHealth();
			}
		}
		
	}

	@Override
	public void render(Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		
		if(timer > 60) {
			g.drawImage(e1, (int)x, (int)y, 64, (int)height, null);
		} else if(timer <= 60) {
			g.drawImage(e2, (int)x - 10, (int)y, 64, (int)height, null);
		}
		
		g.setColor(hc);
		g.fillRect((int)x - 4, (int)y- 20, (int)health, 10);
		int borderWidth = 2;
		g2d.setStroke(new BasicStroke(borderWidth));
		g.setColor(Color.WHITE);
		g.drawRect((int)x - 4, (int)y - 20, 50, 10);
		
		g2d.setColor(Color.red);
		
	}
	
	public void Collision(LinkedList<GameObject> object) {
		
		if(getBounds().intersects(player.getFullBounds())) {
			player.decreaseHealth(ID.DrunkEnemy);
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, 64, 64);
		
	}
	
	public void decreaseHealth() {
		health--;
		r += 6;
		r = GameCP.clamp((int)r, 0, 255);
		b += 1;
		b = GameCP.clamp((int)b, 0, 255);
		g -= 6;
		g = GameCP.clamp((int)g, 0, 255);
		hc = new Color((int)r, (int)g, (int)b);
	}

}
