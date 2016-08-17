package percept.myplan.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.Mood;
import percept.myplan.POJO.SidaSummary;
import percept.myplan.R;
import percept.myplan.adapters.MoodSummaryAdapter;
import percept.myplan.adapters.SidaSummaryAdapter;

public class SidasActivity extends AppCompatActivity {

    private Button BTN_TAKETEST;
    private RecyclerView LST_SIDASUMMARY;
    SidaSummaryAdapter ADAPTER;
    private List<SidaSummary> LIST_SIDA;
    Map<String, String> params;
    private ProgressBar PB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.sidas));

        LST_SIDASUMMARY = (RecyclerView) findViewById(R.id.lstSidaSummary);
        PB = (ProgressBar) findViewById(R.id.pbSidas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SidasActivity.this);
        LST_SIDASUMMARY.setLayoutManager(mLayoutManager);
        LST_SIDASUMMARY.setItemAnimator(new DefaultItemAnimator());
        LIST_SIDA = new ArrayList<>();
        BTN_TAKETEST = (Button) findViewById(R.id.btnTakeSidasTest);

        BTN_TAKETEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SidasActivity.this, SidaTestActivity.class));
            }
        });
PB.setVisibility(View.VISIBLE);
        params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        try {
            new General().getJSONContentFromInternetService(SidasActivity.this, General.PHPServices.GET_SIDACALENDER, params, false, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {
                    PB.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    try {
                        LIST_SIDA = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<SidaSummary>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PB.setVisibility(View.GONE);
                    ADAPTER = new SidaSummaryAdapter(SidasActivity.this, LIST_SIDA);
                    LST_SIDASUMMARY.setAdapter(ADAPTER);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SidasActivity.this.finish();
            return true;
        }
        return false;
    }
}
