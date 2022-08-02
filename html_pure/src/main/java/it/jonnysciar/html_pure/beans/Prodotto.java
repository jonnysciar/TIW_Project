package it.jonnysciar.html_pure.beans;

public class Prodotto {
    private int id;
    private String nome;
    private String imgPath;

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
