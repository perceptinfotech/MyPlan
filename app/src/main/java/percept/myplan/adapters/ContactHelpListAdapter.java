package percept.myplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.apache.http.util.TextUtils;

import java.util.List;

import percept.myplan.AppController;
import percept.myplan.POJO.ContactDisplay;
import percept.myplan.R;
import percept.myplan.customviews.RoundedImageView;

/**
 * Created by percept on 16/7/16.
 */

public class ContactHelpListAdapter extends RecyclerView.Adapter<ContactHelpListAdapter.ContactHelpListHolder> {


    public List<ContactDisplay> LIST_HELPCONTACT;
    ImageLoader imageLoader;
    private String TYPE;

    public class ContactHelpListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView TV_HELPCONTACT, TV_CONTACTCHAR;
        public RoundedImageView IMG_CONTACT, IMG_MSG_CONTACT;

        public ContactHelpListHolder(View itemView) {
            super(itemView);
            TV_HELPCONTACT = (TextView) itemView.findViewById(R.id.tvContactName);
            TV_CONTACTCHAR = (TextView) itemView.findViewById(R.id.tvContactChar);

            IMG_CONTACT = (RoundedImageView) itemView.findViewById(R.id.imgContact);
            IMG_MSG_CONTACT = (RoundedImageView) itemView.findViewById(R.id.imgMsgContact);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public ContactHelpListAdapter(List<ContactDisplay> helpContactList, String type) {
        this.LIST_HELPCONTACT = helpContactList;
        imageLoader = AppController.getInstance().getImageLoader();
        this.TYPE = type;
    }


    @Override
    public ContactHelpListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.helpcontact_list_item, parent, false);
        return new ContactHelpListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ContactHelpListHolder holder, int position) {
        ContactDisplay _contact = LIST_HELPCONTACT.get(position);
        if (!TextUtils.isEmpty(_contact.getFirst_name())) {
            holder.TV_HELPCONTACT.setText(_contact.getFirst_name());
        } else {
            holder.TV_HELPCONTACT.setText(_contact.getPhone());
        }


        if (TYPE.equals("HELP")) {
            holder.IMG_CONTACT.setVisibility(View.GONE);
            holder.IMG_MSG_CONTACT.setVisibility(View.VISIBLE);
            holder.TV_CONTACTCHAR.setVisibility(View.GONE);
        } else {
            holder.IMG_CONTACT.setVisibility(View.VISIBLE);
            holder.TV_CONTACTCHAR.setVisibility(View.VISIBLE);
            holder.IMG_MSG_CONTACT.setVisibility(View.GONE);
        }

        if (_contact.getCon_image().equals("")) {
            if (TYPE.equals("HELP"))
                holder.TV_CONTACTCHAR.setVisibility(View.GONE);
            else
                holder.TV_CONTACTCHAR.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(LIST_HELPCONTACT.get(position).getFirst_name())) {
                holder.TV_CONTACTCHAR.setText(LIST_HELPCONTACT.get(position).getFirst_name().substring(0, 2));
            }


//            holder.IMG_CONTACT.setBackgroundColor(Color.rgb(169, 169, 169));
        } else {
            holder.TV_CONTACTCHAR.setVisibility(View.GONE);
            imageLoader.get(_contact.getCon_image(), new ImageLoader.ImageListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                    if (response.getBitmap() != null) {
                        // load image into imageview
                        if (TYPE.equals("HELP")) {
                            holder.IMG_MSG_CONTACT.setImageBitmap(response.getBitmap());
                        } else {
                            holder.IMG_CONTACT.setImageBitmap(response.getBitmap());
                        }

                    }
                }
            });
        }
//        holder.IMG_CONTACT.setImageBitmap();
    }

    @Override
    public int getItemCount() {
        return this.LIST_HELPCONTACT.size();
    }
}
