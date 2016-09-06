package percept.myplan.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import percept.myplan.AppController;
import percept.myplan.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class ImageDeleteAdapter extends RecyclerView.Adapter<ImageDeleteAdapter.MyViewHolder> {

    ImageLoader imageLoader;
    private Context CONTEXT;
    private List<String> LST_IMG;
    private String path;
    private MyViewHolder holder;

    public ImageDeleteAdapter(Context mContext, List<String> hopeList) {
        this.CONTEXT = mContext;
        this.LST_IMG = hopeList;
        imageLoader = AppController.getInstance().getImageLoader();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_img_item_delete, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        this.holder = holder;
        path = LST_IMG.get(position);


        imageLoader.get(LST_IMG.get(position), new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    ImageDeleteAdapter.this.holder.ivSelectedImg.setImageBitmap(response.getBitmap());
                } else
                    Picasso.with(CONTEXT).load(new File(path)).resize(200, 200).into(holder.ivSelectedImg);
                //ImageDeleteAdapter.this.holder.ivSelectedImg.setImageURI(Uri.parse(path));
            }
        });
        holder.ivDeleteImg.setTag(position);
    }

    @Override
    public int getItemCount() {
        return LST_IMG.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivSelectedImg, ivDeleteImg;

        public MyViewHolder(View view) {
            super(view);
            ivSelectedImg = (ImageView) view.findViewById(R.id.ivSelectedImg);
            ivDeleteImg = (ImageView) view.findViewById(R.id.ivDeleteImg);
            ivDeleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) ivDeleteImg.getTag();
                    LST_IMG.remove(position);
                    notifyDataSetChanged();
                }
            });

        }
    }
}
