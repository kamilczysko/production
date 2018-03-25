package controller;

import javafx.fxml.FXML;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.ComboBox;

import javafx.scene.control.TextArea;
import model.Orders;
import service.OrdersService;

@Controller
public class OrdersController {
	@FXML
	private ComboBox<Orders> orderBox;
	@FXML
	private Label artNameLabel;
	@FXML
	private Label artAmountLabel;
	@FXML
	private TextArea artDescriptionField;

	private Orders selectedItem;
	
	@Autowired
	private MainWindowController mainWindowController;
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private ProcessController processController;
	
	@Autowired
	private OperationsController operationsController;
	
	public void setOrdersListInComboBox(){//ustawienie listy z zamownieniami
		clearSelection();
		List<Orders> list = orderService.getOrders();
		ObservableList<Orders> orders = FXCollections.observableList(list);
		orderBox.getItems().clear();
		orderBox.getItems().addAll(orders);
	}
	
	@FXML
	private void initialize(){
		setOrdersListInComboBox();
	}
	
	
	@FXML
	private void selectedItem(ActionEvent event) {
		processController.clearTable();
		operationsController.clearPane();
		clearSelection();
		this.selectedItem = orderBox.getSelectionModel().getSelectedItem();
		if(this.selectedItem.getOrderInProgress() == 1)
			mainWindowController.lockProcessTab(true);
		else
			mainWindowController.lockProcessTab(false);
//		System.out.println(selectedItem.getArticle().getName()+" - "+ selectedItem.getArticle().getId());
		artNameLabel.setText(selectedItem.getArticle().getName());
		artAmountLabel.setText(selectedItem.getAmount()+"");
		artDescriptionField.setText(selectedItem.getDescription());
	
		processController.setProcessOnList();
	}
	
	public Orders getOrder(){
		return selectedItem;
	}
	
	public int article(){
		if(selectedItem != null)
			return selectedItem.getArticle().getId();
		else
			return -999;
	}
	
	public int amount(){
		if(selectedItem != null)
			return selectedItem.getAmount();
		else
			return 0;
	}
	
	public int order(){
		if(selectedItem != null)
			return selectedItem.getOrderId();
		else
			return -999;
	}
	
	private void clearSelection(){
		artNameLabel.setText("");
		artAmountLabel.setText("");
		artDescriptionField.setText("");
	}
}
