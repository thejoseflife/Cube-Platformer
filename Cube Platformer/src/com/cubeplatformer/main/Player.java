package com.cubeplatformer.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import com.cubeplatformer.main.Block.blockTYPE;	

public class Player extends GameObject {
	
	private Handler handler;
	private BufferedImage playerSPRITE = null;
	private Camera cam;
	
	private Color purple = new Color(204, 80, 165);

	private float gravity = 0.8f;
	private int width = 64, height = 64;
	public int w;
	public float health = 100;
	
	private float r = 0;
	private float g = 255;
	private float b = 0;
	public static boolean isShooting = false;
	private float beerx;
	private float beery;
	public Point beerp;
	private String message = "";
	private int timer = 120;
	
	Color hc = new Color((int)r, (int)g, (int)b);
	
	private final float MAX_SPEED = 10;
	
	public Player(int x, int y, ID id, Handler handler, SpriteSheet ss, Camera cam) {
		super(x, y, id);
		this.handler = handler;
		this.cam = cam;
		
		playerSPRITE = ss.grabImage(1, 1, 32, 32);
		
		w = playerSPRITE.getWidth();
	}

	@Override
	public void tick(LinkedList<GameObject> object) {
		
		health = GameCP.clamp((int)health, 0, 100);
		
		x += velX;
		y += velY;
		
		if(isShooting && velX > 0) {
			beerx += 6;
			beery++;
		} else if(isShooting && velX < 0) {
			beerx -= 6;
			beery++;
		} else if(!isShooting) {
			beerx = x + w/2;
			beery = y + 5;
		}
		
		if(velX == 0) {
			isShooting = false;
		}
		
		if(isShooting) {
			beerp = new Point((int)beerx, (int)beery);
		}
		
		if(falling || jumping) {
			velY += gravity;
		}
		
		if(velY >= MAX_SPEED) {
			velY = MAX_SPEED;
		}
		
		if((y > GameCP.HEIGHT && x < GameCP.WIDTH * 3) || health == 0) {
			GameCP.death_count++;
			respawn();
		}
		
		//changeLevel();
	
		if(!message.equals("")) {
			timer--;
			if(timer <= 0) {
				message = "";
				timer = 120;
			}
			
		}
		
		Collision(handler.object);
		
		if(isShooting == false) {
			beerp = null;
		}
		
		if(x <= cam.getX()) {
			x = cam.getX();
		} else if(x >= cam.getX() + 720 - w - 5) {
			x = cam.getX() + 720 - w - 5;
		}
		
	}

	public void Collision(LinkedList<GameObject> object) {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getID() == ID.Block && ((Block) tempObject).getBlockID() != blockTYPE.leaf && ((Block) tempObject).getBlockID() != blockTYPE.shrink && ((Block) tempObject).getBlockID() != blockTYPE.spike) {
				if(getBounds().intersects(tempObject.getBounds())) {
					falling = false;
					jumping = false;
					velY = 0;
					y = (int)tempObject.getY() - height;
				} else {
					falling = true;
				}
				
				if(getBoundsTop().intersects(tempObject.getBounds()) && ((Block) tempObject).getBlockID() != blockTYPE.leaf && ((Block) tempObject).getBlockID() != blockTYPE.elev) {
					velY = 0;
					y = (int)tempObject.getY() + 64;
				} else if(getBoundsLeft().intersects(tempObject.getBounds())) {
					if(((Block) tempObject).getBlockType() != Block.blockTYPE.dirt && ((Block)tempObject).getBlockType() != Block.blockTYPE.leaf) {
						x = (int)tempObject.getX() + 58;
					} else if(((Block) tempObject).getBlockType() == Block.blockTYPE.dirt) {
						if(velX <= 0) {
							tempObject.x += velX;
						}
					}

				} else if(getBoundsRight().intersects(tempObject.getBounds())) {
					if(((Block) tempObject).getBlockType() != Block.blockTYPE.dirt  && ((Block)tempObject).getBlockType() != Block.blockTYPE.leaf) {
						x = (int)tempObject.getX() - w - 24;
					} else if(((Block) tempObject).getBlockType() == Block.blockTYPE.dirt) {
						if(velX >= 0) {
							tempObject.x += velX;
						}
					}
					
				}
				
			}

			if(tempObject.getID() == ID.Block) {
				if(beerp != null) {
					if(tempObject.getBlockID() != blockTYPE.scoreup) {
						if(tempObject.getBounds().contains(beerp)) {
							isShooting = false;
						}
					} else if(tempObject.getBlockID() == blockTYPE.scoreup) {
						if(tempObject.getBounds().contains(beerp)) {
							GameCP.score++;
							handler.removeObject(tempObject);
							isShooting = false;
						}
					}
				}
			}
			
		}//End of for loop
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(playerSPRITE, (int)x, (int)y, width, height, null);
		Graphics2D g2d = (Graphics2D) g;
		//System.out.println(health);
		g.setColor(hc);
		g.fillRect((int)x - 18, (int)y- 20, (int)health, 10);
		int borderWidth = 2;
		g2d.setStroke(new BasicStroke(borderWidth));
		g.setColor(Color.WHITE);
		g.drawRect((int)x - 18, (int)y - 20, 100, 10);
		
		int borderWidth1 = 1;
		g2d.setStroke(new BasicStroke(borderWidth1));
		
		Font f1 = new Font("Arial", Font.BOLD, 50);
		Font f2 = new Font("Arial", Font.BOLD, 25);
		g.setFont(f1);
		g.setColor(Color.MAGENTA);
		if(GameCP.level == 1) {
			g.drawString("WAD or Arrow keys to move", 50, 100);
			g.drawString("Up to jump, space while moving", 50, 150);
			g.drawString("to shoot webs :)", 50, 200);
		}
		g2d.setColor(Color.white);
		
		g2d.draw(getBounds());
		g2d.draw(getBoundsLeft());
		g2d.draw(getBoundsRight());
		g2d.draw(getBoundsTop());
		
		g.setFont(f2);
		g.setColor(purple);
		g.drawString("Score: " + GameCP.score , (int)cam.getX() + 10, 25);
		g.drawString("Deaths: " + GameCP.death_count , (int)cam.getX() + 10, 50);
		g.drawString(message, 100, 300);
		
		Font f3 = new Font("arial", Font.BOLD, 15);
		g.setFont(f3);
		g.setColor(Color.WHITE);
		
		if(isShooting) {
			int borderWidth2 = 10;
			g2d.setStroke(new BasicStroke(borderWidth2));
			g.drawLine((int)x + w/2, (int)y + 5, (int)beerx, (int)beery);
		}
		
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) (x + 24), (int)y + height/2, w/2, height/2);
		
	}
	public Rectangle getBoundsLeft() {
		return new Rectangle((int)x + 8, (int)y + 5, w/4, height - 10);
		
	}
	public Rectangle getBoundsRight() {
		return new Rectangle((int)x + w + 16, (int)y + 5, w/4, height - 10);
		
	}
	public Rectangle getBoundsTop() {
		return new Rectangle((int)(x + 24), (int)y, w/2, height/2);
		
	}
	
	public Rectangle getFullBounds() {
		return new Rectangle((int)x + 8, (int)y, w + 12, height);
	}
	
	public void respawn() {
		r = 0;
		g = 255;
		b = 0;
		hc = new Color((int)r, (int)g, (int)b);
		x = GameCP.WIDTH / 2 - 50;
		y = GameCP.HEIGHT/2 - 50;
		cam.setX(x - 310 - w/2);
		health = 100;
		isShooting = false;
		velX = 0;
		velY = 0;
	}
	
	@Override
	public float getWidth() {
		return w;
	}
	
	public void decreaseHealth(ID id) {
		if(id == ID.Enemy) {
			health--;
			r += 3;
			r = GameCP.clamp((int)r, 0, 255);
			b += 0.5f;
			b = GameCP.clamp((int)b, 0, 255);
			g -= 3;
			g = GameCP.clamp((int)g, 0, 255);
			hc = new Color((int)r, (int)g, (int)b);
		} else if(id == ID.DrunkEnemy) {
			health -= 2;
			r += 6;
			r = GameCP.clamp((int)r, 0, 255);
			b += 1;
			b = GameCP.clamp((int)b, 0, 255);
			g -= 6;
			g = GameCP.clamp((int)g, 0, 255);
			hc = new Color((int)r, (int)g, (int)b);
		}
	}
	
	/*public void changeLevel() {
		if(GameSC.level == 1) {
			if(y > GameSC.HEIGHT && x >= (GameSC.WIDTH * 3) - 100) {
				if(GameSC.score >= 5) {
					if(health == 100 && GameSC.death_count == 0) {
						GameSC.score++;
					}
					GameSC.inGame = false;
					handler.removeObject(this);
					message = "";
					GameSC.level++;
					
				} else if(GameSC.score < 5 && GameSC.level == 1) {
					respawn();
					message = "Score too low!";
				
				}
			}
		}
		
		if(GameSC.level == 2) {
			if(y > GameSC.HEIGHT && x >= (GameSC.WIDTH * 3) - 100) {
				if(GameSC.score >= 12) {
					if(health == 100 && GameSC.death_count == 0) {
						GameSC.score++;
					}
					GameSC.level++;
					GameSC.inGame = false;
					handler.removeObject(this);
					
				} else if(GameSC.score < 12 && GameSC.level == 2) {
					respawn();
					message = "Score too low!";
				
				}
			}
		}
	}
	
	*/
}
