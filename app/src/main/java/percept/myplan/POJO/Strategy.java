package percept.myplan.POJO;

/**
 * Created by percept on 26/7/16.
 */

public class Strategy {

    private String created_by;
    private String music;
    private String id;
    private String title;
    private String contact_id;
    private String description;
    private String link;
    private String state;
    private String ordering;
    private String image;
    private boolean isSelected;

    public Strategy(String created_by, String music, String id, String title, String contact_id, String description, String link, String state, String ordering, String image, boolean isSelected) {
        this.created_by = created_by;
        this.music = music;
        this.id = id;
        this.title = title;
        this.contact_id = contact_id;
        this.description = description;
        this.link = link;
        this.state = state;
        this.ordering = ordering;
        this.image = image;
        this.isSelected = isSelected;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
