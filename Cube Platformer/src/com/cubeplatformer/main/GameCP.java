package com.cubeplatformer.main;
//Notes:

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;


public class GameCP extends Canvas implements Runnable, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	private BufferedImage spritesheet = null;
	private BufferedImage background = null;
	private BufferedImage menu = null;
	private BufferedImage image = null;
	
	public static final int WIDTH = 720, HEIGHT = (WIDTH * 9) / 12;
	
	private Thread thread;
	private boolean running = false;
	
	private Handler handler;
	public Player player;
	private Camera cam;
	
	public static BufferedImage playeri;
	public static boolean inGame = false;
	
	public static Random r = new Random();	
	public static int level = 2;
	public static int score = 0;
	public static int death_count = 0;
	//public static boolean paused;
	
	private Rectangle start = new Rectangle(WIDTH/2 - 200, HEIGHT /2 - 100, 400, 200);

	public static enum STATE {
		GAME,
		MENU
	}
	
	public static STATE state = STATE.GAME;
	
	public void init() {
		
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try {

			background = loader.loadImage("/background.png");
			spritesheet = loader.loadImage("/sprite_sheet.png");

		} catch (Exception e) {
			System.out.println("Could not load spritesheet.");
		}
		
		SpriteSheet ss = new SpriteSheet(spritesheet);
		
		cam = new Camera(WIDTH/2 - 50 - 310 - 18, 0);
		
		requestFocus();
		
		//handler.createLevel1();
		
		handler = new Handler(loader, this, ss);
		
		player = new Player(WIDTH / 2 - 50, HEIGHT/2 - 50, ID.Player, handler, ss, cam);
		
		this.addKeyListener(new KeyInput(handler, this, player));
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		init();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
			}
			if(running)
				render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	
	private void tick() {
		handler.tick();
		
		for(int i = 0; i < handler.object.size(); i++) {
			if(handler.object.get(i).getID() == ID.Player) {
				cam.tick(handler.object.get(i));
			}
		}
		
		changeLevel();
		
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D)g.create();
		
		if(state == STATE.GAME) {
		
			g.drawImage(background, 0, 0, WIDTH, HEIGHT, this);
		//Graphics here v
		
			g.translate((int)-cam.getX(), (int)-cam.getY());
		
			handler.render(g);
		
			g.translate((int)cam.getX(), (int)cam.getY());
		
			if(level == 2) {
				g.drawImage(image, 100, 50, 500, 300, this);
			}
			
		//Graphics here ^
		} else if(state == STATE.MENU) {
			g.drawImage(menu, 0, 0, WIDTH, HEIGHT, null);
			g2d.draw(start);
			Font fx = new Font("Arial", Font.BOLD, 120);
			g.setFont(fx);
			g.drawString("START", start.x + 5, start.y + 135);
		}
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String args[]) {
		JFrame frame = new JFrame("Cubixy: Cube Platformer");
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		GameCP game = new GameCP();
		frame.add(game);
		frame.setVisible(true);
		game.start();
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		//System.out.println("In");
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		//System.out.println("Out");
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point e1 = e.getPoint();
		if(state == STATE.MENU) {
			if(start.contains(e1)) {
				state = STATE.GAME;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//handler.addObject(new Block(e.getX() - 16, e.getY() - 16, ID.Block, blockTYPE.dirt));
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		//System.out.println("Drag");
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		//System.out.println("Move");
		
	}
	
	public static int clamp(int var, int min, int max) {
		if(var >= max)
			return var = max;
		else if(var <= min)
			return var = min;
		else
			return var;
	}
	
	public void changeLevel() {
		if(!inGame && level == 1) {
			handler.createLevel1();
			handler.addObject(player);
			inGame = true;
		} else if(level == 2 && !inGame) {
			handler.object.removeAll(handler.object);
			inGame = true;
			handler.createLevel2();
			handler.addObject(player);
			player.respawn();
		}
	}
	
}
