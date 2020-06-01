import java.awt.image.BufferedImage;
import java.net.URL;
import java.awt.event.*;
import acm.program.*;
import acm.graphics.*;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.text.DecimalFormat;

public class RiskCalculator extends GraphicsProgram {
	public static final int APPLICATION_WIDTH = 1250;
	public static final int APPLICATION_HEIGHT = 1000;
	
	
	public void init() {
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
	}

	/** Width and height of application window in pixels */
	

	public int WORD_HEIGHT = getHeight()/2 - 100;//-100
	public int IMAGE_HEIGHT = getHeight()/2 - 80;//-80

	private GImage calcLAButton;
	private GImage calcNDButton;

	private GLabel riskNum;
	private GLabel[] riskExplains;
	private GLabel riskWarning;


	@Override
	//use for button 
	public void mousePressed(MouseEvent e) {

		DecimalFormat df = new DecimalFormat("#.000");
		RiskAssessment ra = null;

		double x = e.getX();
		double y = e.getY();
		//&& y < calcButton.getY() && y > (calcButton.getY() - calcButton.getHeight())
		if (x > calcNDButton.getX() && x < calcNDButton.getX() + calcNDButton.getWidth() &&
				y > calcNDButton.getY() && y < calcNDButton.getY() + calcNDButton.getHeight()) {

			ra = getNorthDakotaRisk();
		}

		if (x > calcLAButton.getX() && x < calcLAButton.getX() + calcLAButton.getWidth() &&
				y > calcLAButton.getY() && y < calcLAButton.getY() + calcLAButton.getHeight()) {

			ra = getLosAngelesRisk();

		}

		if (ra != null) {
			printResult(ra.getRisk(), "Estimated Risk: " + df.format(ra.getRisk()),
					"Estimated Risk Breakdown: " + df.format(ra.getRiskDueToPopulation()),
					"was caused by the population, determined by current",
					"brightness. And " + df.format(ra.getRiskDueToDeltaPopulation()) + " was caused by the",
					"change in the local population, determined",
					"from the change in brightness from the last year.");
		}


	}

	public void drawHighWarn() {

		if (riskWarning != null) {
			remove(riskWarning);
		}
		
		riskWarning = new GLabel("High Risk: Only go out if you absolutly must, wear masks, and practice social distancing",getWidth()/40,getHeight()*0.30);
		riskWarning.setFont("TimesRoman-27"); //TimesRoman-27
		riskWarning.setColor(Color.RED);
		
		add(riskWarning);


		
		
	}
	public void drawLowWarn() {

		if (riskWarning != null) {
			remove(riskWarning);
		}

		riskWarning = new GLabel("Lower Risk: If you go out, wear a mask and practice social distancing",getWidth()/24, getHeight()*0.30);
		//LWarn.setLocation((getWidth() - LWarn.getWidth())/2,getHeight()*0.25 );
		riskWarning.setFont("TimesRoman-33");
		riskWarning.setColor(Color.DARK_GRAY);
		add(riskWarning);
	}


	public void printResult(double risk, String riskStr, String... explanationLines) {
		//prints the chance on a 1 - 100 scale as a Glabel

		if (riskNum != null) {
			remove(riskNum);
		}

		if (riskExplains != null) {
			for (int i = 0; i < riskExplains.length; i++) {
				remove(riskExplains[i]);
			}
		}

		riskNum = new GLabel(riskStr,800,getHeight()/2.0 - 100);
		riskNum.setColor(Color.WHITE); 
		riskNum.setFont("TimesRoman-30");
		add(riskNum);

		riskExplains = new GLabel[explanationLines.length];

		for (int i = 0; i < explanationLines.length; i++) {

			GLabel riskExplain = new GLabel(explanationLines[i],800,getHeight()/2.0 - 45 + (i * 40));
			riskExplain.setColor(Color.WHITE);
			riskExplain.setFont("TimesRoman-16");
			add(riskExplain);

			riskExplains[i] = riskExplain;

		}

		
		if(risk > 80) {
			drawHighWarn();
		} else if (risk > 0) {
			drawLowWarn(); 
		}
	
	}
	
	
	public void drawBackground() {
		
		//setBackground(Color.orange);
		
		
		GImage background = new GImage("NasaTemplate.jpg",0,0);
		background.setSize(1250, 1000);
		add(background);
		GLabel title = new GLabel("COVID-19 Risk Calculator", getWidth() * 0.5 - 300, getHeight() * 1.0/6.0);
		title.setFont("Verdana-50"); //TimesRoman-50
		title.setColor(Color.WHITE);
		add(title);
		drawRiskScale();
		GLabel labelLA = new GLabel("Calculate Risk for Los Angeles: ", 0, getHeight() - 600);
		labelLA.setFont("TimesRoman-50");
		labelLA.setColor(Color.WHITE);
		add(labelLA);

		GLabel labelND = new GLabel("Calculate Risk for North Dakota: ", 0, getHeight() - 300);
		labelND.setFont("TimesRoman-50");
		labelND.setColor(Color.WHITE);
		add(labelND);

		drawCalcButton();
		
		
		
	}

	public void drawRiskScale() {

		GImage scale = new GImage("riskScale.png", getWidth() * 0.7,getHeight()/2.0 + 175);
		add(scale);

	}
	
	public void drawCalcButton() {
		
		calcLAButton = new GImage("calculate button.jpg", getWidth()/10, getHeight() - 580);
		calcNDButton = new GImage("calculate button.jpg", getWidth()/10, getHeight() - 280);
		add(calcLAButton);
		add(calcNDButton);

	}
	
	
	public void run () {
		addMouseListeners(); 
		drawBackground(); 
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

		double LAAverageBrightness = getLastYearAverageBrightness(LALocationRow, LALocationCol,
				LASubsetRow, LASubsetCol);

		RiskAssessment ra = getRisk(LACurrentBrightness, LAAverageBrightness);

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

		double NDAverageBrightness = getLastYearAverageBrightness(NDLocationRow, NDLocationCol,
				NDSubsetRow, NDSubsetCol);

		RiskAssessment ra = getRisk(NDCurrentBrightness, NDAverageBrightness);

		return ra;
	}

}
