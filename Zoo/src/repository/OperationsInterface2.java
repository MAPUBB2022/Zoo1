package repository;

@FunctionalInterface
public interface OperationsInterface2<E> {
    boolean hasAttractionOrNot(E e);
}

// Implementation:
// - a specific guest has to go to that specific attraction
// - a specific instructor has to hold that specific show
