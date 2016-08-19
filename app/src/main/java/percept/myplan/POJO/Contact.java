package percept.myplan.POJO;

/**
 * Created by percept on 11/7/16.
 */

public class Contact implements Comparable<Contact>{

    private String ContactName;
    private String PhoneNo;
    private String ContactID;
    private String WEB_ID;
    private boolean isSelected;
    private boolean OriginalSelection;

    public Contact(String contactName, String phoneNo, String contactID, boolean isSelected, boolean originalSelection, String web_id) {
        ContactName = contactName;
        PhoneNo = phoneNo;
        ContactID = contactID;
        this.isSelected = isSelected;
        this.OriginalSelection = originalSelection;
        this.WEB_ID = web_id;
    }

    public Contact(String contactName, String contactNo, boolean isSelected, boolean originalSelection, String web_id) {
        this.ContactName = contactName;
        this.isSelected = isSelected;
        this.PhoneNo = contactNo;
        this.OriginalSelection = originalSelection;
        this.WEB_ID = web_id;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getContactID() {
        return ContactID;
    }

    public void setContactID(String contactID) {
        ContactID = contactID;
    }

    public boolean isOriginalSelection() {
        return OriginalSelection;
    }

    public void setOriginalSelection(boolean originalSelection) {
        OriginalSelection = originalSelection;
    }

    public String getWEB_ID() {
        return WEB_ID;
    }

    public void setWEB_ID(String WEB_ID) {
        this.WEB_ID = WEB_ID;
    }

    @Override
    public int compareTo(Contact contact) {
        return ContactName.compareTo(contact.getContactName());
    }
}
