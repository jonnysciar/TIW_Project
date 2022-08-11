package it.jonnysciar.javascript.beans;

import java.util.ArrayList;
import java.util.List;

public class Prodotto {
    private final int id;
    private final String nome;
    private final String imgPath;
    private List<Opzione> opzioni;

    public Prodotto(int id, String nome, String imgPath) {
        this.id = id;
        this.nome = nome;
        this.imgPath = imgPath;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getImgPath() {
        return imgPath;
    }

    public List<Opzione> getOpzioni() {
        return new ArrayList<>(opzioni);
    }

    public void setOpzioni(List<Opzione> opzioni) {
        this.opzioni = new ArrayList<>(opzioni);
    }

    @Override
    public String toString() {
        return "Prodotto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", imgPath='" + imgPath + '\'' +
                '}';
    }
}
