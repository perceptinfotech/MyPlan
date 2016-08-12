package percept.myplan.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import percept.myplan.Graph.PieGraph;
import percept.myplan.Graph.PieSlice;
import percept.myplan.POJO.Mood;
import percept.myplan.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class SidaSummaryAdapter extends RecyclerView.Adapter<SidaSummaryAdapter.MyViewHolder> {

    private Context CONTEXT;
    private List<Mood> LST_MOOD;
    private TextView TV_MOODSUMTITLE;
    private PieGraph SIDA_PIE_GRAPH;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public MyViewHolder(View view) {
            super(view);

            TV_MOODSUMTITLE = (TextView) view.findViewById(R.id.tvMoodSymmaryTime);
            SIDA_PIE_GRAPH = (PieGraph) view.findViewById(R.id.pieSidagraph);
        }

        @Override
        public void onClick(View view) {
            int i = (int) view.getTag();
            Log.d(":::: Pressed on ", String.valueOf(i));
        }
    }


    public SidaSummaryAdapter(Context mContext, List<Mood> hopeList) {
        this.CONTEXT = mContext;
        this.LST_MOOD = hopeList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sida_summary_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Mood _mood = LST_MOOD.get(position);
//        holder.TV_ALARMTITLE.setText(album.getAlarmName());
        TV_MOODSUMTITLE.setText(_mood.getMOOD_DATE_STRING());
        ArrayList<PieSlice> LST_PIEDATA = new ArrayList<>();
        PieSlice slice = new PieSlice();
        slice.setColor(CONTEXT.getResources().getColor(R.color.pie_color1));
        slice.setValue(2);
        slice.setTitle("first");
        LST_PIEDATA.add(slice);
        slice = new PieSlice();
        slice.setColor(CONTEXT.getResources().getColor(R.color.pie_color2));
        slice.setValue(3);
        slice.setTitle("Second");
        LST_PIEDATA.add(slice);
        SIDA_PIE_GRAPH.removeSlices();

        SIDA_PIE_GRAPH.addSlice(LST_PIEDATA.get(0));
        SIDA_PIE_GRAPH.addSlice(LST_PIEDATA.get(1));
        SIDA_PIE_GRAPH.setPadding(2);

    }

    @Override
    public int getItemCount() {
        return LST_MOOD.size();
    }
}
