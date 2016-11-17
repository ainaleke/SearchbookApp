package com.searchbook;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //hide action bar
        getSupportActionBar().hide();
        Context context=getApplicationContext();
        CharSequence text="Loading...";
        int duration= Toast.LENGTH_LONG;
        Toast toast=Toast.makeText(getBaseContext(),"Loading...",duration);
        toast.show();

        Thread logoTimer=new Thread(){
            //Initialize the thread
            public void run(){
                try{
                    sleep(5000); //this thread should sleep for 5secs
                    //start the new Activity
                    startActivity(new Intent(getApplicationContext(),BookListActivity.class));
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    finish();
                }
            }
        };
        logoTimer.start();

    }
}
