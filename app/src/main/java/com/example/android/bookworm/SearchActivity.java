package com.example.android.bookworm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<String> keywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final ImageView searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView searchBox = findViewById(R.id.search_box);
                String searchEntry = searchBox.getText().toString();

                if (searchEntry.equals("") || searchEntry.trim().length() == 0) {
                    Toast.makeText(SearchActivity.this, "Enter a keyword", Toast.LENGTH_SHORT).show();
                } else {
                    keywords = new ArrayList<>();
                    keywords = getSearchKeywords(searchEntry);
                    Intent listIntent = new Intent(SearchActivity.this, BooklistActivity.class);
                    listIntent.putStringArrayListExtra("KEYWORDS", (ArrayList<String >) keywords);
                    startActivity(listIntent);
                }
            }
        });
    }

    private List<String> getSearchKeywords(String searchEntry) {
        List<String> keywords = new ArrayList<>();

        if (!searchEntry.contains(" ")) {
            keywords.add(searchEntry);
        } else {
            String cleanSearchEntry = searchEntry.trim();
            String[] separateWords = cleanSearchEntry.split("\\s+");
            keywords.addAll(Arrays.asList(separateWords));
        }

        return keywords;
    }
}
