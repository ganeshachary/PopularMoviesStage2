package com.spottechnician.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      /* if (getIntent() != null) {
            //Get EXTRA from intent and attach to Fragment as Argument
          MovieModel m2=getIntent().getParcelableExtra("moviemodel");
            Bundle args = new Bundle();
            args.putParcelable("moviemodel",m2);
           DetailActivityFragment detailActivityFragment=new DetailActivityFragment();
            detailActivityFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_frag,detailActivityFragment).commit();
        }*/


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }



}
