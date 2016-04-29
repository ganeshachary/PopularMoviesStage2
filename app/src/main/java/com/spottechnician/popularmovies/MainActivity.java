package com.spottechnician.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {
    private Boolean mTabletMode = false;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.details_frag)!=null)
        {
            mTabletMode=true;
            DetailActivityFragment detailActivityFragment=new DetailActivityFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.details_frag,detailActivityFragment).commit();
        }


    }

    public boolean isTablet()
    {
        return mTabletMode;
    }

    public void replaceFragment(MovieModel m2)
    {
        Bundle args=new Bundle();
        args.putParcelable("moviemodel",m2);
        DetailActivityFragment detailActivityFragment=new DetailActivityFragment();
        detailActivityFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.details_frag,detailActivityFragment).commit();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}
