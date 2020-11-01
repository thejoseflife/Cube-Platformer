package com.cubeplatformer.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

public class Block extends GameObject {
	
	public static enum blockTYPE {
		grass,
		dirt,
		tree,
		leaf,
		scoreup,
		end,
		shrink,
		elev,
		spike
	}
	
	private BufferedImage spritesheet = null;
	private SpriteSheet ss;
	private Handler handler;
	
	private BufferedImage dirt;
	private BufferedImage wood;
	private BufferedImage leaf;
	private BufferedImage scoreup;
	private BufferedImage end;
	private BufferedImage shrink;
	private BufferedImage elev;
	private BufferedImage spikeface;
	
	private Triangle spike;
	
	private int velW = 0;
	private int velH = 0;
	private int startpos;
	private int distance;
	
	private Color green = new Color(26, 255, 0);
	
	public Block(int x, int y, ID id, blockTYPE blocktype, BufferedImageLoader loader, Handler handler, int startpos, float speed, int distance) {
		super(x, y, id);
		this.handler = handler;
		setBlockType(blocktype);
		
		try {
			spritesheet = loader.loadImage("/blockspritesheet.png");
			ss = new SpriteSheet(spritesheet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		dirt = ss.grabBlock(1, 1, 64, 64);
		wood = ss.grabBlock(2, 1, 64, 64);
		leaf = ss.grabBlock(3, 1, 64, 64);
		scoreup = ss.grabBlock(4, 1, 64, 64);
		end = ss.grabBlock(1, 2, 64, 64);
		shrink = ss.grabBlock(2, 2, 64, 64);
		elev = ss.grabBlock(3, 2, 64, 64);
		spikeface = ss.grabBlock(4, 2, 64, 64);
		
		width = 64;
		height = 64;
		
		if(blocktype == blockTYPE.elev) {
			velY = speed;
			this.startpos = startpos;
			this.distance = distance;
		}
	}

	@Override
	public void tick(LinkedList<GameObject> object) {
		
		width += velW;
		height += velH;
		y += velY;
		x += velX;
		
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			if(tempObject.getID() == ID.Player && getBlockType() == blockTYPE.shrink) {
				if(getBounds().intersects(tempObject.getBounds())) {
					velW = -3;
					velH = -3;
					velX = 1.5f;
					velY = 1.5f;
				}
			} else if(tempObject.getID() == ID.Player && getBlockType() == blockTYPE.spike) {
						if(spike.side1.intersects(((Player)tempObject).getFullBounds()) || spike.side2.intersects(((Player)tempObject).getFullBounds()) || spike.side3.intersects(((Player)tempObject).getFullBounds())) {
							((Player) tempObject).health -= 0.0001f;
						}
			}
		}
		
		if(width <= 0 || height <= 0) {
			handler.removeObject(this);
		}
		
		if(blocktype == blockTYPE.elev) {
			if(y == startpos - distance) {
				velY = -velY;
			} else if(y == startpos) {
				velY = -velY;
			}
		}
		
	}

	@Override
	public void render(Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		
		int borderWidth = 2;
		g2d.setStroke(new BasicStroke(borderWidth));
		
		if(blocktype == blockTYPE.grass) {
			
			g.setColor(green);
			for(int a = 2; a < 64; a += 4) {
				g.drawLine((int)x + a, (int)y, (int)x + a, (int)y - 10);
			}
			g.fillRect((int)x, (int)y, 64, 64);
		} else if(blocktype == blockTYPE.dirt) {
			g.drawImage(dirt, (int)x, (int)y, null);
		} else if(blocktype == blockTYPE.tree) {
			g.drawImage(wood,(int)x, (int)y, 64, 64, null);
		} else if(blocktype == blockTYPE.leaf) {
			g.drawImage(leaf,(int)x, (int)y, 64, 64, null);
		} else if(blocktype == blockTYPE.scoreup) {
			g.drawImage(scoreup,(int)x, (int)y, 64, 64, null);
		} else if(blocktype == blockTYPE.end) {
			g.drawImage(end,(int)x, (int)y, 64, 64, null);
		} else if(blocktype == blockTYPE.shrink) {
			
			g.setColor(green);
			for(int a = 2; a < 64; a += 4) {
				g.drawLine((int)x + a, (int)y, (int)x + a, (int)y - 10);
			}
			g.drawImage(shrink,(int)x, (int)y, (int)width, (int)height, null);
		} else if(blocktype == blockTYPE.elev) {
			g.drawImage(elev, (int)x, (int)y, 64, 64, null);
		} else if(blocktype == blockTYPE.spike) {
			g.setColor(Color.RED);
			spike = new Triangle((int)x, (int)y, 64, 64, true);
			spike.render(g);
			g.drawImage(spikeface,(int)x + 15, (int)y - 40, 32, 32, null);
		}
		
		if(blocktype != blockTYPE.spike) {
			g.setColor(Color.BLACK);
			int borderWidth1 = 3;
			g2d.setStroke(new BasicStroke(borderWidth1));
			g.drawRect((int)x, (int)y, (int)width, (int)height);
		}
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, 64, 64);
	}
	
	public Rectangle getBoundsRight() {
		return new Rectangle((int)x + 32, (int) y + 7, 32, 50);
	}
	
	public Rectangle getBoundsLeft() {
		return new Rectangle((int)x, (int) y + 7, 32, 50);
	}

	public blockTYPE getBlockType() {
		return blocktype;
	}
	
}
