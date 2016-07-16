package percept.myplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by percept on 16/7/16.
 */

public class QuickMessageAdapter extends RecyclerView.Adapter<QuickMessageAdapter.QuickMessageHolder> {


    public class QuickMessageHolder extends RecyclerView.ViewHolder {

        public QuickMessageHolder(View itemView) {
            super(itemView);
        }
    }


    @Override
    public QuickMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(QuickMessageHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
