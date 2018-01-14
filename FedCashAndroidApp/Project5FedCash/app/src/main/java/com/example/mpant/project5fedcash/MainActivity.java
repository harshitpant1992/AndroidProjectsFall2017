package com.example.mpant.project5fedcash;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mpant.common.ServiceInterface;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    protected static final String TAG = "FedCash";
    private ServiceInterface mTreasuryService ;
    private boolean misBound = false;
    int apiMethodNumber=-1;

    Thread thread1;
    EditText day,month,year, noOfWDays;
    ArrayList greatResponseList = new ArrayList();
    ArrayList greatRequestList = new ArrayList();

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            int what = msg.what ;
            switch (what) {
                case 1://list<integer> returned by monthly cash for 12 months
                    greatResponseList.add(msg.obj);
                    greatRequestList.add("monthlyCash()");
                    Log.i(TAG,"Harshit Says Thread returned monthlyCash() success!!");
                    break;
                case 2:// list<integer > contains the open value for the days
                    greatResponseList.add(msg.obj);
                    greatRequestList.add("dailyCash()");
                    Log.i(TAG,"Harshit Says Thread returned dailyCash() success!!");
                    break;
                case 3:
                    //Yearly average
                    greatResponseList.add(msg.obj);//Either integer is put or ArrayList <Integer>
                    greatRequestList.add("yearlyAvg()");
                    Log.i(TAG,"Harshit Says Thread returned Yearly Avg success!!");
                    break;

            }

        }
    }	; // Handler is associated with UI Thread





    private ServiceConnection serviceCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mTreasuryService = ServiceInterface.Stub.asInterface(iBinder);
            misBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mTreasuryService= null;
            misBound=false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1,button2,button3;
        TextView instructions = (TextView)findViewById(R.id.instructions);
        instructions.setText("INSTRUCTIONS\r\n1.Select Method to call by Pressing Button\r\n2.Fill Fields" +
                "\r\n3.Press Call Service Button\r\n4.See Result in Activity2");

        day = (EditText)findViewById(R.id.editText2);
        day.setVisibility(View.INVISIBLE);
        month = (EditText)findViewById(R.id.editText3);
        month.setVisibility(View.INVISIBLE);
        year = (EditText)findViewById(R.id.editText4);
        year.setVisibility(View.INVISIBLE);
        noOfWDays = (EditText)findViewById(R.id.editText5);
        noOfWDays.setVisibility(View.INVISIBLE);

        button1 = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        Button monthlyCash = (Button)findViewById(R.id.button4);
        Button dailyCash = (Button)findViewById(R.id.button5);
        Button yearlyAvg = (Button)findViewById(R.id.button6);

        if (!misBound) {

            boolean b = false;
            Intent i = new Intent("com.example.mpant.common" );
            b=bindService(convertImplicitIntentToExplicitIntent(i),serviceCon, BIND_AUTO_CREATE);
            if (b) {
                Log.i(TAG, "Harshit says bindService() succeeded!");
            } else {
                Log.i(TAG, "Harshit says bindService() failed!");
            }

        }


        monthlyCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year.setVisibility(View.VISIBLE);
                apiMethodNumber=1;
                day.setVisibility(View.INVISIBLE);
                month.setVisibility(View.INVISIBLE);
                noOfWDays.setVisibility(View.INVISIBLE);
            }
        });
        dailyCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                day.setVisibility(View.VISIBLE);
                month.setVisibility(View.VISIBLE);
                year.setVisibility(View.VISIBLE);
                noOfWDays.setVisibility(View.VISIBLE);
                apiMethodNumber=2;
            }
        });
        yearlyAvg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year.setVisibility(View.VISIBLE);
                apiMethodNumber=3;
                day.setVisibility(View.INVISIBLE);
                month.setVisibility(View.INVISIBLE);
                noOfWDays.setVisibility(View.INVISIBLE);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Bind the service
//                if (!misBound) {
//
//                    boolean b = false;
//                    Intent i = new Intent("com.example.mpant.common" );
//
//
//                    b=bindService(convertImplicitIntentToExplicitIntent(i),serviceCon, BIND_AUTO_CREATE);
//                    //b = bindService(i, serviceCon , BIND_AUTO_CREATE);
//                    if (b) {
//                        Log.i(TAG, "Ugo says bindService() succeeded!");
//                    } else {
//                        Log.i(TAG, "Ugo says bindService() failed!");
//                    }
//
//                }
                //SEND TO ACTIVITY 2 To get fragments
                Intent inte = new Intent(getApplicationContext() , Activity2.class);
                Bundle b = new Bundle();
                //b.putParcelableArrayList("request",greatRequestList);
                //b.putParcelableArrayList("response",greatResponseList);
                b.putSerializable("request",greatRequestList);
                b.putSerializable("response",greatResponseList);
                inte.putExtras(b);

                startActivity(inte);


            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Bind the service if not
//                if (!misBound) {
//
//                    boolean b = false;
//                    Intent i = new Intent("com.example.mpant.common" );
//
//
//                    b=bindService(convertImplicitIntentToExplicitIntent(i),serviceCon, BIND_AUTO_CREATE);
//                    //b = bindService(i, serviceCon , BIND_AUTO_CREATE);
//                    if (b) {
//                        Log.i(TAG, "Ugo says bindService() succeeded!");
//                    } else {
//                        Log.i(TAG, "Ugo says bindService() failed!");
//                    }
//
//                }

                //call a method
                try {
                    int day1=-1, month1=-1, year1=-1,wdays1=-1;
                    if(!day.getText().toString().isEmpty()){
                        day1 = Integer.valueOf(day.getText().toString());
                    }
                    if(!month.getText().toString().isEmpty()){
                        month1 = Integer.valueOf(month.getText().toString());
                    }
                    if(!year.getText().toString().isEmpty()){
                        year1 = Integer.valueOf(year.getText().toString());
                    }
                    if(!noOfWDays.getText().toString().isEmpty()){
                        wdays1 = Integer.valueOf(noOfWDays.getText().toString());
                    }

                    thread1 = new Thread1(apiMethodNumber, day1,month1,year1,wdays1);
                    thread1.start();
                    Log.i(TAG, "Harshit says Worker Thread started!");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //unbind to service
                if (misBound) {

                    unbindService(serviceCon);
                    Log.i(TAG, "Harshit says Unbinding Done!");
                }
            }
        });

    }

    //To convert implicit intent to explicit intent
    public Intent convertImplicitIntentToExplicitIntent(Intent implicit) {
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> listOfResolvedInfo = packageManager.queryIntentServices(implicit, 0);

        if ( listOfResolvedInfo==null||listOfResolvedInfo.size()!= 1) {
            return null;
        }
        ResolveInfo serviceInfoName =  listOfResolvedInfo.get(0);
        ComponentName componentName = new ComponentName(serviceInfoName.serviceInfo.packageName, serviceInfoName.serviceInfo.name);
        Intent explicit = new Intent(implicit);
        explicit.setComponent(componentName);
        return explicit;
    }


    class Thread1 extends Thread implements Runnable {
        int apiCallNumber = -1;
        int day1, month1, year1,wdays1;


        public Thread1(int apiCallNumber,int day1, int month1,int year1,int wdays1) {
            this.apiCallNumber= apiCallNumber;
            this.day1 = day1;
            this.month1 = month1;
            this.year1 = year1;
            this.wdays1 = wdays1;
        }

        public void run() {
            int a=-1;
            List<Integer> li;
            if(apiCallNumber==1){//for MonthlyCash
                try {
                    li = mTreasuryService.monthlyCash( year1);

                       // a = mTreasuryService.yearlyAvg( year1);
                    Message m1= mHandler.obtainMessage(1);
//            m1.arg1=secretNum_1;
                    m1.obj= li;
                    mHandler.sendMessage(m1);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }else if(apiCallNumber == 2){//for dailyCash
                try {
                    li = mTreasuryService.dailyCash( day1,month1,year1,wdays1);
                    //a = mTreasuryService.yearlyAvg( year1);
                    Message m1= mHandler.obtainMessage(2);
//            m1.arg1=secretNum_1;
                    m1.obj= li;
                    mHandler.sendMessage(m1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }else if(apiCallNumber == 3){//for yearlyAvg
                try {
                    a = mTreasuryService.yearlyAvg( year1);
                    Message m1= mHandler.obtainMessage(3);
//            m1.arg1=secretNum_1;
                    m1.obj= a;
                    mHandler.sendMessage(m1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }


        }

    }


}
