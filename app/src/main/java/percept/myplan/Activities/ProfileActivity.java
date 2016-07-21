package percept.myplan.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.zip.Inflater;

import percept.myplan.R;

public class ProfileActivity extends AppCompatActivity {

    private EditText EDT_FIRSTNAME, EDT_LASTNAME, EDT_PASSWORD, EDT_EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        EDT_FIRSTNAME = (EditText) findViewById(R.id.edtFirstName);
        EDT_LASTNAME = (EditText) findViewById(R.id.edtLastName);
        EDT_PASSWORD = (EditText) findViewById(R.id.edtPassword);
        EDT_EMAIL = (EditText) findViewById(R.id.edtEmail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ProfileActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_SaveProfile) {
            ProfileActivity.this.finish();
            return true;
        }
        return false;
    }
}
