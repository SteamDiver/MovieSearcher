package edu.vsu.YourMoviesApi.domain;

import javax.persistence.*;

@Entity
@Table(name = "favorite_genre")
public class FavoriteGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int dbGenre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private MovieUser user;

    public int getDbGenre() {
        return dbGenre;
    }

    public void setDbGenre(int dbGenre) {
        this.dbGenre = dbGenre;
    }

    public MovieUser getUser() {
        return user;
    }

    public void setUser(MovieUser user) {
        this.user = user;
    }
}
