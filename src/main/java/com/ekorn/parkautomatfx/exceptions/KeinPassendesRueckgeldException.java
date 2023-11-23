package com.ekorn.parkautomatfx.exceptions;

public class KeinPassendesRueckgeldException extends ParkomatException {
    public KeinPassendesRueckgeldException() {
        super("RueckgeldException", "RueckgeldException", "kein passendes Rückgeld vorhanden!");
    }
}
