package percept.myplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import percept.myplan.POJO.HopeDetail;
import percept.myplan.R;

/**
 * Created by percept on 5/8/16.
 */

public class Basic3NormalViewHolder extends Basic3ViewHolder {

    public static final int LAYOUT_RES = R.layout.vh_normal_view;
    private HopeDetail DETAILS;
    private TextView TV_TEXTVIEW, TV_EDIT;


    public Basic3NormalViewHolder(View itemView) {
        super(itemView);
        TV_TEXTVIEW = (TextView) itemView.findViewById(R.id.text);
        TV_EDIT = (TextView) itemView.findViewById(R.id.tvEdit);
    }

    @Override
    public void bind(RecyclerView.Adapter adapter, Object item,int posi) {
        this.DETAILS = (HopeDetail) item;
        if (this.DETAILS.getTYPE().equals("link")) {
            this.TV_TEXTVIEW.setText(this.DETAILS.getLINK());
        } else {
            this.TV_TEXTVIEW.setText(this.DETAILS.getNOTE());
        }
    }

    @Override
    public void setOnItemClickListener(View.OnClickListener listener) {
        super.setOnItemClickListener(listener);
        TV_EDIT.setOnClickListener(listener);
    }
}