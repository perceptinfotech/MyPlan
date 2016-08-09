package percept.myplan.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import percept.myplan.POJO.Contact;
import percept.myplan.POJO.ContactDisplay;
import percept.myplan.R;
import percept.myplan.adapters.ContactHelpListAdapter;

public class CreateQuickMsgActivity extends AppCompatActivity {


    private TextView TV_EDIT_HELPLIST, TV_ADD_CONTACT;
    private RecyclerView LST_HELP, LST_CONTACTS;
    private List<ContactDisplay> LIST_HELPCONTACT;
    private ContactHelpListAdapter ADPT_CONTACTHELPLIST;

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

        TV_EDIT_HELPLIST = (TextView) findViewById(R.id.tvEditHelpList);
        TV_EDIT_HELPLIST.setVisibility(View.INVISIBLE);
        LST_HELP = (RecyclerView) findViewById(R.id.lstHelpList);
        LST_CONTACTS = (RecyclerView) findViewById(R.id.lstContacts);

        LIST_HELPCONTACT = new ArrayList<>();
        ADPT_CONTACTHELPLIST = new ContactHelpListAdapter(LIST_HELPCONTACT, "HELP");


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
                startActivity(_intent);
            }
        });

        LST_HELP.addOnItemTouchListener(new RecyclerTouchListener(CreateQuickMsgActivity.this, LST_HELP, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                LIST_HELPCONTACT.get(position);
                Toast.makeText(CreateQuickMsgActivity.this, LIST_HELPCONTACT.get(position).getFirst_name(), Toast.LENGTH_SHORT).show();

                startActivity(new Intent(CreateQuickMsgActivity.this, SendMessageActivity.class));
                CreateQuickMsgActivity.this.finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            CreateQuickMsgActivity.this.finish();
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
