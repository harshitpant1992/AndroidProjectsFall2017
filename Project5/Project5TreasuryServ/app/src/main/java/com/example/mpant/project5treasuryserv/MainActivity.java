package com.example.mpant.project5treasuryserv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.mpant.project5treasuryserv.Status;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button);
         tv = (TextView)findViewById(R.id.tV1);
        tv.setText("SEE STATUS HERE ");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Status.status==1){
                    tv.setText( "Service Not Yet Bound");
                }else if(Status.status == 2){
                    tv.setText( "Service Bound but Idle");
                }else if (Status.status ==3){
                    tv.setText( "Service Bound and Running an API method");
                }else if(Status.status ==4){
                    tv.setText( "Service Destroyed");
                }
            }
        });

    }
}
