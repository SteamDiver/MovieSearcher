package edu.vsu.YourMoviesApi.service;

import edu.vsu.YourMoviesApi.domain.DiaryMovie;
import edu.vsu.YourMoviesApi.domain.FavoriteGenre;
import edu.vsu.YourMoviesApi.domain.MovieUser;
import edu.vsu.YourMoviesApi.domain.dto.GenreDTO;
import edu.vsu.YourMoviesApi.domain.dto.MovieDTO;
import edu.vsu.YourMoviesApi.domain.dto.UserDTO;
import edu.vsu.YourMoviesApi.repos.DiaryMovieRepo;
import edu.vsu.YourMoviesApi.repos.FavoriteGenreRepo;
import edu.vsu.YourMoviesApi.repos.MovieUserRepo;
import edu.vsu.YourMoviesApi.security.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieUserService implements UserDetailsService {

    private final MovieUserRepo movieUserRepo;

    private final FavoriteGenreRepo genreRepo;

    private final DiaryMovieRepo movieRepo;

    private final JwtTokenUtil jwtTokenUtil;

    private final MovieDbService movieDbService;

    private final BCryptPasswordEncoder bcryptEncoder;

    public MovieUserService(MovieUserRepo movieUserRepo, FavoriteGenreRepo genreRepo, DiaryMovieRepo movieRepo, JwtTokenUtil jwtTokenUtil, MovieDbService movieDbService, BCryptPasswordEncoder bcryptEncoder) {
        this.movieUserRepo = movieUserRepo;
        this.genreRepo = genreRepo;
        this.movieRepo = movieRepo;
        this.jwtTokenUtil = jwtTokenUtil;
        this.movieDbService = movieDbService;
        this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MovieUser user = movieUserRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

    public MovieUser save(UserDTO user) throws AuthenticationServiceException {
        if (movieUserRepo.findByUsername(user.getUsername()) == null) {
            MovieUser movieUser = new MovieUser();
            movieUser.setUsername(user.getUsername());
            movieUser.setPassword(bcryptEncoder.encode(user.getPassword()));
            return movieUserRepo.save(movieUser);
        } else {
            throw new AuthenticationServiceException("Username already exist!");
        }
    }

    public DiaryMovie addDiaryMovie(String token, MovieDTO movie) throws Exception {

        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        MovieUser movieUser = movieUserRepo.findByUsername(username);

        if (movieUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        if (movieRepo.getByMovieDbIdAndUser(movie.getId(), movieUser) != null) {
            throw new Exception("Movie already in favorite movies!");
        }

        DiaryMovie diaryMovie = new DiaryMovie();

        diaryMovie.setUser(movieUser);
        diaryMovie.setMovieDbId(movie.getId());

        return movieRepo.save(diaryMovie);
    }

    public DiaryMovie updateDiaryMovie(String token, MovieDTO movie) throws Exception {

        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));

        MovieUser movieUser = movieUserRepo.findByUsername(username);

        if (movieUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        DiaryMovie diaryMovie = movieRepo.getByMovieDbIdAndUser(movie.getId(), movieUser);

        if (diaryMovie == null) {
            throw new Exception("Movie not in user diary movie. Add to diary first!");
        }

        diaryMovie.setNote(movie.getNote());

        return movieRepo.save(diaryMovie);
    }

    public List<MovieDTO> getDiaryMovies(String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));

        MovieUser movieUser = movieUserRepo.findByUsername(username);

        if (movieUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        List<DiaryMovie> diaryMovies = movieRepo.getAllByUser(movieUser);
        ArrayList<MovieDTO> movieDTOS = new ArrayList<>();

        diaryMovies.forEach(dm -> {
            MovieDTO movieInfo = movieDbService.getMovieInfo(dm.getMovieDbId());
            movieInfo.setNote(dm.getNote());
            movieDTOS.add(movieInfo);
        });
        return movieDTOS;
    }

    public MovieDTO getDiaryMovie(String token, int movieID) {
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));

        MovieUser movieUser = movieUserRepo.findByUsername(username);

        if (movieUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        DiaryMovie dm = movieRepo.getByMovieDbIdAndUser(movieID, movieUser);

        MovieDTO movieInfo = movieDbService.getMovieInfo(dm.getMovieDbId());
        movieInfo.setNote(dm.getNote());

        return movieInfo;
    }

    public void removeDiaryMovie(String token, MovieDTO movie) throws Exception {
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));

        MovieUser movieUser = movieUserRepo.findByUsername(username);

        if (movieUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        DiaryMovie diaryMovie = movieRepo.getByMovieDbIdAndUser(movie.getId(), movieUser);

        if (diaryMovie == null) {
            throw new Exception("Movie not in user genres!");
        }

        movieRepo.delete(diaryMovie);
    }

    public FavoriteGenre addFavoriteGenre(String token, GenreDTO genre) throws Exception {
        if (genreRepo.getByDbGenre(genre.getId()) != null) {
            throw new Exception("Genre already in favorite genres!");
        }
        FavoriteGenre favoriteGenre = new FavoriteGenre();

        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));

        MovieUser movieUser = movieUserRepo.findByUsername(username);

        if (movieUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        favoriteGenre.setUser(movieUser);
        favoriteGenre.setDbGenre(genre.getId());

        return genreRepo.save(favoriteGenre);
    }

    public List<GenreDTO> getFavoriteGenres(String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));

        MovieUser movieUser = movieUserRepo.findByUsername(username);

        if (movieUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<FavoriteGenre> favoriteGenres = genreRepo.getAllByUser(movieUser);
        ArrayList<GenreDTO> genreDTOS = new ArrayList<>();
        favoriteGenres.forEach(fg -> {
            genreDTOS.add(new GenreDTO(MovieDbService.movieDbGenres.get(fg.getDbGenre()).getName(), fg.getDbGenre()));
        });
        return genreDTOS;
    }

    public void removeFavoriteGenre(String token, GenreDTO genre) throws Exception {
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));

        MovieUser movieUser = movieUserRepo.findByUsername(username);

        if (movieUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<FavoriteGenre> favoriteGenres = genreRepo.getAllByUser(movieUser);

        Optional<FavoriteGenre> genreOptional = favoriteGenres.stream().filter(favoriteGenre -> MovieDbService.movieDbGenres.get(favoriteGenre.getDbGenre()).getId() == genre.getId()
                && favoriteGenre.getUser().getUsername().equals(username)).findAny();

        if (genreOptional.isPresent()) {
            genreRepo.delete(genreOptional.get());
        } else {
            throw new Exception("Genre not in user genres!");
        }
    }
}