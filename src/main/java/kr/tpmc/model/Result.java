package kr.tpmc.model;

public class Result<T> {
    private final boolean isError;
    private final T value;

    private Result(boolean isError, T value) {
        this.isError = isError;
        this.value = value;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(false, value);
    }

    public static <T> Result<T> error(T value) {
        return new Result<>(true, value);
    }

    public boolean isError() {
        return isError;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return isError ? "Error: " + value : "Success: " + value;
    }
}
