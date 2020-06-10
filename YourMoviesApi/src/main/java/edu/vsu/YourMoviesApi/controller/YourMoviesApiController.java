package edu.vsu.YourMoviesApi.controller;

import edu.vsu.YourMoviesApi.domain.DiaryMovie;
import edu.vsu.YourMoviesApi.domain.FavoriteGenre;
import edu.vsu.YourMoviesApi.domain.MovieDbGenre;
import edu.vsu.YourMoviesApi.domain.dto.GenreDTO;
import edu.vsu.YourMoviesApi.domain.dto.MovieDTO;
import edu.vsu.YourMoviesApi.service.MovieDbService;
import edu.vsu.YourMoviesApi.service.MovieUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/yourmovies")
public class YourMoviesApiController {

    private final MovieDbService movieDbService;
    private final MovieUserService movieUserService;


    public YourMoviesApiController(MovieDbService movieDbService, MovieUserService movieUserService) {
        this.movieDbService = movieDbService;
        this.movieUserService = movieUserService;
    }

    @GetMapping("search/movie/{page}")
    public ResponseEntity<List<MovieDTO>> searchMovie(@RequestParam String query, @PathVariable int page) {
        System.out.println(query);
        return ResponseEntity.ok().body(movieDbService.searchMovie(query, page));
    }

    @GetMapping("recommendation/movie/{page}")
    public ResponseEntity<List<MovieDTO>> genreRecommendation(@RequestParam List<String> genres, @PathVariable int page) {
        return ResponseEntity.ok().body(movieDbService.genreRecommendation(genres, page));
    }

    @GetMapping("movie/info/{movieId}")
    public ResponseEntity<?> getMovieInfo(@PathVariable int movieId) {
        try {
            return ResponseEntity.ok().body(movieDbService.getMovieInfo(movieId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("genre/movie")
    public void updateMovieDbGenre() throws IOException {
        movieDbService.updateDbGenres();
    }

    @GetMapping("genre/all")
    public ResponseEntity<List<MovieDbGenre>> getSupportedGenres() {
        return ResponseEntity.ok().body(movieDbService.getSupportedGenres());
    }

    @PostMapping("genre")
    public ResponseEntity<?> addFavoriteGenre(@RequestHeader String authorization, @RequestBody GenreDTO genre) {
        try {
            FavoriteGenre favoriteGenre = movieUserService.addFavoriteGenre(authorization, genre);
            if (favoriteGenre != null) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("genre")
    public ResponseEntity<?> getFavoriteGenres(@RequestHeader String authorization) {
        try {
            return ResponseEntity.ok().body(movieUserService.getFavoriteGenres(authorization));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("genre")
    public ResponseEntity<?> removeFavoriteGenre(@RequestHeader String authorization, @RequestBody GenreDTO genre) {
        try {
            movieUserService.removeFavoriteGenre(authorization, genre);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("movie")
    public ResponseEntity<?> addDiaryMovie(@RequestHeader String authorization, @RequestBody MovieDTO movie) {
        try {
            DiaryMovie diaryMovie = movieUserService.addDiaryMovie(authorization, movie);
            if (diaryMovie != null) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("movie")
    public ResponseEntity<?> updateDiaryMovie(@RequestHeader String authorization, @RequestBody MovieDTO movie) {
        try {
            DiaryMovie diaryMovie = movieUserService.updateDiaryMovie(authorization, movie);
            if (diaryMovie != null) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("movie")
    public ResponseEntity<?> getDiaryMovies(@RequestHeader String authorization) {
        try {
            return ResponseEntity.ok().body(movieUserService.getDiaryMovies(authorization));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("movie/{movieId}")
    public ResponseEntity<?> getDiaryMovie(@RequestHeader String authorization, @PathVariable int movieId) {
        try {
            return ResponseEntity.ok().body(movieUserService.getDiaryMovie(authorization, movieId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("movie")
    public ResponseEntity<?> removeDiaryMovie(@RequestHeader String authorization, @RequestBody MovieDTO movie) {
        try {
            movieUserService.removeDiaryMovie(authorization, movie);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
