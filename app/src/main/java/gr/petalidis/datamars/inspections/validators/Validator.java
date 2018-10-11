package gr.petalidis.datamars.inspections.validators;

public interface Validator<T> {
    boolean isValid(T value);
}
