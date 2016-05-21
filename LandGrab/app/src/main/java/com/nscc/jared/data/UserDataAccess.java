package com.nscc.jared.data;


import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class UserDataAccess {
    static InputStream is = null;

    public boolean loginComplete = false;
    public boolean powerCompletebool = false;
    public int id;
    public int power = 0;
    public int level = 1;
    public int xp = 0;

    public void setupUser(int user_id, String name, String color)
    {
        SetupUser task = new SetupUser();
        String[] params = {Integer.toString(user_id), name, color};
        task.execute(params);
    }

    public void updateUser(int user_id, int amount, int max)
    {
        UpdateUser task = new UpdateUser();
        String[] params = {Integer.toString(user_id), Integer.toString(amount), Integer.toString(max)};
        task.execute(params);
    }

    public void login(String user, String pass)
    {
        LoginUser task = new LoginUser();
        String[] params = {user, pass};
        task.execute(params);
    }

   public void loginComplete(String token)
   {
       id = Integer.parseInt(token);
       loginComplete = true;
   }

    public void getStats(String user)
    {
        GetUserPower task = new GetUserPower();
        String[] params = {user};
        task.execute(params);
    }

    public void statsComplete(String power, String level, String xp)
    {
        this.power = Integer.parseInt(power);
        this.level = Integer.parseInt(level);
        this.xp = Integer.parseInt(xp);
        powerCompletebool = true;
    }

    //                                           Param, Progress, Return
    private class LoginUser extends AsyncTask<String, Void, String> {

        public AsyncResponse delegate = null;

        @Override
        protected String doInBackground(String... params) {
            String id = "";
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.jaredeverett.ca/darmok/Android_API/login.php");

                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("username", params[0]));
                nameValuePair.add(new BasicNameValuePair("password", params[1]));

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
                id = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return id;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loginComplete(s);
        }
    }

    //                                           Param, Progress, Return
    private class SetupUser extends AsyncTask<String, Void, String> {

        public AsyncResponse delegate = null;

        @Override
        protected String doInBackground(String... params) {
            String id = "";
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.jaredeverett.ca/darmok/Android_API/setup.php");

                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
                nameValuePair.add(new BasicNameValuePair("name", params[1]));
                nameValuePair.add(new BasicNameValuePair("color", params[2]));

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
                id = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return id;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    //                                           Param, Progress, Return
    private class GetUserPower extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... params) {
            String id = "";
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.jaredeverett.ca/darmok/Android_API/getUserPower.php");

                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("username", params[0]));

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
                id = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return id;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String[] data = s.split(":");
            statsComplete(data[0], data[1], data[2]);
        }
    }

    //                                           Param, Progress, Return
    private class UpdateUser extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... params) {
            String id = "";
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.jaredeverett.ca/darmok/Android_API/updateUser.php");

                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
                nameValuePair.add(new BasicNameValuePair("xp", params[1]));
                nameValuePair.add(new BasicNameValuePair("xpMax", params[2]));

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
                id = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return id;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

}
