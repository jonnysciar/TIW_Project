package it.jonnysciar.javascript.beans;

public class Prodotto {
    private final int id;
    private final String nome;
    private final String imgPath;

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

    @Override
    public String toString() {
        return "Prodotto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", imgPath='" + imgPath + '\'' +
                '}';
    }
}
