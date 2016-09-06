package percept.myplan.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import percept.myplan.AppController;
import percept.myplan.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private Context CONTEXT;
    private List<String> LST_IMG;
    ImageLoader imageLoader;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView IMG_COVER;

        public MyViewHolder(View view) {
            super(view);
            IMG_COVER = (ImageView) view.findViewById(R.id.imgImg);

        }
    }


    public ImageAdapter(Context mContext, List<String> hopeList) {
        this.CONTEXT = mContext;
        this.LST_IMG = hopeList;
        imageLoader = AppController.getInstance().getImageLoader();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_img_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

//        Picasso.with(CONTEXT).load(LST_IMG.get(position)).into(holder.ivSelectedImg);


        imageLoader.get(LST_IMG.get(position), new ImageLoader.ImageListener() {

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

    @Override
    public int getItemCount() {
        return LST_IMG.size();
    }
}
