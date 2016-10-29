package app;

public class EmployeeId {

    private final long value;

    public EmployeeId(final long value) {
        if (value < 0) {
            throw new IllegalArgumentException("EmployeeId must be larger than zero");
        }

        this.value = value;
    }

    public EmployeeId(final String value) {
        this(Long.parseLong(value));
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}
