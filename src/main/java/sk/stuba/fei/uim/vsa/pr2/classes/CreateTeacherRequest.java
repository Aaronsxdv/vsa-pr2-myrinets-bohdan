package sk.stuba.fei.uim.vsa.pr2.classes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CreateTeacherRequest {

    @Getter
    @Setter
    @JsonProperty("aisId")
    private Long aisId;

    @Getter
    @Setter
    @JsonProperty("name")
    private String name;

    @Getter
    @Setter
    @JsonProperty("email")
    private String email;

    @Getter
    @Setter
    @JsonProperty("password")
    private String password;

    @Getter
    @Setter
    @JsonProperty("institute")
    private String institute;

    @Getter
    @Setter
    @JsonProperty("department")
    private String department;

}
