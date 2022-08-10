package it.jonnysciar.javascript.enums;

public enum TipoOpzione {
    IN_OFFERTA("in offerta"),
    NORMALE("normale");

    private final String text;

    TipoOpzione(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static TipoOpzione getTipoOpzione(String tipo) {
        return TipoOpzione.valueOf(tipo.toUpperCase().replace(' ', '_'));
    }

}
