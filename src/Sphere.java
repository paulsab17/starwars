

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Sphere extends Sprite
{
	private int radius;
	
	private Ellipse2D.Double sphereDrawing;
	
	protected Sphere(int x, int y, int dx, int dy, int rad, Color col){
		xPos = x;
		yPos = y;
		xVel = dx;
		yVel = dy;
		radius = rad;
		color = col;
		sphereDrawing = new Ellipse2D.Double(xPos-radius, yPos-radius,radius*2,radius*2);
		setShape(sphereDrawing);
	}
	protected Sphere(int x, int y, int rad, Color col){
		this(x,y,0,0,rad,col);
	}
	protected Sphere(int x, int y, int rad, Color col, int ang){
		this(x,y,0,0,rad,col);
		angle = ang;
	}
	protected Sphere (int x, int y, int radius){
		this(x,y,radius,Color.BLUE);
	}
	protected Sphere(){
		this(50,50,50,Color.BLUE);
	}
	protected void draw(Graphics gvar){
		Graphics2D g2 = (Graphics2D) gvar;
		
		sphereDrawing = new Ellipse2D.Double(xPos-radius, yPos-radius,radius*2,radius*2);
		setShape(sphereDrawing);
		g2.draw(sphereDrawing);
		g2.setColor(this.color);
		g2.fill(sphereDrawing);
		this.setShape(sphereDrawing);
	}	
}

