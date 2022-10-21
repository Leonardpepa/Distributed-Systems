package Model;

public interface CRUDRepository<T> {
    public T create(T object);
    public T read(int id);
    public T update(T object);
    public T delete(T object);
}
