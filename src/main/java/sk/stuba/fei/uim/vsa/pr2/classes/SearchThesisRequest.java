package sk.stuba.fei.uim.vsa.pr2.classes;

import lombok.Getter;
import lombok.Setter;

public class SearchThesisRequest {
    @Getter
    @Setter
    private int studentId;
    @Getter
    @Setter
    private int teacherId;
}
