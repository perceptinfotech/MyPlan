package percept.myplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import percept.myplan.Classes.Contact;
import percept.myplan.Classes.QuickMessage;
import percept.myplan.R;
import percept.myplan.customviews.RoundedImageView;

/**
 * Created by percept on 16/7/16.
 */

public class ContactHelpListAdapter extends RecyclerView.Adapter<ContactHelpListAdapter.ContactHelpListHolder> {


    public List<Contact> LIST_HELPCONTACT;

    public class ContactHelpListHolder extends RecyclerView.ViewHolder {
        public TextView TV_HELPCONTACT;
        public RoundedImageView IMG_CONTACT;

        public ContactHelpListHolder(View itemView) {
            super(itemView);
            TV_HELPCONTACT = (TextView) itemView.findViewById(R.id.tvContactName);
            IMG_CONTACT = (RoundedImageView) itemView.findViewById(R.id.imgContact);
        }
    }

    public ContactHelpListAdapter(List<Contact> helpContactList) {
        this.LIST_HELPCONTACT = helpContactList;
    }

    @Override
    public ContactHelpListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.helpcontact_list_item, parent, false);
        return new ContactHelpListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactHelpListHolder holder, int position) {
        Contact _contact = LIST_HELPCONTACT.get(position);
        holder.TV_HELPCONTACT.setText(_contact.getContactName());
//        holder.IMG_CONTACT.setImageBitmap();
    }

    @Override
    public int getItemCount() {
        return this.LIST_HELPCONTACT.size();
    }
}
