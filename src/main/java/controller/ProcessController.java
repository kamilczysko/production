package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import controller.functions.ShortTableCell;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import model.Batch;
import model.Operation;
import model.Process;
import service.BatchService;
import service.OperationService;
import service.ProcessService;

@Controller
public class ProcessController {
	@FXML
	private TableView<Batch> partTable;
	@FXML
	private TableColumn<Batch, String> lpColumn;
	@FXML
	private TableColumn<Batch, String> groupColumn;
	@FXML
	private TableColumn<Batch, String> idColumn;
	@FXML
	private TableColumn<Batch, Short> amountColumn;
	@FXML
	private TableColumn<Batch, String> descriptionColumn;
	@FXML
	private MenuItem printMenu;
	@FXML
	private MenuItem selectAllMenu;
	@FXML
	private MenuItem undoTableMenuItem;
	@FXML
	private MenuItem joinDownMenuItem;
	@FXML
	private ListView<Process> procList;
	@FXML
	private ContextMenu contextMenu;
	@FXML
	private MenuItem procMenuItem;
	@FXML
	private MenuItem editMenuItem;
	@FXML
	private MenuItem deleteMenuItem;
	@FXML
	private TextArea procDescArea;
	
	@Autowired
	private OrdersController ordersController;
	@Autowired
	private ProcessService procService;
	@Autowired
	private OperationService operationService;
	@Autowired
	private BatchService batchService;
//	@Autowired
//	private ProcDefController procDefController;
	
	private List<Batch> batches = new ArrayList<Batch>();
	
	@Autowired
	private OperationsController operationsController;
	
	private int batchSize;
	
	public Map<String, List<Batch>> getBatches(){
		Map <String, List<Batch>> map = new TreeMap<String, List<Batch>>();
		map.put("a", new ArrayList<Batch>());
		map.put("b", new ArrayList<Batch>());
		map.put("c", new ArrayList<Batch>());
		map.put("d", new ArrayList<Batch>());
		for(Batch b : batches)
			if(map.containsKey(b.getGrupa()))
				map.get(b.getGrupa()).add(b);
			
		return map;
	}
	
	public void clearTable(){
		partTable.getItems().clear();
	}
	
	private final int ORDER_IN_PROGRESS = 1;
	private boolean loadMode = false;
	public void setProcessOnList(){
		int selectedArt = ordersController.article();
		procList.getItems().clear();
		List<Process> list;
		if(ordersController.getOrder().getOrderInProgress() == ORDER_IN_PROGRESS){
			int orderId = ordersController.getOrder().getOrderId();
			list = procService.getProcessForOrder(orderId);
			this.batches = batchService.getByOrder(orderId);
			loadMode = true;
		}
		else{
			list = procService.getByArt(selectedArt);
			loadMode = false;
		}
		procList.getItems().addAll(list);
	}
	
	@FXML
	private void initialize() {
		listSettings();
		tableSettings();
	}

	
	private void listSettings(){
		procList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Process>() {

			@Override
			public void changed(ObservableValue<? extends Process> arg0, Process arg1, Process arg2) {
				Process proc = arg0.getValue();
				if (proc != null) {
					setProcessParamsOnWindow(arg0.getValue());
					batchSize = arg0.getValue().getBatch();
					List<Operation> operationForProcess = operationService.getOperationForProcess(proc);
					operationsController.loadProcessFromBase(operationForProcess, proc);
					groups = operationsController.groups;
					if(!loadMode)
						generateBatches(ordersController.amount(), batchSize);
					else{
						partTable.getItems().clear();
						partTable.getItems().addAll(batches);

					}
						
					//TODO
				}
			}
		});
	}
	
	private void tableSettings() {
		partTable.isEditable();

		idColumn.setCellValueFactory(new PropertyValueFactory<Batch, String>("Zlecenie"));
		idColumn.setVisible(true);

		amountColumn.setCellValueFactory(new PropertyValueFactory<Batch, Short>("Ilosc"));
		amountColumn.setEditable(true);

		groupColumn.setCellValueFactory(new PropertyValueFactory<Batch, String>("Grupa"));//

		lpColumn.setCellValueFactory(new PropertyValueFactory<Batch, String>("NrPartii"));

		partTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		partTable.getSelectionModel().cellSelectionEnabledProperty();
		partTable.setEditable(true);

		amountColumn.setEditable(true);
		amountColumn.setCellValueFactory(new PropertyValueFactory<Batch, Short>("Ilosc"));
		amountColumn.setCellFactory(cal -> new ShortTableCell());
		amountColumn.setOnEditCommit(new EventHandler<CellEditEvent<Batch, Short>>() {
			@Override
			public void handle(CellEditEvent<Batch, Short> t) {
				short newValue = t.getNewValue();
				System.out.println(newValue);
				int index = partTable.getSelectionModel().getSelectedIndex();
				if(index == 0){
//					System.out.println("ganz pierwszy element");
					generateBatches(ordersController.amount(), newValue);
				}
				else{
					String grupa = partTable.getSelectionModel().getSelectedItem().getGrupa();
					if(!partTable.getItems().get(--index).getGrupa().equals(grupa)){
//						System.out.println("pierwszy zdrugiej grupy");
						generateBatchesForNextGroup(ordersController.amount(), newValue, grupa.charAt(0));
					}		
				}
				
				if(newValue == 0){
					Batch b = partTable.getSelectionModel().getSelectedItem();
					batches.remove(b);
					partTable.getItems().clear();
					partTable.getItems().addAll(batches);
				}
			}
		});

		descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		descriptionColumn.setEditable(true);
		descriptionColumn.setOnEditCommit(new EventHandler<CellEditEvent<Batch, String>>() {
			@Override
			public void handle(CellEditEvent<Batch, String> t) {
				((Batch) t.getTableView().getItems().get(t.getTablePosition().getRow())).setOpis(t.getNewValue());
			}
		});
	}
	
	private void generateBatchesForNextGroup(int amount, int batchSize, char group) {

		int normalBatches = (int) amount / batchSize;
		int lastBatch = (int) amount % batchSize;
		deleteGroupFromTable(group+"");
		int batchNum = getLastIndexFromTable()+1;
		for (int i = 0; i < normalBatches; i++) {
			setBatch(batchSize, group, batchNum);
			batchNum += 1;
		}
		if (lastBatch > 0) {
			setBatch(lastBatch, group, batchNum);
			batchNum += 1;
		}
		partTable.getItems().clear();
		partTable.getItems().addAll(batches);

	}
	
	private void deleteGroupFromTable(String group){
		List<Batch> toRemove = new ArrayList<Batch>();
		for(Batch b : batches)
			if(b.getGrupa().equals(group))
				toRemove.add(b);
			
		batches.removeAll(toRemove);
		System.out.println(batches.size()+" - wymiar tablicy");
	}
	
	private int getLastIndexFromTable(){
		int size = partTable.getItems().size() - 1;
		return size;
	}
	
	
	private void generateBatches(int amount, int batchSize){//generowanie partii
			
		batches.clear();
		int normalBatches = (int)amount/batchSize;
		int lastBatch  = (int)amount%batchSize;
		int batchNum = 0;
		char group = 'a';
		for(int j = 0; j < groups; j ++){
			for(int i = 0; i < normalBatches; i++){
				setBatch(batchSize, group, batchNum);
				batchNum += 1;
			}
			if(lastBatch > 0){
//				System.out.println("last batch: "+ lastBatch);
				setBatch(lastBatch, group, batchNum);
				batchNum += 1;
			}
			group += 1;
		}
		partTable.getItems().clear();
		partTable.getItems().addAll(batches);

	}
	
	
	
	private void setBatch(int amount, char group, int batchNum){
		Batch b = new Batch();
		b.setIlosc((short) amount);
		b.setZlecenie(ordersController.order());
		b.setGrupa(group+"");
		b.setNrPartii(batchNum);
		batches.add(b);
	}
	
	
	private int groups = -1;//liczba grup z procesu - do tworznia partii
	
	private void setProcessParamsOnWindow(Process proc){
		if(procList.getSelectionModel().getSelectedItem() != null){
			String info = proc.getName()+"\n ------------------------- \n partia: "+proc.getBatch()+""
					+ "\n \n"+proc.getDescription();
			procDescArea.setText(info);
			
//			List<Operation> operationForProcess = operationService.getOperationForProcess(proc);
//			this.groups = procDefController.groups;
//			System.out.println("grupy: "+groups);
		}
	}
	
	// Event Listener on MenuItem[#printMenu].onAction
	@FXML
	public void print(ActionEvent event) {
	}
	// Event Listener on MenuItem[#selectAllMenu].onAction
	@FXML
	public void selectAll(ActionEvent event) {
		partTable.getSelectionModel().selectAll();
	}
	// Event Listener on MenuItem[#undoTableMenuItem].onAction
	@FXML
	public void undoPortions(ActionEvent event) {
		generateBatches(ordersController.amount(), batchSize);
	}
	// Event Listener on MenuItem[#joinDownMenuItem].onAction
	@FXML
	public void joinDown(ActionEvent event) {
		joinDown();
	}
	private  void joinDown(){
		Batch selectedItem = partTable.getSelectionModel().getSelectedItem();
		int indexOf = partTable.getItems().indexOf(selectedItem);
		String grupa = selectedItem.getGrupa();
		
		indexOf++;
		Batch actualItem = selectedItem;
		
		while (actualItem.getGrupa().equals(grupa) && indexOf <= partTable.getItems().size() - 1) {
			actualItem = partTable.getItems().get(indexOf);
			if (actualItem.getGrupa().equals(grupa)) {
				selectedItem.setIlosc((short) (actualItem.getIlosc() + selectedItem.getIlosc()));
				batches.remove(actualItem);
			}
			indexOf++;
		}
		
		partTable.getItems().clear();
		partTable.getItems().addAll(batches);
		
	}
	@FXML
	public void listEvent(MouseEvent event) {
	}
	@FXML
	public void addProces(ActionEvent event) {
	}
	@FXML
	public void editEvent(ActionEvent event) {
	}


	@FXML
	public void deleteEvent(ActionEvent event) {
		
	}
}
