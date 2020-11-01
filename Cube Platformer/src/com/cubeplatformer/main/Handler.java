package com.cubeplatformer.main;

import java.awt.Graphics;
import java.util.LinkedList;

import com.cubeplatformer.main.Block.blockTYPE;
import com.cubeplatformer.main.Decoration.decorTYPE;

public class Handler {

	private BufferedImageLoader loader;
	private SpriteSheet ss;
	private GameCP game;
	
	public Handler(BufferedImageLoader loader, GameCP game, SpriteSheet ss) {
		this.loader = loader;
		this.game = game;
		this.ss = ss;
	}
	
	LinkedList<GameObject> object = new LinkedList<GameObject>();
	
	private GameObject tempObject;
	
	public void tick() {
		for(int i = 0; i < object.size(); i++) {
			tempObject = object.get(i);
		
			tempObject.tick(object);
		}
		
	}
	
	public void render(Graphics g) {
		for(int i = 0; i < object.size(); i++) {
			tempObject = object.get(i);
			
			tempObject.render(g);
			
		}
	}
	
	public void addObject(GameObject object) {
		this.object.add(object);
	}
	
	public void removeObject(GameObject object) {
		this.object.remove(object);
	}
	
	public void createLevel1() {
		for(int a = 0; a < GameCP.WIDTH * 3; a += 64) {
			addObject(new Block(a, GameCP.HEIGHT - 93, ID.Block, blockTYPE.grass, loader, this, 0, 0, 0));
		}
		
		for(int i = 0; i < object.size(); i++) {
			if(object.get(i).getID() == ID.Block && object.get(i).getBlockID() == blockTYPE.grass) {
				if(object.get(i).getX() == 1280) {
					removeObject(object.get(i));
				}
			}
		}
		
		addObject(new Block(700, GameCP.HEIGHT - 157, ID.Block, blockTYPE.tree, loader, this, 0, 0, 0));
		addObject(new Block(700, GameCP.HEIGHT - 221, ID.Block, blockTYPE.tree, loader, this, 0, 0, 0));
		addObject(new Block(700, GameCP.HEIGHT - 285, ID.Block, blockTYPE.tree, loader, this, 0, 0, 0));
		addObject(new Block(700, GameCP.HEIGHT - 349, ID.Block, blockTYPE.leaf, loader, this, 0, 0, 0));
		addObject(new Block(764, GameCP.HEIGHT - 285, ID.Block, blockTYPE.leaf, loader, this, 0, 0, 0));
		addObject(new Block(636, GameCP.HEIGHT - 285, ID.Block, blockTYPE.leaf, loader, this, 0, 0, 0));
		addObject(new Block(636, GameCP.HEIGHT - 349, ID.Block, blockTYPE.leaf, loader, this, 0, 0, 0));
		addObject(new Block(764, GameCP.HEIGHT - 349, ID.Block, blockTYPE.leaf, loader, this, 0, 0, 0));
		
		addObject(new Block(1280, GameCP.HEIGHT - 93, ID.Block, blockTYPE.shrink, loader, this, 0, 0, 0));
		
		addObject(new Block(200, GameCP.HEIGHT - 157, ID.Block, blockTYPE.dirt, loader, this, 0, 0, 0));
		addObject(new Block(500, GameCP.HEIGHT - 221, ID.Block, blockTYPE.dirt, loader, this, 0, 0, 0));
		addObject(new Block(800, GameCP.HEIGHT - 221, ID.Block, blockTYPE.scoreup, loader, this, 0, 0, 0));
		addObject(new Enemy(810, GameCP.HEIGHT - 93 - 64, ID.Enemy, ss, this, 810, game.player));
		addObject(new DrunkEnemy(1500, GameCP.HEIGHT - 93 - 64, ID.DrunkEnemy, ss, this, game.player));
		addObject(new DrunkEnemy(1600, GameCP.HEIGHT - 93 - 64, ID.DrunkEnemy, ss, this, game.player));
		addObject(new DrunkEnemy(1700, GameCP.HEIGHT - 93 - 64, ID.DrunkEnemy, ss, this, game.player));
		for(int i = 0; i < 5; i++) {
			addObject(new Block(1856 + (i *64), GameCP.HEIGHT - 157, ID.Block, blockTYPE.end, loader, this, 0, 0, 0));
		}
		for(int i = 0; i < 4; i++) {
			addObject(new Block(1920 + (i *64), GameCP.HEIGHT - 221, ID.Block, blockTYPE.end, loader, this, 0, 0, 0));
		}
		for(int i = 0; i < 3; i++) {
			addObject(new Block(1984 + (i *64), GameCP.HEIGHT - 285, ID.Block, blockTYPE.end, loader, this, 0, 0, 0));
		}
		addObject(new Block(2048, GameCP.HEIGHT - 349, ID.Block, blockTYPE.end, loader, this, 0 , 0 , 0));
		addObject(new Block(2112, GameCP.HEIGHT - 349, ID.Block, blockTYPE.end, loader, this, 0 , 0 , 0));
		addObject(new Block(2112, GameCP.HEIGHT - 413, ID.Block, blockTYPE.end, loader, this, 0 , 0 , 0));
	}
	
	public void createLevel2() {
		//Logo goes in open space between spikes and wood
		for(int a = 0; a < GameCP.WIDTH * 3; a += 64) {
			addObject(new Block(a, GameCP.HEIGHT - 93, ID.Block, blockTYPE.grass, loader, this, 0 , 0 , 0));
		}
		
		addObject(new Block(450, GameCP.HEIGHT - 157, ID.Block, blockTYPE.elev, loader, this, GameCP.HEIGHT - 157, -4, 200));
		for(int b = 0; b < 12; b++) {
			addObject(new Block(550 + (b * 64), GameCP.HEIGHT - 93, ID.Block, blockTYPE.spike, loader, this, GameCP.HEIGHT - 157, -4, 200));
		}
		for(int b = 0; b < 15; b++) {
			addObject(new Block(550 + (b * 64), GameCP.HEIGHT - 400, ID.Block, blockTYPE.tree, loader, this, GameCP.HEIGHT - 157, -4, 200));
			addObject(new Decoration(550 + (b * 64), GameCP.HEIGHT - 336, ID.Decoration, loader, decorTYPE.vine));
		}
		addObject(new Block(1318 - 64, GameCP.HEIGHT - 250, ID.Block, blockTYPE.scoreup, loader, this, 0, 0, 0));
		addObject(new DrunkEnemy(1350, GameCP.HEIGHT - 157, ID.DrunkEnemy, ss, this, game.player));
	}
	
	public void clearLevel() {
		for(int i = 0; i < object.size(); i++) {
			if(object.get(i).getID() != ID.Player) {
				object.remove(i);
			}
		}
	}
	
}
