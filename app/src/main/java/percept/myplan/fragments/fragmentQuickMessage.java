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

import percept.myplan.Activities.CreateQuickMsgActivity;
import percept.myplan.POJO.QuickMessage;
import percept.myplan.R;
import percept.myplan.adapters.QuickMessageAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentQuickMessage extends Fragment {

    public static final int INDEX = 8;

    private RecyclerView LSTQUICKMSG;
    private List<QuickMessage> LIST_QUICKMSG;
    private QuickMessageAdapter ADAPTER;

    private TextView TV_ADDNEWMSG;

    public fragmentQuickMessage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Quick Message");

        View _view = inflater.inflate(R.layout.fragment_quick_message, container, false);

        TV_ADDNEWMSG= (TextView) _view.findViewById(R.id.tvAddNewMessage);

        LSTQUICKMSG = (RecyclerView) _view.findViewById(R.id.lstQuickMsg);
        LIST_QUICKMSG = new ArrayList<>();
        LIST_QUICKMSG.add(new QuickMessage("Message 1", "Mom"));
        LIST_QUICKMSG.add(new QuickMessage("Message 2", "Dad"));
        LIST_QUICKMSG.add(new QuickMessage("Message 3", "Bro"));
        ADAPTER = new QuickMessageAdapter(LIST_QUICKMSG);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        LSTQUICKMSG.setLayoutManager(mLayoutManager);
        LSTQUICKMSG.setItemAnimator(new DefaultItemAnimator());
        LSTQUICKMSG.setAdapter(ADAPTER);
        setHasOptionsMenu(true);

        TV_ADDNEWMSG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent _intent = new Intent(getActivity().getApplicationContext(), CreateQuickMsgActivity.class);
            startActivity(_intent);

            }
        });
        return _view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.quickmessage, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_Quickmsg) {
            Intent _intent = new Intent(getActivity().getApplicationContext(), CreateQuickMsgActivity.class);
            startActivity(_intent);

            return true;
        }
        return false;
    }
}
