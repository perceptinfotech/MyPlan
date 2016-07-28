package percept.myplan.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import percept.myplan.AppController;
import percept.myplan.Global.Constant;
import percept.myplan.POJO.Hope;
import percept.myplan.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class HopeAdapter extends RecyclerView.Adapter<HopeAdapter.MyViewHolder> {

    private Context CONTEXT;
    private List<Hope> LST_HOPE;
    ImageLoader imageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TV_TITLE;
        public ImageView IMG_COVER;

        public MyViewHolder(View view) {
            super(view);
            TV_TITLE = (TextView) view.findViewById(R.id.title);
            IMG_COVER = (ImageView) view.findViewById(R.id.thumbnail);

        }
    }


    public HopeAdapter(Context mContext, List<Hope> hopeList) {
        this.CONTEXT = mContext;
        this.LST_HOPE = hopeList;
        imageLoader = AppController.getInstance().getImageLoader();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hope_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Hope album = LST_HOPE.get(position);

        if (position == LST_HOPE.size() - 1) {
            holder.TV_TITLE.setText(CONTEXT.getResources().getString(R.string.addnewbox));
            holder.IMG_COVER.setBackgroundColor(CONTEXT.getResources().getColor(android.R.color.white));
        } else {
            holder.TV_TITLE.setText(album.getTITLE());
//            Picasso.with(CONTEXT).load(album.getIMG_COVER()).into(holder.IMG_COVER);
            imageLoader.get(album.getIMG_COVER(), new ImageLoader.ImageListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                    if (response.getBitmap() != null) {
                        // load image into imageview
                        holder.IMG_COVER.setImageBitmap(response.getBitmap());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return LST_HOPE.size();
    }
}
