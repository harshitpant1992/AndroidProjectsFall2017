package com.example.mpant.project5treasuryserv;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.mpant.common.ServiceInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import com.example.mpant.project5treasuryserv.Status;

public class TreasuryServ extends Service {
    private static final String TAG = "TreasuryServ";
    public TreasuryServ() {
    }

    private TreasuryServImpl myImpl1= new TreasuryServImpl();


    private class TreasuryServImpl extends ServiceInterface.Stub{

        @Override
        public List monthlyCash(int year) throws RemoteException {
            synchronized (this) {
                Status.status=3;
                URL url = null;
                HttpURLConnection urlConn = null;
                String result = "";
                //QUERY
                String queryStr = "SELECT DISTINCT  \"open_mo\" \n" +
                        "FROM\n" +
                        "t1\n" +
                        "WHERE ( \"year\" = '"+year +"' AND \"month\" > '0' AND \"month\" < '13' AND \"account\" = 'Federal Reserve Account'  )";
                String urlStr = "http://api.treasury.io/cc7znvq/47d80ae900e04f2/sql/?q=" + URLEncoder.encode(queryStr);

                //Fetching the JSON result for the above query and building a string
                try {

                    url = new URL(urlStr);
                    urlConn = (HttpURLConnection) url.openConnection();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    InputStream in = new BufferedInputStream(urlConn.getInputStream());
                    Scanner sc = new Scanner(in).useDelimiter("\\A");
                    result = sc.hasNext() ? sc.next() : "";
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConn.disconnect();
                }

                ArrayList<Integer> l1 = new ArrayList(1);
                //Parsing the JSON string to build List
                if (result != null) {
                    try {
                        // Getting JSON Array node
                        JSONArray jsonarray = new JSONArray(result);//getJSONArray(jsonStr);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject c = jsonarray.getJSONObject(i);

                            String id = c.getString("open_mo");
                            System.out.println(id);
                            l1.add(Integer.valueOf(id) );
                        }
                    } catch (final JSONException e) {
                        Log.e("MONTHLY CASH ", "Json parsing error: " + e.getMessage());
                    }
                }
                return l1;
            }

        }

        @Override
        public List dailyCash(int day, int month, int year, int numberOfWorkingDays) throws RemoteException {

            synchronized (this) {
                Status.status=3;
                URL url = null;
                HttpURLConnection urlConn = null;
                String result = "";

                //
                //OLD Query
//                String queryStr = "SELECT \"open_today\"\n" +
//                        "FROM\n" +
//                        "t1\n" +
//                        "WHERE (\"month\" = '"+month+"' AND \"year\" = '"+year+"' AND \"day\" > '"+(day-1)+"' AND \"day\" < '"+(day+numberOfWorkingDays+1)+"' AND \"account\" = 'Federal Reserve Account')";
//                String urlStr = "http://api.treasury.io/cc7znvq/47d80ae900e04f2/sql/?q=" + URLEncoder.encode(queryStr);

                int newDay = day;
                ArrayList<Integer> l1 = new ArrayList(1);
                int count=0;
                while(count< numberOfWorkingDays+1) {

                    String queryStrTemp = "SELECT \"open_today\"\n" +
                            "FROM\n" +
                            "t1\n" +
                            "WHERE (\"month\" = '" + month + "' AND \"year\" = '" + year + "' AND \"day\" = '" + newDay + "' AND \"account\" = 'Federal Reserve Account')";
                    String urlStrTemp = "http://api.treasury.io/cc7znvq/47d80ae900e04f2/sql/?q=" + URLEncoder.encode(queryStrTemp);

                    //Fetching the JSON for query
                    try {
                        url = new URL(urlStrTemp);
                        urlConn = (HttpURLConnection) url.openConnection();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        InputStream in = new BufferedInputStream(urlConn.getInputStream());
                        Scanner sc = new Scanner(in).useDelimiter("\\A");
                        result = sc.hasNext() ? sc.next() : "";
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        urlConn.disconnect();
                    }
                    System.out.println(result);
                    System.out.println(result == ("[]"));
                    //Parsing the JSON String to build List
                    if (result != null) {
                        try {
                            // Getting JSON Array node
                            JSONArray jsonarray = new JSONArray(result);//getJSONArray(jsonStr);
                            if (jsonarray.length() != 0) {
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonObj = jsonarray.getJSONObject(i);

                                    String value = jsonObj.getString("open_today");
                                    System.out.println(value);
                                    l1.add(Integer.valueOf(value));
                                }
                                count++;
                                newDay = newDay + 1;
                            } else {
                                newDay = newDay + 1;
                            }

                        } catch (final JSONException e) {
                            Log.e("DAILY CASH ", "Json parsing error: " + e.getMessage());
                        }

                    }
                }



                //Fetching the JSON for query
                /*
                try {
                    url = new URL(urlStr);
                    urlConn = (HttpURLConnection) url.openConnection();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    InputStream in = new BufferedInputStream(urlConn.getInputStream());
                    Scanner sc = new Scanner(in).useDelimiter("\\A");
                    result = sc.hasNext() ? sc.next() : "";
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConn.disconnect();
                }
                System.out.println(result);
                ArrayList<Integer> l1 = new ArrayList(1);
                //Parsing the JSON String to build List
                if (result != null) {
                    try {
                        // Getting JSON Array node
                        JSONArray jsonarray = new JSONArray(result);//getJSONArray(jsonStr);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObj = jsonarray.getJSONObject(i);

                            String value = jsonObj.getString("open_today");
                            System.out.println(value);
                            l1.add(Integer.valueOf(value) );
                        }
                    } catch (final JSONException e) {
                        Log.e("DAILY CASH ", "Json parsing error: " + e.getMessage());
                    }
                } */

                return l1;
            }

        }

        @Override
        public int yearlyAvg(int year) throws RemoteException {

            synchronized (this) {
                Status.status=3;
                URL url = null;
                HttpURLConnection urlConn = null;
                String result = "";
                //QUERY
                String queryStr = "SELECT AVG( \"open_today\") \n" +
                        "FROM\n" +
                        "t1\n" +
                        "WHERE ( \"year\" = '"+year+ "' AND \"account\" = 'Federal Reserve Account'  )";
                String urlStr = "http://api.treasury.io/cc7znvq/47d80ae900e04f2/sql/?q=" + URLEncoder.encode(queryStr);

                //Fetching the query result as JSON string
                try {
                    url = new URL(urlStr);
                    urlConn = (HttpURLConnection) url.openConnection();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    InputStream in = new BufferedInputStream(urlConn.getInputStream());
                    Scanner s = new Scanner(in).useDelimiter("\\A");
                    result = s.hasNext() ? s.next() : "";
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConn.disconnect();
                }
                System.out.println(result);
                // using substring to fetch the avg from the JSON string
                int yearlyAvg= Double.valueOf(result.substring((result.indexOf(":") + 1), result.lastIndexOf("}")).trim()).intValue();
                System.out.println(yearlyAvg);
                return yearlyAvg;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"OnBind called to client but idle");
        Status.status=2;
        // TODO: Return the communication channel to the service.
        return myImpl1;
    }


    @Override
    public void onDestroy() {

        Log.i(TAG,"OnDestroy method called");
        Status.status=4;
        super.onDestroy();

    }



}
