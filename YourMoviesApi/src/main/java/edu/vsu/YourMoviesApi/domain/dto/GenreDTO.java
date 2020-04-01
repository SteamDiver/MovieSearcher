package edu.vsu.YourMoviesApi.domain.dto;

public class GenreDTO {
//    private String username;
    private String name;
    private int id;

//    public GenreDTO(String username, String name) {
//        this.username = username;
//        this.name = name;
//    }

    public GenreDTO(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public GenreDTO(String name) {
        this.name = name;
    }

//    public GenreDTO(String username, String name, int id) {
//        this.username = username;
//        this.name = name;
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
