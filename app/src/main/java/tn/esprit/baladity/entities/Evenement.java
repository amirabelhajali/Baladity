package tn.esprit.baladity.entities;



public class Evenement {

    private int id;
    private String titre;
    private String description;
    private String image;
    private String dateAjout;
    private String dateEvent;
    private User user;

    public Evenement() {
    }

    public Evenement(String titre, String description, String image, String dateAjout, String dateEvent, User user) {
        this.titre = titre;
        this.description = description;
        this.image = image;
        this.dateAjout = dateAjout;
        this.dateEvent = dateEvent;
        this.user = user;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(String dateAjout) {
        this.dateAjout = dateAjout;
    }

    public String getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(String dateEvent) {
        this.dateEvent = dateEvent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
