package tn.esprit.baladity.entities;




public class Pdf {

    private int id;
    private String titre;
    private String pdf;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public Pdf(int id, String titre, String pdf) {
        this.id = id;
        this.titre = titre;
        this.pdf = pdf;
    }

    public Pdf() {
    }

    public Pdf(String titre, String pdf) {
        this.titre = titre;
        this.pdf = pdf;
    }

    public Pdf(String pdf) {
        this.pdf = pdf;
    }

    @Override
    public String toString() {
        return "Pdf{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", pdf='" + pdf + '\'' +
                '}';
    }
}
