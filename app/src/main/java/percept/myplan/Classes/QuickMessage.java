package percept.myplan.Classes;

/**
 * Created by percept on 20/7/16.
 */

public class QuickMessage {

    private String Message;
    private String ContactName;


    public QuickMessage(String message, String contactName) {
        this.ContactName = contactName;
        this.Message = message;

    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }


    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

}
