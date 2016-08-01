package percept.myplan.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import percept.myplan.AppController;
import percept.myplan.POJO.Alarm;
import percept.myplan.POJO.Hope;
import percept.myplan.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.MyViewHolder> {

    private Context CONTEXT;
    private List<Alarm> LST_HOPE;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TV_TITLE;


        public MyViewHolder(View view) {
            super(view);
            TV_TITLE = (TextView) view.findViewById(R.id.title);


        }
    }


    public AlarmAdapter(Context mContext, List<Alarm> hopeList) {
        this.CONTEXT = mContext;
        this.LST_HOPE = hopeList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Alarm album = LST_HOPE.get(position);

        if (position == LST_HOPE.size() - 1) {
            holder.TV_TITLE.setText(CONTEXT.getResources().getString(R.string.addnewbox));

        } else {
            holder.TV_TITLE.setText(album.getAlarmName());
        }
    }

    @Override
    public int getItemCount() {
        return LST_HOPE.size();
    }
}
