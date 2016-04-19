package com.nscc.jared.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.nscc.jared.biz.Cell;
import com.nscc.jared.biz.CellSupport;
import com.nscc.jared.landgrab.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class CellDataAccess {
    static InputStream is = null;
    static JSONObject jObj = null;

    protected ArrayList<Cell> cells = new ArrayList<>();
    protected ArrayList<CellSupport> supports = new ArrayList<>();

    public boolean loadCellsComplete = false;

    public void loadCellsInArea(double lat, double lng)
    {
        // run network task
        LoadCellsInArea task = new LoadCellsInArea();
        String[] params = {Double.toString(lat), Double.toString(lng)};
        task.execute(params);
    }

    public void loadCellsComplete(String json)
    {
        cells.clear();
        supports.clear();

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        try {

            // Get cells from JSON

            JSONArray jsonArray = jObj.optJSONArray("cell");
            if (jsonArray != null) {
                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int id = Integer.parseInt(jsonObject.optString("id"));
                    double lat = Double.parseDouble(jsonObject.optString("lat"));
                    double lng = Double.parseDouble(jsonObject.optString("lng"));
                    String name = jsonObject.optString("name");

                    cells.add(new Cell(id, lat, lng, name));
                }
            }

            // Get Supports from JSON

            jsonArray = jObj.optJSONArray("support");
            if (jsonArray != null) {
                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String _username = jsonObject.optString("username");
                    int supporters = Integer.parseInt(jsonObject.optString("amount"));
                    int cellId = Integer.parseInt(jsonObject.optString("cell_id"));
                    double lat = Double.parseDouble(jsonObject.optString("lat"));
                    double lng = Double.parseDouble(jsonObject.optString("lng"));
                    String name = jsonObject.optString("name");
                    String color = jsonObject.optString("color");

                    supports.add(new CellSupport(_username, supporters, cellId, lat, lng, name, color));
                }
            }
            loadCellsComplete = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //                                           Param, Progress, Return
    private class LoadCellsInArea extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String json = "";
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.jaredeverett.ca/darmok/Android_API/getCellsInArea.php");

                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("lat", params[0]));
                nameValuePair.add(new BasicNameValuePair("lng", params[1]));

                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loadCellsComplete(s);
        }
    }
}
