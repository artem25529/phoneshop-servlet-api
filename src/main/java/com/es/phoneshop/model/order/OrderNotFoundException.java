package com.es.phoneshop.model.order;

public class OrderNotFoundException extends RuntimeException{
    private Long id;

    public OrderNotFoundException(Long id) {
        this.id = id;
    }

    public OrderNotFoundException(){

    }

    public OrderNotFoundException(String message) {
        super(message);
    }

    public Long getId() {
        return id;
    }



}
