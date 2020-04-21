package java.edu.vsu.yourmovies.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JwtResponse {
    @SerializedName("token")
    @Expose
    private String jwtToken;

    public JwtResponse() {
    }

    public JwtResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getToken() {
        return this.jwtToken;
    }
}