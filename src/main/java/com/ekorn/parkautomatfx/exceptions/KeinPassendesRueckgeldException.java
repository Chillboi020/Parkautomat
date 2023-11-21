package com.ekorn.parkautomatfx.exceptions;

public class KeinPassendesRueckgeldException extends ParkomatException {
    public KeinPassendesRueckgeldException() {
        super("RückgeldException", "RückgeldException", "kein passendes Rückgeld vorhanden!");
    }
}
