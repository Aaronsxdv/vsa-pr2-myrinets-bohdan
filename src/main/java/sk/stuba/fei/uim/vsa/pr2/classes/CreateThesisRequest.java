package sk.stuba.fei.uim.vsa.pr2.classes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.vsa.pr2.solution.ThesisType;

import java.util.List;

public class CreateThesisRequest  {

    @Getter
    @Setter
    @JsonProperty("registrationNumber")
    private String registrationNumber;

    @Getter
    @Setter
    @JsonProperty("title")
    private String title;

    @Getter
    @Setter
    @JsonProperty("description")
    private String description	;

    @Getter
    @Setter
    @JsonProperty("institute")
    private ThesisType type;


}
