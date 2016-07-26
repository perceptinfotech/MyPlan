package percept.myplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import percept.myplan.Classes.QuickMessage;
import percept.myplan.Classes.Symptom;
import percept.myplan.R;

/**
 * Created by percept on 16/7/16.
 */

public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.SymptomHolder> {


    public List<Symptom> LIST_SYMPTOM;

    public class SymptomHolder extends RecyclerView.ViewHolder {
        public TextView TV_SYMPTOM;

        public SymptomHolder(View itemView) {
            super(itemView);
            TV_SYMPTOM = (TextView) itemView.findViewById(R.id.tvSymptom);
        }
    }

    public SymptomAdapter(List<Symptom> quickMessageList) {
        this.LIST_SYMPTOM = quickMessageList;
    }

    @Override
    public SymptomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.symptom_list_item, parent, false);

        return new SymptomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SymptomHolder holder, int position) {
        Symptom _symptom = LIST_SYMPTOM.get(position);
        holder.TV_SYMPTOM.setText(_symptom.getTitle());
    }

    @Override
    public int getItemCount() {
        return this.LIST_SYMPTOM.size();
    }
}
