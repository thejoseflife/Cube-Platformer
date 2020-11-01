package com.cubeplatformer.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class Triangle {

	private int height;
	private int bottom;
	private int x;
	private int y;
	public Rectangle[] bounds;
	private boolean fill = false;
	
	public Line2D side1;
	public Line2D side2;
	public Line2D side3;
	
	
	public Triangle(int x, int y, int width, int height, boolean fill) {
		this.bottom = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.fill = fill;
		
		bounds = new Rectangle[bottom * height];
		
		side1 = new Line2D.Float(x, y, x + bottom, y);
		side2 = new Line2D.Float(x, y, x + bottom/2, y - height);
		side3 = new Line2D.Float(x + bottom, y, x + (bottom/2), y - height);
 
	}
	
	public void render(Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.draw(side1);
		g2d.draw(side2);
		g2d.draw(side3);
		
		if(fill) {
			for(int i = 0; i < height; i++) {
				for(int a = 0; a < bottom - i; a++) {
					bounds[a] = new Rectangle(x + a + (i/2), y - i, 1, 1);
					g2d.draw(bounds[a]);
				}
			}
		}
		
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(3));
		g2d.draw(side1);
		g2d.draw(side2);
		g2d.draw(side3);
		
		g2d.setColor(Color.red);

	}
	
}
