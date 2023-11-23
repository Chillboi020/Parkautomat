package com.ekorn.parkautomatfx;

import com.ekorn.parkautomatfx.exceptions.KeinPassendesRueckgeldException;
import com.ekorn.parkautomatfx.exceptions.ParkomatException;
import com.ekorn.parkautomatfx.exceptions.WechselgeldException;
import com.ekorn.parkautomatfx.parkautomat.Geldmenge;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private Button[] muenzButtons;
    private int randBetrag;
    private boolean zahlungsvorgang = false;
    private final Alert a = new Alert(Alert.AlertType.ERROR);
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
        btn_fertig.setDisable(false);
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

            int index = 0;
            for (int i = 0; i < muenzButtons.length; i++) {
                if (muenzButtons[i] == b) {
                    index = i;
                    break;
                }
            }

            Geldmenge gm = new Geldmenge();
            try {
                gm.addAnzahl(index, 1);
            } catch (WechselgeldException e) {
                fehlerProtokoll("Programmfehler: beim hinzufügen von Münzen hat etwas nicht geklappt!");
            }
            updateValues(gm, geld_gezahlt, false);

            // payment proceeds automatically if it's possible to pay
            if (geld_gezahlt.getBetrag() >= randBetrag) {
                payment();
            }
        }
    }

    /**
     * When finished, then we will try to pay the amount. <br>
     * However, since it works automatically this button is kinda
     * useless.
     */
    @FXML
    public void onFertig() {
        if (zahlungsvorgang) {
            payment();
        }
    }
    //endregion

    //region Methods
    // the initialization of the controller
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        muenzButtons = new Button[]{btn_zehn_ct, btn_zwanzig_ct, btn_fuenfzig_ct,
                btn_ein_euro, btn_zwei_euro, btn_fuenf_euro, btn_zehn_euro, btn_zwanzig_euro};

        // All three GeldPanes get initialized
        updateValues(readStartgeldmenge(), geld_bestand, true);
        btn_fertig.setDisable(true);
        reset();
        updateBetrag(lbl_betrag, 0);
    }

    // reads the Geldmenge from init.csv to the first GeldPane
    public Geldmenge readStartgeldmenge() {
        Geldmenge gm = new Geldmenge();
        try (FileReader fr = new FileReader("init.csv"); Scanner sc = new Scanner(fr);) {
            sc.nextLine();
            String[] data = sc.nextLine().split(";");
            for (int i = 0; i < data.length; i++) {
                gm.setAnzahl(i, Integer.parseInt(data[i]));
            }
        } catch (IOException | WechselgeldException e) {
            fehlerProtokoll(e.getMessage());
        }
        return gm;
    }

    // every error gets written into the "fehler.log" file with a timestamp
    public void fehlerProtokoll(String msg) {
        try (FileWriter fw = new FileWriter("fehler.log", true);) {
            String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
            fw.write(timestamp + ": " + msg + "\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // updates the values whenever something happens
    public void updateValues(Geldmenge gm, GeldPane gp, boolean reset) {
        try {
            for (int i = 0; i < muenzButtons.length; i++) {
                if (reset) {
                    gp.setValue(i, gm.getAnzahl(i));
                } else {
                    gp.addValue(i, gm.getAnzahl(i));
                }
            }
            updateBetrag(gp.getGesamt(), gp.getBetrag());
        } catch (WechselgeldException e) {
            fehlerProtokoll(e.getMsg());
        }
    }

    // updates a Label with the value
    public void updateBetrag(Label l, int betrag) {
        l.setText(String.format("%.2f", (double) betrag / 100) + "€");
    }

    // the payment method for this controller
    public void payment() {
        try {
            Geldmenge wechsel = geld_bestand.payment(randBetrag, geld_gezahlt.getGeldSpeicher());
            updateValues(wechsel, geld_rueckgeld, false);

            updateBetrag(geld_bestand.getGesamt(), geld_bestand.getBetrag());
        } catch (ParkomatException e) {
            updateBetrag(lbl_betrag, 0);
            updateValues(new Geldmenge(), geld_gezahlt, true);
            fehlerProtokoll(e.getMsg());
            a.setContentText(e.getMsg());
            a.setHeaderText(e.getHeader());
            a.setTitle(e.getTitle());
            a.showAndWait();
        }
        btn_fertig.setDisable(true);
        btn_random_betrag.setDisable(false);
        zahlungsvorgang = false;
    }

    // resets all values
    public void reset() {
        updateValues(new Geldmenge(), geld_gezahlt, true);
        updateValues(new Geldmenge(), geld_rueckgeld, true);
    }
    //endregion
}