package percept.myplan.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.crosswall.photo.pick.PickConfig;
import percept.myplan.R;

import static percept.myplan.Activities.AddStrategyActivity.LIST_IMG;

public class AddStrategyImageActivity extends AppCompatActivity {

    private TextView TV_CHOOSEEXISTING, TV_TAKENEW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strategy_image);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.addimage));


        TV_CHOOSEEXISTING = (TextView) findViewById(R.id.tvChooseExisting);
        TV_TAKENEW = (TextView) findViewById(R.id.tvTakeNew);

        TV_CHOOSEEXISTING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PickConfig.Builder(AddStrategyImageActivity.this)
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

        TV_TAKENEW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == PickConfig.PICK_REQUEST_CODE) {
            LIST_IMG = data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);
            Toast.makeText(AddStrategyImageActivity.this, "pick size: " + LIST_IMG.size(), Toast.LENGTH_SHORT).show();
            AddStrategyImageActivity.this.finish();
        }
    }
}
