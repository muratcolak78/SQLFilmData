package com.example.sqlfilmdata;

public class Film {

    private int film_id;
    private String title;
    private String description;
    private int release_year;
    private String language;
    private double rental_rate;
    private int rental_duration;

    public Film(int film_id, String title, String description, int release_year, String language, double rental_rate, int rental_duration) {
        this.film_id = film_id;
        this.title = title;
        this.description = description;
        this.release_year = release_year;
        this.language = language;
        this.rental_rate = rental_rate;
        this.rental_duration = rental_duration;
    }
    public int getFilm_id() {
        return film_id;
    }

    public void setFilm_id(int film_id) {
        this.film_id = film_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public double getRental_rate() {
        return rental_rate;
    }

    public void setRental_rate(double rental_rate) {
        this.rental_rate = rental_rate;
    }

    public int getRental_duration() {
        return rental_duration;
    }

    public void setRental_duration(int rental_duration) {
        this.rental_duration = rental_duration;
    }
}
