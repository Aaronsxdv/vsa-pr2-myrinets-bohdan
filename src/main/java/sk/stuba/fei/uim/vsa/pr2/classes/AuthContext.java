package sk.stuba.fei.uim.vsa.pr2.classes;

public class AuthContext {
    private String userType;
    private Long userId;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
