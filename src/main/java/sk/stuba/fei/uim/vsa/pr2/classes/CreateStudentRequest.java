package sk.stuba.fei.uim.vsa.pr2.classes;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CreateStudentRequest {

    @JsonProperty("aisId")
    private Long aisId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("year")
    private List<Integer> year;

    @JsonProperty("term")
    private Integer term;

    @JsonProperty("programme")
    private String programme;

    public Long getAisId() {
        return aisId;
    }

    public void setAisId(Long aisId) {
        this.aisId = aisId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Integer> getYear() {
        return year;
    }

    public void setYear(List<Integer> year) {
        this.year = year;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }
}
