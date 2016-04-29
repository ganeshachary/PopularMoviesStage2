package com.spottechnician.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.spottechnician.popularmovies.MovieModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OnesTech on 23/04/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movie.db";

    static final String TABLE_NAME = "moviedetails";

    private static final String KEY_ID = "id";
    private static final String TITLE = "title";
    private static final String OVERVIEW = "overview";
    private static final String DATE = "date";
    private static final String VOTE = "vote";
    private static final String POSTERPATH = "posterpath";
    private static final String REVIEWSURL = "reviewurl";
    private static final String TRAILORURL = "trailorurl";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " TEXT ," + TITLE + " TEXT,"
                + OVERVIEW + " TEXT, " + DATE + " TEXT," + VOTE + " TEXT, " + POSTERPATH + " TEXT, " + REVIEWSURL + " TEXT, " + TRAILORURL + " TEXT" + ")";
        db.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public Boolean addMovie(MovieContract movieContract) {
        if (getMovie(movieContract.getId().trim())) {
            return false;
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, movieContract.getId());
            values.put(TITLE, movieContract.getTitle());
            values.put(OVERVIEW, movieContract.getOverview());
            values.put(DATE, movieContract.getDate());
            values.put(VOTE, movieContract.getVote());
            values.put(POSTERPATH, movieContract.getPosterpath());
            values.put(REVIEWSURL, movieContract.getReviewurl());
            values.put(TRAILORURL, movieContract.getTrailorurl());
            db.insert(TABLE_NAME, null, values);
            db.close();
            return true;
        }


    }

    public Boolean getMovie(String id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, TITLE, OVERVIEW, DATE, VOTE, POSTERPATH, REVIEWSURL,
                TRAILORURL}, KEY_ID + " = ?", new String[]{id}, null, null, null);

        if (cursor.moveToFirst()) {
//
//
//            MovieContract movieContract = new MovieContract(cursor.getString(0),
//                    cursor.getString(1),
//                    cursor.getString(2), cursor.getString(3),
//                    cursor.getString(4), cursor.getString(5),
//                    cursor.getString(6), cursor.getString(7));
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }


    }

    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return count
        return count;
    }

    public List<MovieModel> getAllMovievs() {
        ArrayList<MovieModel> movieContractList = new ArrayList<MovieModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MovieModel movieContract = new MovieModel();
                movieContract.setId(cursor.getString(0));
                movieContract.setOriginal_title(cursor.getString(1));
                movieContract.setOverview(cursor.getString(2));
                movieContract.setRelease_date(cursor.getString(3));
                movieContract.setVote_average(cursor.getString(4));
                movieContract.setPosterpath(cursor.getString(5));
                // movieContract.setReviewurl(cursor.getString(6));
                //movieContract.setTrailorurl(cursor.getString(7));
                // Adding contact to list
                movieContractList.add(movieContract);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return movieContractList;
    }
}
