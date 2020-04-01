package edu.vsu.YourMoviesApi.repos;

import edu.vsu.YourMoviesApi.domain.DiaryMovie;
import edu.vsu.YourMoviesApi.domain.MovieUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryMovieRepo extends CrudRepository<DiaryMovie, Long> {
    DiaryMovie getById(Long id);

    DiaryMovie getByMovieDbIdAndUser(int name, MovieUser user);

    List<DiaryMovie> getAllByUser(MovieUser user);
}
