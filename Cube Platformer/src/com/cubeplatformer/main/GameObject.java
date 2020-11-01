package com.cubeplatformer.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.cubeplatformer.main.Block.blockTYPE;

public abstract class GameObject {

	protected float x, y;
	protected ID id;
	protected blockTYPE blocktype;
	protected float velX, velY;
	protected float height, width;
	protected boolean falling = true;
	protected boolean jumping = false;
	
	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public GameObject(int x, int y, ID id) {
		this.x = x;
		this.y = y;
		this.id = id;
	}
	
	public abstract void tick(LinkedList<GameObject> object);
	public abstract void render(Graphics g);
	public abstract Rectangle getBounds();
	
	public void setX(int d) {
		this.x = d;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setVelX(int x) {
		this.velX = x;
	}
	
	public void setVelY(int y) {
		this.velY = y;
	}
	
	public double getVelX() {
		return velX;
	}
	
	public double getVelY() {
		return velY;
	}
	
	public ID getID() {
		return id;
	}
	
	public void setID(ID id) {
		this.id = id;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getWidth() {
		return width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setBlockType(blockTYPE blocktype) {
		this.blocktype = blocktype;
	}
	
	public blockTYPE getBlockID() {
		return blocktype;
	}
}
