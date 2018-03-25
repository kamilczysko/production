package controller.functions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import model.Timing;
import service.TimingService;

public class GanttChart extends Pane {

	int offset = 15;// odsuniecie od krawedzi
	int scale = 1;// skalowanie

	private double width;

	private Line vind, hind;

	SimpleDoubleProperty xSize = new SimpleDoubleProperty(1500), ySize = new SimpleDoubleProperty(1500);

//	private List<Timing> timingList = null;
	private Date zeroDate = null;
	private List<GanttLabel> ganttLab;
	private double mouseFactor = 1.0;
	private Label dateLabel;

	private List<Line> indicators;
	private List<Text> dates;
	private Line timeIndicatorLine;
	private Text textForTimeIndi;
	private TimingService timingService;
	
	private SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat dayFormat = new SimpleDateFormat("dd.MM.yyyy");

	
	public void initChart(List<Timing> timingList) {

		vind = new Line();
		hind = new Line();

		vind.setStrokeWidth(0.3);
		hind.setStrokeWidth(0.3);

		indicators = new ArrayList<Line>();
		dates = new ArrayList<Text>();

		this.getChildren().addAll(vind, hind);

		this.setPrefWidth(1500);
		this.setPrefHeight(1500);

		this.prefWidthProperty().isEqualTo(xSize);
		this.prefHeightProperty().isEqualTo(ySize);

		width = this.getPrefWidth();
		// sortShit(timingList);
		
		setZeroDate(findFirst(timingList));
		drawVerticalAxis();

		ganttLab = new ArrayList<GanttLabel>();

		drawOperations(timingList);

		this.setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				double sceneX = (arg0.getX()-offset) * mouseFactor;

				Date calculatedDate = calculateDate(sceneX);
				dateLabel.setText(dayFormat.format(calculatedDate) + "\n" + hourFormat.format(calculatedDate));

				drawIndicators(arg0.getX(), arg0.getY());
			}
		});

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.getButton() == MouseButton.PRIMARY)
					makeIndi(arg0.getX());
			}
		});

		timeIndicatorLine = makeTimeIndicatingLine();
		textForTimeIndi = makeTextForTimeIndicator();

		TimerTask tt = new TimerTask() {
			@Override
			public void run() {

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						long time = System.currentTimeMillis();
						double pos = (time - zeroDate.getTime())/1000;
						timeIndicatorLine.setStartX(pos*mouseFactor+offset);
						timeIndicatorLine.setEndX(pos*mouseFactor+offset);
						textForTimeIndi.setTranslateX(pos*mouseFactor+5+offset);
						textForTimeIndi.setText(hourFormat.format(new Date(time)));
						
						
					
					}
				});
			}
		};
		t = new Timer();
		t.scheduleAtFixedRate(tt, 0, 30000);// co 0.5 minute
	}
	

	private Timer t;
	
	public void closeThread(){
		if(t != null)
			t.cancel();
	}

	private Line makeTimeIndicatingLine() {
		Line l = makeLine(50, 0, 50, this.getPrefHeight(), 1);
		l.getStyleClass().add("actualTimeCursor");
		this.getChildren().add(l);
		return l;
	}
	
	private Text makeTextForTimeIndicator(){
		Text t = new Text();
		t.setTranslateY(10);
		this.getChildren().add(t);
		return t;
	}

	private void makeIndi(double xPos) {
		Line line = makeLine(xPos, 0, xPos, getPrefHeight(), 0.1);
		line.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.getButton() == MouseButton.SECONDARY)
					getChildren().remove(line);
				int indexOfLine = indicators.indexOf(line);
				getChildren().remove(dates.get(indexOfLine));
				getChildren().remove(line);
				dates.remove(indexOfLine);
				indicators.remove(indexOfLine);
			}
		});
		Text t = new Text();
		t.setText(hourFormat.format(calculateDate((xPos-offset) * mouseFactor)));
		t.setTranslateX(xPos + 5);
		t.setTranslateY(75);

		t.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.getButton() == MouseButton.SECONDARY)
					getChildren().remove(t);
				int indexOfDate = dates.indexOf(t);
				getChildren().remove(indicators.get(indexOfDate));
				getChildren().remove(t);
				dates.remove(indexOfDate);
				indicators.remove(indexOfDate);
			}
		});

		indicators.add(line);
		dates.add(t);

		this.getChildren().add(line);
		this.getChildren().add(t);
	}

	private void refreshIndies(double factor) {
		ListIterator<Line> lin = indicators.listIterator();

		while (lin.hasNext()) {
			Line next = lin.next();
			if (this.getChildren().contains(next)) {
				next.setStartX(next.getStartX() * factor);
				next.setEndX(next.getEndX() * factor);
			}
		}

		ListIterator<Text> dat = dates.listIterator();

		while (dat.hasNext()) {
			Text d = dat.next();
			if (this.getChildren().contains(d)) {
				d.setTranslateX(d.getTranslateX() * factor);
			}
		}
	}

	private Date calculateDate(double xPos) {
		long calculated = (long) (zeroDate.getTime() + xPos*1000);

		Date dat = new Date(calculated);

		return dat;
	}

	private List<GanttLabel> labList = null;

	private void drawIndicators(double x, double y) {
		hind.setStartX(0);
		hind.setEndX(this.getPrefWidth());
		hind.setStartY(y);
		hind.setEndY(y);

		vind.setStartX(x);
		vind.setEndX(x);
		vind.setStartY(0);
		vind.setEndY(this.getPrefHeight());
	}

	private void drawOperations(List<Timing> timingList) {
		double posY = 120;
		double posX = 500;
		labList = new ArrayList<GanttLabel>();
		sortShit(timingList);
//		Timing prevTiming = null;
		for (Timing t : timingList) {
			t.getProduction().getOperation();
			GanttLabel lab = new GanttLabel();
			lab.setTimingService(timingService);
			lab.setParams(t, zeroDate.getTime() / 1000, scale, offset);
			ganttLab.add(lab);
			labList.add(lab);
			lab.setPosY(posY);
			posY += 50;

			this.getChildren().add(lab);

			if (posX < lab.getTranslateX())
				posX = lab.getTranslateX() + lab.getPrefWidth() + 100;

			this.setPrefHeight(posY);
			double  w = lab.getTranslateX()+lab.getPrefWidth();
			if(width < w)
				width = w;
	
			this.setPrefWidth(width);
//			prevTiming = t;
		}

		drawHorizontalLine(this.getPrefWidth());
		timeLine.setEndX(this.getPrefWidth());
	}
	
	private void refreshChart(){
		this.getChildren().removeAll(ganttLab);
		
	}

	public void setLabFactor(double factor) {
		mouseFactor /= factor;
		refreshIndies(factor);
		timeIndicatorLine.setStartX(timeIndicatorLine.getStartX() * factor);
		timeIndicatorLine.setEndX(timeIndicatorLine.getEndX() * factor);
		textForTimeIndi.setTranslateX(timeIndicatorLine.getStartX()+5);
		if (ganttLab != null && !ganttLab.isEmpty())
			for (GanttLabel lab : ganttLab) {
				lab.changeFactor(factor);
				double sizeX = lab.getTranslateX() + lab.getPrefWidth();
				this.getChildren().remove(timeLine);
				drawHorizontalLine(sizeX);

				if (this.getPrefWidth() < sizeX)
					this.setPrefWidth(sizeX + 100);
				double sizeY = lab.getTranslateY() + lab.getPrefHeight();
				if (this.getPrefHeight() < sizeY)
					this.setPrefHeight(sizeY + 50);
			}
	}
	
	public void setDateLabelToExpressDate(Label label) {
		this.dateLabel = label;
	}

	private void setZeroDate(Date first) {
		zeroDate = first;
	}

	private void sortShit(List<Timing> list) {
		Collections.sort(list, new Comparator<Timing>() {

			@Override
			public int compare(Timing arg0, Timing arg1) {
				return (int) (arg0.getStart().after(arg1.getStart()) ? arg1.getId() : arg0.getId());
			}
		});

	}

	private Date findFirst(List<Timing> timingList) {
		Date tmp = timingList.get(0).getStart();
//		System.out.println("koczo"+ timingList.get(0).getStart());
		for (Timing t : timingList) {
			if (tmp.after(t.getStart()))
				tmp = t.getStart();

		}
//		System.out.println(tmp+" poczatek");
		return tmp;
	}

	private void drawVerticalAxis() {
		this.getChildren().add(makeLine(offset, 0, offset, this.getPrefHeight(), 0.3));
	}

	private Line timeLine = null;

	private void drawHorizontalLine(double xRange) {
		timeLine = makeLine(offset, 100, xRange, 100, 2);
		this.getChildren().add(timeLine);
	}

	private Line makeLine(double x1, double y1, double x2, double y2, double stroke) {
		Line l = new Line();
		l.setStartX(x1);
		l.setStartY(y1);
		l.setEndX(x2);
		l.setEndY(y2);
		l.setStrokeWidth(stroke);
		return l;
	}

	public void setTimingService(TimingService timingService) {
		this.timingService = timingService;
		
	}

}
