package d2.api.events.enums;

public enum AccountStatus {

    ADMIN(2),
    ENABLED(1),
    DISABLED(0),
    BLOCKED(-1);

    private int status;

    AccountStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
