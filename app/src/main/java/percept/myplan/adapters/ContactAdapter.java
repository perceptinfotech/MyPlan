package percept.myplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import percept.myplan.Classes.Contact;
import percept.myplan.R;

/**
 * Created by percept on 11/7/16.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> LIST_CONTACT;

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView TV_CONTACTNAME;
        public CheckBox CHK_CONTACTSELECTION;

        public ContactViewHolder(View itemView) {
            super(itemView);
            TV_CONTACTNAME = (TextView) itemView.findViewById(R.id.tvContactName);
            CHK_CONTACTSELECTION = (CheckBox) itemView.findViewById(R.id.chkSelection);
        }
    }

    public ContactAdapter(List<Contact> contactList) {
        this.LIST_CONTACT = contactList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact _contact = LIST_CONTACT.get(position);
        holder.TV_CONTACTNAME.setText(_contact.getContactName());
        holder.CHK_CONTACTSELECTION.setChecked(_contact.isSelected());
    }


    @Override
    public int getItemCount() {
        return LIST_CONTACT.size();
    }
}
