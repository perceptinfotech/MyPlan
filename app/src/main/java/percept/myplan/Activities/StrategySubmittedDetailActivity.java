package percept.myplan.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.StrategyDetails;
import percept.myplan.R;
import percept.myplan.adapters.StrategySubmittedOthersAdapter;

import static percept.myplan.Activities.AddStrategyToSymptomActivity.GET_STRATEGIES;
import static percept.myplan.fragments.fragmentStrategies.ADDED_STRATEGIES;

/**
 * Created by percept on 19/8/16.
 */
public class StrategySubmittedDetailActivity extends AppCompatActivity {
    private RecyclerView RCV_STRATEGIESOTHERS;
    private TextView tvSubmittedByName, tvSubmittedEntries;
    private ProgressBar PB;

    private String USER_ID;
    private ArrayList<StrategyDetails> listStrategyDetails;

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
        PB = (ProgressBar) findViewById(R.id.progressBar);
        tvSubmittedByName = (TextView) findViewById(R.id.tvSubmittedByName);
        tvSubmittedEntries = (TextView) findViewById(R.id.tvSubmittedEntries);


        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        RCV_STRATEGIESOTHERS.setLayoutManager(llm);

        if (getIntent().hasExtra(Constant.USER_ID))
            USER_ID = getIntent().getStringExtra(Constant.USER_ID);
        tvSubmittedByName.setText(getString(R.string.user) + " "
                + getIntent().getStringExtra(Constant.CREATED_BY_NAME));
        Log.e(":::USER_ID", USER_ID);
        PB.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("user_id", USER_ID);
        try {
            new General().getJSONContentFromInternetService(StrategySubmittedDetailActivity.this,
                    General.PHPServices.GET_USER_STRATEGY, params,
                    false, false, true, new VolleyResponseListener() {
                        @Override
                        public void onError(VolleyError message) {
                            PB.setVisibility(View.GONE);
                        }

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(":::::StrategyDetail::", "" + response.toString());

                            Gson gson = new Gson();
                            try {
                                listStrategyDetails = gson.fromJson(response.getJSONArray(Constant.DATA)
                                        .toString(), new TypeToken<ArrayList<StrategyDetails>>() {
                                }.getType());
                                RCV_STRATEGIESOTHERS.setAdapter(new StrategySubmittedOthersAdapter(
                                        StrategySubmittedDetailActivity.this, listStrategyDetails));
                                PB.setVisibility(View.GONE);
                                if (listStrategyDetails.size() > 1)
                                    tvSubmittedEntries.setText(getString(R.string.submitted) + " " +
                                            listStrategyDetails.size() + " " + getString(R.string.entries));
                                else if (listStrategyDetails.size() > 0)
                                    tvSubmittedEntries.setText(getString(R.string.submitted) + " " +
                                            listStrategyDetails.size() + " " + getString(R.string.entry));
                                else
                                    tvSubmittedEntries.setText(getString(R.string.no_entry_submitted));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            StrategySubmittedDetailActivity.this.finish();
            return true;
        }
        return false;
    }

    public void addMyStrategy(int position) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("id", listStrategyDetails.get(position).getID());

        PB.setVisibility(View.VISIBLE);
        try {
            new General().getJSONContentFromInternetService(StrategySubmittedDetailActivity.this,
                    General.PHPServices.ADD_MYSTRATEGY, params, false, false, true, new VolleyResponseListener() {
                        @Override
                        public void onError(VolleyError message) {
                            PB.setVisibility(View.GONE);
                        }

                        @Override
                        public void onResponse(JSONObject response) {
                            PB.setVisibility(View.GONE);
                            Log.d("::::::: ", response.toString());
                            ADDED_STRATEGIES = true;
                            if (getIntent().hasExtra("FROM_SYMPTOM")) {
                                GET_STRATEGIES = true;
                            }
                            if (getIntent().hasExtra("FROM_SYMPTOM_INSPI")) {
                                GET_STRATEGIES = true;
                            }
                            setResult(RESULT_OK);
                            StrategySubmittedDetailActivity.this.finish();

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
