package percept.myplan.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
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

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.Contact;
import percept.myplan.POJO.ContactDisplay;
import percept.myplan.R;
import percept.myplan.adapters.ContactHelpListAdapter;
import percept.myplan.fragments.fragmentContacts;

import static percept.myplan.fragments.fragmentContacts.CONTACT_NAME;
import static percept.myplan.fragments.fragmentContacts.HELP_CONTACT_NAME;
import static percept.myplan.fragments.fragmentContacts.LIST_CONTACTS;
import static percept.myplan.fragments.fragmentContacts.LIST_HELPCONTACTS;

public class CreateQuickMsgActivity extends AppCompatActivity {


    private TextView TV_EDIT_HELPLIST, TV_ADD_CONTACT;
    private RecyclerView LST_HELP, LST_CONTACTS;


    private ContactHelpListAdapter ADPT_CONTACTHELPLIST;
    private List<ContactDisplay> LIST_ALLCONTACTS;
    private ContactHelpListAdapter ADPT_CONTACTLIST;
    private ProgressBar PB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quick_msg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.title_activity_create_quick_msg));

        LIST_ALLCONTACTS = new ArrayList<>();
        LIST_HELPCONTACTS = new ArrayList<>();
        LIST_CONTACTS = new ArrayList<>();
        CONTACT_NAME = new HashMap<>();

        TV_EDIT_HELPLIST = (TextView) findViewById(R.id.tvEditHelpList);
        TV_EDIT_HELPLIST.setVisibility(View.INVISIBLE);
        LST_HELP = (RecyclerView) findViewById(R.id.lstHelpList);
        LST_CONTACTS = (RecyclerView) findViewById(R.id.lstContacts);
        PB = (ProgressBar) findViewById(R.id.pbCreateQuickMsg);

        TV_ADD_CONTACT = (TextView) findViewById(R.id.tvAddContact);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CreateQuickMsgActivity.this);
        LST_HELP.setLayoutManager(mLayoutManager);
        LST_HELP.setItemAnimator(new DefaultItemAnimator());
        LST_HELP.setAdapter(ADPT_CONTACTHELPLIST);

        RecyclerView.LayoutManager mLayoutManagerContact = new LinearLayoutManager(CreateQuickMsgActivity.this);
        LST_CONTACTS.setLayoutManager(mLayoutManagerContact);
        LST_CONTACTS.setItemAnimator(new DefaultItemAnimator());
        LST_CONTACTS.setAdapter(ADPT_CONTACTHELPLIST);

        TV_ADD_CONTACT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(CreateQuickMsgActivity.this, AddContactActivity.class);
                _intent.putExtra("FROM_QUICKMSG", "FROM_QUICKMSG");
                startActivity(_intent);
            }
        });

        LST_HELP.addOnItemTouchListener(new RecyclerTouchListener(CreateQuickMsgActivity.this, LST_HELP, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                LIST_HELPCONTACTS.get(position);
                Toast.makeText(CreateQuickMsgActivity.this, LIST_HELPCONTACTS.get(position).getFirst_name(), Toast.LENGTH_SHORT).show();

                startActivity(new Intent(CreateQuickMsgActivity.this, SendMessageActivity.class));
                CreateQuickMsgActivity.this.finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        LST_CONTACTS.addOnItemTouchListener(new RecyclerTouchListener(CreateQuickMsgActivity.this, LST_HELP, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                LIST_CONTACTS.get(position);
                Toast.makeText(CreateQuickMsgActivity.this, LIST_CONTACTS.get(position).getFirst_name(), Toast.LENGTH_SHORT).show();

                startActivity(new Intent(CreateQuickMsgActivity.this, SendMessageActivity.class));
                CreateQuickMsgActivity.this.finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        getContacts();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fragmentContacts.GET_CONTACTS) {
            getContacts();
            fragmentContacts.GET_CONTACTS = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            CreateQuickMsgActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_addContact) {
            Intent _intent = new Intent(CreateQuickMsgActivity.this, AddContactActivity.class);
            _intent.putExtra("FROM_QUICKMSG", "FROM_QUICKMSG");
            startActivity(_intent);

            return true;
        }
        return false;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    private void getContacts() {
        PB.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        try {
            LIST_HELPCONTACTS.clear();
            CONTACT_NAME.clear();
            LIST_CONTACTS.clear();
            new General().getJSONContentFromInternetService(CreateQuickMsgActivity.this, General.PHPServices.GET_CONTACTS, params, false, false, false, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {
                    PB.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(JSONObject response) {
                    PB.setVisibility(View.GONE);
                    Log.d(":::::::::::::: ", response.toString());

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
        }
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private CreateQuickMsgActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final CreateQuickMsgActivity.ClickListener clickListener) {
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
