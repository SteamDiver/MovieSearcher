package edu.vsu.YourMoviesApi.domain;

public class MovieDbGenre {
    private int id;
    private String name;

    public MovieDbGenre() {
    }

    public MovieDbGenre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
