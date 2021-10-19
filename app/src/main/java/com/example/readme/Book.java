package com.example.readme;

public class Book {

    /**
     * Title of the book
     */
    private String mTitle;
    /**
     * Author of the book
     */
    private String mAuthor;
    /**
     * Price of the book
     */
    private String mCurrency;
    /**
     * Country code of language
     */
    private String mLanguage;
    /**
     * Url of the book
     */
    private String mBuyLink;

    private double mPrice;

    /**
     * @param title     - (String) name of the book i.e.: "Harry Potter i Kamień Filozoficzny"
     * @param author    - (String) name of author i.e.: "J.K. Rowling"
     * @param currency      - (String) currency code for amount, i.e: "PLN"
     * @param language  - (String) country code i.e.: "PL"
     */
    public Book(String title, String author, String language, String currency, String buyLink, double price) {
        mTitle = title;
        mAuthor = author;
        mLanguage = language;
        mCurrency = currency;
        mBuyLink = buyLink;
        mPrice = price;
    }

    //Returns

    public String getAuthor() {
        return mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public String getBuyLink() {
        return mBuyLink;
    }

    public double getPrice() {
        return mPrice;
    }
}
