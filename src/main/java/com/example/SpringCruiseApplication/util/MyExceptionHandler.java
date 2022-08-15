package com.example.SpringCruiseApplication.util;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyExceptionHandler {
    static public List parseConsValidException(ConstraintViolationException e){

        return e.getConstraintViolations().stream()
                .map(a->a.getMessageTemplate())
                .collect(Collectors.toList());

    }
}
