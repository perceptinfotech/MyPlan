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

import percept.myplan.Activities.AddNewSymptomActivity;
import percept.myplan.Activities.SymptomDetailsActivity;
import percept.myplan.POJO.Symptom;
import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.R;
import percept.myplan.adapters.SymptomAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentSymptoms extends Fragment {

    public static final int INDEX = 1;

    private RecyclerView LST_SYMPTOM;
    private List<Symptom> LIST_SYMPTOM;
    private SymptomAdapter ADAPTER;
    private Button BTN_DANGERSIGNAL;
    private TextView TV_ADDNEW_SYMPTOM;
    public static Boolean GET_STRATEGY = false;

    private ProgressBar PB;

    public fragmentSymptoms() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Symptoms");
        View _View = inflater.inflate(R.layout.fragment_symptoms, container, false);
        LST_SYMPTOM = (RecyclerView) _View.findViewById(R.id.lstSymptom);
        BTN_DANGERSIGNAL = (Button) _View.findViewById(R.id.btnDangerSignal);
        PB = (ProgressBar) _View.findViewById(R.id.pbGetSymptoms);
        LIST_SYMPTOM = new ArrayList<>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        LST_SYMPTOM.setLayoutManager(mLayoutManager);
        LST_SYMPTOM.setItemAnimator(new DefaultItemAnimator());

        TV_ADDNEW_SYMPTOM = (TextView) _View.findViewById(R.id.tvAddNewSymptom);
        TV_ADDNEW_SYMPTOM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddNewSymptomActivity.class));
            }
        });

        try {
            PB.setVisibility(View.VISIBLE);
            new General().getJSONContentFromInternetService(getActivity(), General.PHPServices.GET_SYMPTOMS, params, false, false, false, new VolleyResponseListener() {
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
                        LIST_SYMPTOM = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<Symptom>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ADAPTER = new SymptomAdapter(LIST_SYMPTOM);
                    LST_SYMPTOM.setAdapter(ADAPTER);
                }
            });
        } catch (Exception e) {
            PB.setVisibility(View.GONE);
            e.printStackTrace();
        }

        LST_SYMPTOM.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), LST_SYMPTOM, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                LIST_SYMPTOM.get(position);
                Intent _intent = new Intent(getActivity(), SymptomDetailsActivity.class);
                _intent.putExtra("SYMPTOM_ID", LIST_SYMPTOM.get(position).getId());
                startActivity(_intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return _View;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fragmentSymptoms.GET_STRATEGY) {
            try {
                PB.setVisibility(View.VISIBLE);
                Map<String, String> params = new HashMap<String, String>();
                params.put("sid", Constant.SID);
                params.put("sname", Constant.SNAME);
                new General().getJSONContentFromInternetService(getActivity(), General.PHPServices.GET_SYMPTOMS, params, false, false, true, new VolleyResponseListener() {
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
                            LIST_SYMPTOM = gson.fromJson(response.getJSONArray(Constant.DATA)
                                    .toString(), new TypeToken<List<Symptom>>() {
                            }.getType());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ADAPTER = new SymptomAdapter(LIST_SYMPTOM);
                        LST_SYMPTOM.setAdapter(ADAPTER);
                    }
                });
            } catch (Exception e) {
                PB.setVisibility(View.GONE);
                e.printStackTrace();
            }
            fragmentSymptoms.GET_STRATEGY = false;
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private fragmentSymptoms.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final fragmentSymptoms.ClickListener clickListener) {
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
