package com.mealtiger.backend.rest.model;

/**
 * This interface is used for requests made by the user towards the API.
 * Special request objects are needed because
 * a) they serve as data transfer objects to prevent malicious attacks
 * b) some values may not be set in requests but are saved in database models (e.g. ratings in the Recipe model)
 * c) certains values need to be checked using the JSR-305 annotations such as 'NotNull' and 'Min' or 'Max'. These should not be used in database entities.
 * @param <T> Model for the request.
 */
public interface Request<T extends QueriedObject> {

    /**
     * This method converts a request into the entity it is derived from.
     * @return Object of the entity's type.
     */
    T toEntity();

}
