package percept.myplan.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import percept.myplan.Activities.StrategyDetailsOtherActivity;
import percept.myplan.AppController;
import percept.myplan.Global.Constant;
import percept.myplan.R;


/**
 * Created by percept on 2/5/15.
 */
public class dialogStrategyImg extends Dialog implements View.OnClickListener {
    private Context CONTEXT;
    private String IMG_LINK;
    private ImageView imgStrImageDetail;

    public dialogStrategyImg(Context context, String _imgLink) {

        super(context, R.style.DialogThemeCustom);
        this.CONTEXT = context;
        this.IMG_LINK = _imgLink;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_image_detail);
        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        imgStrImageDetail = (ImageView) findViewById(R.id.imgStrImageDetail);

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        imageLoader.get(IMG_LINK, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    imgStrImageDetail.setImageBitmap(response.getBitmap());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
