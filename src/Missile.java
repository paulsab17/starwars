

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

public class Missile extends Sprite{
	
	protected Missile(int x,int y, Color col, int ang){
		xPos = x;
		yPos = y;
		color = col;
		angle = ang;
		xVel = -10.0*Math.cos(angle*Math.PI/180.0);
		yVel = -10.0*Math.sin(angle*Math.PI/180.0);
		initiateBody();
		
	}
	protected Missile(int x,int y,int ang){
		this(x,y,Color.GREEN,ang);
	}
	protected void draw(Graphics gvar){
		Graphics2D g2 = (Graphics2D) gvar;
		
		Point[] points = new Point[4];
		points[0] = new Point(xPos-2,yPos-4);
		points[1] = new Point(xPos+2,yPos-4);
		points[2] = new Point(xPos+2,yPos+4);
		points[3] = new Point(xPos-2,yPos+4);
		
		for(int i=0;i<points.length;i++){
			double x = points[i].getX();
			double y = points[i].getY();
			double currentAng = Math.atan((y-yPos)/(x-xPos))*180/Math.PI;
			if(x-xPos < 0){
				currentAng+=180;
			}
			double rad = Math.sqrt(Math.pow((x-xPos),2)+Math.pow((y-yPos),2));
			double newAng = (currentAng+angle-90)*Math.PI/180;
			double newX = rad*Math.cos(newAng);
			double newY = rad*Math.sin(newAng);
			points[i].move((int)newX+xPos, (int)newY+yPos);
		}
		Polygon body = new Polygon();
		for(int i=0;i<4;i++){
			body.addPoint((int)points[i].getX(), (int)points[i].getY());
		}
		g2.setColor(color);
		g2.fill(body);
		setShape(body);
	}
	private void initiateBody(){
		Point[] points = new Point[4];
		points[0] = new Point(xPos-2,yPos-4);
		points[1] = new Point(xPos+2,yPos-4);
		points[2] = new Point(xPos+2,yPos+4);
		points[3] = new Point(xPos-2,yPos+4);
		
		for(int i=0;i<points.length;i++){
			double x = points[i].getX();
			double y = points[i].getY();
			double currentAng = Math.atan((y-yPos)/(x-xPos))*180/Math.PI;
			if(x-xPos < 0){
				currentAng+=180;
			}
			double rad = Math.sqrt(Math.pow((x-xPos),2)+Math.pow((y-yPos),2));
			double newAng = (currentAng+angle-90)*Math.PI/180;
			double newX = rad*Math.cos(newAng);
			double newY = rad*Math.sin(newAng);
			points[i].move((int)newX+xPos, (int)newY+yPos);
		}
		Polygon body = new Polygon();
		for(int i=0;i<4;i++){
			body.addPoint((int)points[i].getX(), (int)points[i].getY());
		}
		setShape(body);
	}
}
