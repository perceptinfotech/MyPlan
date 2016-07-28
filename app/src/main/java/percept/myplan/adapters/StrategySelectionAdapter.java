package percept.myplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import percept.myplan.POJO.Strategy;
import percept.myplan.R;
import percept.myplan.customviews.CircleCheckBox;

/**
 * Created by percept on 16/7/16.
 */

public class StrategySelectionAdapter extends RecyclerView.Adapter<StrategySelectionAdapter.SymptomHolder> {


    public List<Strategy> LIST_STRATEGY;
    private boolean onBind;

    public class SymptomHolder extends RecyclerView.ViewHolder implements CircleCheckBox.OnCheckedChangeListener {
        public TextView TV_STRATEGY;
        public CircleCheckBox CHK_BOX;

        public SymptomHolder(View itemView) {
            super(itemView);
            TV_STRATEGY = (TextView) itemView.findViewById(R.id.tvStrategy);
            CHK_BOX = (CircleCheckBox) itemView.findViewById(R.id.chbx);
            CHK_BOX.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CircleCheckBox view, boolean isChecked) {
            if (!onBind) {
                // your process when checkBox changed
                // ...
                int _i = (int) CHK_BOX.getTag();
                LIST_STRATEGY.get(_i).setSelected(isChecked);
//                notifyDataSetChanged();
                notifyDataSetChanged();
            }
        }
    }

    public StrategySelectionAdapter(List<Strategy> quickMessageList) {
        this.LIST_STRATEGY = quickMessageList;
    }

    @Override
    public SymptomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strategy_list_item_select, parent, false);

        return new SymptomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SymptomHolder holder, int position) {
        Strategy _strategy = LIST_STRATEGY.get(position);
        holder.TV_STRATEGY.setText(_strategy.getTitle());
        if (_strategy.isSelected()) {
            onBind = true;
            holder.CHK_BOX.setChecked(true);
            onBind = false;
        } else {
            onBind = true;
            holder.CHK_BOX.setChecked(false);
            onBind = false;
        }
        holder.CHK_BOX.setTag(position);
    }

    @Override
    public int getItemCount() {
        return this.LIST_STRATEGY.size();
    }
}
