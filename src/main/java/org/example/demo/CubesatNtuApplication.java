package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

@SpringBootApplication
public class CubesatNtuApplication extends Application {

    // тест
    private static ConfigurableApplicationContext springContext;
    private FXMLLoader fxmlLoader;

    @Override
    public void init() {
        springContext = SpringApplication.run(CubesatNtuApplication.class);
        fxmlLoader = new FXMLLoader(getClass().getResource("/views/main-view.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1250, 600);

        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png")));
            stage.getIcons().add(icon); // Устанавливаем иконку для окна
        } catch (Exception e) {
            System.err.println("Ошибка загрузки иконки: " + e.getMessage());
        }


        stage.setScene(scene);
        stage.setTitle("Aerospace Database");
        stage.show();
    }

    @Override
    public void stop() {
        springContext.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
