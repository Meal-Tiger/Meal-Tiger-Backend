package com.mealtiger.backend.rest.model;

/**
 * This interface is used to declare a object that is queried by the api. As such an object it must declare a response.
 */
public interface QueriedObject {

    /**
     * This is used to construct a response from a queried object.
     * @return the response to a request asking for this resource.
     */
    Response toResponse();

}
