package com.salesianostriana.dam.trianafy.error.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubError implements SubErrorInterface{

    public SubError(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public String object;
    public String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String field;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object rejectedValue;

    public static SubErrorInterface fromObjectError(ObjectError error){
        SubError result = new SubError(error.getObjectName(), error.getDefaultMessage());
        if( error instanceof FieldError){
            result.setField(((FieldError) error).getField());
            result.setRejectedValue(((FieldError) error).getRejectedValue());
        }
        return result;
    }

}
