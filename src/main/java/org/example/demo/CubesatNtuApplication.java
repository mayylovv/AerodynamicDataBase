package org.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CubesatNtuApplication extends Application {

	private static ConfigurableApplicationContext springContext;
	private FXMLLoader fxmlLoader;

	@Override
	public void init() throws Exception {
		springContext = SpringApplication.run(CubesatNtuApplication.class);
		fxmlLoader = new FXMLLoader(getClass().getResource("/views/main-view.fxml"));
		fxmlLoader.setControllerFactory(springContext::getBean);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(fxmlLoader.load(), 1250, 600);
		stage.setScene(scene);
		stage.setTitle("AerospaceDatabase");
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		springContext.close();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
