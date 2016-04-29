package com.spottechnician.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewsFragment extends Fragment {
    final static String baseurlformovie = "http://api.themoviedb.org/3/movie/";
    String values;
    ListView listView;
    String[] jsonarry = new String[2];
    ArrayList<String> listreviewlist = new ArrayList<String>();
    public ReviewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent i = getActivity().getIntent();
        values = i.getStringExtra("values");
        View rootview = inflater.inflate(R.layout.fragment_reviews, container, false);
        listView = (ListView) rootview.findViewById(R.id.listreviewactivity);
        FetchMovieDetails fetchMovieDetails = new FetchMovieDetails();
        fetchMovieDetails.execute(values);
        if (listreviewlist.isEmpty()) {
            listreviewlist.add("No Reviews Yet");
        }
        listView.setAdapter(null);
        listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listreviewlist));




        return rootview;
    }

    public void parseReview(String jsonstring) {
        JSONObject jsonObjectReview;
        JSONArray jsonArrayReview;
        try {
            jsonObjectReview = new JSONObject(jsonstring);

            jsonArrayReview = jsonObjectReview.getJSONArray("results");
            listreviewlist.clear();
            for (int i = 0; i < jsonArrayReview.length(); i++) {
                JSONObject jsonObject = jsonArrayReview.getJSONObject(i);
                String content = jsonObject.getString("content");
                String author = jsonObject.getString("author");
                listreviewlist.add(content);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    class FetchMovieDetails extends AsyncTask<String, Void, String[]> {
        ProgressDialog progress;
        BufferedReader bufferedReader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getContext());
            progress.setMessage("Downloading");
            progress.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String id = params[0];
            String reviewurl = baseurlformovie + params[0] + "/reviews?api_key=" + BuildConfig.MOVIEDB_API;
            HttpURLConnection httpURLConnection = null;
            HttpURLConnection httpURLConnection2;
            InputStream inputStream2 = null;

            StringBuffer stringBuffer2 = new StringBuffer();
            String line;
            try {

                URL url2 = new URL(reviewurl);

                httpURLConnection2 = (HttpURLConnection) url2.openConnection();
                httpURLConnection2.setRequestMethod("GET");
                httpURLConnection2.connect();
                inputStream2 = httpURLConnection2.getInputStream();


                bufferedReader = new BufferedReader(new InputStreamReader(inputStream2));

                jsonarry[1] = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer2.append(line);
                }
                jsonarry[1] = stringBuffer2.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return jsonarry;
        }

        @Override
        protected void onPostExecute(String... jsonarray) {
            //Log.e("MovieTrailor",jsonarray[0]);
            //Log.e("MovieReviews",jsonarray[1]);

            parseReview(jsonarray[1]);
            if (listreviewlist.isEmpty()) {
                listreviewlist.add("No Reviews Yet");
            }
            listView.setAdapter(null);

            listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listreviewlist));
            progress.dismiss();
        }
    }
}
