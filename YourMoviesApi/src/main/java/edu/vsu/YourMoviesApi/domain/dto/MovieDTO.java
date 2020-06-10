package edu.vsu.YourMoviesApi.domain.dto;

public class MovieDTO {
    private int id;
    private String note;
    private String posterPath;
    private String title;
    private String originCountry;
    private String releaseDate;
    private String overview;
    private boolean adult;
    private int revenue;
    private int runtime;
    private float voteAverage;

    private String poster;

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public static final class MovieDTOBuilder {
        private int id;
        private String note;
        private String posterPath;
        private String title;
        private String originCountry;
        private String releaseDate;
        private String overview;
        private boolean adult;
        private int revenue;
        private int runtime;
        private float voteAverage;
        private String poster;

        private MovieDTOBuilder() {
        }

        public static MovieDTOBuilder create() {
            return new MovieDTOBuilder();
        }

        public MovieDTOBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public MovieDTOBuilder withNote(String note) {
            this.note = note;
            return this;
        }

        public MovieDTOBuilder withPosterPath(String posterPath) {
            this.posterPath = posterPath;
            return this;
        }

        public MovieDTOBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public MovieDTOBuilder withOriginCountry(String originCountry) {
            this.originCountry = originCountry;
            return this;
        }

        public MovieDTOBuilder withReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public MovieDTOBuilder withOverview(String overview) {
            this.overview = overview;
            return this;
        }

        public MovieDTOBuilder withAdult(boolean adult) {
            this.adult = adult;
            return this;
        }

        public MovieDTOBuilder withRevenue(int revenue) {
            this.revenue = revenue;
            return this;
        }

        public MovieDTOBuilder withRuntime(int runtime) {
            this.runtime = runtime;
            return this;
        }

        public MovieDTOBuilder withVoteAverage(float voteAverage) {
            this.voteAverage = voteAverage;
            return this;
        }

        public MovieDTOBuilder withPoster(String poster) {
            this.poster = poster;
            return this;
        }

        public MovieDTO build() {
            MovieDTO movieDTO = new MovieDTO();
            movieDTO.setId(id);
            movieDTO.setNote(note);
            movieDTO.setPosterPath(posterPath);
            movieDTO.setTitle(title);
            movieDTO.setOriginCountry(originCountry);
            movieDTO.setReleaseDate(releaseDate);
            movieDTO.setOverview(overview);
            movieDTO.setAdult(adult);
            movieDTO.setRevenue(revenue);
            movieDTO.setRuntime(runtime);
            movieDTO.setVoteAverage(voteAverage);
            movieDTO.setPoster(poster);
            return movieDTO;
        }
    }
}


