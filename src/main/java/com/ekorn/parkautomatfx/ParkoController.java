package com.ekorn.parkautomatfx;

import com.ekorn.parkautomatfx.parkautomat.Geldmenge;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;


public class ParkoController implements Initializable {

    //region FXML elements
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
    //endregion

    //region Variables
    private ArrayList<Button> muenzButtons = new ArrayList<>();
    private int randBetrag;
    private boolean zahlungsvorgang = false;
    //endregion


    //region FXML Methods

    /**
     * Generates a random amount of money between 1€ and 20€.
     */
    @FXML
    public void onRandomBetrag() {
        Random rand = new Random();
        randBetrag = rand.nextInt(100, 2000);
        while (randBetrag % 10 != 0) {
            randBetrag++;
        }
        zahlungsvorgang = true;
        btn_random_betrag.setDisable(true);
        updateBetrag(lbl_betrag, randBetrag);
        reset();
    }

    /**
     * Adds or removes money from the current GeldPane.
     *
     * @param mouse the mouse event.
     */
    @FXML
    public void addGeld(MouseEvent mouse) {
        if (zahlungsvorgang) {
            Button b = (Button) mouse.getSource();
            int index = muenzButtons.indexOf(b);

            int i = 1;
            Geldmenge gm = new Geldmenge();
            gm.addAnzahl(index, i);
            updateValues(gm, geld_gezahlt, false);

            // payment proceeds automatically if it's possible to pay
            if (geld_gezahlt.getBetrag() >= randBetrag) {
                payment();
                btn_fertig.setDisable(true);
            }
        }
    }

    /**
     * When finished, then we will try to pay the amount.
     */
    @FXML
    public void onFertig() {
        if (zahlungsvorgang) {
            if (geld_gezahlt.getBetrag() < randBetrag) {
                throw new IllegalArgumentException("Noch nicht genug Geld zum zahlen!");
            }
            payment();
        }
    }
    //endregion

    //region Methods
    // The initialization of the controller
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        muenzButtons = new ArrayList<>() {{
            add(btn_zehn_ct);
            add(btn_zwanzig_ct);
            add(btn_fuenfzig_ct);
            add(btn_ein_euro);
            add(btn_zwei_euro);
            add(btn_fuenf_euro);
            add(btn_zehn_euro);
            add(btn_zwanzig_euro);
        }};

        // All three GeldPanes get initialized
        updateValues(readStartgeldmenge(), geld_bestand, true);
        reset();
        updateBetrag(lbl_betrag, 0);
    }

    // Reads the Geldmenge from init.csv to the first GeldPane
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
    public void updateValues(Geldmenge gm, GeldPane gp, boolean reset) {
        for (int i = 0; i < muenzButtons.size(); i++) {
            if (reset) {
                gp.setValue(i, gm.getAnzahl(i));
            } else {
                gp.addValue(i, gm.getAnzahl(i));
            }
        }
        updateBetrag(gp.getGesamt(), gp.getBetrag());
    }

    // Updates a Label with the value
    public void updateBetrag(Label l, int betrag) {
        l.setText(String.format("%.2f", (double) betrag / 100) + "€");
    }

    public void payment() {
        try {
            Geldmenge wechsel = geld_bestand.payment(randBetrag, geld_gezahlt.getGeldSpeicher());

            updateValues(wechsel, geld_rueckgeld, false);
            updateBetrag(geld_bestand.getGesamt(), geld_bestand.getBetrag());

            btn_random_betrag.setDisable(false);
        } catch (Exception e) {
            btn_random_betrag.setDisable(false);
            updateBetrag(lbl_betrag, 0);
            updateValues(new Geldmenge(), geld_gezahlt, true);
            System.out.println(e.getMessage());
        }
        zahlungsvorgang = false;
    }

    public void reset() {
        updateValues(new Geldmenge(), geld_gezahlt, true);
        updateValues(new Geldmenge(), geld_rueckgeld, true);
    }
    //endregion
}