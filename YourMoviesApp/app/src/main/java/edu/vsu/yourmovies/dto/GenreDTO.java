package java.edu.vsu.yourmovies.dto;

public class GenreDTO {
    private String name;
    private int id;

    public GenreDTO(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public GenreDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
