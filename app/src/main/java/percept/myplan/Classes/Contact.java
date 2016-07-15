package percept.myplan.Classes;

/**
 * Created by percept on 11/7/16.
 */

public class Contact {

    private String ContactName;
    private String PhoneNo;
    private boolean isSelected;

    public Contact(String contactName,String contactNo, boolean isSelected) {
        this.ContactName = contactName;
        this.isSelected = isSelected;
        this.PhoneNo=contactNo;
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
}
