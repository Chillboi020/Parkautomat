package com.ekorn.parkautomatfx.parkautomat;

public class Kasse {
    // 10c, 20c, 50c, 1€, 2€, 5€, 10€, 20€
    private static final int[] MUENZART = {10, 20, 50, 100, 200, 500, 1000, 2000};
    private Geldmenge geldspeicher;

    /**
     * Creates a new Kasse with a Geldspeicher.
     * @param gm The Geldspeicher.
     */
    public Kasse(Geldmenge gm) {
        setGeldspeicher(gm);
    }

    /**
     * Creates a new Kasse with an empty Geldspeicher.
     */
    public Kasse() {
        this(new Geldmenge());
    }

    /**
     * Gets the Geldspeicher of this Kasse.
     * @return The Geldspeicher of this Kasse.
     */
    public Geldmenge getGeldspeicher() {
        return geldspeicher;
    }

    /**
     * Sets the Geldspeicher of this Kasse.
     * @param gm The Geldspeicher to set.
     */
    public void setGeldspeicher(Geldmenge gm) {
        this.geldspeicher = gm;
    }

    /**
     * Gets the total amount of the Geldspeicher.
     * @return Total amount of the Geldspeicher.
     */
    public int getBetrag() {
        int betrag = 0;
        for (int i = 0; i < MUENZART.length; i++) {
            betrag += MUENZART[i] * geldspeicher.getAnzahl(i);
        }
        return betrag;
    }

    /**
     * This is where the payment is done.
     * @param preis The price to pay.
     * @param gm The Geldmenge to pay the price with.
     * @return The change.
     */
    public Geldmenge bezahle(int preis, Geldmenge gm) {
        Geldmenge wechselgeld = new Geldmenge();
        String msg = "Wechseln nicht möglich!";

        // Important as backup in case an error occurs
        Geldmenge kopie_geldspeicher = new Geldmenge(geldspeicher);

        // The total amount of the given Geldmenge
        // Updates the Geldspeicher afterward
        int gm_betrag = 0;
        for (int i = 0; i < MUENZART.length; i++) {
            gm_betrag += MUENZART[i] * gm.getAnzahl(i);
            if (gm.getAnzahl(i) > 0) {
                geldspeicher.addAnzahl(i, gm.getAnzahl(i));
            }
        }
        int restbetrag = gm_betrag - preis;

        // If restbetrag is not a multiple of 10, it is not possible to pay.
        if (restbetrag % 10 != 0) {
            throw new IllegalArgumentException(msg);
        }

        try {
            int anzahl = 0;
            int i;
            // Here the restbetrag is calculated (Geldspeicher is updated respectively
            for (i = MUENZART.length - 1; restbetrag > 0;) {
                if (i < 0) {
                    throw new IllegalArgumentException(msg);
                }
                if ((restbetrag - MUENZART[i]) >= 0 /*&& geldspeicher.getAnzahl(i) > 0*/) {
                    restbetrag -= MUENZART[i];
                    anzahl++;
                } else {
                    wechselgeld.addAnzahl(i, anzahl);
                    geldspeicher.addAnzahl(i, -anzahl);
                    anzahl = 0;
                    i--;
                }
            }
            // One last update of the values
            wechselgeld.addAnzahl(i, anzahl);
            geldspeicher.addAnzahl(i, -anzahl);

        } catch (Exception e) {
            // Revert changes of the Geldspeicher
            geldspeicher = kopie_geldspeicher;
            throw new IllegalArgumentException(e.getMessage());
        }
        return wechselgeld;
    }
}
