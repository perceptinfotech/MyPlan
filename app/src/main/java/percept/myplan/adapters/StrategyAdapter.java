package percept.myplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import percept.myplan.POJO.Strategy;
import percept.myplan.POJO.Symptom;
import percept.myplan.R;

/**
 * Created by percept on 16/7/16.
 */

public class StrategyAdapter extends RecyclerView.Adapter<StrategyAdapter.SymptomHolder> {


    public List<Strategy> LIST_STRATEGY;

    public class SymptomHolder extends RecyclerView.ViewHolder {
        public TextView TV_STRATEGY;

        public SymptomHolder(View itemView) {
            super(itemView);
            TV_STRATEGY = (TextView) itemView.findViewById(R.id.tvStrategy);
        }
    }

    public StrategyAdapter(List<Strategy> quickMessageList) {
        this.LIST_STRATEGY= quickMessageList;
    }

    @Override
    public SymptomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strategy_list_item, parent, false);

        return new SymptomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SymptomHolder holder, int position) {
        Strategy _strategy = LIST_STRATEGY.get(position);
        holder.TV_STRATEGY.setText(_strategy.getTitle());
    }

    @Override
    public int getItemCount() {
        return this.LIST_STRATEGY.size();
    }
}
