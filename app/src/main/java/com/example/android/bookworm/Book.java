package com.example.android.bookworm;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable{
    private String mTitle;
    private List<String> mAuthors;
    private String mPublisher;
    private String mPublishedDate;
    private String mDescription;
    private int mPageCount;
    private List<String> mCategories;
    private String mLanguage;
    private String mPreviewLink;
    private String mInfoLink;
    private String mBuyLink;
    private String mTextSnippet;

    public Book(String title, List<String> authors, String publisher, String publishedDate, String description, int pageCount, List<String> categories, String language, String previewLink, String infoLink, String buyLink, String textSnippet){

        mTitle = title;
        mAuthors = authors;
        mPublisher = publisher;
        mPublishedDate = publishedDate;
        mDescription = description;
        mPageCount = pageCount;
        mCategories = categories;
        mLanguage = language;
        mPreviewLink = previewLink;
        mInfoLink = infoLink;
        mBuyLink = buyLink;
        mTextSnippet = textSnippet;
    }

    public String getBookTitle() {
        return mTitle;
    }

    public List<String> getAuthors() {
        return mAuthors;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public String getBookDescription() {
        return mDescription;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public List<String> getCategories() {
        return mCategories;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public String getPreviewLink() {
        return mPreviewLink;
    }

    public String getInfoLink() {
        return mInfoLink;
    }

    public String getBuyLink() {
        return mBuyLink;
    }

    public String getTextSnippet() {
        return mTextSnippet;
    }
}
