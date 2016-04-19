package com.nscc.jared.landgrab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nscc.jared.data.ObjectManager;

public class MainActivity extends Activity {

    private ObjectManager objects = new ObjectManager();

    private Handler h;
    private int FRAME_RATE = 1000;

    // Shared prefs are so useful
    public static final String PREFS_NAME = "AOP_PREFS";
    SharedPreferences sharedpreferences;

    private EditText etUser;
    private EditText etPass;
    private Button btnMap;
    private Button btnRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        btnMap  = (Button) findViewById(R.id.btnMap);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);
        btnRequest = (Button) findViewById(R.id.btnRequest);

        h = new Handler();
        btnMap.setOnClickListener(startClick);
        btnRequest.setOnClickListener(requestClick);

        // automatic login
        int user_id = sharedpreferences.getInt("user_id", 0);
        if (user_id != 0)
        {
            startActivity(new Intent("MapActivity"));
            Toast.makeText(getApplicationContext(), "Logging in as user: " + user_id, Toast.LENGTH_SHORT).show();
        }

    }

    private View.OnClickListener startClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            login(etUser.getText().toString(), etPass.getText().toString());
        }
    };

    private View.OnClickListener requestClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            Toast.makeText(getApplicationContext(), "Invites are closed.", Toast.LENGTH_SHORT).show();
        }
    };

    public void login(String user, String pass)
    {
        objects.login(user, pass);
        h.postAtTime(checkForLogin, SystemClock.uptimeMillis() + 400);
    }

    private Runnable checkForLogin = new Runnable() {
        @Override
        public void run() {
            if (objects.waitingForLoginToLoad)
            {
                if (objects.loginComplete())
                {
                    if (objects.user_id == 0)
                    {
                        // login failed
                        Toast.makeText(getApplicationContext(), "login failed", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("user_id", objects.user_id);
                        editor.putString("username", etUser.getText().toString());
                        editor.apply();

                        // this wasnt working
//                        if (sharedpreferences.getBoolean("setup", true))
//                        {
//                            SharedPreferences.Editor editor2 = sharedpreferences.edit();
//                            editor2.putBoolean("setup", false);
//                            editor2.apply();
//                            startActivity(new Intent("SetupActivity"));
//                        }
//                        else
//                        {
//                            startActivity(new Intent("MapActivity"));
//                        }

                        startActivity(new Intent("SetupActivity"));
                    }
                }
                h.postDelayed(checkForLogin, FRAME_RATE);
            }
            else
            {
                h.removeCallbacks(checkForLogin);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
