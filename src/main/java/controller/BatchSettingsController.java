package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Batch;
import model.Operation;
import model.Station;
import model.StationType;
import service.StationService;

@Controller
public class BatchSettingsController {
	@FXML
	private ListView<Batch> batchList;
	@FXML
	private Button addButton;
	@FXML
	private Button removeButton;
	@FXML
	private ComboBox<Station> stationBox;
	@FXML
	private ListView<Batch> stationList;
	
	private SimpleBooleanProperty listIsEmpty = new SimpleBooleanProperty(false);

	@Autowired
	private StationService stationService;
	
	private Operation operation;
	private MultiValueMap<Long, Batch> mapWithBatchesAddedToStations;
	
	@FXML 
	private void initialize(){
		addButton.setDisable(true);
		removeButton.setDisable(true);
		batchList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		stationList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		stationList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Batch>() {

			@Override
			public void changed(ObservableValue<? extends Batch> arg0, Batch arg1, Batch arg2) {
								
				if(stationList.getSelectionModel().getSelectedItems().size() > 0)
					removeButton.setDisable(false);
				else
					removeButton.setDisable(true);
				
			}
		});
		
		batchList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Batch>() {

			@Override
			public void changed(ObservableValue<? extends Batch> arg0, Batch arg1, Batch arg2) {
				if(batchList.getSelectionModel().getSelectedItems().size() > 0)
					addButton.setDisable(false);
				else
					addButton.setDisable(true);
				
			}
		});
		
//		batchList.getItems().addListener(new ListChangeListener<Batch>(){
//
//			@Override
//			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Batch> arg0) {
//				if(arg0.getList().size() == 0)
//					actualItem.setGraphic(setSingleOperationGraphic(true));
//				else
//					actualItem.setGraphic(setSingleOperationGraphic(false));
//					
//				System.out.println(arg0.getList().size());
//				
//			}});
		
	}
//	
	private Node setSingleOperationGraphic(boolean done){
		if(!done)
			return new ImageView(new Image(getClass().getResourceAsStream("/icons/operationWrong.png")));
		return new ImageView(new Image(getClass().getResourceAsStream("/icons/operationRight.png")));
	}
	
	public void resetLists(){
		batchList.getItems().clear();
		stationList.getItems().clear();
	}
		
	Map<String, List<Batch>> batchMap = new TreeMap<String, List<Batch>>();
	
	private List<Batch> wholeBatchList;
		
	public void setBatchList(List<Batch> list){
		
		batchList.getItems().clear();
		wholeBatchList = new ArrayList<Batch>();
		for(Batch b : list)
			if(!hasElement(b)){
				wholeBatchList.add(b);
			}
		batchList.getItems().addAll(wholeBatchList);
		stationBox.getSelectionModel().selectFirst();
	}
	
	private boolean hasElement(Batch b){
		Set<Long> key = mapWithBatchesAddedToStations.keySet();
		for(Long k : key)
			if(mapWithBatchesAddedToStations.get(k).contains(b))
				return true;
		
		return false;
	}
	
	private void initStationList(StationType st){
		List<Station> byType = stationService.getByType(st);
		stationBox.getItems().clear();
		stationBox.getItems().addAll(byType);
	}
	
	public void setOperationData(Operation operation){
		this.operation = operation;
		initStationList(operation.getStationType());
		this.mapWithBatchesAddedToStations = operation.getMap();
	}
	
	private Station selectedStation;
	
	@FXML
	private void selectStation(){
		this.selectedStation = stationBox.getSelectionModel().getSelectedItem();
//		System.out.println(selectedStation+" - stanowisko");
		if(selectedStation != null)
			setStatList();			
	}
	
	private void setStatList(){
		stationList.getItems().clear();
		List<Batch> objects = mapWithBatchesAddedToStations.get(selectedStation.getStationId());
		if(objects != null){
			List<Batch> list = objects;
			for(Batch b : list){
				stationList.getItems().add(b);
			}
		}
	}
	
	// Event Listener on MenuItem.onAction
	@FXML
	public void selectAllInBatchList(ActionEvent event) {
		batchList.getSelectionModel().selectAll();
	}
	// Event Listener on Button[#addButton].onAction
	@FXML
	public void addBatches(ActionEvent event) {
//		Station station = stationBox.getSelectionModel().getSelectedItem();
		ObservableList<Batch> selectedItems = batchList.getSelectionModel().getSelectedItems();
		long key = selectedStation.getStationId();
		for(Batch b : selectedItems)
			mapWithBatchesAddedToStations.add(key, b);
		
		this.operation.setMap(mapWithBatchesAddedToStations);
		setStatList();
		batchList.getItems().removeAll(selectedItems);
		
		if(batchList.getItems().isEmpty()){
			this.actualItem.setGraphic(setSingleOperationGraphic(true));
			this.operation.setReady(true);
		}
	}
	
	// Event Listener on Button[#removeButton].onAction
	@FXML
	public void removeBatches(ActionEvent event) {
		List<Batch> selectedItems = stationList.getSelectionModel().getSelectedItems();
		long key = selectedStation.getStationId();
		for(Batch b : selectedItems)
			mapWithBatchesAddedToStations.get(key).remove(b);
		
		batchList.getItems().addAll(selectedItems);
		stationList.getItems().removeAll(selectedItems);
		this.operation.setMap(mapWithBatchesAddedToStations);

		this.operation.setReady(false);
		this.actualItem.setGraphic(setSingleOperationGraphic(false));
		
	}
	
	// Event Listener on MenuItem.onAction
	@FXML
	public void selectAllInStationList(ActionEvent event) {
		stationList.getSelectionModel().selectAll();
	}
	
	public boolean isReady(){
		return batchList.getItems().isEmpty();
	}

	private TreeItem actualItem;
	public void setItem(TreeItem item) {
		this.actualItem = item;
		
	}
}
