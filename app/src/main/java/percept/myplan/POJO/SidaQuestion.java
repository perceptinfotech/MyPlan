package percept.myplan.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by percept on 16/8/16.
 */

public class SidaQuestion {
    @SerializedName("id")
    private String ID;
    @SerializedName("question")
    private String Question;
    @SerializedName("ordering")
    private String Ordering;
    @SerializedName("state")
    private String State;
    private String SIDA_ANSWER;

    public SidaQuestion(String ID, String question, String ordering, String state, String sidaAnswer) {
        this.ID = ID;
        Question = question;
        Ordering = ordering;
        State = state;
        SIDA_ANSWER = sidaAnswer;
    }

    public String getSIDA_ANSWER() {
        return SIDA_ANSWER;
    }

    public void setSIDA_ANSWER(String SIDA_ANSWER) {
        this.SIDA_ANSWER = SIDA_ANSWER;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getOrdering() {
        return Ordering;
    }

    public void setOrdering(String ordering) {
        Ordering = ordering;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
