package percept.myplan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.POJO.SymptomStrategy;
import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Global.VolleyResponseListener;
import percept.myplan.R;
import percept.myplan.adapters.SymptomStrategyAdapter;

public class SymptomDetailsActivity extends AppCompatActivity {

    private String SYMPTOM_ID;
    private TextView TV_TITLE, TV_TEXT;
    private RecyclerView LST_SYMPTOMSTRATEGY;
    private List<SymptomStrategy> LIST_SYMPTOMSTRATEGY;
    private SymptomStrategyAdapter ADAPTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.title_activity_symptoms));

        SYMPTOM_ID = getIntent().getExtras().getString("SYMPTOM_ID");
        TV_TITLE = (TextView) findViewById(R.id.tvTitle);
        TV_TEXT = (TextView) findViewById(R.id.tvText);
        LST_SYMPTOMSTRATEGY = (RecyclerView) findViewById(R.id.lstSymptomStrategy);
        LIST_SYMPTOMSTRATEGY = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SymptomDetailsActivity.this);
        LST_SYMPTOMSTRATEGY.setLayoutManager(mLayoutManager);
        LST_SYMPTOMSTRATEGY.setItemAnimator(new DefaultItemAnimator());

        GetSymptomDetail();
        // For opening strategy details of other
//        Intent _intent = new Intent(getActivity(), StrategyDetailsOtherActivity.class);
//        _intent.putExtra("STRATEGY_ID", LIST_STRATEGY.get(position).getId());
//        startActivity(_intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.symptoms_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SymptomDetailsActivity.this.finish();
        } else if (item.getItemId() == R.id.action_editSymptoms) {
            Toast.makeText(SymptomDetailsActivity.this, "edit Called", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void GetSymptomDetail() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("id", SYMPTOM_ID);
        try {
            new General().getJSONContentFromInternetService(SymptomDetailsActivity.this, General.PHPServices.GET_SYMPTOM, params, false, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {

                }

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(":::: ", response.toString());
                    try {
                        TV_TITLE.setText(response.getJSONObject(Constant.DATA).getString(Constant.TITLE));
                        TV_TEXT.setText(response.getJSONObject(Constant.DATA).getString(Constant.DESC));
                        Gson gson = new Gson();
                        try {
                            LIST_SYMPTOMSTRATEGY = gson.fromJson(response.getJSONObject(Constant.DATA).getJSONArray(Constant.STRATEGIE)
                                    .toString(), new TypeToken<List<SymptomStrategy>>() {
                            }.getType());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ADAPTER = new SymptomStrategyAdapter(LIST_SYMPTOMSTRATEGY);
                        LST_SYMPTOMSTRATEGY.setAdapter(ADAPTER);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
