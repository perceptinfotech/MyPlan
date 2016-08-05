package percept.myplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import percept.myplan.R;

/**
 * Created by percept on 5/8/16.
 */

public class Basic3NormalViewHolder extends Basic3ViewHolder {

    public static final int LAYOUT_RES = R.layout.vh_normal_view;

    private TextView TV_TEXTVIEW;

    public Basic3NormalViewHolder(View itemView) {
        super(itemView);
        TV_TEXTVIEW = (TextView) itemView.findViewById(R.id.text);
    }

    @Override
    public void bind(RecyclerView.Adapter adapter, Object item) {
        this.TV_TEXTVIEW.setText("Rudrik Patel");
    }
}