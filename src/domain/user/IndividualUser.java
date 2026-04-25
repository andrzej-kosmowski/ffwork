package domain.user;

import discount.Discount;
import discount.NoDiscount;
import discount.StudentDiscount;

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

    @Override
    public Discount getDiscount() {
        return (studentId != null && !studentId.isBlank())
                ? new StudentDiscount()
                : new NoDiscount();
    }
}
