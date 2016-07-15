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
public class fragmentContacts extends Fragment {

    public static final int INDEX = 3;


    public fragmentContacts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View _View = inflater.inflate(R.layout.fragment_contacts, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Contacts");

        setHasOptionsMenu(true);

        return _View;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_addContact) {
            Intent _intent = new Intent(getActivity().getApplicationContext(), AddContactActivity.class);
            startActivity(_intent);

            return true;
        }
        return false;
    }


}
