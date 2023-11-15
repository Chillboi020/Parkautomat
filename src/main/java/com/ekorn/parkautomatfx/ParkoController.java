package com.ekorn.parkautomatfx;

import com.ekorn.parkautomatfx.parkautomat.Geldmenge;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;


public class ParkoController implements Initializable {

    @FXML
    private Label lbl_betrag;
    @FXML
    private Button btn_random_betrag;
    @FXML
    private Button btn_zehn_ct;
    @FXML
    private Button btn_zwanzig_ct;
    @FXML
    private Button btn_fuenfzig_ct;
    @FXML
    private Button btn_ein_euro;
    @FXML
    private Button btn_zwei_euro;
    @FXML
    private Button btn_fuenf_euro;
    @FXML
    private Button btn_zehn_euro;
    @FXML
    private Button btn_zwanzig_euro;
    @FXML
    private Button btn_fertig;
    @FXML
    private GeldPane geld_bestand;
    @FXML
    private GeldPane geld_gezahlt;
    @FXML
    private GeldPane geld_rueckgeld;

    private ArrayList<Button> muenzButtons = new ArrayList<>();
    private int randBetrag;

    @FXML
    public void onRandomBetrag() {
        Random rand = new Random();
        randBetrag = rand.nextInt(100, 2000);
        while (randBetrag % 10 != 0) {
            randBetrag++;
        }
        btn_random_betrag.setDisable(true);
        lbl_betrag.setText(String.format("%.2f", (double) randBetrag / 100) + "€");
    }

    @FXML
    public void addGeld(MouseEvent mouse) {
        Button b = (Button) mouse.getSource();
        int index = muenzButtons.indexOf(b);

        int i = 1;
        // With right-click you remove one coin
        if (mouse.getButton() == MouseButton.SECONDARY) {
            i = -1;
        }

        // negative values become impossible
        if (geld_gezahlt.getValue(index) + i >= 0)
        {
            Geldmenge gm = new Geldmenge();
            gm.setAnzahl(index, i);
            updateValues(gm, geld_gezahlt);
        }
    }

    @FXML
    public void onFertig() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        muenzButtons = new ArrayList<>() {{
            add(btn_zehn_ct); add(btn_zwanzig_ct);
            add(btn_fuenfzig_ct); add(btn_ein_euro);
            add(btn_zwei_euro); add(btn_fuenf_euro);
            add(btn_zehn_euro); add(btn_zwanzig_euro);
        }};

        // All three GeldPanes get initialized
        updateValues(readStartgeldmenge(), geld_bestand);
        updateValues(new Geldmenge(), geld_gezahlt);
        updateValues(new Geldmenge(), geld_rueckgeld);
    }

    // Reads the Geldmenge from init.csv
    public Geldmenge readStartgeldmenge() {
        Geldmenge gm = new Geldmenge();
        try {
            FileReader fr = new FileReader("init.csv");
            Scanner sc = new Scanner(fr);

            sc.nextLine();
            String[] data = sc.nextLine().split(";");
            for (int i = 0; i < data.length; i++) {
                gm.setAnzahl(i, Integer.parseInt(data[i]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return gm;
    }

    // Updates the values whenever something happens
    public void updateValues(Geldmenge gm, GeldPane gp) {
        for (int i = 0; i < muenzButtons.size(); i++) {
            gp.setValue(i, gm.getAnzahl(i));
        }
        updateBetrag(gp.getGesamt(), gp.getBetrag());
    }

    // Updates a Label with the value
    public void updateBetrag(Label l, int betrag) {
        l.setText(String.format("%.2f", (double) betrag / 100) + "€");
    }
}