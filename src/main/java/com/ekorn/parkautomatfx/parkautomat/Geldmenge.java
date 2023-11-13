package com.ekorn.parkautomatfx.parkautomat;

public class Geldmenge {

    private final int[] muenzMenge = new int[8];

    public Geldmenge(int ten_cent, int twenty_cent, int fifty_cent,
                     int one, int two, int five, int ten, int twenty) {
        int[] mengen = {ten_cent, twenty_cent, fifty_cent, one, two, five, ten, twenty};

        for (int i = 0; i < muenzMenge.length; i++) {
            setAnzahl(i, mengen[i]);
        }
    }

    public Geldmenge(int ten_cent, int twenty_cent, int fifty_cent, int one, int two) {
        this(ten_cent, twenty_cent, fifty_cent, one, two, 0, 0, 0);
    }

    public Geldmenge() {
        this(0, 0, 0, 0, 0);
    }

    public Geldmenge(Geldmenge copy) {
        this(copy.getAnzahl(0), copy.getAnzahl(1), copy.getAnzahl(2),
                copy.getAnzahl(3), copy.getAnzahl(4), copy.getAnzahl(5),
                copy.getAnzahl(6), copy.getAnzahl(7));
    }

    public int getAnzahl(int muenzart) {
        return muenzMenge[muenzart];
    }

    public void setAnzahl(int muenzart, int anzahl) {
        muenzMenge[muenzart] += anzahl;
    }

    @Override
    public String toString() {
        String output = String.format("%sx10c, %sx20c, %sx50c, %sx1€, %sx2€, ",
                muenzMenge[0], muenzMenge[1], muenzMenge[2], muenzMenge[3], muenzMenge[4]);
        if (muenzMenge[5] == 0 && muenzMenge[6] == 0 && muenzMenge[7] == 0) {
            output += "keine Scheine";
        } else {
            output += String.format("%sx5€, %sx10€, %sx20€", muenzMenge[5], muenzMenge[6], muenzMenge[7]);
        }
        return output;
    }
}
