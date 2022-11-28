package repository;

public interface ICrudRepository<ID, E>{
    void add(E e);
    void delete(ID id);
    void update(ID id, E e);
    E findByID(ID id);
}
