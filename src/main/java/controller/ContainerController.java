package controller;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import controller.functions.ColorTableCell;
import controller.functions.print.PrintArtBarCode;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import model.Container;
import service.ContainerService;

@Controller
public class ContainerController {

	@FXML
	private TableColumn<Container, Date> deleteDateColumn;

	@FXML
	private TableColumn<Container, Short> signColumn;

	@FXML
	private TableColumn<Container, Color> colorColumn;

	@FXML
	private TableView<Container> table;

	@FXML
	private TableColumn<Container, Short> idColumn;

	@FXML
	private TableColumn<Container, Date> createDateColumn;
	
	  @FXML
	    private TableColumn<Container, Short> deletedMark;

	    @FXML
	    private TableView<Container> tableWithDeleted;

	    @FXML
	    private TableColumn<Container, Date> deletedCreateDate;

	    @FXML
	    private TableColumn<Container, Date> deletedDeleteDate;

	    @FXML
	    private TableColumn<Container, Short> deletedId;

	    @FXML
	    private TableColumn<Container, Color> deletedColor;

	@Autowired
	private ContainerService containerService;

	@FXML
	private void initialize() {

		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		deleteDateColumn.setCellValueFactory(new PropertyValueFactory<Container, Date>("dataKasacji"));
		createDateColumn.setCellValueFactory(new PropertyValueFactory<Container, Date>("dataUtworzenia"));
		colorColumn.setCellValueFactory(new PropertyValueFactory<Container, Color>("color"));
		colorColumn.setCellFactory(ColorTableCell<Container>::new);
		signColumn.setCellValueFactory(new PropertyValueFactory<Container, Short>("oznaczenie"));
		idColumn.setCellValueFactory(new PropertyValueFactory<Container, Short>("id"));

		
		tableWithDeleted.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		deletedDeleteDate.setCellValueFactory(new PropertyValueFactory<Container, Date>("dataKasacji"));
		deletedCreateDate.setCellValueFactory(new PropertyValueFactory<Container, Date>("dataUtworzenia"));
		deletedColor.setCellValueFactory(new PropertyValueFactory<Container, Color>("color"));
//		deletedColor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Container,Color>, ObservableValue<Color>>() {
//
//			@Override
//			public ObservableValue<Color> call(CellDataFeatures<Container, Color> arg0) {
//				getce
//				return null;
//			}
//		});
		deletedMark.setCellValueFactory(new PropertyValueFactory<Container, Short>("oznaczenie"));
		deletedId.setCellValueFactory(new PropertyValueFactory<Container, Short>("id"));
		
		fillTableWithContainers();
	}

	private List<Container> getRecords() {
		return containerService.getAllContainers();
	}

	private void fillTableWithContainers() {
		table.getItems().clear();
		table.getItems().addAll(getRecords());
		
		tableWithDeleted.getItems().clear();
		tableWithDeleted.getItems().addAll(getDeletedContainers());
	}
	
	private List<Container> getDeletedContainers(){
		return containerService.getAllDeletedContainers();
	}

	// Event Listener on Button.onAction
	@FXML
	public void deleteContainer(ActionEvent event) {
		ObservableList<Container> selectedItems = table.getSelectionModel().getSelectedItems();
		setRemoved(selectedItems);
		// containerService.remove(selectedItems);
		// table.getItems().removeAll(selectedItems);
	}

	private void setRemoved(List<Container> containerList) {
		for (Container c : containerList)
			c.setDataKasacji(new Date(System.currentTimeMillis()));
		containerService.save(containerList);
		fillTableWithContainers();
	}

	// Event Listener on Button.onAction
	@FXML
	public void saveContainser(ActionEvent event) {
		ObservableList<Container> items = table.getItems();
		containerService.save(items);
		fillTableWithContainers();
	}

	private Container makeNewContainer() {
		Container container = new Container();
		short markForContainer = getLastGoodMark();
		System.out.println("mark: " + markForContainer);
		markForContainer++;
		container.setOznaczenie(markForContainer);

		return container;
	}

	private short getLastGoodMark() {
		ObservableList<Container> tableItems = table.getItems();
		ListIterator<Container> tableItemsIterator = tableItems.listIterator();

		Container previous = null;
		while (tableItemsIterator.hasNext()) {
			Container actual = tableItemsIterator.next();
			if (previous != null) {
				if ((actual.getOznaczenie() - previous.getOznaczenie()) > 1)
					return previous.getOznaczenie();
			} else if (actual.getOznaczenie() > 1)
				return 0;

			previous = actual;
		}
		if (previous != null)
			return previous.getOznaczenie();
		else
			return 0;
	}

	@FXML
	public void addContainer() {
		Container newContainer = makeNewContainer();
		table.getItems().add(newContainer.getOznaczenie() - 1, newContainer);
	}
	
	@FXML
	private void print(){
		ObservableList<Container> selectedItems = table.getSelectionModel().getSelectedItems();
		
		String [] [] listToPrint = new String[selectedItems.size()][4];
		int index = 0;
		for(Container c : selectedItems){
			listToPrint[index][0] = c.getOznaczenie()+" Kuweta ";
			listToPrint[index][1] =  "  ";
			listToPrint[index][2] = c.getOznaczenie()+" ";
			listToPrint[index][3] = c.getOznaczenie()+" ";
//			System.out.println(c +" ---kuweta");
			index++;
		}
		PrintArtBarCode print = new PrintArtBarCode(0,0,listToPrint);

		
	}
}
