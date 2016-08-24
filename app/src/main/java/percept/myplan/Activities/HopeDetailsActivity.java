package percept.myplan.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.HopeDetail;
import percept.myplan.R;
import percept.myplan.adapters.Basic3Adapter;
import percept.myplan.adapters.HopeDetailsAdapter;
import percept.myplan.toro.Toro;

public class HopeDetailsActivity extends AppCompatActivity {
    Map<String, String> params;
    private List<HopeDetail> LIST_HOPEDETAILS;
    private RecyclerView LST_HOPEDETAILS;
    private HopeDetailsAdapter ADAPTER;
    public static boolean GET_HOPE_DETAILS = false;

    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    private ProgressBar PB;
    private String HOPE_TITLE;
    private CoordinatorLayout REL_COORDINATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hope_details);
        Toro.attach(this);

        Toro.setStrategy(Toro.Strategies.MOST_VISIBLE_TOP_DOWN);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        if (getIntent().hasExtra("HOPE_TITLE")) {
            mTitle.setText(getIntent().getExtras().getString("HOPE_TITLE"));
            HOPE_TITLE = getIntent().getExtras().getString("HOPE_TITLE");
        } else
            mTitle.setText("Hope Box");

//        LST_HOPEDETAILS = (RecyclerView) findViewById(R.id.lstHopeDetails);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HopeDetailsActivity.this);
//        LST_HOPEDETAILS.setLayoutManager(mLayoutManager);
//        LST_HOPEDETAILS.setItemAnimator(new DefaultItemAnimator());


        mRecyclerView = (RecyclerView) findViewById(R.id.lstHopeDetails);

        REL_COORDINATE = (CoordinatorLayout) findViewById(R.id.snakeBar);

        PB = (ProgressBar) findViewById(R.id.pbgetHopeDetail);
        Toro.register(mRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HopeDetailsActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        if (layoutManager instanceof LinearLayoutManager) {
//            mRecyclerView.addItemDecoration(new DividerItemDecoration(HopeDetailsActivity.this,
//                    ((LinearLayoutManager) layoutManager).getOrientation()));
        }


        GetHopeDetails();
    }

    private void GetHopeDetails() {
        params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("id", getIntent().getExtras().getString("HOPE_ID"));
        try {
            PB.setVisibility(View.VISIBLE);
            new General().getJSONContentFromInternetService(HopeDetailsActivity.this, General.PHPServices.GET_HOPEBOX, params, true, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {
                    PB.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(JSONObject response) {
                    PB.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    try {
                        LIST_HOPEDETAILS = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<HopeDetail>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("::::::  ", String.valueOf(LIST_HOPEDETAILS.size()));
//                    ADAPTER = new HopeDetailsAdapter(HopeDetailsActivity.this, LIST_HOPEDETAILS);
//                    LST_HOPEDETAILS.setAdapter(ADAPTER);
                    mAdapter = new Basic3Adapter(HopeDetailsActivity.this, LIST_HOPEDETAILS, HOPE_TITLE);
                    mRecyclerView.setHasFixedSize(false);
                    mRecyclerView.setAdapter(mAdapter);
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
                            GetHopeDetails();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    protected void onDestroy() {
        Toro.detach(this);
        Toro.unregister(mRecyclerView);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Toro.detach(this);
        Toro.unregister(mRecyclerView);
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GET_HOPE_DETAILS) {
            GetHopeDetails();
            GET_HOPE_DETAILS = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_hope_element, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            HopeDetailsActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_InsertHopeElement) {

            Intent _intent = new Intent(HopeDetailsActivity.this, HopeDetailsAddElementActivity.class);
            _intent.putExtra("HOPE_NAME", getIntent().getExtras().getString("HOPE_TITLE"));
            _intent.putExtra("HOPE_ID", getIntent().getExtras().getString("HOPE_ID"));
            startActivity(_intent);

//            Toast.makeText(HopeDetailsActivity.this, "Add Hope Element clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
