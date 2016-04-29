package com.spottechnician.popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.spottechnician.popularmovies.data.MovieDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    ConnectionDetector cd;
    private Boolean mTablet;

    ArrayList<MovieModel> movielist=new ArrayList<MovieModel>();
    SharedPreferences sharedPreferences;


    GridView gridView;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        String sort=sharedPreferences.getString("sort","");
        updateMovies(sort);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences = this.getActivity().getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        cd = new ConnectionDetector(getContext());
        if (!cd.isConnectingToInternet()) {

            Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.popular)
        {
            if (cd.isConnectingToInternet()) {
                updateMovies("popular");
            } else {
                Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_SHORT).show();
            }
        }
        else if(id==R.id.toprated)
        {
            if (cd.isConnectingToInternet()) {
                updateMovies("toprated");
            } else {
                Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.fav) {
            updateMoviesFromDatabase();
        }
        return super.onOptionsItemSelected(item);
    }

    void updateMoviesFromDatabase() {
        MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
        if (movieDbHelper.getCount() > 0) {

            movielist.clear();
            movielist = (ArrayList<MovieModel>) movieDbHelper.getAllMovievs();
            gridView.setAdapter(new ImageAdapter(getActivity(), movielist));
        } else {
            Toast.makeText(getContext(), "no favorite movie added", Toast.LENGTH_SHORT).show();
        }


    }
    private void updateMovies(String sort)
    {
        FetchMovieData fetchMovieData=new FetchMovieData();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("sort", sort);
        editor.apply();
        fetchMovieData.execute(sort);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView =inflater.inflate(R.layout.fragment_main,container,false);

        gridView=(GridView)rootView.findViewById(R.id.maingrid);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) rootView.findViewById(R.id.toolbar);
        // gridView.setAdapter(new ImageAdapter(getActivity()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(getContext(),"YOu clicke position"+position,Toast.LENGTH_SHORT).show();
                MovieModel m2=movielist.get(position);
               mTablet=((MainActivity)getActivity()).isTablet();
                if(!mTablet)
               {

                   Intent intent=new Intent(getActivity(),DetailActivity.class);
                   intent.putExtra("moviemodel",m2);
                   startActivity(intent);

               }
                else
               {
                   ((MainActivity)getActivity()).replaceFragment(m2);
               }







            }
        });

        return rootView;


    }

    private void parseJsonInList(String result)
    {
        JSONObject mainObject;
        try {
            mainObject = new JSONObject(result);
            movielist.clear();
            JSONArray mainArray= mainObject.getJSONArray("results");
            for(int i=0;i<mainArray.length();i++)
            {
                JSONObject movieObject= mainArray.getJSONObject(i);
                String id=movieObject.get("id").toString();
                String  posterpath=movieObject.get("poster_path").toString();
                String  backdrop_path= movieObject.get("backdrop_path").toString();
                String overview=movieObject.get("overview").toString();
                String original_title=movieObject.get("original_title").toString();
                String release_date=movieObject.get("release_date").toString();
                String vote_average=movieObject.get("vote_average").toString();
                MovieModel m = new MovieModel(id, posterpath, backdrop_path, overview, original_title, release_date, vote_average);
                movielist.add(m);
            }

            gridView.setAdapter(new ImageAdapter(getActivity(),movielist));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class FetchMovieData extends AsyncTask<String,Void,String>
    {
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getContext());
            progress.setMessage("Downloading");
            progress.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String sort=params[0];
            String baseurl;
            if (sort.equals("toprated")) {
                baseurl = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + BuildConfig.MOVIEDB_API;
            }
            else   {
                baseurl = "http://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.MOVIEDB_API;
            }String LOGTAG="BaseAdapter";
            String line, jsonstring;
            HttpURLConnection httpURLConnection=null;
            BufferedReader bufferedReader=null;
            try{

                URL url=new URL(baseurl);
                httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream=httpURLConnection.getInputStream();
                StringBuffer stringBuffer=new StringBuffer();
                if(inputStream==null)
                {
                    return "nodata";
                }

                bufferedReader=new BufferedReader(new InputStreamReader(inputStream));

                while((line=bufferedReader.readLine())!=null)
                {
                    stringBuffer.append(line);
                }

                if(stringBuffer.length()==0)
                {
                    return "nodata";
                }
                jsonstring =stringBuffer.toString();

                if(jsonstring!=null) {
                    return  jsonstring;
                }


            }
            catch (Exception e)
            {


                return e.toString();
            }finally {
                if(httpURLConnection!=null)
                {
                    httpURLConnection.disconnect();
                }
                if(bufferedReader!=null)
                {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return "nodata";
        }

        @Override
        protected void onPostExecute(String result) {
            if(result!=null)
            {
                parseJsonInList(result);
                progress.dismiss();
            }

        }
    }




}
