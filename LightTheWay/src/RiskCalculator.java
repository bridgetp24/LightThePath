import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.Buffer;
import java.util.Scanner;
import java.io.Console;
import java.awt.event.*;
import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;

import static org.imgscalr.Scalr.OP_GRAYSCALE;

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
		getLosAngelesRisk();
		getNorthDakotaRisk();
	}

	// image processing

	// get image from url
	private static final String EPSG = "4326"; // this means lat/lon geographic
	private static final String PRODUCT_NAME = "VIIRS_SNPP_DayNightBand_ENCC";
	// Nighttime Imagery (Day/Night Band, Enhanced Near Constant Contrast)

	private static final String TILE_MATRIX_SET = "GoogleMapsCompatible_Level6";
	private static final String TILE_MATRIX = "6"; // 7 is more precise but it causes werid tearing
	private static final String ZOOM_LEVEL = "500m"; // highest resolution, 500m per pixel

	private static final String MOST_RECENT_VALID_TIME = "2020-02-01";
	private static final int GRID_SIZE = 3; //split each image into a 3x3 grid because the images cover quite a large ground area

	public BufferedImage getPhoto(String date, int row, int col) {
		String baseUrl = "https://gibs.earthdata.nasa.gov/wmts/epsg" + EPSG +
				"/best/wmts.cgi?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=" + PRODUCT_NAME +
				"&STYLE=&TILEMATRIXSET=" + ZOOM_LEVEL + "&TILEMATRIX=" + TILE_MATRIX + "&TILEROW=" + row +
				"&TILECOL=" + col + "&FORMAT=image%2Fpng&TIME=" + date;
		try {
			URL url = new URL(baseUrl);
			BufferedImage image = ImageIO.read(url);
			return image;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public BufferedImage cropPhoto(BufferedImage image, int subrow, int subcol) {

		int tileW = image.getWidth() / GRID_SIZE;
		int tileH = image.getHeight() / GRID_SIZE;

		int x = subrow * tileW;
		int y = subcol * tileH;

		return Scalr.crop(image, x, y, tileW, tileH);
	}

	public double getAverageBrightness(BufferedImage image) {
		int sumBrightness = 0;
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				int rgb = image.getRGB(i, j);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb & 0xFF);
				int gray = (r + g + b) / 3;
				sumBrightness += gray;
			}
		}

		return sumBrightness / (double) (image.getWidth() * image.getHeight());
	}

	public RiskAssessment getRisk(double currentBrightness, double averageBrightness) {
		// todo maybe add y intercept
		double deltaBrightness = currentBrightness - averageBrightness;
		double risk = currentBrightness + deltaBrightness;
		return new RiskAssessment(
				risk,
				currentBrightness,
				deltaBrightness
			);
	}


	public double getLastYearAverageBrightness(int row, int col, int subrow, int subcol) {
		double sumBrightness = 0;
		for (int i = 1; i <= 12; i++) {
			String month = String.format("%02d", i);
			String date = "2019-" + month + "-01";
			System.out.println("Getting brightness from: " + date);
			BufferedImage imag = getPhoto(date, row, col);
			imag = cropPhoto(imag, subrow, subcol);
			sumBrightness += getAverageBrightness(imag);
		}

		return sumBrightness / 12.0;
	}

	// unfortunately, have to hard code since the GIBS api doesn't provide an easy way to get a specify location / identify a location
	public RiskAssessment getLosAngelesRisk() {
		int LALocationRow = 11;
		int LALocationCol = 13;

		int LASubsetRow = 1;
		int LASubsetCol = 2;

		BufferedImage image = getPhoto(MOST_RECENT_VALID_TIME, LALocationRow, LALocationCol);
		image = cropPhoto(image, LASubsetRow, LASubsetCol);

		double LACurrentBrightness = getAverageBrightness(image);
		System.out.println("Current Brightness: " + LACurrentBrightness);

		double LAAverageBrightness = getLastYearAverageBrightness(LALocationRow, LALocationCol,
				LASubsetRow, LASubsetCol);

		print("Average Brightness Last Year: " + LAAverageBrightness);

		RiskAssessment ra = getRisk(LACurrentBrightness, LAAverageBrightness);
		System.out.println("LA Estimated Risk: " + ra.getRisk());
		System.out.println("LA Estimated Risk Breakdown: " + ra.getRiskDueToPopulation() + " was caused by the population," +
				" determined by current brightness. And " + ra.getRiskDueToDeltaPopulation() +
				" was caused by the change in the local population, determined from the change in brightness from the last year.");

		return ra;
	}

	public RiskAssessment getNorthDakotaRisk() {
		int NDLocationRow = 9;
		int NDLocationCol = 16;

		int NDSubsetRow = 1;
		int NDSubsetCol = 0;

		BufferedImage image = getPhoto(MOST_RECENT_VALID_TIME, NDLocationRow, NDLocationCol);
		image = cropPhoto(image, NDSubsetRow, NDSubsetCol);

		double NDCurrentBrightness = getAverageBrightness(image);
		System.out.println("Current Brightness: " + NDCurrentBrightness);

		double NDAverageBrightness = getLastYearAverageBrightness(NDLocationRow, NDLocationCol,
				NDSubsetRow, NDSubsetCol);

		print("Average Brightness Last Year: " + NDAverageBrightness);

		RiskAssessment ra = getRisk(NDCurrentBrightness, NDAverageBrightness);
		System.out.println("North Dekota Estimated Risk: " + ra.getRisk());
		System.out.println("ND Estimated Risk Breakdown: " + ra.getRiskDueToPopulation() + " was caused by the population," +
				" determined by current brightness. And " + ra.getRiskDueToDeltaPopulation() +
				" was caused by the change in the local population, determined from the change in brightness from the last year.");

		return ra;
	}

}
