package com.cubeplatformer.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

public class Decoration extends GameObject {

	private BufferedImage spritesheet;
	private SpriteSheet ss;
	
	private BufferedImage vine;
	
	public enum decorTYPE {
		vine,
		torch
	}
	
	public decorTYPE decortype;
	
	public Decoration(int x, int y, ID id, BufferedImageLoader loader, decorTYPE d) {
		super(x, y, id);
		
		this.decortype = d;
		try {
			spritesheet = loader.loadImage("/decorspritesheet.png");
			ss = new SpriteSheet(spritesheet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		vine = ss.grabBlock(1, 1, 64, 64);
		
	}

	@Override
	public void tick(LinkedList<GameObject> object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		if(decortype == decorTYPE.vine) {
			g.drawImage(vine, (int)x, (int)y, 64, 64, null);
		}
		
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

}
