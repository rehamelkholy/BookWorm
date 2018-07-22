package com.example.android.bookworm;

import android.text.TextUtils;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){}

    private static List<Book> extractItemFromJson(String bookJSON){
        if (TextUtils.isEmpty(bookJSON)){
            return null;
        }

        List<Book> books = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < bookArray.length(); i++){
                JSONObject currentBook = bookArray.getJSONObject(i);

                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");

                JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                List<String> authors = new ArrayList<>();
                if (authorsArray != null) {
                    for (int j = 0; j < authorsArray.length(); j++) {
                        authors.add(authorsArray.getString(j));
                    }
                }else {
                    authors.add("");
                }

                String publisher = volumeInfo.optString("publisher");
                String publishedDate = volumeInfo.optString("publishedDate");
                String description = volumeInfo.optString("description");
                int pageCount = volumeInfo.optInt("pageCount");

                JSONArray categoriesArray = volumeInfo.optJSONArray("categories");
                List<String> categories = new ArrayList<>();
                if (categoriesArray != null) {
                    for (int j = 0; j < categoriesArray.length(); j++) {
                        categories.add(categoriesArray.optString(j));
                    }
                }else {
                    categories.add("");
                }

                String language = volumeInfo.getString("language");
                String previewLink = volumeInfo.optString("previewLink");
                String infoLink = volumeInfo.optString("infoLink");

                JSONObject saleInfo = currentBook.optJSONObject("saleInfo");
                String buyLink = saleInfo.optString("buyLink");

                JSONObject searchInfo = currentBook.optJSONObject("searchInfo");
                String textSnippet;
                if(searchInfo != null) {
                    textSnippet = searchInfo.optString("textSnippet");
                }else {
                    textSnippet = "";
                }

                Book book = new Book(title, authors, publisher, publishedDate, description, pageCount, categories, language, previewLink, infoLink, buyLink, textSnippet);
                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return books;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Book> fetchBookData(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            e.printStackTrace();
        }

        return extractItemFromJson(jsonResponse);
    }
}
