package com.example.android.bookworm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookAdapter extends ArrayAdapter<Book> {

    private final Context mContext;

    public BookAdapter(@NonNull Context context, ArrayList<Book> pBooks) {
        super(context, 0, pBooks);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book book = getItem(position);

        TextView bookTitleView = listItemView.findViewById(R.id.title_text_view);
        bookTitleView.setText(book.getBookTitle());

        TextView authorsView = listItemView.findViewById(R.id.authors_view);
        String authors = "";
        List<String> authorsList = book.getAuthors();
        for (int i = 0; i < authorsList.size(); i++){
            if (i == (authorsList.size() - 1)){
                if (authorsList.size() > 1){
                    authors = authors + "and " + authorsList.get(i);
                } else {
                    authors = authors + authorsList.get(i);
                }
            } else {
                authors = authors + authorsList.get(i) + ", ";
            }
        }
        authorsView.setText(authors);

        TextView publisherView = listItemView.findViewById(R.id.publisher_view);
        publisherView.setText(mContext.getResources().getString(R.string.publisher_text_view) + book.getPublisher());

        TextView publicationYear = listItemView.findViewById(R.id.publication_year);
        publicationYear.setText(mContext.getResources().getString(R.string.publication_year_text_view) + book.getPublishedDate());

        TextView languageView = listItemView.findViewById(R.id.language_view);
        languageView.setText(mContext.getResources().getString(R.string.language_view) + " " + formatLanguageName(book.getLanguage()));

        TextView pageCountView = listItemView.findViewById(R.id.page_count);
        pageCountView.setText(mContext.getResources().getString(R.string.page_count) + " " + String.valueOf(book.getPageCount()));

        TextView textSnippetView = listItemView.findViewById(R.id.text_snippet);
        textSnippetView.setText("\"" + Html.fromHtml(book.getTextSnippet()) + "\"");

        return listItemView;
    }

    private String formatLanguageName(String languageCode){
        Locale locale = new Locale(languageCode);
        return locale.getDisplayLanguage(locale);
    }
}
