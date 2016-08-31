package percept.myplan.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import percept.myplan.Global.Constant;
import percept.myplan.POJO.HopeDetail;
import percept.myplan.R;

public class AddStrategyMusicActivity extends AppCompatActivity {

    public static boolean CLOSE_PAGE = false;
    private final int SET_LINK = 25;
    private TextView TV_CHOOSEFROMPHONE, TV_CHOOSEFROMLINK;
    private String FROM = "";
    private String HOPE_TITLE = "";
    private String HOPE_ID = "";
    private String HOPE_ELEMENT_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strategy_music);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.addmusic));

        if (getIntent().hasExtra("FROM_HOPE")) {
            FROM = getIntent().getExtras().getString("FROM_HOPE");
            HOPE_TITLE = getIntent().getExtras().getString("HOPE_TITLE");
            HOPE_ID = getIntent().getExtras().getString("HOPE_ID");
            if (getIntent().hasExtra(Constant.DATA)) {
                HopeDetail _Detail = (HopeDetail) getIntent().getExtras().getSerializable(Constant.DATA);
                HOPE_ELEMENT_ID = _Detail.getID();
            }
        }

        TV_CHOOSEFROMPHONE = (TextView) findViewById(R.id.tvChooseFromPhone);
        TV_CHOOSEFROMLINK = (TextView) findViewById(R.id.tvChooseFromLink);
        TV_CHOOSEFROMPHONE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
//                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                startActivityForResult(i, 1);

//                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
//                chooseFile.setType("audio/*");
//                chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                startActivityForResult(Intent.createChooser(chooseFile, "Choose a file") , 2);

                Intent _intent = new Intent(AddStrategyMusicActivity.this, MusicListActivity.class);

                if (getIntent().hasExtra("FROM_HOPE")) {
                    _intent.putExtra("HOPE_TITLE", HOPE_TITLE);
                    _intent.putExtra("FROM_HOPE", "FROM_HOPE");
                    _intent.putExtra("HOPE_ID", HOPE_ID);
                    _intent.putExtra("HOPE_ELEMENT_ID", HOPE_ELEMENT_ID);

                }

                if (getIntent().hasExtra("FROM_EDIT")) {
                    _intent.putExtra("FROM_EDIT", "TRUE");
                }

                startActivity(_intent);
            }
        });

        TV_CHOOSEFROMLINK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddStrategyMusicActivity.this, AddStrategyLinksActivity.class), SET_LINK);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CLOSE_PAGE) {
            AddStrategyMusicActivity.this.finish();
            CLOSE_PAGE = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SET_LINK:
                if (resultCode == RESULT_OK) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("LINK", data.getStringExtra("LINK"));
                    setResult(Activity.RESULT_OK, returnIntent);
                    AddStrategyMusicActivity.this.finish();
                }
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AddStrategyMusicActivity.this.finish();
            return true;
        }
        return false;
    }
}
