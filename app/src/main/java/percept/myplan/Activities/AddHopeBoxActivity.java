package percept.myplan.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import me.crosswall.photo.pick.PickConfig;
import percept.myplan.R;

public class AddHopeBoxActivity extends AppCompatActivity {

    private Button BTN_ADDFOLDERIMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hope_box);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        BTN_ADDFOLDERIMG = (Button) findViewById(R.id.btnAddFolderImage);

        BTN_ADDFOLDERIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PickConfig.Builder(AddHopeBoxActivity.this)
                        .pickMode(PickConfig.MODE_MULTIP_PICK)
                        .maxPickSize(10)
                        .spanCount(3)
                        //.showGif(true)
                        .checkImage(false) //default false
                        .useCursorLoader(false) //default true
                        .toolbarColor(R.color.colorPrimary)
                        .build();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_hopebox, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AddHopeBoxActivity.this.finish();
        } else if (item.getItemId() == R.id.action_addHopeBox) {
            Toast.makeText(AddHopeBoxActivity.this, "Saved Called", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
