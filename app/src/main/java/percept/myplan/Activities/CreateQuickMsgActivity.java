package percept.myplan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import percept.myplan.Classes.Contact;
import percept.myplan.R;
import percept.myplan.adapters.ContactHelpListAdapter;

public class CreateQuickMsgActivity extends AppCompatActivity {


    private TextView TV_EDIT_HELPLIST, TV_ADD_CONTACT;
    private RecyclerView LST_HELP, LST_CONTACTS;
    private List<Contact> LIST_HELPCONTACT;
    private ContactHelpListAdapter ADPT_CONTACTHELPLIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quick_msg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);


        TV_EDIT_HELPLIST = (TextView) findViewById(R.id.tvEditHelpList);
        TV_EDIT_HELPLIST.setVisibility(View.INVISIBLE);
        LST_HELP = (RecyclerView) findViewById(R.id.lstHelpList);
        LST_CONTACTS = (RecyclerView) findViewById(R.id.lstContacts);

        LIST_HELPCONTACT = new ArrayList<>();
        LIST_HELPCONTACT.add(new Contact("Children Phone", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Paul", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Mom", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Madelaine", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Kate", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Jenna", "1234567890", false));
        ADPT_CONTACTHELPLIST = new ContactHelpListAdapter(LIST_HELPCONTACT);


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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            CreateQuickMsgActivity.this.finish();
            return true;
        }
        return false;
    }
}
