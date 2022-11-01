package repository;

public interface ICrudRepository<ID, E>{
    void add(E e);
    void delete(ID id);
    void update(E e, ID id);
    E findByID(ID id);
}
