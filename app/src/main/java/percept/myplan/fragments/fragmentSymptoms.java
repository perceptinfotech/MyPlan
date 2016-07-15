package percept.myplan.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import percept.myplan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentSymptoms extends Fragment {

    public static final int INDEX = 1;
    public fragmentSymptoms() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Symptoms");
        return inflater.inflate(R.layout.fragment_symptoms, container, false);
    }

}
