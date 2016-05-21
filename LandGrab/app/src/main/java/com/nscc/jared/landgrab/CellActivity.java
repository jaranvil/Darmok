package com.nscc.jared.landgrab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nscc.jared.biz.RecruitAnimation;
import com.nscc.jared.biz.XPTriggers;
import com.nscc.jared.data.AddSupportData;
import com.nscc.jared.data.ObjectManager;
import com.nscc.jared.data.UserDataAccess;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;


public class CellActivity extends Activity {

    private ObjectManager objects;
    public static final String PREFS_NAME = "AOP_PREFS";
    SharedPreferences sharedpreferences;

    private TextView tvName;
    private TextView tvLandName;
    private TextView tvUsername;
    private TextView tvUsername2;
    private TextView tvPlayer;
    private TextView tvSupport;
    private TextView tvNoSupport;
    private ImageView ivThumbnail;
    private ListView lvSupportList;
    private Button btnRecruit;
    private Button btnAddSupport;
    private Button btnAddSupport2;
    private TextView tvOwnerSupport;
    private TextView tvPlayerSupport;
    private TextView tvSupportAvl;
    private LinearLayout lyAddSupportPopup;
    private EditText etSupportInput;

    private RecruitAnimation supportCanvas;
    private Handler h;
    private final int FRAME_RATE = 30;

    private Bitmap bmp;
    private String imageUrl;

    private double lat;
    private double lng;
    private String username;
    private int playerSupport;

    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<Integer> supporters = new ArrayList<>();

    private boolean canRecruit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell);

        sharedpreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        tvName = (TextView) findViewById(R.id.tvName);
        tvLandName = (TextView) findViewById(R.id.tvLandName);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvUsername2 = (TextView) findViewById(R.id.tvUsername2);
        ivThumbnail = (ImageView) findViewById(R.id.ivThumbnail);
        lvSupportList = (ListView) findViewById(R.id.lvSupportList);
        btnRecruit = (Button) findViewById(R.id.btnRecruit);
        tvPlayer = (TextView) findViewById(R.id.tvPlayer);
        tvSupport = (TextView) findViewById(R.id.tvSupport);
        tvNoSupport = (TextView) findViewById(R.id.tvNoSupport);
        btnAddSupport = (Button) findViewById(R.id.btnAddSupport);
        btnAddSupport2 = (Button) findViewById(R.id.btnAddSupport2);
        tvOwnerSupport = (TextView) findViewById(R.id.tvOwnerSupport);
        tvPlayerSupport = (TextView) findViewById(R.id.tvPlayerSupport);
        tvSupportAvl = (TextView) findViewById(R.id.tvSupportAvl);
        lyAddSupportPopup = (LinearLayout) findViewById(R.id.addSupportPopup);
        etSupportInput = (EditText) findViewById(R.id.etSupportInput);

        supportCanvas = (RecruitAnimation) findViewById(R.id.SupportCanvas);

        Bundle extras = getIntent().getExtras();
        if(extras != null)//if bundle has content
        {
            this.objects = (ObjectManager) getIntent().getSerializableExtra("objects");

            this.lat = extras.getDouble("lat");
            this.lng = extras.getDouble("lng");
            this.username = extras.getString("username", "");
            this.usernames = extras.getStringArrayList("usernames");
            this.supporters = extras.getIntegerArrayList("supporters");

            double userLat = extras.getDouble("userLat");
            double userLng = extras.getDouble("userLng");

            // TODO - hemisphere fix here
            if (userLat > this.lat && userLat < this.lat + 0.001)
                if (userLng < this.lng && userLng > this.lng - 0.001)
                    canRecruit = true;

            tvLandName.setText(extras.getString("name"));
            imageUrl = "https://maps.googleapis.com/maps/api/staticmap?center="+extras.getDouble("markerLat")+"%20"+extras.getDouble("markerLng")+"&zoom=18&size=200x300&maptype=normal";

            // find player supporters
            this.playerSupport = 0;
            String player = sharedpreferences.getString("username", "");

            if (usernames != null)
            {
                for (int i = 0;i < usernames.size();i++)
                {
                    if (usernames.get(i).equals(player))
                        playerSupport = supporters.get(i);
                }
            }

        }

        // start supporter drawing
        supportCanvas.usernames = usernames;
        supportCanvas.supporters = supporters;
        h = new Handler();
        h.postAtTime(r, SystemClock.uptimeMillis() + 400);

        tvName.setText("Location: " + lat + ", " + lng);
        tvUsername.setText(this.username);
        tvUsername2.setText(this.username);
        if (supporters != null)
            tvOwnerSupport.setText(supporters.get(0)+"");
        tvPlayerSupport.setText(playerSupport+"");
        tvSupportAvl.setText(sharedpreferences.getInt("supporters", 0)+"");
        getCellThumbnail();

        if (usernames != null)
        {
            setupSupportList();
        }
        else
        {
            tvPlayer.setVisibility(View.GONE);
            tvSupport.setVisibility(View.GONE);
            tvNoSupport.setVisibility(View.VISIBLE);
        }

        if (canRecruit)
            btnRecruit.setBackgroundColor(Color.parseColor("#00ffff"));

        btnRecruit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (canRecruit)
                {
                    int COOLDOWN = 1000;
                    boolean tooHot = false;
                    long difference = 0;

                    // check recruiting cooldown
                    String CELL = lat+lng+"";
                    long unixTime = System.currentTimeMillis() / 1000L;

                    long lastRecruit = sharedpreferences.getLong(CELL, 0);
                    if (lastRecruit != 0)
                    {
                        difference = unixTime - lastRecruit;
                        if (difference < COOLDOWN)
                        {
                            tooHot = true;
                        }
                    }

                    if (tooHot)
                    {
                        Toast.makeText(getApplicationContext(), (COOLDOWN - difference) + " seconds cooldown", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // TODO move complex decision on supporters earned by recruit
                        Random r = new Random();
                        int newSupport = r.nextInt(5);

                        int currentSupporter = sharedpreferences.getInt("supporters", 0);
                        SharedPreferences.Editor editor2 = sharedpreferences.edit();
                        editor2.putInt("supporters", currentSupporter + newSupport);
                        editor2.putLong(CELL, unixTime);
                        editor2.apply();

                        XPTriggers xp = new XPTriggers(sharedpreferences.getInt("user_id", 0), sharedpreferences.getInt("level", 1));
                        xp.recruit();

                        Toast.makeText(getApplicationContext(), "+" + newSupport + " supporters \t +10 xp", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "You must be in a cell to recruit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAddSupport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                lyAddSupportPopup.setVisibility(View.VISIBLE);

            }
        });

        btnAddSupport2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                int currentSupporter = sharedpreferences.getInt("supporters", 0);

                int supportToAdd = Integer.parseInt(etSupportInput.getText().toString());

                if (currentSupporter >= supportToAdd)
                {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putInt("supporters", currentSupporter - supportToAdd);
                    editor.apply();

                    AddSupportData db = new AddSupportData();
                    db.addSupport(lat, lng, sharedpreferences.getInt("user_id", 0), supportToAdd);

                    XPTriggers xp = new XPTriggers(sharedpreferences.getInt("user_id", 0), sharedpreferences.getInt("level", 1));
                    xp.addSupport();

                    Toast.makeText(getApplicationContext(), "+25xp \t support added", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not enough available supporters.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    private Runnable r = new Runnable() {
        @Override
        public void run() {

            if (!supportCanvas.done)
            {
                // draw
                supportCanvas.invalidate();
                h.postDelayed(r, FRAME_RATE);
            }
            else
            {
                h.removeCallbacks(r);
            }
        }
    };


    private void finishWithResult(int supportEarned)
    {
        Bundle extras = new Bundle();
        extras.putInt("newSupport", supportEarned);
        Intent intent = new Intent();
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void getCellThumbnail()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(imageUrl).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null)
                    ivThumbnail.setImageBitmap(bmp);
            }

        }.execute();
    }

    public void setupSupportList()
    {
        // the list adapater needs a array the size of the list
        String[] test= new String[usernames.size()];

        SupportListAdapter adapter = new SupportListAdapter(CellActivity.this, supporters, usernames, test);
        lvSupportList.setAdapter(adapter);
        lvSupportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//                //Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
//                Intent i = new Intent("VideoActivity");
//                Bundle extras = new Bundle();   //create bundle object
//                extras.putInt("id", videos.get(position).id);
//                i.putExtras(extras);
//                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cell, menu);
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
