package com.ekorn.parkautomatfx.parkautomat;

import com.ekorn.parkautomatfx.exceptions.KeinPassendesRueckgeldException;
import com.ekorn.parkautomatfx.exceptions.WechselgeldException;

public class Kasse {
    // 10c, 20c, 50c, 1€, 2€, 5€, 10€, 20€
    private static final int[] MUENZART = {10, 20, 50, 100, 200, 500, 1000, 2000};
    private Geldmenge geldspeicher;

    /**
     * Creates a new Kasse with a Geldspeicher.
     *
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
     *
     * @return The Geldspeicher of this Kasse.
     */
    public Geldmenge getGeldspeicher() {
        return geldspeicher;
    }

    /**
     * Sets the Geldspeicher of this Kasse.
     *
     * @param gm The Geldspeicher to set.
     */
    public void setGeldspeicher(Geldmenge gm) {
        this.geldspeicher = gm;
    }

    /**
     * Gets the total amount of the Geldspeicher.
     *
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
     *
     * @param preis The price to pay.
     * @param gm    The Geldmenge to pay the price with.
     * @return The change.
     * @throws WechselgeldException            Any error within the payment
     * @throws KeinPassendesRueckgeldException If the change isn't fitting
     */
    public Geldmenge bezahle(int preis, Geldmenge gm) throws WechselgeldException, KeinPassendesRueckgeldException {
        // If restbetrag is not a multiple of 10, it is not possible to pay.
        if (preis % 10 != 0) {
            throw new WechselgeldException(1, 0);
        }

        // Important as backup in case an error occurs
        Geldmenge kopie_geldspeicher = new Geldmenge(geldspeicher);

        // The total amount of the given Geldmenge
        // Updates the Geldspeicher afterward
        int gm_betrag = 0;
        for (int i = 0; i < MUENZART.length; i++) {
            gm_betrag += MUENZART[i] * gm.getAnzahl(i);
            if (gm.getAnzahl(i) > 0) {
                kopie_geldspeicher.addAnzahl(i, gm.getAnzahl(i));
            }
        }
        if (gm_betrag == 0) {
            throw new WechselgeldException(2, 0);
        }

        int restbetrag = gm_betrag - preis;
        if (restbetrag < 0) {
            throw new WechselgeldException(3, 0);
        }

        Geldmenge wechselgeld = new Geldmenge();
        // if there is no need for change
        if (restbetrag != 0) {
            // Here the restbetrag is calculated (Geldspeicher is updated respectively)
            for (int anzahl = 0, i = MUENZART.length - 1; i >= 0; ) {
                if ((restbetrag - MUENZART[i]) >= 0) {
                    restbetrag -= MUENZART[i];
                    anzahl++;
                } else {
                    wechselgeld.addAnzahl(i, anzahl);
                    try {
                        kopie_geldspeicher.addAnzahl(i, -anzahl);
                    } catch (WechselgeldException e) {
                        throw new KeinPassendesRueckgeldException();
                    }
                    anzahl = 0;
                    i--;
                }
            }
        }
        // When everything worked out
        geldspeicher = kopie_geldspeicher;
        return wechselgeld;
    }
}
