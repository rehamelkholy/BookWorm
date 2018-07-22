package com.example.android.bookworm;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BooklistActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>>{

    public static final String LOG_TAG = BooklistActivity.class.getName();
    private static final int BOOK_LOADER_ID = 1;
    private static final String GOOGLE_BOOKS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes";
    private BookAdapter mAdapter;
    private TextView mEmptyStateView;
    private ProgressBar mProgressBar;
    private boolean isConnected;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklist);

        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        ListView bookListView = (ListView) findViewById(R.id.list);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = mAdapter.getItem(position);
                Intent bookIntent = new Intent(BooklistActivity.this, BookActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("BOOK", (Serializable) book);
                bookIntent.putExtra("BUNDLE", bundle);
                startActivity(bookIntent);
            }
        });

        mEmptyStateView = (TextView) findViewById(R.id.empty_state_view);
        bookListView.setEmptyView(mEmptyStateView);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        LoaderManager loaderManager = getLoaderManager();

        if (isConnected){
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        }else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateView.setText(R.string.no_internet);
        }
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        ArrayList<String> keywords = getIntent().getStringArrayListExtra("KEYWORDS");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(GOOGLE_BOOKS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        for (int i = 0; i < keywords.size(); i++){
            uriBuilder.appendQueryParameter("q", keywords.get(i));
        }
        uriBuilder.appendQueryParameter("orderBy", orderBy);
        uriBuilder.appendQueryParameter("langRestrict", "en");

        return new BookLoader(this, uriBuilder.toString());
    }

    private int mOnLoadFinishedCounter = 0;
    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        if (mOnLoadFinishedCounter == 1) {
            mEmptyStateView.setText(R.string.no_books);
        }
        mAdapter.clear();

        if (books != null && !books.isEmpty()){
            mAdapter.addAll(books);
        }

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}
