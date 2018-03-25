package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import controller.functions.GanttChart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import model.Batch;
import model.Orders;
import model.Timing;
import service.BatchService;
import service.OrdersService;
import service.ProductionService;
import service.TimingService;

@Controller
public class ChartController {
	@FXML
	private ComboBox<Orders> orderBox;
	@FXML
	private ScrollPane scrollPane;
	@FXML 
	private Label dateLabel;

	@Autowired
	private OrdersService ordersService;
	@Autowired
	private BatchService batchService;
	@Autowired
	private ProductionService prodService;
	@Autowired
	private TimingService timingService;
	
	private final String[] colors = { // kolory operacji na wykresie
			"#528FFF", "#65C798", "#9E71A7", "#FDEBA9", "#BC7C03", "#FF7C56", "#968BCA", "#9DDC85", "#877BAE" };
	private Map<Long, String> colorsForOperations;
	private short colorIndex = 0;
//	private final String[] dayColorsArray = { "#FFEE73", "#E4F870", "#57C081", "#AAD2DD", "#BD9FF6", "#FC8A90",
//			"#FF9B48" };// kolory dni
	
//	private List<Timing> timings;//lista timingow do ogarniecia
	
	private GanttChart gantt;
	
	@FXML
	private void initialize(){
		setOrdersList();
	}
	
	public void setOrdersList(){
		List<Orders> orders = ordersService.getInProgressOrders();
		orderBox.getSelectionModel().clearSelection();
		orderBox.getItems().clear();
		orderBox.getItems().addAll(orders);
	}
	
	public void clearPane(){
		scrollPane.setContent(null);
	}
	
	// Event Listener on ComboBox[#orderBox].onAction
	@FXML
	public void selectOrder(ActionEvent event) {
		makeChart();
	}
	
	private void makeChart(){
		this.colorsForOperations = new TreeMap<Long, String>();
		this.colorIndex = 0;
		if (orderBox.getSelectionModel().getSelectedItem() != null) {
			Orders selectedItem = orderBox.getSelectionModel().getSelectedItem();
			List<Timing> timings = new ArrayList<Timing>();
			List<Batch> batches = getBatches(selectedItem.getOrderId());
			for (Batch b : batches)
				timings.addAll(prodService.getTimingsForOrder(b));

			setColorForOperation(timings);	
			
			gantt = new GanttChart();
			gantt.setTimingService(timingService);
			gantt.setDateLabelToExpressDate(dateLabel);
			gantt.initChart(timings);
			scrollPane.setContent(gantt);
		}
	}
	
	private void setColorForOperation(List<Timing> timings) {

		for (Timing t : timings) {
			long idOperacja = t.getProduction().getOperation().getIDOperacja();
			if (colorsForOperations.containsKey(idOperacja)) {
				t.setColor(colorsForOperations.get(idOperacja));
			} else {
				colorsForOperations.put(idOperacja, colors[colorIndex]);
				t.setColor(colors[colorIndex]);
				
				if(colorIndex == colors.length-1)
					colorIndex = 0;
				else
					colorIndex++;
			}
			System.out.println(colorsForOperations);
		}
	}
	
	private double factor = 1.0;
	
	@FXML
	private void bigger(){
			factor = 1.5;			
			if(factor != 0)
				gantt.setLabFactor(factor);
	}
	
	@FXML
	private void smaller() {
		factor = 0.5;
		if (factor != 0)
			gantt.setLabFactor(factor);
	}

	private List<Batch> getBatches(int orderId){
		return batchService.getByOrder(orderId);
	}
	
	@FXML
	private void refresh(){
		makeChart();
	}
}
