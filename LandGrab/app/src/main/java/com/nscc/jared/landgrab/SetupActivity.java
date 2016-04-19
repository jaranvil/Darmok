package com.nscc.jared.landgrab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.nscc.jared.data.UserDataAccess;

public class SetupActivity extends Activity {

    public static final String PREFS_NAME = "AOP_PREFS";
    SharedPreferences sharedpreferences;

    private Button btnBegin;
    private RadioGroup rgGroup;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        sharedpreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        btnBegin = (Button) findViewById(R.id.btnBegin);
        rgGroup = (RadioGroup) findViewById(R.id.rgGroup);
        etName = (EditText) findViewById(R.id.etName);

        btnBegin.setOnClickListener(beginClick);

    }

    private View.OnClickListener beginClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {

            String color = ((RadioButton)findViewById(rgGroup.getCheckedRadioButtonId() )).getText().toString();
            String name = etName.getText().toString();

            UserDataAccess db = new UserDataAccess();
            db.setupUser(sharedpreferences.getInt("user_id", 0), name, color);

            startActivity(new Intent("MapActivity"));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
