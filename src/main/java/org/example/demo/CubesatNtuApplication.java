package org.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
		Parent root = fxmlLoader.load();
		Scene scene = new Scene(root, 1250, 600);
		stage.setScene(scene);
		stage.setTitle("AerospaceDatabase");

		ScrollPane scrollPane = (ScrollPane) root.lookup("#cubesatScroller"); // найдите ваш ScrollPane
		if (scrollPane != null) {
			scrollPane.setVvalue(0); // Сначала прокрутите вверх
		}

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
