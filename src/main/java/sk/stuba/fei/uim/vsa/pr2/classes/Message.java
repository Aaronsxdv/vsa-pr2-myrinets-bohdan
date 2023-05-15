package sk.stuba.fei.uim.vsa.pr2.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
class Error{
    public String type;
    public String trace;

}
public class Message {
    @Getter
    @Setter
    private Integer code;

    @Getter
    @Setter
    private String message;

    public Message(Integer code,String message){
        this.code = code;
        this.message = message;
    }
    public Message(Integer code,String message,String type,String trace){
        this.code = code;
        this.message = message;
        this.error = new Error(type,trace);
    }

    @Getter
    @Setter
    private Error error;
}
