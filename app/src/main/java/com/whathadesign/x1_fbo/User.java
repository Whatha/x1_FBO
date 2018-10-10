package com.whathadesign.x1_fbo;

/**
 * Created by Daniel on 03/05/2018.
 */

public class User {
    int id;
    String firstName, lastName, email,fbo;

    public User(int id, String firstName, String lastName, String email,String fbo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.fbo=fbo;
    }
}
