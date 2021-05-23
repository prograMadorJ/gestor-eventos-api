package com.desafiobackend.gestoreventosapi.base;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class RestServiceBaseImpl<T, D extends DTO> implements RestServiceBaseInterface<T, D> {

    @Autowired
    protected MongoRepository repository;
    @Autowired
    protected ModelMapper modelMapper;

    @Override
    public void create(T o) {
        repository.save(o);
    }

    @Override
    public String update(String id, T o) {
        Object result = repository.findById(id).orElse(null);
        if(Objects.equals(result, null)) {
            return "not found";
        } else {
            repository.save(o);
            return "updated";
        }
    }

    @Override
    public String delete(String id) {
        Object result = repository.findById(id).orElse(null);
        if(Objects.equals(result, null)) {
            return "not found";
        } else {
            repository.deleteById(id);
            return "deleted";
        }
    }

    @Override
    public T getOne(String id) {
        Object result = repository.findById(id).orElse(null);
        if(!Objects.equals(result, null)) return (T) result;
        return null;
    }

    @Override
    public D getOne(String id, Class<? extends D> clazz) {
        Object result = repository.findById(id).orElse(null);
        if(!Objects.equals(result, null)) return modelMapper.map(result, clazz);
        return null;
    }

    @Override
    public List<T> getAll() {
        List<D> result = repository.findAll();
        if(result.size() > 0) return (List<T>) result;
        return null;
    }

    @Override
    public List<D> getAll(Class<? extends D> clazz) {
        List<D> result = repository.findAll();
        if(result.size() > 0) return (List<D>) repository.findAll().stream().map(e -> modelMapper.map(e, clazz)).collect(Collectors.toList());
        return null;
    }

    @Override
    public D toDTO(T o, Class<? extends D> clazz) {
        return modelMapper.map(o, clazz);
    }

    @Override
    public T toEntity(D o, Class<? extends T> clazz) {
        return modelMapper.map(o, clazz);
    }
}
