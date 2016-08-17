package percept.myplan.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Activities.AddContactActivity;
import percept.myplan.Activities.EmergencyContactActivity;
import percept.myplan.Activities.HelpListEditActivity;
import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Global.Utils;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.ContactDisplay;
import percept.myplan.R;
import percept.myplan.adapters.ContactHelpListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentContacts extends Fragment {

    public static final int INDEX = 3;
    private TextView TV_EMERGENCYNO, TV_EDIT_EMERGENCYNO, TV_EDIT_HELPLIST, TV_ADDCONTACT;
    private RecyclerView LST_HELP, LST_CONTACTS;
    private List<ContactDisplay> LIST_ALLCONTACTS;
    public static List<ContactDisplay> LIST_HELPCONTACTS;
    public static List<ContactDisplay> LIST_CONTACTS;
    public static HashMap<String, String> CONTACT_NAME;
    public static HashMap<String, String> HELP_CONTACT_NAME;
    public static String EMERGENCY_CONTACT_NAME;

    private ContactHelpListAdapter ADPT_CONTACTHELPLIST;

    private ContactHelpListAdapter ADPT_CONTACTLIST;
    public static boolean GET_CONTACTS = false;
    private Utils UTILS;
    private CoordinatorLayout REL_COORDINATE;
    public fragmentContacts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View _View = inflater.inflate(R.layout.fragment_contacts, container, false);


        TV_EMERGENCYNO = (TextView) _View.findViewById(R.id.tvEmergencyNo);
        TV_EDIT_EMERGENCYNO = (TextView) _View.findViewById(R.id.tvEditEmergencyContact);
        TV_EDIT_HELPLIST = (TextView) _View.findViewById(R.id.tvEditHelpList);
        TV_ADDCONTACT = (TextView) _View.findViewById(R.id.tvAddContact);

        LST_HELP = (RecyclerView) _View.findViewById(R.id.lstHelpList);
        LST_CONTACTS = (RecyclerView) _View.findViewById(R.id.lstContacts);

        REL_COORDINATE = (CoordinatorLayout) _View.findViewById(R.id.snakeBar);

        LIST_ALLCONTACTS = new ArrayList<>();
        LIST_HELPCONTACTS = new ArrayList<>();
        LIST_CONTACTS = new ArrayList<>();
        CONTACT_NAME = new HashMap<>();
        HELP_CONTACT_NAME = new HashMap<>();
        UTILS = new Utils(getActivity());

        if (!UTILS.getPreference("EMERGENCY_CONTACT_NAME").equals("")) {
            TV_EMERGENCYNO.setText(UTILS.getPreference("EMERGENCY_CONTACT_NAME"));
        } else {
            TV_EMERGENCYNO.setText("112");
        }

        GetContacts();
        ADPT_CONTACTHELPLIST = new ContactHelpListAdapter(LIST_HELPCONTACTS, "HELP");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        LST_HELP.setLayoutManager(mLayoutManager);
        LST_HELP.setItemAnimator(new DefaultItemAnimator());
        LST_HELP.setAdapter(ADPT_CONTACTHELPLIST);


        ADPT_CONTACTLIST = new ContactHelpListAdapter(LIST_CONTACTS, "CONTACT");

        RecyclerView.LayoutManager mLayoutManagerContact = new LinearLayoutManager(getActivity());
        LST_CONTACTS.setLayoutManager(mLayoutManagerContact);
        LST_CONTACTS.setItemAnimator(new DefaultItemAnimator());
        LST_CONTACTS.setAdapter(ADPT_CONTACTLIST);
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
                Intent _intent = new Intent(getActivity(), HelpListEditActivity.class);
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

        LST_HELP.addOnItemTouchListener(new fragmentContacts.RecyclerTouchListener(getActivity(), LST_HELP, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity(), LIST_ALLCONTACTS.get(position).getFirst_name(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        LST_CONTACTS.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), LST_CONTACTS, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity(), LIST_ALLCONTACTS.get(position).getFirst_name(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return _View;
    }

    private void GetContacts() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        try {
            clearData();
            new General().getJSONContentFromInternetService(getActivity(), General.PHPServices.GET_CONTACTS, params, true, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {

                }

                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    try {
                        LIST_ALLCONTACTS = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<ContactDisplay>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (ContactDisplay _obj : LIST_ALLCONTACTS) {
                        if (_obj.getHelplist().equals("0")) {
                            CONTACT_NAME.put(_obj.getId(), _obj.getFirst_name());
                            LIST_CONTACTS.add(_obj);
                        } else {
                            HELP_CONTACT_NAME.put(_obj.getId(), _obj.getFirst_name());
                            LIST_HELPCONTACTS.add(_obj);
                        }
                    }
                    ADPT_CONTACTHELPLIST = new ContactHelpListAdapter(LIST_HELPCONTACTS, "HELP");
                    LST_HELP.setAdapter(ADPT_CONTACTHELPLIST);

                    ADPT_CONTACTLIST = new ContactHelpListAdapter(LIST_CONTACTS, "CONTACT");
                    LST_CONTACTS.setAdapter(ADPT_CONTACTLIST);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar
                    .make(REL_COORDINATE, getResources().getString(R.string.nointernet), Snackbar.LENGTH_LONG)
                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GetContacts();
                        }
                    });

            // Changing message text color
            snackbar.setActionTextColor(Color.RED);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);

            snackbar.show();
        }
    }

    private void clearData() {
        LIST_ALLCONTACTS.clear();
        LIST_CONTACTS.clear();
        LIST_HELPCONTACTS.clear();
        CONTACT_NAME.clear();
        HELP_CONTACT_NAME.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GET_CONTACTS) {
            GetContacts();
            GET_CONTACTS = false;
        }
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

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private fragmentContacts.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final fragmentContacts.ClickListener clickListener) {
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
