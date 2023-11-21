package com.ekorn.parkautomatfx.exceptions;

import java.util.HashMap;

public class WechselgeldException extends ParkomatException {
    private static final String[] errorsDE = {
            "Wechselgeld Fehler",
            "keine zehner Zahl!", "bitte Münzen einwerfen!", "nicht genug Geld zum zahlen!", "keine passenden Münzen vorhanden!"
    };

    private static final String[] languages = {
            "DE", "EN"
    };

    private static final HashMap<String, String[]> errors = new HashMap<>() {{
        put("DE", errorsDE);
    }};

    public WechselgeldException(int type, int lang) {
        super(errors.get(languages[lang])[0], errors.get(languages[lang])[0], errors.get(languages[lang])[type]);
    }
}
