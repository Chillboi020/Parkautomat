package com.ekorn.parkautomatfx.exceptions;

import java.util.HashMap;

public class WechselgeldException extends ParkomatException {
    //region ErrorData
    private static final String[] errorsDE = {
            "keine zehner Zahl!", "bitte Münzen einwerfen!", "nicht genug Geld zum zahlen!", "keine passenden Münzen vorhanden!"
    };

    private static final String[] errorsEN = {
            "not a multiple of ten!", "please insert coins!", "not enough money to pay with!", "no suitable coins available!"
    };

    private static final String[] languages = {
            "DE", "EN"
    };

    private static final HashMap<String, String[]> errors = new HashMap<>() {{
        put("DE", errorsDE);
        put("EN", errorsEN);
    }};
    //endregion

    //region Constructors
    public WechselgeldException(int type, int lang) {
        super("WechselgeldException", "WechselgeldException", errors.get(languages[lang])[type]);
    }
    //endregions

    //region Methods
    @Override
    public String getMessage() {
        return getMsg();
    }
    //endregion
}
