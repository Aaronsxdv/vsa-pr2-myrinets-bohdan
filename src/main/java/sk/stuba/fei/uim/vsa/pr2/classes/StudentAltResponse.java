package sk.stuba.fei.uim.vsa.pr2.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.vsa.pr2.solution.Student;

@AllArgsConstructor
public class StudentAltResponse {

    @Getter
    @Setter
    private Long aisId;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private Integer year;

    @Getter
    @Setter
    private String programme;
    @Getter
    @Setter
    private Integer thesis;

    public StudentAltResponse(Student s){
        this.aisId = s.getAisId();
        this.programme = s.getStudyProgramme();
        this.year = s.getYear();
        this.email = s.getEmail();
        this.name = s.getName();
        this.thesis = Math.toIntExact(s.getThesis().getId());
    }

}
