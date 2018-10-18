package com.varadhismartek.pathshalamanagement.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.FirebaseDatabase;
import com.varadhismartek.pathshalamanagement.Fragment.Addroute;
import com.varadhismartek.pathshalamanagement.Fragment.ByLocation;
import com.varadhismartek.pathshalamanagement.Fragment.Modify;
import com.varadhismartek.pathshalamanagement.Fragment.ModifyChange;
import com.varadhismartek.pathshalamanagement.Fragment.Transport;
import com.varadhismartek.pathshalamanagement.POJO_Classes.Submit;
import com.varadhismartek.pathshalamanagement.R;

import java.io.Serializable;

public class BottomBarPage extends AppCompatActivity {

    Fragment fragments = null;
    private int id;
    Bundle bundles,newbundle;
    Menu menu;
    Addroute addroute;
    Modify modify;
    ModifyChange modifyChange;
    ByLocation byLocation;
    Submit submit;
    Bundle bundle1;
    public static BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            selectedItem(item);
            return false;


           /* switch (item.getItemId()) {

                case R.id.bottom_createroutes:
                    //fragment=Transport.newInstance();
                    Transport transport=new Transport();
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.bottom_container,transport).commit();
                    break;

                case R.id.bottom_addroutes:

                    fragment=Addroute.newInstance();

                    break;

                case R.id.bottom_modifyroutes:

                    break;
            }

            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.bottom_container,fragment).commit();

            return true;*/

        }

    };


    public void selectedItem(MenuItem item) {

        item.setChecked(true);


        switch (item.getItemId())
        {

            case R.id.bottom_createroutes:
                pushFragment(new Transport());
                break;

            case R.id.bottom_addroutes:

                if (fragments==null)
                {
                    Toast.makeText(getApplicationContext(),"please create the route",Toast.LENGTH_LONG).show();
                }

                else
                {
                    pushFragment(fragments);
                   // fragments=null;
                }


                break;

            case R.id.bottom_modifyroutes:

                pushFragment(new Modify());
                fragments=null;

                break;

        }
    }


    public void pushFragment(Fragment fragment) {

        if (fragment==null){
            return;
        }

        fragment.setArguments(bundles);

        FragmentManager fragmentManager=getSupportFragmentManager();
        if (fragmentManager!=null){
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            if (fragmentTransaction!=null){
                fragmentTransaction.replace(R.id.bottom_container,fragment).commit();
            }else {
                Toast.makeText(getApplicationContext(),"Nothing for fragment transaction",Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"There is no fragment manager",Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar_page);


        addroute=new Addroute();
        modify=new Modify();
        modifyChange=new ModifyChange();
        byLocation=new ByLocation();

        newbundle=getIntent().getExtras();

        if (newbundle==null)
        {
            Toast.makeText(getApplicationContext(),"No bundle found",Toast.LENGTH_LONG).show();
        }else
            {

            Log.d("bundles",newbundle.getInt("counts")+"");
        }


        navigation =findViewById(R.id.navigation);

        menu=navigation.getMenu();
        selectedItem(menu.getItem(0));


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    public void loadFragments(Bundle bundle,Fragment fragment)
    {

            this.fragments=fragment;

          /*  if (fragment==null)
            {
                fragments=null;
            }*/
            this.bundles=new Bundle();
            this.bundles=bundle;

           /* if (bundle!=null)
            {*/

                Log.d("originbottom",bundle.getParcelable("originlatlng")+" "+bundle.getParcelable("destinylatlng"));

                bundles.putInt("TAG", bundle.getInt("TAG"));
                bundles.putString("routeno", bundle.getString("routeno"));
                bundles.putString("stop_counts", bundle.getString("stop_counts"));
                bundles.putInt("stop_old_counts",bundle.getInt("stop_old_counts"));
                bundles.putParcelable("originlatlng", bundle.getParcelable("originlatlng"));
                bundles.putParcelable("destinylatlng", bundle.getParcelable("destinylatlng"));
                bundles.putSerializable("arraylist", bundle.getSerializable("arraylist"));

               // Log.d("checkarraylist",bundle.getSerializable("arraylist").toString());

                fragment.setArguments(bundles);
           /* }*/

          //  Log.d("blocks",bundle.getInt("counts")+"");

            selectedItem(menu.getItem(1));

    }


    public void loadModify(Bundle bundle, Fragment fragment)
    {
        this.bundle1=new Bundle();
        this.bundle1=bundle;

        /*bundle.putSerializable("modify",bundle.getSerializable("modify"));*/

        bundle1.putString("routeno",bundle.getString("routeno"));

        bundle1.putString("assit_name",bundle.getString("assit_name"));
        bundle1.putString("busname",bundle.getString("busname"));
        bundle1.putString("busno",bundle.getString("busno"));
        bundle1.putString("caretaker_name",bundle.getString("caretaker_name"));
        bundle1.putString("destiny",bundle.getString("destiny"));
        bundle1.putString("driver_mobno",bundle.getString("driver_mobno"));
        bundle1.putString("driver_name",bundle.getString("driver_name"));
        bundle1.putString("gps_details",bundle.getString("gps_details"));
        bundle1.putString("routeno",bundle.getString("routeno"));
        bundle1.putString("seating",bundle.getString("seating"));
        bundle1.putString("starting",bundle.getString("starting"));
        bundle1.putString("stop_counts",bundle.getString("stop_counts"));
        bundle1.putString("trspt_mgr_mobno",bundle.getString("trspt_mgr_mobno"));
        bundle1.putString("trspt_mgr_name",bundle.getString("trspt_mgr_name"));
        bundle1.putSerializable("arraylist",bundle.getSerializable("arraylist"));

        Log.d("submitget",bundle1.toString()+"");

        fragment.setArguments(bundle1);

      //  selectedItem(menu.getItem(2));

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        try
        {
            fragmentTransaction.replace(R.id.bottom_container, fragment).commit();
        }catch (IllegalStateException e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


}
