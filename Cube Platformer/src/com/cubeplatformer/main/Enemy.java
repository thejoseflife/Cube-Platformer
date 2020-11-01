package com.cubeplatformer.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Enemy extends GameObject {

	private Handler handler;
	private Player player;
	
	private final int startpos;
	private int w = 42;
	private float gravity = 0.8f;
	public float health = 50;
	public static boolean resetting = false;
	
	private float r = 0;
	private float g = 255;
	private float b = 0;
	
	public int fvelX;
	
	Color hc = new Color((int)r, (int)g, (int)b);
	
	private BufferedImage e1;
	private BufferedImage e2;
	private BufferedImage ex;
	
	public Enemy(int x, int y, ID id, SpriteSheet ss, Handler handler, int startpos, Player player) {
		super(x, y, id);
		this.handler = handler;
		this.startpos = startpos;
		this.player = player;
		
		velX = 3;
		height = 64;
		
		e1 = ss.grabImage(2, 1, 32, 32);
		e2 = ss.grabImage(3, 1, 32, 32);
		ex = ss.grabImage(6, 1, 32, 32);
	}

	@Override
	public void tick(LinkedList<GameObject> object) {
		
		health = GameCP.clamp((int)health, 0, 50);
		
		x += velX;
		y += velY;

			if(x >= startpos + 300) {
				velX = -3;
				fvelX = -3;
			} else if(x <= startpos) {
				velX = 3;
				fvelX = 3;
			}
		
		Collision(handler.object);
		
		if(falling || jumping) {
			velY += gravity;
		}
		
		if(health == 0) {
			GameCP.score++;
			handler.removeObject(this);
		}
		
		if(player.beerp != null) {
			if(getFullBounds().contains(player.beerp)) {
				decreaseHealth();
			}
		}
		
	}

	@Override
	public void render(Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		
		if(velX > 0) {
			g.drawImage(e1, (int)x, (int)y, 64, (int)height, null);
		} else if(velX < 0) {
			g.drawImage(e2, (int)x, (int)y, 64, (int)height, null);
		} else if(velX == 0) {
			g.drawImage(ex, (int)x, (int)y, 64, (int)height, null);
		}
		
		g.setColor(hc);
		g.fillRect((int)x - 4, (int)y- 20, (int)health, 10);
		int borderWidth = 2;
		g2d.setStroke(new BasicStroke(borderWidth));
		g.setColor(Color.WHITE);
		g.drawRect((int)x - 4, (int)y - 20, 50, 10);
		
		g2d.setColor(Color.red);
		/*
		g2d.draw(getBounds());
		g2d.draw(getBoundsTop());
		g2d.draw(getBoundsRight());
		g2d.draw(getBoundsLeft());*/
		
	}
	
	public void Collision(LinkedList<GameObject> object) {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getID() == ID.Block) {
				if(getBounds().intersects(tempObject.getBounds())) {
					falling = false;
					jumping = false;
					velY = 0;
					y = (int)tempObject.getY() - height;
				} else {
					falling = true;
				}
				
				if(getBoundsTop().intersects(tempObject.getBounds())) {
					velY = 0;
					y = (int)tempObject.getY() + 64;
				} 
				
				if(getBoundsLeft().intersects(tempObject.getBounds())) {
					if(((Block) tempObject).getBlockType() != Block.blockTYPE.dirt) {
						x = (int)tempObject.getX() + 64;
					} else if(((Block) tempObject).getBlockType() == Block.blockTYPE.dirt) {
						if(velX <= 0) {
							tempObject.x += velX;
						}
					}
					
				}
				
				if(getBoundsRight().intersects(tempObject.getBounds())) {
					if(((Block) tempObject).getBlockType() != Block.blockTYPE.dirt) {
						x = (int)tempObject.getX() - w;
					} else if(((Block) tempObject).getBlockType() == Block.blockTYPE.dirt) {
						if(velX >= 0) {
							tempObject.x += velX;
						}
					}
					
				}
				
			}
		}
		
		if(player.getID() == ID.Player) {
			if(getFullBounds().intersects(player.getFullBounds())) {
				player.decreaseHealth(ID.Enemy);
			}
		}
		
	}

	public Rectangle getBounds() {
		return new Rectangle((int) (x + (w/4)), (int) ((int)y + height/2), w/2, (int)height/2);
		
	}
	public Rectangle getBoundsLeft() {
		return new Rectangle((int)x, (int)y + 5, w/4, (int) (height - 10));
		
	}
	public Rectangle getBoundsRight() {
		return new Rectangle((int)x + w/2 + w/4, (int)y + 5, w/4, (int) (height - 10));
		
	}
	public Rectangle getBoundsTop() {
		return new Rectangle((int)x + (w/4), (int)y, w/2, (int) (height/2));
		
	}
	public Rectangle getFullBounds() {
		return new Rectangle((int)x, (int) y, (int)w, (int)height);
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
