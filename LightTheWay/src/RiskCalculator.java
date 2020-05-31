import java.util.Scanner;
import java.io.Console;
import java.awt.event.*;
import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.*;

public class RiskCalculator extends GraphicsProgram {
	public static final int APPLICATION_WIDTH = 1250;
	public static final int APPLICATION_HEIGHT = 1000;
	
	
	public void init() {
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
	}

	/** Width and height of application window in pixels */
	
	public int risk; 
	public String address; 
	public int chance; 
	public int WORD_HEIGHT = getHeight()/2 - 100;//-100
	public int IMAGE_HEIGHT = getHeight()/2 - 80;//-80
	GImage calcButton; 
	


	@Override
	//use for button 
	public void mousePressed(MouseEvent e) {

		double x = e.getX();
		double y = e.getY();
		//&& y < calcButton.getY() && y > (calcButton.getY() - calcButton.getHeight())
		if (x > calcButton.getX() && x < calcButton.getX() + calcButton.getWidth() ){ 
			
			printChance(); 
		}

	}
	

	
	public void getAddress() {
	
		Scanner sc = new Scanner(System.in);
		address = sc.nextLine(); 
		println("Your address is "+ address);
		
		
	}
	
	public void drawHighWarn() {
		
		
		GLabel HWarn = new GLabel("High Risk: Only go out if you absolutly must, wear masks, and practice social distancing",getWidth()/40,getHeight()*0.30);
		HWarn.setFont("TimesRoman-27"); //TimesRoman-27
		HWarn.setColor(Color.RED);
		
		add(HWarn); 
		
		
	}
	public void drawLowWarn() {

		GLabel LWarn = new GLabel("Lower Risk: If you go out, wear a mask and practice social distancing",getWidth()/24, getHeight()*0.30);
		//LWarn.setLocation((getWidth() - LWarn.getWidth())/2,getHeight()*0.25 );
		LWarn.setFont("TimesRoman-33"); 
		LWarn.setColor(Color.DARK_GRAY);
		add(LWarn);
	}
	
	public void calculateChance(String address) {
		
		int lightfactor = 50; 
		int changeInLightFactor = 50; 
		int externalFactor = 50; 
		
		/*
		 * Right now, this is a random number but this method would be filled with a connection
		 * to an algorithm that would calculate relative risk using light movement data, data from airlines, 
		 * and other factors. This method would input the user's address as a parameter and connect to 
		 * a map API like google maps. 
		 * double index = lightFactor + changeInLightFactor + externalFactor;
		 */
		chance = (int)((Math.random() * (101)));
		
		 
	}
	
	
	public void printChance() {
		//prints the chance on a 1 - 100 scale as a Glabel
		GLabel riskNum = new GLabel("" + chance ,getWidth()*0.6 + 200,getHeight()/2 - 100); 
		riskNum.setColor(Color.WHITE); 
		riskNum.setFont("TimesRoman-50"); 
		add(riskNum); 
		
		if(chance > 60) {
			drawHighWarn();
		} else {
			drawLowWarn(); 
		}
	
	}
	
	
	public void drawBackground() {
		
		//setBackground(Color.orange);
		
		
		GImage background = new GImage("NasaTemplate.jpg",0,0);
		background.setSize(1250, 1000);
		add(background);
		GLabel title = new GLabel("COVID-19 Risk Calculator", getWidth() * 0.5 - 300, getHeight() * 1/6);
		title.setFont("Verdana-50"); //TimesRoman-50
		title.setColor(Color.WHITE);
		add(title);
		drawRiskScale();
		GLabel label = new GLabel("Address: ", getWidth()/10, getHeight()/2 - 100);
		label.setFont("TimesRoman-50");
		label.setColor(Color.WHITE);
		add(label);
		drawInputBox(); 
		drawCalcButton();
		
		
		
	}
	
	public void drawInputBox() {
		
		/**
		 * this is a placeholder for an actual user input mechanism 
		 */
		
		GImage box = new GImage("inputbox.png",getWidth()/10,getHeight()/2 - 80);
		add(box);
	}
	public void drawRiskScale() {
		
		GLabel risk = new GLabel("Risk:",getWidth()*0.6,getHeight()/2 - 100); 
		risk.setColor(Color.WHITE); 
		risk.setFont("TimesRoman-50"); 
		add(risk); 
		
		GImage scale = new GImage("riskScale.png", getWidth() * 0.6,getHeight()/2 - 80);
		add(scale);

	}
	
	public void drawCalcButton() {
		
		calcButton = new GImage("calculate button.jpg", getWidth()/10, getHeight()* 0.75); 
		add(calcButton); 
		
	}
	
	
	public void run () {
		addMouseListeners(); 
		drawBackground(); 
		calculateChance("enter address here"); 

		 	
		
	}
}
