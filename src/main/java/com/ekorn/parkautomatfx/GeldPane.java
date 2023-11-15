package com.ekorn.parkautomatfx;

import com.ekorn.parkautomatfx.parkautomat.Geldmenge;
import com.ekorn.parkautomatfx.parkautomat.Kasse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class GeldPane extends Pane {
    @FXML
    private TextField txt_zehnCent = new TextField("0");
    @FXML
    private TextField txt_zwanzigCent = new TextField("0");
    @FXML
    private TextField txt_fuenfzigCent = new TextField("0");
    @FXML
    private TextField txt_einEuro = new TextField("0");
    @FXML
    private TextField txt_zweiEuro = new TextField("0");
    @FXML
    private TextField txt_fuenfEuro = new TextField("0");
    @FXML
    private TextField txt_zehnEuro = new TextField("0");
    @FXML
    private TextField txt_zwanzigEuro = new TextField("0");
    @FXML
    private Label lbl_gesamt;

    private final Kasse k = new Kasse();

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

    /**
     * gets the amount of the given money-type from the current GeldPane.
     *
     * @param type the money-type (1 - 8).
     * @return the amount.
     */
    public int getValue(int type) {
        return k.getGeldspeicher().getAnzahl(type);
    }

    /**
     * sets the amount of the given money-type from the current GeldPane.
     * @param type the money-type (1 - 8).
     * @param amount the amount.
     */
    public void setValue(int type, int amount) {
        k.getGeldspeicher().setAnzahl(type, amount);
        int i = k.getGeldspeicher().getAnzahl(type);

        TextField[] muenzen = {txt_zehnCent, txt_zwanzigCent, txt_fuenfzigCent, txt_einEuro, txt_zweiEuro, txt_fuenfEuro, txt_zehnEuro, txt_zwanzigEuro};
        muenzen[type].setText(String.valueOf(i));
    }

    public int getBetrag() {
        return k.getBetrag();
    }

    public Label getGesamt() {
        return lbl_gesamt;
    }

    public Geldmenge getGeldSpeicher() {
        return k.getGeldspeicher();
    }

    public void setGeldSpeicher(Geldmenge gm) {
        k.setGeldspeicher(gm);
    }

    public Geldmenge payment(int price, Geldmenge gm) {
        return k.bezahle(price, gm);
    }
}
