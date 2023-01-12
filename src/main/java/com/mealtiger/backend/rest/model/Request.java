package com.mealtiger.backend.rest.model;

public interface Request<T extends QueriedObject> {

    T toEntity();

}
