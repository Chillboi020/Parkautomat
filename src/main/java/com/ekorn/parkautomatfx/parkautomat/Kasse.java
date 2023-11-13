package com.ekorn.parkautomatfx.parkautomat;

public class Kasse {
    // 10c, 20c, 50c, 1€, 2€, 5€, 10€, 20€
    private static final int[] MUENZART = {10, 20, 50, 100, 200, 500, 1000, 2000};
    private Geldmenge geldspeicher;

    /**
     * Legt eine Kasse mit einem Geldspeicher an.
     * @param gm Der Geldspeicher.
     */
    public Kasse(Geldmenge gm) {
        setGeldspeicher(gm);
    }

    /**
     * Legt eine Kasse mit einem leeren Geldspeicher an.
     */
    public Kasse() {
        this(new Geldmenge());
    }

    /**
     * Ruft den Geldspeicher dieser Kasse ab.
     * @return Der Geldspeicher der Kasse.
     */
    public Geldmenge getGeldspeicher() {
        return geldspeicher;
    }

    /**
     * Setzt den Geldspeicher dieser Kasse.
     * @param gm Der Geldspeicher, den man hinzufügen will.
     */
    public void setGeldspeicher(Geldmenge gm) {
        this.geldspeicher = gm;
    }

    /**
     * Gibt den Gesamtbetrag im Speicher zurück.
     * @return Gesamtbetrag im Geldspeicher.
     */
    public int getBetrag() {
        int betrag = 0;
        for (int i = 0; i < MUENZART.length; i++) {
            betrag += MUENZART[i] * geldspeicher.getAnzahl(i);
        }
        return betrag;
    }

    /**
     * Hier wird die Bezahlung ausgeführt
     * @param preis Der zu zahlende Preis.
     * @param gm Die Geldmenge, mit der gezahlt wird.
     * @return Das Wechselgeld.
     */
    public Geldmenge bezahle(int preis, Geldmenge gm) {
        Geldmenge wechselgeld = new Geldmenge();

        // Wichtig als Backup, falls ein Fehler auftritt
        Geldmenge kopie_geldspeicher = new Geldmenge(geldspeicher);

        // Der Gesamtbetrag der übergebenen Geldmenge
        // Der Geldspeicher der Kasse wird dementsprechend aktualisiert
        int gm_betrag = 0;
        for (int i = 0; i < MUENZART.length; i++) {
            gm_betrag += MUENZART[i] * gm.getAnzahl(i);
            if (gm.getAnzahl(i) > 0) {
                geldspeicher.setAnzahl(i, gm.getAnzahl(i));
            }
        }
        int restbetrag = gm_betrag - preis;

        // Falls der Restbetrag keine Zehnerzahlen hat
        if (restbetrag % 10 != 0) {
            throw new IllegalArgumentException("Wechseln nicht möglich!");
        }

        try {
            int anzahl = 0;
            int i = 0;

            // Hier wird der Restbetrag ausgeglichen (Geldspeicher wird aktualisiert!)
            for (i = MUENZART.length - 1; restbetrag != 0;) {
                if ((restbetrag - MUENZART[i]) >= 0 && geldspeicher.getAnzahl(i) > 0) {
                    restbetrag -= MUENZART[i];
                    anzahl++;
                } else {
                    wechselgeld.setAnzahl(i, anzahl);
                    geldspeicher.setAnzahl(i, -anzahl);
                    anzahl = 0;
                    i--;
                }
            }
            // Zum Schluss muss nochmal aktualisiert werden!
            wechselgeld.setAnzahl(i, anzahl);
            geldspeicher.setAnzahl(i, -anzahl);

            // Falls im Geldspeicher negative Anzahlen von Münzen auftreten
            for (i = 0; i < MUENZART.length; i++) {
                if (geldspeicher.getAnzahl(i) < 0) {
                    throw new IllegalArgumentException("Im Geldspeicher reichen die Münzen nicht aus!");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // Geldspeicher auf letzten Stand zurücksetzen
            geldspeicher = kopie_geldspeicher;
            return new Geldmenge();
        }
        return wechselgeld;
    }
}
