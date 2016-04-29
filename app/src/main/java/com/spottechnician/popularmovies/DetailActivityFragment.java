package com.spottechnician.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spottechnician.popularmovies.data.MovieContract;
import com.spottechnician.popularmovies.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

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
import java.util.List;

import static android.support.v4.view.MenuItemCompat.getActionProvider;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    final static String baseurlformovie = "http://api.themoviedb.org/3/movie/";
    String iddb, titledb, overviewdb, datedb, votedb, posterpathdb, reviewurldb, trailorurldb, links = "";
    String[] jsonarry = new String[2];
    String baseurl = "http://image.tmdb.org/t/p/w185/";
    List<String> listtrailorcode = new ArrayList<String>();
    ArrayList<String> listreviewlist = new ArrayList<String>();
    ImageView imageView = null;
    FetchMovieDetails fetchMovieDetails;
    TextView overviewtext, releasedatetext, ratingtext, titletext;
    ListView trailorlistview;


    public DetailActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container,false);


        MovieModel m=null;

        if(getActivity().getIntent().getExtras()!=null)
        {
            Intent intent = getActivity().getIntent();
             m = intent.getExtras().getParcelable("moviemodel");
        }

        if (m != null) {

            String id = m.getId();
            iddb = id;


            String posterpath = m.getPosterpath().substring(1);
           // Log.e("POSTERpaTH",posterpath);


            String overview = m.getOverview();
            overviewdb = overview;

            String release_date = m.getRelease_date();
            datedb = release_date;
            String original_title = m.getOriginal_title();
            titledb = original_title;

            String vote_average = m.getVote_average();
            votedb = vote_average;
            String finalurl = baseurl + posterpath;
            posterpathdb = finalurl;


            fetchMovieDetails = new FetchMovieDetails();
            fetchMovieDetails.execute(id);
            // List view for trailor and reviews
            // reviewslistview=(ListView)rootView.findViewById(R.id.reviewlistview);
            //ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,listreviewlist);
            //reviewslistview.setAdapter(arrayAdapter);
            trailorlistview = (ListView) rootView.findViewById(R.id.trailorlistview);
            //ArrayAdapter<String> arrayAdapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,listtrailorcode);
            //trailorlistview.setAdapter(arrayAdapter2);
            trailorlistview.setAdapter(null);
            trailorlistview.setAdapter(new ListTrailorAdapter(getActivity(), listtrailorcode));

            trailorlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String url = "http://www.youtube.com/watch?v=" + listtrailorcode.get(position);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                }
            });

            imageView = (ImageView) rootView.findViewById(R.id.back_drop_image);
            Picasso.with(getActivity()).load(posterpathdb).into(imageView);
            overviewtext = (TextView) rootView.findViewById(R.id.description);
            overviewtext.setText(overview);
            releasedatetext = (TextView) rootView.findViewById(R.id.releasedate);
            releasedatetext.setText(release_date);
            ratingtext = (TextView) rootView.findViewById(R.id.rating);
            ratingtext.setText(vote_average + "/10");
            titletext = (TextView) rootView.findViewById(R.id.movietitle);
            titletext.setText(original_title);


            Button reviewbtn = (Button) rootView.findViewById(R.id.reviewbtn);
            reviewbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent MyIntent = new Intent(getContext(), Reviews.class);
                    MyIntent.putExtra("values", iddb);
                    startActivity(MyIntent);

                }
            });


            //adding favorite movies
            Button favbtn = (Button) rootView.findViewById(R.id.favoritebtn);
            favbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addMovieToDatabase();
                }
            });

            // imageView.setImageResource(Thumnails[position]);
        }
        else if(getArguments()!=null)
        {
            Bundle args=new Bundle();
            args=getArguments();
            MovieModel m2=args.getParcelable("moviemodel");
            String id = m2.getId();
            iddb = id;


            String posterpath = m2.getPosterpath().substring(1);



            String overview = m2.getOverview();
            overviewdb = overview;

            String release_date = m2.getRelease_date();
            datedb = release_date;
            String original_title = m2.getOriginal_title();
            titledb = original_title;

            String vote_average = m2.getVote_average();
            votedb = vote_average;
            String finalurl = baseurl + posterpath;
            posterpathdb = finalurl;


            fetchMovieDetails = new FetchMovieDetails();
            fetchMovieDetails.execute(id);
            // List view for trailor and reviews
            // reviewslistview=(ListView)rootView.findViewById(R.id.reviewlistview);
            //ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,listreviewlist);
            //reviewslistview.setAdapter(arrayAdapter);
            trailorlistview = (ListView) rootView.findViewById(R.id.trailorlistview);
            //ArrayAdapter<String> arrayAdapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,listtrailorcode);
            //trailorlistview.setAdapter(arrayAdapter2);
            trailorlistview.setAdapter(null);
            trailorlistview.setAdapter(new ListTrailorAdapter(getActivity(), listtrailorcode));

            trailorlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String url = "http://www.youtube.com/watch?v=" + listtrailorcode.get(position);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                }
            });

            imageView = (ImageView) rootView.findViewById(R.id.back_drop_image);
            Picasso.with(getActivity()).load(posterpathdb).into(imageView);
            overviewtext = (TextView) rootView.findViewById(R.id.description);
            overviewtext.setText(overview);
            releasedatetext = (TextView) rootView.findViewById(R.id.releasedate);
            releasedatetext.setText(release_date);
            ratingtext = (TextView) rootView.findViewById(R.id.rating);
            ratingtext.setText(vote_average + "/10");
            titletext = (TextView) rootView.findViewById(R.id.movietitle);
            titletext.setText(original_title);


            Button reviewbtn = (Button) rootView.findViewById(R.id.reviewbtn);
            reviewbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent MyIntent = new Intent(getContext(), Reviews.class);
                    MyIntent.putExtra("values", iddb);
                    startActivity(MyIntent);

                }
            });


            //adding favorite movies
            Button favbtn = (Button) rootView.findViewById(R.id.favoritebtn);
            favbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addMovieToDatabase();
                }
            });

            // imageView.setImageResource(Thumnails[position]);

        }



        return rootView;
    }


    public void addMovieToDatabase() {
        MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());

        MovieContract movieContract = new MovieContract(iddb, titledb, overviewdb, datedb,
                votedb, posterpathdb, reviewurldb, trailorurldb);
        if (movieDbHelper.addMovie(movieContract)) {
            Toast.makeText(getContext(), "Added to favorite", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "Already in Favorite", Toast.LENGTH_SHORT).show();

        }


    }

    public void parseTrailor(String jsonstring) {
        JSONObject jsonObjectTrailor;
        JSONArray jsonArrayTrailor;
        try {
            jsonObjectTrailor = new JSONObject(jsonstring);
            links = "";
            jsonArrayTrailor = jsonObjectTrailor.getJSONArray("results");
            listtrailorcode.clear();
            for (int i = 0; i < jsonArrayTrailor.length(); i++) {
                JSONObject jsonObject = jsonArrayTrailor.getJSONObject(i);
                String key = jsonObject.getString("key");
                listtrailorcode.add(key);

                links = links + "\n" + "http://www.youtube.com/watch?v=" + listtrailorcode.get(i);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        ShareActionProvider mShareActionProvider = (ShareActionProvider) getActionProvider(menuItem);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareTrailorIntent());
        } else {
            Log.d("Share button", "Share Action Provider is null?");
        }
    }


    private Intent createShareTrailorIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (links != null) {
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    titledb + links);
        } else {
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    titledb);
        }

        return shareIntent;
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
            String trailorurl = baseurlformovie + params[0] + "/videos?api_key=" + BuildConfig.MOVIEDB_API;
            HttpURLConnection httpURLConnection = null;
            StringBuffer stringBuffer1 = new StringBuffer();
            InputStream inputStream1 = null;
            String line = "";
            try {
                URL url1 = new URL(trailorurl);

                httpURLConnection = (HttpURLConnection) url1.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                inputStream1 = httpURLConnection.getInputStream();


                if (inputStream1 == null) {
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream1));

                jsonarry[0] = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer1.append(line);
                }
                jsonarry[0] = stringBuffer1.toString();


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
                if (inputStream1 != null) {
                    try {
                        inputStream1.close();
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
            parseTrailor(jsonarray[0]);

            trailorlistview.setAdapter(null);
            trailorlistview.setAdapter(new ListTrailorAdapter(getContext(), listtrailorcode));
            progress.dismiss();
        }
    }
}
