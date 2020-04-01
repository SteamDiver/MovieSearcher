package edu.vsu.YourMoviesApi.repos;

import edu.vsu.YourMoviesApi.domain.FavoriteGenre;
import edu.vsu.YourMoviesApi.domain.MovieUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteGenreRepo extends CrudRepository<FavoriteGenre, Integer> {
    FavoriteGenre getByDbGenre(Integer dbGenre);

    List<FavoriteGenre> getAllByUser(MovieUser user);
}
