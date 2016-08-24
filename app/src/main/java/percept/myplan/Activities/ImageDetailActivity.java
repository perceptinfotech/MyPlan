package percept.myplan.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import percept.myplan.AppController;
import percept.myplan.R;

public class ImageDetailActivity extends AppCompatActivity {

    private ImageView IMG_DETAILS;
    private String IMG_LINK = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        mTitle.setText(getResources().getString(R.string.sidas));
        mTitle.setText(getIntent().getExtras().getString("TITLE"));
        if (getIntent().hasExtra("IMG_LINK"))
            IMG_LINK = getIntent().getExtras().getString("IMG_LINK");
        IMG_DETAILS = (ImageView) findViewById(R.id.imgImageDetail);

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        imageLoader.get(IMG_LINK, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    IMG_DETAILS.setImageBitmap(response.getBitmap());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ImageDetailActivity.this.finish();
        }
        return false;
    }
}
