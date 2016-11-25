package com.searchbook;

/**
 * Created by Olasumbo Ogunyemi on 11/25/2016.
 */
public class BookDetails {
    private String authorName;
    private String ISBN;
    private String physical_format;
    private int weight;
    private String openLibraryKey;
    private String publish_date;


    private String coverUrl;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getPhysical_format() {
        return physical_format;
    }

    public void setPhysical_format(String physical_format) {
        this.physical_format = physical_format;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getOpenLibraryKey() {
        return openLibraryKey;
    }

    public void setOpenLibraryKey(String openLibraryKey) {
        this.openLibraryKey = openLibraryKey;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }


    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

}
