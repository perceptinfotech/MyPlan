package percept.myplan.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.HopeDetail;
import percept.myplan.R;
import percept.myplan.adapters.HopeDetailsAdapter;

public class HopeDetailsActivity extends AppCompatActivity {
    Map<String, String> params;
    private List<HopeDetail> LIST_HOPEDETAILS;
    private RecyclerView LST_HOPEDETAILS;
    private HopeDetailsAdapter ADAPTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hope_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        if (getIntent().hasExtra("HOPE_TITLE"))
            mTitle.setText(getIntent().getExtras().getString("HOPE_TITLE"));
        else
            mTitle.setText("Hope Box");

        LST_HOPEDETAILS = (RecyclerView) findViewById(R.id.lstHopeDetails);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HopeDetailsActivity.this);
        LST_HOPEDETAILS.setLayoutManager(mLayoutManager);
        LST_HOPEDETAILS.setItemAnimator(new DefaultItemAnimator());

        params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("id", getIntent().getExtras().getString("HOPE_ID"));
        try {
            new General().getJSONContentFromInternetService(HopeDetailsActivity.this, General.PHPServices.GET_HOPEBOX, params, false, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {

                }

                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    try {
                        LIST_HOPEDETAILS = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<HopeDetail>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("::::::  ", String.valueOf(LIST_HOPEDETAILS.size()));
                    ADAPTER = new HopeDetailsAdapter(HopeDetailsActivity.this, LIST_HOPEDETAILS);
                    LST_HOPEDETAILS.setAdapter(ADAPTER);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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

            Toast.makeText(HopeDetailsActivity.this, "Add Hope Element clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
