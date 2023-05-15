package sk.stuba.fei.uim.vsa.pr2.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.vsa.pr2.solution.Student;
import sk.stuba.fei.uim.vsa.pr2.solution.Teacher;

@AllArgsConstructor
public class TeacherAltResponse {

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
    private String institute;

    @Getter
    @Setter
    private String department;
    @Getter
    @Setter
    private int[] theses;

    public TeacherAltResponse(Teacher s){
        this.aisId = s.getAisId();
        this.theses = s.getSupervisedTheses().stream().mapToInt(t -> Math.toIntExact(t.getId())).toArray();
        this.email = s.getEmail();
        this.name = s.getName();
        this.institute = s.getInstitute();
        this.department = s.getDepartment();
    }

}
