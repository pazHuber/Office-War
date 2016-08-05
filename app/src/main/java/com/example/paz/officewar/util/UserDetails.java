package com.example.paz.officewar.util;

/**
 * Created by paz on 23/06/16.
 */
public class UserDetails  {
    private String name;
    private Integer score;

    public UserDetails() {}

    public UserDetails(String name, Integer score) {
        this.name = name;
        this.score = score;
    }

    public Integer getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
