package percept.myplan.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import percept.myplan.R;
import percept.myplan.adapters.StrategySubmittedOthersAdapter;

/**
 * Created by percept on 19/8/16.
 */
public class StrategySubmittedDetailActivity extends AppCompatActivity {
    private RecyclerView RCV_STRATEGIESOTHERS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_submitted_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.title_activity_strategy_details_other_submitted));

        RCV_STRATEGIESOTHERS = (RecyclerView) findViewById(R.id.rcvStrategyiesOthers);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        RCV_STRATEGIESOTHERS.setLayoutManager(llm);
        RCV_STRATEGIESOTHERS.setAdapter(new StrategySubmittedOthersAdapter(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            StrategySubmittedDetailActivity.this.finish();
            return true;
        }
        return false;
    }
}
