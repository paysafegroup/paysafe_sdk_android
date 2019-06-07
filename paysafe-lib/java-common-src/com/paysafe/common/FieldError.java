package com.paysafe.common;

import com.paysafe.common.impl.DomainObject;

public class FieldError implements DomainObject {

    private String field;
    private String error;


    /**
    * Get Field.
    *
    * @return Field.
    * */
    public final String getField() {
        return field;
    }

    /**
    * Set Field.
    *
    * @param field Error field.
    * */
    public final void setField(final String field) {
        this.field = field;
    }

    /**
    * Get Error.
    *
    * @return Error.
    * */
    public final String getError() {
        return error;
    }

    /**
    * Set Error.
    *
    * @param error Error.
    * */
    public final void setError(final String error) {
        this.error = error;
    }
} // end of class FieldError
