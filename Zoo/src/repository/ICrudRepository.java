package repository;

public interface ICrudRepository<ID, E> {
    public void add(E e);
    public void delete(ID id);
    void update(ID id, E e);
    E findbyId(ID id);

}
