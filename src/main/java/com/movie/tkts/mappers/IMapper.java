package com.movie.tkts.mappers;

public interface IMapper<T, S> {

    S toDto(T t);

    T toEntity(S s);

}
