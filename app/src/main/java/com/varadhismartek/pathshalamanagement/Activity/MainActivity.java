package com.varadhismartek.pathshalamanagement.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.varadhismartek.pathshalamanagement.Fragment.Addroute;
import com.varadhismartek.pathshalamanagement.Fragment.Transport;
import com.varadhismartek.pathshalamanagement.R;

public class MainActivity extends AppCompatActivity {

    Transport transport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transport=new Transport();
        Addroute addroute=new Addroute();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,transport).commit();

    }
}
