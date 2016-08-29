package percept.myplan.Activities;

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
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import java.util.List;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.InspirationCategory;
import percept.myplan.R;
import percept.myplan.adapters.InspirationCategoryAdapter;

import static percept.myplan.Activities.AddStrategyToSymptomActivity.GET_STRATEGIES;
import static percept.myplan.fragments.fragmentStrategies.ADDED_STRATEGIES;

public class InspirationCategoryActivity extends AppCompatActivity {

    private RecyclerView LST_INSPIRATION_CATEGORY;

    private List<InspirationCategory> LIST_CATEGORY;
    private InspirationCategoryAdapter ADAPTER;
    private ProgressBar PB;
    private CoordinatorLayout REL_COORDINATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspiration_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.inspiration));

        LST_INSPIRATION_CATEGORY = (RecyclerView) findViewById(R.id.lstInsporationCategory);

        REL_COORDINATE = (CoordinatorLayout) findViewById(R.id.snakeBar);

        PB = (ProgressBar) findViewById(R.id.pbInspiCate);
        LIST_CATEGORY = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(InspirationCategoryActivity.this);
        LST_INSPIRATION_CATEGORY.setLayoutManager(mLayoutManager);
        LST_INSPIRATION_CATEGORY.setItemAnimator(new DefaultItemAnimator());
        LST_INSPIRATION_CATEGORY.addOnItemTouchListener(new RecyclerTouchListener(InspirationCategoryActivity.this, LST_INSPIRATION_CATEGORY, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                LIST_CATEGORY.get(position);
                Intent _intent = new Intent(InspirationCategoryActivity.this, CategoryStrategyActivity.class);
                _intent.putExtra("CATEGORY_NAME", LIST_CATEGORY.get(position).getCategoryName());
                _intent.putExtra("CATEGORY_ID", LIST_CATEGORY.get(position).getCategoryId());
                if (getIntent().hasExtra("FROM_SYMPTOM_INSPI"))
                    _intent.putExtra("FROM_SYMPTOM_INSPI", getIntent().getExtras().getString("FROM_SYMPTOM_INSPI"));
                startActivity(_intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        GetInspirationCategory();
    }

    private void GetInspirationCategory() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        try {
            PB.setVisibility(View.VISIBLE);
            new General().getJSONContentFromInternetService(InspirationCategoryActivity.this, General.PHPServices.GET_CATEGORIES, params, true, false, false, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {
                    PB.setVisibility(View.GONE);
                    Log.d("::::::::::: Error", message.getMessage()+"");
                }

                @Override
                public void onResponse(JSONObject response) {
                    PB.setVisibility(View.GONE);
                    Log.d(":::: ", response.toString());
                    Gson gson = new Gson();
                    try {
                        LIST_CATEGORY = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<InspirationCategory>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ADAPTER = new InspirationCategoryAdapter(LIST_CATEGORY);
                    LST_INSPIRATION_CATEGORY.setAdapter(ADAPTER);
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
                            GetInspirationCategory();
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
    protected void onResume() {
        super.onResume();
        if (ADDED_STRATEGIES) {
            InspirationCategoryActivity.this.finish();
        }
        if (GET_STRATEGIES) {
            InspirationCategoryActivity.this.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            InspirationCategoryActivity.this.finish();
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
