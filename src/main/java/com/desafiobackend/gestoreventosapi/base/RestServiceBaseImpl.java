package com.desafiobackend.gestoreventosapi.base;


import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class RestServiceBaseImpl<T, D extends DTO> implements RestServiceBaseInterface<T, D> {

    private final MongoRepository repository;

    private final ModelMapper modelMapper = new ModelMapper();

    public RestServiceBaseImpl(MongoRepository repository) {
        this.repository = repository;
    }

    public String create(T o) {
        if (o == null) return BaseResponseMessages.GIVEN_NULL;
        Object result = repository.save(o);
        return result != null ? BaseResponseMessages.CREATED : BaseResponseMessages.CREATE_ERROR;
    }


    @Override
    public String update(String id, T o) {
        if (o == null) {
            return BaseResponseMessages.GIVEN_NULL;

        } else if(Objects.equals(id, null)) {
            return BaseResponseMessages.ID_NULL;
        }

        Object result = repository.findById(id).orElse(null);
        if(Objects.equals(result, null)) {
            return BaseResponseMessages.NOT_FOUND;
        } else {
            repository.save(o);
            return BaseResponseMessages.UPDATED;
        }
    }

    @Override
    public String delete(String id) {
        if(Objects.equals(id, null)) {
            return BaseResponseMessages.ID_NULL;
        }
        Object result = repository.findById(id).orElse(null);
        if(Objects.equals(result, null)) {
            return BaseResponseMessages.NOT_FOUND;
        } else {
            repository.deleteById(id);
            return BaseResponseMessages.DELETED;
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
