package com.example.readme;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by dobry on 02.07.17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private static final String LOG_TAG = BookAdapter.class.getSimpleName();

    public BookAdapter(Activity context, ArrayList<Book> Books) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, Books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);
        Log.i(LOG_TAG, "Item position: " + position);

        // Find the TextView in the list_item.xml (mapping)
        TextView titleBookTextView = (TextView) listItemView.findViewById(R.id.book_title);
        TextView authorBookTextView = (TextView) listItemView.findViewById(R.id.author);
        TextView languageCode = (TextView) listItemView.findViewById(R.id.country_code);
        TextView currencyCode = (TextView) listItemView.findViewById(R.id.currency_code);
        TextView price = (TextView) listItemView.findViewById(R.id.book_price);

        titleBookTextView.setText(currentBook.getTitle());
        authorBookTextView.setText(currentBook.getAuthor());
        languageCode.setText(currentBook.getLanguage());
        currencyCode.setText(currentBook.getCurrency());
        String formattedPrice = formatPrice(currentBook.getPrice());
        price.setText(formattedPrice);

        Log.i(LOG_TAG, "ListView has been returned");
        return listItemView;

    }
    // Format with two decimal places for price value
    private String formatPrice(double price) {
        DecimalFormat priceF = new DecimalFormat("0.00");
        return priceF.format(price);
    }


}
