package percept.myplan.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import percept.myplan.Activities.AddContactActivity;
import percept.myplan.Activities.EmergencyContactActivity;
import percept.myplan.Activities.HelpListActivity;
import percept.myplan.POJO.Contact;
import percept.myplan.R;
import percept.myplan.adapters.ContactHelpListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentContacts extends Fragment {

    public static final int INDEX = 3;
    private TextView TV_EMERGENCYNO, TV_EDIT_EMERGENCYNO, TV_EDIT_HELPLIST, TV_ADDCONTACT;
    private RecyclerView LST_HELP, LST_CONTACTS;
    private List<Contact> LIST_HELPCONTACT;

    private ContactHelpListAdapter ADPT_CONTACTHELPLIST;


    public fragmentContacts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View _View = inflater.inflate(R.layout.fragment_contacts, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Contacts");

        TV_EMERGENCYNO = (TextView) _View.findViewById(R.id.tvEmergencyNo);
        TV_EDIT_EMERGENCYNO = (TextView) _View.findViewById(R.id.tvEditEmergencyContact);
        TV_EDIT_HELPLIST = (TextView) _View.findViewById(R.id.tvEditHelpList);
        TV_ADDCONTACT = (TextView) _View.findViewById(R.id.tvAddContact);

        LST_HELP = (RecyclerView) _View.findViewById(R.id.lstHelpList);
        LST_CONTACTS = (RecyclerView) _View.findViewById(R.id.lstContacts);

        LIST_HELPCONTACT = new ArrayList<>();
        LIST_HELPCONTACT.add(new Contact("Children Phone", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Paul", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Mom", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Madelaine", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Kate", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Jenna", "1234567890", false));
        ADPT_CONTACTHELPLIST = new ContactHelpListAdapter(LIST_HELPCONTACT);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        LST_HELP.setLayoutManager(mLayoutManager);
        LST_HELP.setItemAnimator(new DefaultItemAnimator());
        LST_HELP.setAdapter(ADPT_CONTACTHELPLIST);


        TV_EMERGENCYNO.setText("112");

        RecyclerView.LayoutManager mLayoutManagerContact = new LinearLayoutManager(getActivity());
        LST_CONTACTS.setLayoutManager(mLayoutManagerContact);
        LST_CONTACTS.setItemAnimator(new DefaultItemAnimator());
        LST_CONTACTS.setAdapter(ADPT_CONTACTHELPLIST);
        setHasOptionsMenu(true);

        TV_EDIT_EMERGENCYNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(getActivity(), EmergencyContactActivity.class);
                startActivity(_intent);
            }
        });

        TV_EDIT_HELPLIST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(getActivity(), HelpListActivity.class);
                startActivity(_intent);
            }
        });

        TV_ADDCONTACT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(getActivity().getApplicationContext(), AddContactActivity.class);
                startActivity(_intent);
            }
        });
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
