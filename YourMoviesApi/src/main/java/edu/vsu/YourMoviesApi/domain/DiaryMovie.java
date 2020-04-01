package edu.vsu.YourMoviesApi.domain;

import javax.persistence.*;

@Entity
@Table(name = "diary_movie")
public class DiaryMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int movieDbId;

    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    private MovieUser user;

    public long getId() {
        return id;
    }

    public int getMovieDbId() {
        return movieDbId;
    }

    public void setMovieDbId(int movieDbId) {
        this.movieDbId = movieDbId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public MovieUser getUser() {
        return user;
    }

    public void setUser(MovieUser user) {
        this.user = user;
    }
}
