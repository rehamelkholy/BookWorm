package com.example.android.bookworm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class BookActivity extends AppCompatActivity {
    public static final String LOG_TAG = BookActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Intent bookIntent = getIntent();
        Bundle bundle = bookIntent.getBundleExtra("BUNDLE");
        final Book book = (Book) bundle.getSerializable("BOOK");

        TextView bookTitleView = (TextView) findViewById(R.id.book_title);
        bookTitleView.setText(book.getBookTitle());

        TextView authorsView = (TextView) findViewById(R.id.authors);
        List<String> authorsList = book.getAuthors();
        String authors = "";
        for (int i = 0; i < authorsList.size(); i++){
            if (i == (authorsList.size() - 1)){
                if (authorsList.size() > 1){
                    authors = authors + "and " + authorsList.get(i);
                }else {
                    authors = authors + authorsList.get(i);
                }
            }else {
                authors = authors + authorsList.get(i) + ", ";
            }
        }
        authorsView.setText(authors);

        TextView descriptionView = (TextView) findViewById(R.id.description);
        String description = "\"" + Html.fromHtml(book.getBookDescription()) + "\"";
        descriptionView.setText(description);

        TextView publisherView = (TextView) findViewById(R.id.publisher);
        publisherView.setText(book.getPublisher());

        TextView publishedDateView = (TextView) findViewById(R.id.published_date);
        publishedDateView.setText(book.getPublishedDate());

        TextView pageCountView = (TextView) findViewById(R.id.page_count);
        pageCountView.setText(this.getResources().getString(R.string.pages, book.getPageCount()));

        TextView categoriesView = (TextView) findViewById(R.id.categories);
        List<String> categoriesList = book.getCategories();
        String categories = "";
        for (int i = 0; i < categoriesList.size(); i++){
            if (i == (categoriesList.size() - 1)){
                if (categoriesList.size() > 1){
                    categories = categories + "and " + categoriesList.get(i);
                }else {
                    categories = categories + categoriesList.get(i);
                }
            }else {
                categories = categories + categoriesList.get(i) + ", ";
            }
        }
        categoriesView.setText(categories);

        TextView languageView = (TextView) findViewById(R.id.language);
        languageView.setText(formatLanguageName(book.getLanguage()));

        Button previewButton = (Button) findViewById(R.id.preview_button);
        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String previewLink = book.getPreviewLink();
                if (previewLink.isEmpty())
                {
                    Toast.makeText(BookActivity.this, "No preview link available", Toast.LENGTH_SHORT).show();
                }else {
                    Uri previewPage = Uri.parse(previewLink);
                    Intent previewIntent = new Intent(Intent.ACTION_VIEW, previewPage);
                    if (previewIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(previewIntent);
                    }
                }
            }
        });

        Button infoButton = (Button) findViewById(R.id.info_button);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String infoLink = book.getInfoLink();
                if (infoLink.isEmpty())
                {
                    Toast.makeText(BookActivity.this, "No info link available", Toast.LENGTH_SHORT).show();
                }else {
                    Uri infoPage = Uri.parse(infoLink);
                    Intent infoIntent = new Intent(Intent.ACTION_VIEW, infoPage);
                    if (infoIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(infoIntent);
                    }
                }
            }
        });

        Button buyButton = (Button) findViewById(R.id.buy_button);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buyLink = book.getBuyLink();
                if (buyLink.isEmpty())
                {
                    Toast.makeText(BookActivity.this, "No buying link available", Toast.LENGTH_SHORT).show();
                }else {
                    Uri buyPage = Uri.parse(buyLink);
                    Intent buyIntent = new Intent(Intent.ACTION_VIEW, buyPage);
                    if (buyIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(buyIntent);
                    }
                }
            }
        });
    }

    private String formatLanguageName(String languageCode){
        Locale locale = new Locale(languageCode);
        return locale.getDisplayLanguage(locale);
    }
}
