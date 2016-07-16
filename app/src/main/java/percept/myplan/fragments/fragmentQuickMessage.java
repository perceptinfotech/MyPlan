package percept.myplan.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import percept.myplan.Activities.AddContactActivity;
import percept.myplan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentQuickMessage extends Fragment {

    public static final int INDEX = 8;

    public fragmentQuickMessage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Quick Message");

        View _view = inflater.inflate(R.layout.fragment_quick_message, container, false);
        setHasOptionsMenu(true);
        return _view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_Quickmsg) {
//            Intent _intent = new Intent(getActivity().getApplicationContext(), AddContactActivity.class);
//            startActivity(_intent);

            return true;
        }
        return false;
    }
}
