

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;

public class Sprite {
	protected int xPos;
	protected int yPos;
	protected int angle;
	protected double xVel;
	protected double yVel;
	protected Color color;
	protected boolean isAlive = true;
	
	private Shape spriteShape;
	
	protected void die(){
		isAlive = false;
	}
	protected void revive(){
		isAlive = true;
	}
	protected void setPos(int x, int y){
		xPos = x;
		yPos = y;
	}
	protected void moveNow(int x, int y){
		xPos+=x;
		yPos+=y;
	}
	protected void moveNow(){
		xPos+=xVel;
		yPos+=yVel;
	}
	protected int getX(){
		return xPos;
	}
	protected int getY(){
		return yPos;
	}
	protected int getAng(){
		return angle;
	}
	protected void setShape(Shape a){
		spriteShape = a;
	}
	protected Shape getShape(){
		return spriteShape;
	}
	protected void changeColor(Color newColor){
		color = newColor;
	}
	protected boolean intersects(Shape other){			
		Area thisArea = new Area(other);
		thisArea.intersect(new Area(spriteShape));
		return !thisArea.isEmpty();
	}
	
	protected void draw(Graphics gvar){
		Graphics2D g2 = (Graphics2D) gvar;
		
		g2.drawString("Null Sprite!", 100, 100);
	}
}
