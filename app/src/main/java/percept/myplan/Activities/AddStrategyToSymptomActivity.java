package percept.myplan.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.Strategy;
import percept.myplan.R;
import percept.myplan.adapters.StrategyAdapter;
import percept.myplan.adapters.StrategySelectionAdapter;

public class AddStrategyToSymptomActivity extends AppCompatActivity {


    private TextView TV_ADDSTRATEGY;
    private RecyclerView LST_STRATEGY;
    private List<Strategy> LIST_STRATEGY;
    private StrategySelectionAdapter ADAPTER;
    private Button BTN_INSPIRATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strategy_to_symptom);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.addastrategy));


        TV_ADDSTRATEGY = (TextView) findViewById(R.id.tvAddNewSymptom);

        TV_ADDSTRATEGY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        LST_STRATEGY = (RecyclerView) findViewById(R.id.lstStrategy);
        BTN_INSPIRATION = (Button) findViewById(R.id.btnInspiration);

        LIST_STRATEGY = new ArrayList<>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AddStrategyToSymptomActivity.this);
        LST_STRATEGY.setLayoutManager(mLayoutManager);
        LST_STRATEGY.setItemAnimator(new DefaultItemAnimator());


        try {
            new General().getJSONContentFromInternetService(AddStrategyToSymptomActivity.this, General.PHPServices.GET_STRATEGIES, params, false, false, false, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {

                }

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(":::: ", response.toString());
                    Gson gson = new Gson();
                    try {
                        LIST_STRATEGY = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<Strategy>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ADAPTER = new StrategySelectionAdapter(LIST_STRATEGY);
                    LST_STRATEGY.setAdapter(ADAPTER);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        LST_STRATEGY.addOnItemTouchListener(new RecyclerTouchListener(AddStrategyToSymptomActivity.this, LST_STRATEGY, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                LIST_STRATEGY.get(position);
//                Intent _intent = new Intent(AddStrategyToSymptomActivity.this, StrategyDetailsOwnActivity.class);
//                _intent.putExtra("STRATEGY_ID", LIST_STRATEGY.get(position).getId());
//                startActivity(_intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        BTN_INSPIRATION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sid", Constant.SID);
                params.put("sname", Constant.SNAME);
                try {
                    new General().getJSONContentFromInternetService(AddStrategyToSymptomActivity.this, General.PHPServices.GET_INSPIRATIONS, params, false, false, false, new VolleyResponseListener() {
                        @Override
                        public void onError(VolleyError message) {
                            Log.d("::::::::::: ", ":::");
                        }

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("::::::::::: ", response.toString());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent returnIntent = new Intent();
//            returnIntent.putExtra("result",result);
            setResult(Activity.RESULT_OK, returnIntent);
            AddStrategyToSymptomActivity.this.finish();
            return true;
        }
        return false;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
