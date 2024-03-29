package com.example.demo.exception;

import javax.print.Doc;

public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(Long id){
        super("not find doctor with id "+id);
    }

}
