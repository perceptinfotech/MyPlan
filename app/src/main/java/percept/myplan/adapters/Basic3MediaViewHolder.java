package percept.myplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import percept.myplan.AppController;
import percept.myplan.POJO.HopeDetail;
import percept.myplan.R;

/**
 * Created by percept on 5/8/16.
 */

public class Basic3MediaViewHolder extends Basic3ViewHolder {

    public static final int LAYOUT_RES = R.layout.vh_image_view;

    private HopeDetail DETAILS;
    final ImageView imgCardImage;
    final TextView tvCardImage, tvCardImageEdit;

    public Basic3MediaViewHolder(View itemView) {
        super(itemView);
        imgCardImage = (ImageView) itemView.findViewById(R.id.imgCardImage);
        tvCardImage = (TextView) itemView.findViewById(R.id.tvCardImage);
        tvCardImageEdit = (TextView) itemView.findViewById(R.id.tvCardImageEdit);
    }

    @Override
    public void bind(RecyclerView.Adapter adapter, Object item, int posi) {

        this.DETAILS = (HopeDetail) item;
        tvCardImage.setText(this.DETAILS.getMEDIA_TITLE());
        imgCardImage.setTag(posi);
        tvCardImageEdit.setTag(posi);
        imgCardImage.setImageResource(R.drawable.media_hope);
    }

    @Override
    public void setOnItemClickListener(View.OnClickListener listener) {
        super.setOnItemClickListener(listener);
        tvCardImageEdit.setOnClickListener(listener);
        imgCardImage.setOnClickListener(listener);
    }
}