package percept.myplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import percept.myplan.Classes.Contact;
import percept.myplan.R;

/**
 * Created by percept on 11/7/16.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> LIST_CONTACT;
    private boolean SINGLE_CHECK;
    private boolean onBind;

    public class ContactViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        public TextView TV_CONTACTNAME;
        public CheckBox CHK_CONTACTSELECTION;

        public ContactViewHolder(View itemView) {
            super(itemView);
            TV_CONTACTNAME = (TextView) itemView.findViewById(R.id.tvContactName);
            CHK_CONTACTSELECTION = (CheckBox) itemView.findViewById(R.id.chkSelection);
            CHK_CONTACTSELECTION.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (!onBind) {
                // your process when checkBox changed
                // ...
                int _i = (int) CHK_CONTACTSELECTION.getTag();
                if (SINGLE_CHECK) {
                    for (int i = 0; i < LIST_CONTACT.size(); i++) {
                        LIST_CONTACT.get(i).setSelected(false);
                    }
                }
                LIST_CONTACT.get(_i).setSelected(b);
//                notifyDataSetChanged();
                notifyDataSetChanged();
            }
        }
    }

    public ContactAdapter(List<Contact> contactList, boolean singleCheck) {
        this.LIST_CONTACT = contactList;
        this.SINGLE_CHECK = singleCheck;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        Contact _contact = LIST_CONTACT.get(position);
        holder.TV_CONTACTNAME.setText(_contact.getContactName());
        onBind = true;
        holder.CHK_CONTACTSELECTION.setChecked(_contact.isSelected());
        onBind = false;
        holder.CHK_CONTACTSELECTION.setTag(position);
    }


    @Override
    public int getItemCount() {
        return LIST_CONTACT.size();
    }
}
