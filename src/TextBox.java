

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class TextBox extends Sprite{
	private String text;
	private int width;
	private int height;
	private Color backColor;
	private boolean drawBackground = true;
	private Color textColor;
	
	protected TextBox(String words,int x, int y,Color bC,Color tC){
		text = words;
		backColor = bC;
		textColor = tC;
		xPos = x;
		yPos = y;
	}
	protected TextBox(String words,int x, int y,Color tC){
		text = words;
		drawBackground = false;
		textColor = tC;
		xPos = x;
		yPos = y;
	}
	
	protected void setText(String newText){
		text = newText;
	}
	protected void draw(Graphics gvar){
		Graphics2D g2 = (Graphics2D) gvar;
		
		Font textFont = new Font("DialogueInput",Font.BOLD,40);
		g2.setFont(textFont);
		
		FontMetrics font = g2.getFontMetrics();
		int stringWidth = font.stringWidth(text);
		width = stringWidth + 40;
		height = font.getHeight()+10;
		
		Rectangle background = new Rectangle((int)(xPos-width/2),(int)(yPos-height/2),width,height);
		setShape(background);
		if(drawBackground){
			g2.setColor(backColor);
			g2.fill(background);
			g2.setColor(textColor);
			g2.draw(background);
		}
		g2.setColor(textColor);
		g2.drawString(text,(int)(xPos-width/2)+20,(int)(yPos+height/2)-20);
	}
}
