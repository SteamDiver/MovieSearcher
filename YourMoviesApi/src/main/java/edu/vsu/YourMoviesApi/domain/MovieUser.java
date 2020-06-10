package edu.vsu.YourMoviesApi.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "movie_user")
public class MovieUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    @JsonIgnore
    private String password;
    private Date dateRegistration;
    private Date dateLastVisited;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<DiaryMovie> diaryMovies;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<FavoriteGenre> favoriteGenres;

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<FavoriteGenre> getFavoriteGenres() {
        return favoriteGenres;
    }

    public void setFavoriteGenres(Set<FavoriteGenre> favoriteGenres) {
        this.favoriteGenres = favoriteGenres;
    }

    public Set<DiaryMovie> getDiaryMovies() {
        return diaryMovies;
    }

    public void setDiaryMovies(Set<DiaryMovie> diaryMovies) {
        this.diaryMovies = diaryMovies;
    }

    public Date getDateRegistration() {
        return dateRegistration;
    }

    public void setDateRegistration(Date dateRegistration) {
        this.dateRegistration = dateRegistration;
    }

    public Date getDateLastVisited() {
        return dateLastVisited;
    }

    public void setDateLastVisited(Date dateLastVisited) {
        this.dateLastVisited = dateLastVisited;
    }
}
