package com.example.readme;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    /** URL for earthquake data from the Google Books dataset */
    private static final String BOOK_REQUEST_BASE_URL =
            "https://www.googleapis.com/books/v1/volumes?";

    /** Book loader*/
    private static final int BOOK_LOADER = 1;

    /** Book Adapter*/
    private BookAdapter mAdapter;

    /** Empty text view*/
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Find the list */
        ListView bookListView =(ListView) findViewById(R.id.list);

        /* Set the empty text view */
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        /* Search button*/
        Button btn_search = (Button) findViewById(R.id.search_button);

        // Create a new adapter that takes an empty list
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current earthquake that was clicked on
                Book currentBook = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentBook.getBuyLink());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);

            }
        });

        // Get reference to the ConnectivityManager to check state of network connectivity
        final ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Show the loading indicator while new data is being fetched
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.VISIBLE);

            // Get a reference to the LoaderManager, in order to interact with loaders.
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            //LoaderManager
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER, null, MainActivity.this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            /*View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);*/

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        /* Set click listener on the button */
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get details on the currently active default data network
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                // If there is a network connection, fetch data
                if (networkInfo != null && networkInfo.isConnected()) {
                    // Clear the current content
                    mAdapter.clear();
                    // Hide the empty state text view
                    mEmptyStateTextView.setVisibility(View.GONE);

                    // Show the loading indicator while new data is being fetched
                    View loadingIndicator = findViewById(R.id.loading_spinner);
                    loadingIndicator.setVisibility(View.VISIBLE);

                    EditText search_book = (EditText) findViewById(R.id.search_view_field);
                    if(TextUtils.isEmpty(search_book.getText().toString().trim())){
                        // If the search is on empty text
                        bookListView.setEmptyView(mEmptyStateTextView);
                    } else {
                        getLoaderManager().restartLoader(BOOK_LOADER, null, MainActivity.this);
                    }
                }
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(BOOK_REQUEST_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        EditText search_book = (EditText) findViewById(R.id.search_view_field);
        String queryBook = search_book.getText().toString().trim();
        uriBuilder.appendQueryParameter(getString(R.string.query), queryBook);
        uriBuilder.appendQueryParameter(getString(R.string.max_results), "10");

        return new BookLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.empty_view_text);

        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
