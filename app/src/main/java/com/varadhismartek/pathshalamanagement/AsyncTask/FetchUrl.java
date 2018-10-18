package com.varadhismartek.pathshalamanagement.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by varadhi5 on 4/12/17.
 */

public class FetchUrl extends AsyncTask<String,Void,String> {

    private Context context;
    private GoogleMap map;
    private ProgressDialog dialog;

    public FetchUrl(Context addroute, GoogleMap googleMap) {
        this.context=addroute;
        this.map=googleMap;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog=new ProgressDialog(context);
        dialog.setMessage("Fetching route..Please Wait");
        dialog.show();

    }

    @Override
    protected String doInBackground(String... strings) {

        String data="";
        try {
            data=downloadUrl(strings[0]);
            Log.d("Task",data);
        }catch (Exception e){
            Log.d("Task exception",e.toString());
        }

        return data;
    }

    private String downloadUrl(String string) throws IOException {
        String data="";
        InputStream inputStream = null;
        HttpURLConnection connection=null;
        try {
            URL url=new URL(string);
            connection=(HttpURLConnection)url.openConnection();
            connection.connect();

            inputStream=connection.getInputStream();

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer sb=new StringBuffer();

            String line="";

            while ((line=bufferedReader.readLine())!=null)
            {
                    sb.append(line);
            }
            data=sb.toString();
            Log.d("downloadUrl", data.toString());
            bufferedReader.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            inputStream.close();
            connection.disconnect();
        }

        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //calling the parsertask to complete the task
        ParserTask parserTask=new ParserTask(context,map);
        parserTask.execute(s);
        dialog.dismiss();

    }
}
