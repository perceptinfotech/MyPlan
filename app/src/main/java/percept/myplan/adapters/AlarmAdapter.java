package percept.myplan.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import percept.myplan.POJO.Alarm;
import percept.myplan.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.MyViewHolder> {

    private Context CONTEXT;
    private List<Alarm> LST_HOPE;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView TV_ALARMTITLE, TV_ALARMEDIT, TV_ALARMTIME;
        public Switch SWITCH_STATUS;


        public MyViewHolder(View view) {
            super(view);

            TV_ALARMTITLE = (TextView) view.findViewById(R.id.tvAlarmTitle);
            TV_ALARMEDIT = (TextView) view.findViewById(R.id.tvAlarmEdit);
            TV_ALARMTIME = (TextView) view.findViewById(R.id.tvAlarmTime);
            TV_ALARMEDIT.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int i = (int) view.getTag();
            Log.d(":::: Pressed on ", String.valueOf(i));
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
        holder.TV_ALARMTITLE.setText(album.getAlarmName());
//                TV_ALARMTIME

        Date date = new Date(Long.valueOf(album.getAlarmTime()));
        int _hour = date.getHours();
        int _min = date.getMinutes();
        holder.TV_ALARMTIME.setText(_hour + ":" + _min);
        holder.TV_ALARMEDIT.setTag(position);

    }

    @Override
    public int getItemCount() {
        return LST_HOPE.size();
    }
}
