package percept.myplan.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import percept.myplan.R;

public class HopeDetailsAddElementActivity extends AppCompatActivity {

    private EditText EDT_TITLE;
    private TextView TV_ADDIMAGE, TV_ADDMUSIC, TV_ADDVIDEO, TV_ADDNOTE, TV_ADDLINK;

    private String HOPE_NAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hope_details_add_element);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.newelement));

        if (getIntent().hasExtra("HOPE_NAME"))
            HOPE_NAME = getIntent().getExtras().getString("HOPE_NAME");

        EDT_TITLE = (EditText) findViewById(R.id.edtHopeElementTitle);
        TV_ADDIMAGE = (TextView) findViewById(R.id.tvChooseHopElementImage);
        TV_ADDMUSIC = (TextView) findViewById(R.id.tvChooseHopElementMusic);
        TV_ADDVIDEO = (TextView) findViewById(R.id.tvChooseHopElementVideo);
        TV_ADDNOTE = (TextView) findViewById(R.id.tvChooseHopElementNote);
        TV_ADDLINK = (TextView) findViewById(R.id.tvChooseHopElementLink);

        TV_ADDIMAGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(HopeDetailsAddElementActivity.this, AddStrategyImageActivity.class);
                _intent.putExtra("HOPE_TITLE", EDT_TITLE.getText().toString().trim());
                _intent.putExtra("FROM_HOPE", "FROM_HOPE");
                _intent.putExtra("HOPE_ID", getIntent().getExtras().getString("HOPE_ID"));
                startActivity(_intent);
                HopeDetailsAddElementActivity.this.finish();
            }
        });

        TV_ADDMUSIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(HopeDetailsAddElementActivity.this, AddStrategyMusicActivity.class);
                _intent.putExtra("HOPE_TITLE", EDT_TITLE.getText().toString().trim());
                _intent.putExtra("FROM_HOPE", "FROM_HOPE");
                _intent.putExtra("HOPE_ID", getIntent().getExtras().getString("HOPE_ID"));
                startActivity(_intent);
                HopeDetailsAddElementActivity.this.finish();
            }
        });

        TV_ADDVIDEO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TV_ADDNOTE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TV_ADDLINK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(HopeDetailsAddElementActivity.this, AddStrategyLinksActivity.class);
                _intent.putExtra("HOPE_TITLE", EDT_TITLE.getText().toString().trim());
                _intent.putExtra("FROM_HOPE", "FROM_HOPE");
                _intent.putExtra("HOPE_ID", getIntent().getExtras().getString("HOPE_ID"));
                startActivity(_intent);
                HopeDetailsAddElementActivity.this.finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            HopeDetailsAddElementActivity.this.finish();
        }
        return false;
    }
}
