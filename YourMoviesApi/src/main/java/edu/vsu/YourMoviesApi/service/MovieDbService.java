package edu.vsu.YourMoviesApi.service;

import edu.vsu.YourMoviesApi.constant.MovieDbUrlConstants;
import edu.vsu.YourMoviesApi.domain.MovieDbGenre;
import edu.vsu.YourMoviesApi.domain.dto.MovieDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Service
public class MovieDbService {
    public static Map<Integer, MovieDbGenre> movieDbGenres = new HashMap<>();
    private final RestTemplate restTemplate;
    @Value("${api.key}")
    private String apiKey;

    public MovieDbService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<MovieDTO> searchMovie(String query, int page) {
        String request = UriComponentsBuilder
                .fromUriString(MovieDbUrlConstants.MAIN_URL)
                .path(MovieDbUrlConstants.SEARCH_MOVIE)
                .queryParam("api_key", apiKey)
                .queryParam("language", "ru-RU")
                .queryParam("page", page)
                .queryParam("query", query)
                .build().toUriString();

        ResponseEntity<String> exchange = restTemplate.exchange(request, HttpMethod.GET, null, String.class);

        //TODO add exception

        JSONArray movies = new JSONObject(exchange.getBody()).getJSONArray("results");

        List<MovieDTO> movieDTOS = new ArrayList<>();
        long start = System.currentTimeMillis();
        movies.forEach(movie -> {
            JSONObject movieInfo = new JSONObject(movie.toString());
            int id = movieInfo.getInt("id");
            String posterPath = movieInfo.get("poster_path").toString();
            String title = movieInfo.getString("title");
            String overview = movieInfo.getString("overview");
            if (posterPath != null && !posterPath.equals("null")) {
                byte[] poster = new byte[0];
                try {
                    poster = new URL("https://image.tmdb.org/t/p/w342" + posterPath).openStream().readAllBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String encode = Base64.getEncoder().encodeToString(poster);
                movieDTOS.add(MovieDTO.MovieDTOBuilder.create()
                        .withPosterPath(posterPath).withTitle(title)
                        .withOverview(overview).withId(id).withPoster(encode).build());
            } else {
                movieDTOS.add(MovieDTO.MovieDTOBuilder.create()
                        .withPosterPath(posterPath).withTitle(title)
                        .withOverview(overview).withId(id).build());
            }

        });
        long end = System.currentTimeMillis();
        System.out.println("TIME: " + (end - start));

        return movieDTOS;
    }

    public MovieDTO getMovieInfo(int movieId) {
        String request = UriComponentsBuilder
                .fromUriString(MovieDbUrlConstants.MAIN_URL)
                .path(MovieDbUrlConstants.MOVIE_INFO)
                .path("/" + movieId)
                .queryParam("api_key", apiKey)
                .queryParam("language", "ru-RU")
                .build().toUriString();

        ResponseEntity<String> exchange = restTemplate.exchange(request, HttpMethod.GET, null, String.class);

        MovieDTO movieDTO = getMovieDTO(exchange.getBody());
        return movieDTO;
    }

    private MovieDTO getMovieDTO(String exchange) {
        JSONObject movieInfo = new JSONObject(exchange);

        String posterPath = movieInfo.getString("poster_path");
        String title = movieInfo.getString("title");
        String releaseDate = movieInfo.getString("release_date");
        String overview = movieInfo.getString("overview");
        boolean adult = movieInfo.getBoolean("adult");
        int revenue = movieInfo.getInt("revenue");
        int runtime = movieInfo.getInt("runtime");
        float voteAverage = movieInfo.getFloat("vote_average");
        int id = movieInfo.getInt("id");

        String encode = null;
        if (posterPath != null && !posterPath.equals("null")) {
            byte[] poster = new byte[0];
            try {
                poster = new URL("https://image.tmdb.org/t/p/w342" + posterPath).openStream().readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            encode = Base64.getEncoder().encodeToString(poster);
        }

        return MovieDTO.MovieDTOBuilder.create()
                .withPoster(encode)
                .withId(id)
                .withPosterPath(posterPath)
                .withTitle(title)
                .withReleaseDate(releaseDate)
                .withOverview(overview)
                .withAdult(adult)
                .withRevenue(revenue)
                .withRuntime(runtime)
                .withVoteAverage(voteAverage).build();
    }

    public ResponseEntity<String> genreRecommendation(List<String> genres, int page) {
        String request = UriComponentsBuilder
                .fromUriString(MovieDbUrlConstants.MAIN_URL)
                .path(MovieDbUrlConstants.GENRE_RECOMMENDATION)
                .queryParam("api_key", apiKey)
                .queryParam("language", "ru-RU")
                .queryParam("sort_by", "popularity.desc")
                .queryParam("page", page)
                .queryParam("with_genres", String.join("%2C", genres))
                .build().toUriString();

        ResponseEntity<String> exchange = restTemplate.exchange(request, HttpMethod.GET, null, String.class);

        if (exchange.getStatusCode().isError()) {
            return ResponseEntity.status(exchange.getStatusCode()).build();
        } else {
            return exchange;
        }
    }

    @PostConstruct
    public void updateDbGenres() throws IOException {
        String request = UriComponentsBuilder
                .fromUriString(MovieDbUrlConstants.MAIN_URL)
                .path(MovieDbUrlConstants.GENRE_NAMES)
                .queryParam("api_key", apiKey)
                .queryParam("language", "ru-RU")
                .build().toUriString();

        String json = restTemplate.getForObject(request, String.class);

        JSONArray genres = new JSONObject(json).getJSONArray("genres");

        genres.forEach(genre -> {
            Object[] values = ((JSONObject) genre).toMap().values().toArray();
            movieDbGenres.put(Integer.valueOf(values[1].toString()), new MovieDbGenre(Integer.parseInt(values[1].toString()), values[0].toString()));
        });
    }
}
