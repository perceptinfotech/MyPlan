package percept.myplan.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Classes.Symptom;
import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Global.VolleyResponseListener;
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
        LIST_SYMPTOM = new ArrayList<>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        try {
            new General().getJSONContentFromInternetService(getActivity(), General.PHPServices.GET_SYMPTOMS, params, false, false, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {

                }

                @Override
                public void onResponse(JSONObject response) {

                    Log.d(":::: ", response.toString());
                    Gson gson = new Gson();
                    try {
                        LIST_SYMPTOM = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<Symptom>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("::::::::", "::::::::");
                    ADAPTER = new SymptomAdapter(LIST_SYMPTOM);
                    LST_SYMPTOM.setAdapter(ADAPTER);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        return _View;
    }

}
