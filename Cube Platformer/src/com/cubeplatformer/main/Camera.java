package com.cubeplatformer.main;

public class Camera {

	private float x;
	private float y;
	
	public Camera(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void tick(GameObject player) {
		if(player.getX() >= x + 720 - 200 - player.getWidth()) {
			x += 3;
		} else if(player.getX() <= x + 200) {
			x -= 3;
		} else {
			x += 0;
		}
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
}
