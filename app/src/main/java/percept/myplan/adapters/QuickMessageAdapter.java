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

/**
 * Created by percept on 16/7/16.
 */

public class QuickMessageAdapter extends RecyclerView.Adapter<QuickMessageAdapter.QuickMessageHolder> {


    public List<QuickMessage> LST_QMSG;

    public class QuickMessageHolder extends RecyclerView.ViewHolder {
        public TextView TV_QUICKMSG;

        public QuickMessageHolder(View itemView) {
            super(itemView);
            TV_QUICKMSG = (TextView) itemView.findViewById(R.id.tvQuickMsg);
        }
    }

    public QuickMessageAdapter(List<QuickMessage> quickMessageList) {
        this.LST_QMSG = quickMessageList;
    }

    @Override
    public QuickMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quick_message_list_item, parent, false);

        return new QuickMessageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QuickMessageHolder holder, int position) {
        QuickMessage _contact = LST_QMSG.get(position);
        holder.TV_QUICKMSG.setText(_contact.getMessage() + " " + _contact.getContactName());
    }

    @Override
    public int getItemCount() {
        return this.LST_QMSG.size();
    }
}
