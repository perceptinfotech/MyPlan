package percept.myplan.Classes;

/**
 * Created by percept on 22/7/16.
 */

public class Hope {

    private String ID;
    private String IMG_COVER;
    private String TITLE;

    public Hope(String ID, String IMG_COVER, String TITLE) {
        this.ID = ID;
        this.IMG_COVER = IMG_COVER;
        this.TITLE = TITLE;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIMG_COVER() {
        return IMG_COVER;
    }

    public void setIMG_COVER(String IMG_COVER) {
        this.IMG_COVER = IMG_COVER;
    }


}
