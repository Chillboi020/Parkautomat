package com.ekorn.parkautomatfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ParkoApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ParkoApplication.class.getResource("parkautomat-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Der Parkautomat");
        stage.getIcons().add(new Image(Objects.requireNonNull(ParkoApplication.class.getResourceAsStream("/icon.png"))));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}