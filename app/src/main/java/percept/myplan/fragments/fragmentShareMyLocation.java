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
public class fragmentShareMyLocation extends Fragment {

    public static final int INDEX = 9;
    public fragmentShareMyLocation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Share My Location");
        return inflater.inflate(R.layout.fragment_share_my_location, container, false);
    }

}
