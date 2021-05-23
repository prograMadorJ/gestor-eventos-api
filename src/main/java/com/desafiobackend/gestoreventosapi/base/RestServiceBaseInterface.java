package com.desafiobackend.gestoreventosapi.base;

import java.util.List;

public interface RestServiceBaseInterface<T, D extends DTO> {

    void create(T o);

    String update(String id, T o);

    String delete(String id);

    T getOne(String id);

    D getOne(String id, Class<? extends D> clazz);

    List<T> getAll();

    List<D> getAll(Class<? extends D> clazz);

    D toDTO(T o, Class<? extends D> clazz);

    T toEntity(D o, Class<? extends T> clazz);
}
