package percept.myplan.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import percept.myplan.Dialogs.dialogAddStrategy;
import percept.myplan.R;

/**
 * Created by percept on 19/8/16.
 */
public class StrategySubmittedOthersAdapter extends RecyclerView.Adapter<StrategySubmittedOthersAdapter.StrategyAdapter> {

    private Context context;

    public StrategySubmittedOthersAdapter(Context context) {
        this.context = context;
    }

    public class StrategyAdapter extends RecyclerView.ViewHolder {
        public ImageView ivAddStrategy;
        public TextView tvStrategyDetail;

        public StrategyAdapter(View itemView) {
            super(itemView);
            ivAddStrategy = (ImageView) itemView.findViewById(R.id.ivAddStrategy);
            tvStrategyDetail = (TextView) itemView.findViewById(R.id.tvStrategyDetail);
        }

        public void setOnItemClickListener(View.OnClickListener listener) {
            this.itemView.setOnClickListener(listener);
            ivAddStrategy.setOnClickListener(listener);
        }
    }


    @Override
    public StrategyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View _itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_strategy_submitted_others, parent, false);
        final StrategyAdapter _adapter = new StrategyAdapter(_itemView);
        _adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.ivAddStrategy:
                        openConfimationDialog((Integer) view.getTag());
                        break;
                }
            }
        });
        return _adapter;
    }

    @Override
    public void onBindViewHolder(StrategyAdapter holder, int position) {
        holder.tvStrategyDetail.setText(context.getString(R.string.description) + " " + position);
        holder.tvStrategyDetail.setTag(position);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private void openConfimationDialog(int position)
    {
        dialogAddStrategy _dialog=new dialogAddStrategy(context) {
            @Override
            public void onClickYes() {
                dismiss();
            }

            @Override
            public void onClickNo() {
                dismiss();
            }
        };
        _dialog.setCanceledOnTouchOutside(false);
        _dialog.show();

    }
}
