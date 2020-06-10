package edu.vsu.YourMoviesApi.repos;

import edu.vsu.YourMoviesApi.domain.MovieUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieUserRepo extends CrudRepository<MovieUser, Long> {
    MovieUser findByUsername(String username);
}