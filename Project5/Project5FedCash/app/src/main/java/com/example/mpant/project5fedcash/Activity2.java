package com.example.mpant.project5fedcash;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.mpant.project5fedcash.RequestFrag2.ListSelectionListener;

import java.util.ArrayList;

public class Activity2 extends Activity implements
        ListSelectionListener {

    public static String[] mRequestArray;
    public static String[] mResponseArray;
    private ResponseFrag2 mDetailsFragment;

    private static final String TAG = "FedCashActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");

        //Receive the Intent and set corresponding Arraylists
        Bundle b = getIntent().getExtras();
        ArrayList request= new ArrayList();
        ArrayList response= new ArrayList();
        if(b!=null) {
            request = (ArrayList)b.getSerializable("request");
            response = (ArrayList)b.getSerializable("response");
        }

        //These 2 arrays will be used for storing the string responses gotten from Activity 1
        mRequestArray = new String[request.size()];
        mResponseArray = new String[response.size()];

        //Converting the ArrayLists that contain Integer and ArrayList to String Arrays
        for(int i=0; i< request.size(); i++){
            mRequestArray[i]= (String)request.get(i); //request array can be copied as is
            //For response array we have case1: contains ArrayList or cas2 contains an integer
            if( response.get(i) instanceof ArrayList ){// case1: from the
                //Arraylist we build a string by iterating thru all items in ArrayList and concatenating them
                String s="";
                for(int j=0;j< ((ArrayList) response.get(i)).size(); j++){
                    s=s+((ArrayList) response.get(i)).get(j)+"\r\n";
                }
                mResponseArray[i]=s;
            }else{//contains an integer can be put as is by converting to String
                mResponseArray[i]= String.valueOf(response.get(i));//convert the int yearlyAvg to String
            }
        }


        setContentView(R.layout.activity_2);
        // Get a reference to the ResponseFragment
        mDetailsFragment =
                (ResponseFrag2) getFragmentManager().findFragmentById(R.id.response);//details is Response Fragment
    }

    // Implement ListSelectionListener interface
    // Called by TitlesFragment when the user selects an item in the TitlesFragment
    @Override
    public void onListSelection(int index) {
        if (mDetailsFragment.getShownIndex() != index) {
            // Tell the QuoteFragment to show the quote string at position index
            mDetailsFragment.showQuoteAtIndex(index);
        }
    }



}
