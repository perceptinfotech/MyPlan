package percept.myplan.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.SymptomStrategy;
import percept.myplan.R;
import percept.myplan.adapters.SymptomStrategyAdapter;
import percept.myplan.fragments.fragmentSymptoms;

public class AddNewSymptomActivity extends AppCompatActivity {

    private TextView TV_ADDSTRATEGY;

    private static final int ADDSTRATEGY = 3;
    private String STR_STRATEGYID = "";
    private EditText EDT_TITLE, EDT_DESC;
    private RecyclerView LST_SYMPTOMSTRATEGY;
    public static List<SymptomStrategy> LIST_ADDSYMPTOMSTRATEGY;
    private SymptomStrategyAdapter ADAPTER;
    private ProgressBar PB;
    private CoordinatorLayout REL_COORDINATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_symptom);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.title_activity_addsymptom));

        TV_ADDSTRATEGY = (TextView) findViewById(R.id.tvAddStrategy);
        PB = (ProgressBar) findViewById(R.id.pbAddSymptom);

        LST_SYMPTOMSTRATEGY = (RecyclerView) findViewById(R.id.lstSymptomStrategy);
        LIST_ADDSYMPTOMSTRATEGY = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AddNewSymptomActivity.this);
        LST_SYMPTOMSTRATEGY.setLayoutManager(mLayoutManager);
        LST_SYMPTOMSTRATEGY.setItemAnimator(new DefaultItemAnimator());
        REL_COORDINATE = (CoordinatorLayout) findViewById(R.id.snakeBar);

        TV_ADDSTRATEGY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddNewSymptomActivity.this, AddStrategyToSymptomActivity.class), ADDSTRATEGY);
            }
        });

        EDT_TITLE = (EditText) findViewById(R.id.edtTitle);
        EDT_DESC = (EditText) findViewById(R.id.edtDescription);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_symptom, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADDSTRATEGY) {
            if (resultCode == Activity.RESULT_OK) {
                STR_STRATEGYID = data.getStringExtra("result");
                ADAPTER = new SymptomStrategyAdapter(LIST_ADDSYMPTOMSTRATEGY);
                LST_SYMPTOMSTRATEGY.setAdapter(ADAPTER);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AddNewSymptomActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_saveSymptom) {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            AddNewSymptom();
            return true;
        }
        return false;
    }

    private void AddNewSymptom() {
        PB.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("title", EDT_TITLE.getText().toString().trim());
        params.put("description", EDT_DESC.getText().toString().trim());
        params.put("strategy_id", STR_STRATEGYID);
        params.put("state", "1");
        params.put("id", "");

        try {
            new General().getJSONContentFromInternetService(AddNewSymptomActivity.this, General.PHPServices.SAVE_SYMPTOM, params, true, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {
                    PB.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(JSONObject response) {
                    fragmentSymptoms.GET_STRATEGY = true;
                    Log.d(":::::", response.toString());
                    PB.setVisibility(View.GONE);
                    AddNewSymptomActivity.this.finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            PB.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(REL_COORDINATE, getResources().getString(R.string.nointernet), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AddNewSymptom();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
