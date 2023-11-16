package com.ekorn.parkautomatfx.parkautomat;

public class ParkoTest2 {
    public static void main(String[] args) {
        Geldmenge g = new Geldmenge(2, 3, 1, 2, 3);
        Kasse k = new Kasse(g);

        Geldmenge z1 = new Geldmenge(0, 0, 0, 0, 0, 1, 0, 0);
        Geldmenge r1 = new Geldmenge();

        System.out.println(k.getGeldspeicher());

        try {
            r1 = k.bezahle(260, z1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(k.getGeldspeicher());
        System.out.println(r1);

        Geldmenge r2 = new Geldmenge();
        try {
            r2 = k.bezahle(260, z1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
