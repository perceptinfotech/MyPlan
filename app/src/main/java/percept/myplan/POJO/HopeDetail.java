package percept.myplan.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by percept on 3/8/16.
 */

public class HopeDetail {

    @SerializedName("music")
    private String MUSIC;
    @SerializedName("id")
    private String ID;
    @SerializedName("hope_id")
    private String HOPE_ID;
    @SerializedName("link")
    private String LINK;
    @SerializedName("state")
    private String STATE;
    @SerializedName("ordering")
    private String ORDERING;
    @SerializedName("type")
    private String TYPE;
    @SerializedName("media")
    private String MEDIA;
    @SerializedName("note")
    private String NOTE;
    @SerializedName("video")
    private String VIDEO;
    @SerializedName("media_title")
    private String MEDIA_TITLE;

    public HopeDetail(String MUSIC, String ID, String HOPE_ID, String LINK, String STATE,
                      String ORDERING, String MEDIA, String NOTE, String VIDEO, String MEDIA_TITLE, String type) {
        this.MUSIC = MUSIC;
        this.ID = ID;
        this.HOPE_ID = HOPE_ID;
        this.LINK = LINK;
        this.STATE = STATE;
        this.ORDERING = ORDERING;
        this.MEDIA = MEDIA;
        this.NOTE = NOTE;
        this.VIDEO = VIDEO;
        this.MEDIA_TITLE = MEDIA_TITLE;
        this.TYPE = type;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getMEDIA_TITLE() {
        return MEDIA_TITLE;
    }

    public void setMEDIA_TITLE(String MEDIA_TITLE) {
        this.MEDIA_TITLE = MEDIA_TITLE;
    }

    public String getMUSIC() {
        return MUSIC;
    }

    public void setMUSIC(String MUSIC) {
        this.MUSIC = MUSIC;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHOPE_ID() {
        return HOPE_ID;
    }

    public void setHOPE_ID(String HOPE_ID) {
        this.HOPE_ID = HOPE_ID;
    }

    public String getLINK() {
        return LINK;
    }

    public void setLINK(String LINK) {
        this.LINK = LINK;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getORDERING() {
        return ORDERING;
    }

    public void setORDERING(String ORDERING) {
        this.ORDERING = ORDERING;
    }

    public String getMEDIA() {
        return MEDIA;
    }

    public void setMEDIA(String MEDIA) {
        this.MEDIA = MEDIA;
    }

    public String getNOTE() {
        return NOTE;
    }

    public void setNOTE(String NOTE) {
        this.NOTE = NOTE;
    }

    public String getVIDEO() {
        return VIDEO;
    }

    public void setVIDEO(String VIDEO) {
        this.VIDEO = VIDEO;
    }

}
