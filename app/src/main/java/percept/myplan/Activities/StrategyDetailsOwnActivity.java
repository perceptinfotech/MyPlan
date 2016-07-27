package percept.myplan.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import percept.myplan.R;
import percept.myplan.adapters.ImageAdapter;

public class StrategyDetailsOwnActivity extends AppCompatActivity {


    private RecyclerView LST_OWNSTRATEGYIMG;
    private List<String> LIST_IMAGE;
    private ImageAdapter ADAPTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_details_own);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        LST_OWNSTRATEGYIMG = (RecyclerView) findViewById(R.id.lstOwnStrategyImage);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LST_OWNSTRATEGYIMG.setLayoutManager(mLayoutManager);
        LST_OWNSTRATEGYIMG.setItemAnimator(new DefaultItemAnimator());
        LIST_IMAGE = new ArrayList<>();
        LIST_IMAGE.add("dsada");
        LIST_IMAGE.add("dsada");
        LIST_IMAGE.add("dsada");
        LIST_IMAGE.add("dsada");
        LIST_IMAGE.add("dsada");

        ADAPTER = new ImageAdapter(StrategyDetailsOwnActivity.this, LIST_IMAGE);
        LST_OWNSTRATEGYIMG.setAdapter(ADAPTER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            StrategyDetailsOwnActivity.this.finish();
        }
        return false;
    }
}
