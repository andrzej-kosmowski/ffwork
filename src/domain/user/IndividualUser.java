package domain.user;

public class IndividualUser extends User {
    private String studentId;

    public IndividualUser(String email, String displayName) {
        super(email, displayName);
    }

    public IndividualUser(String email, String displayName, String studentId) {
        super(email, displayName);
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }
}
