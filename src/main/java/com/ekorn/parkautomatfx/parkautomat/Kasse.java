package com.ekorn.parkautomatfx.parkautomat;

import com.ekorn.parkautomatfx.exceptions.KeinPassendesRueckgeldException;
import com.ekorn.parkautomatfx.exceptions.WechselgeldException;

public class Kasse {
    //region Fields
    // 10c, 20c, 50c, 1€, 2€, 5€, 10€, 20€
    private static final int[] MUENZART = {10, 20, 50, 100, 200, 500, 1000, 2000};
    private Geldmenge geldspeicher;
    private int lang;
    //endregion

    //region Constructors
    /**
     * Creates a new Kasse with a Geldspeicher.
     *
     * @param gm The Geldspeicher.
     */
    public Kasse(Geldmenge gm, int lang) {
        setLang(lang);
        setGeldspeicher(gm);
    }

    public Kasse(Geldmenge gm) {
        this(gm, 0);
    }

    /**
     * Creates a new Kasse with an empty Geldspeicher.
     */
    public Kasse() {
        this(new Geldmenge());
    }
    //endregion

    //region Methods
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
        // if restbetrag is not a multiple of 10, it is not possible to pay.
        if (preis % 10 != 0) {
            throw new WechselgeldException(0, lang);
        }

        // important as backup in case an error occurs.
        Geldmenge kopie_geldspeicher = new Geldmenge(geldspeicher);

        // the total amount of the given Geldmenge
        // updates the Geldspeicher afterward.
        int gm_betrag = 0;
        for (int i = 0; i < MUENZART.length; i++) {
            gm_betrag += MUENZART[i] * gm.getAnzahl(i);
            if (gm.getAnzahl(i) > 0) {
                kopie_geldspeicher.addAnzahl(i, gm.getAnzahl(i));
            }
        }
        // no coins inserted.
        if (gm_betrag == 0) {
            throw new WechselgeldException(1, lang);
        }

        int restbetrag = gm_betrag - preis;
        // not enough money to pay with.
        if (restbetrag < 0) {
            throw new WechselgeldException(2, lang);
        }

        Geldmenge wechselgeld = new Geldmenge();
        // if there is no need for change.
        if (restbetrag != 0) {
            int i = 0;
            int anzahl = 0;
            int simulated_restbetrag = restbetrag;
            // Here the restbetrag is calculated (Geldspeicher is updated respectively).
            for (i = MUENZART.length - 1; restbetrag > 0;) {
                if ((simulated_restbetrag - MUENZART[i]) >= 0) {
                    simulated_restbetrag -= MUENZART[i];
                    anzahl++;
                } else {
                    // when we reach the 10c pieces,
                    // if it doesn't work, then we throw an Exception.
                    if (i == 0) {
                        try {
                            wechselgeld.addAnzahl(i, anzahl);
                            kopie_geldspeicher.addAnzahl(i, -anzahl);
                            restbetrag -= MUENZART[i] * anzahl;
                        } catch (WechselgeldException e) {
                            throw new KeinPassendesRueckgeldException();
                        }
                    }
                    // for the bigger coin types.
                    else {
                        try {
                            wechselgeld.addAnzahl(i, anzahl);
                            kopie_geldspeicher.addAnzahl(i, -anzahl);
                            restbetrag -= MUENZART[i] * anzahl;
                        } catch (WechselgeldException ignored) {
                        }
                    }
                    // update the simulated restbetrag so it works like it should.
                    simulated_restbetrag = restbetrag;
                    anzahl = 0;
                    i--;
                }
            }
        }
        // When everything worked out.
        geldspeicher = kopie_geldspeicher;
        return wechselgeld;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }
    //endregion
}
