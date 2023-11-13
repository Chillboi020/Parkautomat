package com.ekorn.parkautomatfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class GeldPane extends Pane {
    @FXML
    private TextField txt_zehnCent;
    @FXML
    private TextField txt_zwanzigCent;
    @FXML
    private TextField txt_fuenfzigCent;
    @FXML
    private TextField txt_einEuro;
    @FXML
    private TextField txt_zweiEuro;
    @FXML
    private TextField txt_fuenfEuro;
    @FXML
    private TextField txt_zehnEuro;
    @FXML
    private TextField txt_zwanzigEuro;
    @FXML
    private Label lbl_gesamt;

    public GeldPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("geld-pane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
