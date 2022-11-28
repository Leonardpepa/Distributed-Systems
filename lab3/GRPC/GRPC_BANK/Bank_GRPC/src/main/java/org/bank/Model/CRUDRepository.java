package org.bank.Model;

public interface CRUDRepository <T> {
    T create(T object);

    T read(int id);

    T update(T object);

    T delete(T object);
}
