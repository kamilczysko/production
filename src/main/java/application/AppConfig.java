package application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import controller.BatchSettingsController;
import controller.ChartController;
import controller.ContainerController;
import controller.MainWindowController;
import controller.OperationsController;
import controller.OrdersController;
import controller.ProcessController;
import controller.functions.SaveProduction;
import controller.functions.TimingTool;

@Configuration
public class AppConfig {

	@Bean(name = "mainWindowController")
	MainWindowController mainWindowController() {
		return new MainWindowController();
	}

	@Bean(name = "ordersController")
	OrdersController ordersController() {
		return new OrdersController();
	}

	@Bean(name = "processController")
	ProcessController procesController() {
		return new ProcessController();
	}
	
	@Bean(name = "batchSettingsController")
	BatchSettingsController batchSettingsController() {
		return new BatchSettingsController();
	}

	@Bean(name = "operationsController") // domyslny scope - singleton
	OperationsController operationsController() {
		return new OperationsController();
	}
	
	@Bean
	ContainerController containerController() {
		return new ContainerController();
	}

	@Bean(name="saveProduction")
	SaveProduction saveProduction(){
		return new SaveProduction();
	}
	
	@Bean(name="timingTool")
	TimingTool timingTool(){
		return new TimingTool();
	}
	
	@Bean
	ChartController chartController(){
		return new ChartController();
	}
	

}
