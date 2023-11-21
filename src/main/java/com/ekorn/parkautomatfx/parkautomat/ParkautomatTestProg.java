package com.ekorn.parkautomatfx.parkautomat;

import com.ekorn.parkautomatfx.exceptions.WechselgeldException;

public class ParkautomatTestProg {
    public static void main(String[] args) throws WechselgeldException {
        // 5x10c 0x20c, 8x50c, 0x1€, 7x2€, keine Scheine
        Geldmenge startMenge = new Geldmenge(5, 0, 8, 0, 7, 0, 0, 0);
        System.out.println(startMenge);

        // Erzeugt eine Kasse mit dem obigen Startkapital
        Kasse kasse = new Kasse(startMenge);
        //System.out.println("Geldbetrag: " + kasse.getBetrag() + " Cent");

        // Zahlt den geforderten Betrag mit 1x5€
        // Der Kassenautomat gibt 2,40€ zurück(4,0,0,0,1,0,0,0)
        // Da keine 20erl vorhanden sind, werden stattdessen vier 10erl zurückgegeben.
        // Zum Geldspeicher der Kasse kommt ein 5€-Schein hinzu und vier 10erl sowie
        // ein 2€ Stück weg.
        Geldmenge zahlung1 = new Geldmenge(0, 0, 0, 0, 0, 1, 0, 0);
        Geldmenge rueck1 = kasse.bezahle(260, zahlung1);

        // Zahlt den geforderten Betrag von 2,60€ mit 2x2€
        // Der Kassenautomat kann dieses Mal nicht anständig zurückgeben, weil
        // nicht genügend 20erl und 10erl vorhanden sind.
        // ==> Die Methode bezahle wirft eine Exception.
        // Der Inhalt des Münzspeichers, bleibt bei dieser fehlgeschlagenen Zahlung
        // unverändert.
        Geldmenge zahlung2 = new Geldmenge(0, 0, 0, 0, 2);  // Konstruktor nur für Münzen
        Geldmenge rueck2 = kasse.bezahle(260, zahlung2); // ==> Exception

        // System.out.println(kasse.getGeldspeicher());
        // Im Geldspeicher der Kasse sind nun (1,0,8,0,6,1,0,0)
    }
}
