package percept.myplan.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Dialogs.dialogAddStrategy;
import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.InspirationWiseStrategy;
import percept.myplan.POJO.Strategy;
import percept.myplan.R;
import percept.myplan.adapters.InspirationWiseStrategyAdapter;

import static percept.myplan.Activities.AddStrategyToSymptomActivity.GET_STRATEGIES;
import static percept.myplan.fragments.fragmentStrategies.ADDED_STRATEGIES;


public class StrategyDetailsOtherActivity extends AppCompatActivity {

    private String STRATEGY_ID;
    private ProgressBar PB;
    private Strategy clsStrategy;
    private TextView TV_DESCRIPTION, TV_USEDBY, TV_SUBMITTEDBY, TV_CATEGORY;
    private Button BTN_ADDTOMYSTRATEGIES;
    Map<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_details_other);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.title_activity_strategy_details_other));

        PB = (ProgressBar) findViewById(R.id.pbGetStrategy);
        TV_DESCRIPTION = (TextView) findViewById(R.id.tvDescription);
        TV_USEDBY = (TextView) findViewById(R.id.tvUsedBy);
        TV_SUBMITTEDBY = (TextView) findViewById(R.id.tvSubmittedBy);
        TV_CATEGORY = (TextView) findViewById(R.id.tvCategory);

        BTN_ADDTOMYSTRATEGIES = (Button) findViewById(R.id.btnAddToMyStrategies);

        if (getIntent().hasExtra("FROM_SYMPTOM")) {
            BTN_ADDTOMYSTRATEGIES.setVisibility(View.GONE);
        }

        if (getIntent().hasExtra("STRATEGY_ID")) {
            STRATEGY_ID = getIntent().getExtras().getString("STRATEGY_ID");
            mTitle.setText(getIntent().getExtras().getString("STRATEGY_NAME"));
        }
        params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("id", STRATEGY_ID);

        try {
            PB.setVisibility(View.VISIBLE);
            new General().getJSONContentFromInternetService(StrategyDetailsOtherActivity.this, General.PHPServices.GET_STRATEGY, params,
                    false, false, true, new VolleyResponseListener() {
                        @Override
                        public void onError(VolleyError message) {
                            PB.setVisibility(View.GONE);
                        }

                        @Override
                        public void onResponse(JSONObject response) {

                            PB.setVisibility(View.GONE);
                            Log.d(":::: ", response.toString());
                            Gson gson = new Gson();
                            try {
                                clsStrategy = gson.fromJson(response.getJSONObject(Constant.DATA)
                                        .toString(), new TypeToken<Strategy>() {
                                }.getType());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("::: Strategy Details", clsStrategy.getID());
                            TV_DESCRIPTION.setText(clsStrategy.getDescription());
                            TV_USEDBY.setText("1234");
                            TV_SUBMITTEDBY.setText("ID " + clsStrategy.getCreatedBy());

                            if (getIntent().hasExtra("CATEGORY_NAME"))
                                TV_CATEGORY.setText(getIntent().getExtras().getString("CATEGORY_NAME"));
                            else
                                TV_CATEGORY.setText(clsStrategy.getCategoryName());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        BTN_ADDTOMYSTRATEGIES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogAddStrategy _dialogDate = new dialogAddStrategy(StrategyDetailsOtherActivity.this) {
                    @Override
                    public void onClickYes() {
                        dismiss();
                        PB.setVisibility(View.VISIBLE);
                        try {
                            new General().getJSONContentFromInternetService(StrategyDetailsOtherActivity.this,
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
                                            StrategyDetailsOtherActivity.this.finish();

                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onClickNo() {
                        dismiss();
                    }
                };
                _dialogDate.setCanceledOnTouchOutside(false);
                _dialogDate.show();

            }
        });
        TV_SUBMITTEDBY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StrategyDetailsOtherActivity.this, StrategySubmittedDetailActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            StrategyDetailsOtherActivity.this.finish();
            return true;
        }
        return false;
    }
}
