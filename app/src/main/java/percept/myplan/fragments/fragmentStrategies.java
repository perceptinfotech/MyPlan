package percept.myplan.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import percept.myplan.Activities.AddStrategyActivity;
import percept.myplan.Activities.InspirationCategoryActivity;
import percept.myplan.Activities.StrategyDetailsOwnActivity;
import percept.myplan.POJO.Strategy;
import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.R;
import percept.myplan.adapters.StrategyAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentStrategies extends Fragment {

    public static final int INDEX = 2;
    private RecyclerView LST_STRATEGY;
    private List<Strategy> LIST_STRATEGY;
    private StrategyAdapter ADAPTER;
    private Button BTN_INSPIRATION;
    private TextView TV_ADDNEWSTRATEGY;
    public static boolean ADDED_STRATEGIES = false;
    private ProgressBar PB;
    Map<String, String> params;

    public fragmentStrategies() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Strategies");
        View _View = inflater.inflate(R.layout.fragment_strategies, container, false);

        LST_STRATEGY = (RecyclerView) _View.findViewById(R.id.lstStrategy);
        BTN_INSPIRATION = (Button) _View.findViewById(R.id.btnInspiration);
        TV_ADDNEWSTRATEGY = (TextView) _View.findViewById(R.id.tvAddNewStrategy);
        PB = (ProgressBar) _View.findViewById(R.id.pbGetStrategies);
        setHasOptionsMenu(true);
        LIST_STRATEGY = new ArrayList<>();
        params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        LST_STRATEGY.setLayoutManager(mLayoutManager);
        LST_STRATEGY.setItemAnimator(new DefaultItemAnimator());

        TV_ADDNEWSTRATEGY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddStrategyActivity.class));
            }
        });

        try {
            PB.setVisibility(View.VISIBLE);
            new General().getJSONContentFromInternetService(getActivity(), General.PHPServices.GET_STRATEGIES, params, false, false, false, new VolleyResponseListener() {
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
                        LIST_STRATEGY = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<Strategy>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ADAPTER = new StrategyAdapter(LIST_STRATEGY);
                    LST_STRATEGY.setAdapter(ADAPTER);
                }
            });
        } catch (Exception e) {
            PB.setVisibility(View.GONE);
            e.printStackTrace();
        }

        LST_STRATEGY.addOnItemTouchListener(new fragmentStrategies.RecyclerTouchListener(getActivity(), LST_STRATEGY, new fragmentStrategies.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                LIST_STRATEGY.get(position);
                Intent _intent = new Intent(getActivity(), StrategyDetailsOwnActivity.class);
                _intent.putExtra("STRATEGY_ID", LIST_STRATEGY.get(position).getID());
                _intent.putExtra("STRATEGY_NAME", LIST_STRATEGY.get(position).getTitle());
                startActivity(_intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        BTN_INSPIRATION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent _intent = new Intent(getActivity(), InspirationCategoryActivity.class);
                startActivity(_intent);
            }
        });
        return _View;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.strategy, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ADDED_STRATEGIES) {
            try {
                PB.setVisibility(View.VISIBLE);
                new General().getJSONContentFromInternetService(getActivity(), General.PHPServices.GET_STRATEGIES, params, false, false, true, new VolleyResponseListener() {
                    @Override
                    public void onError(VolleyError message) {
                        PB.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        PB.setVisibility(View.GONE);
                        LIST_STRATEGY.clear();
                        Log.d(":::: ", response.toString());
                        Gson gson = new Gson();
                        try {
                            LIST_STRATEGY = gson.fromJson(response.getJSONArray(Constant.DATA)
                                    .toString(), new TypeToken<List<Strategy>>() {
                            }.getType());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ADAPTER = new StrategyAdapter(LIST_STRATEGY);
                        LST_STRATEGY.setAdapter(ADAPTER);
                    }
                });
            } catch (Exception e) {
                PB.setVisibility(View.GONE);
                e.printStackTrace();
            }
            ADDED_STRATEGIES = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_addStrategy) {
//            Intent _intent = new Intent(getActivity().getApplicationContext(), AddContactActivity.class);
//            startActivity(_intent);

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
        private fragmentStrategies.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final fragmentStrategies.ClickListener clickListener) {
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
