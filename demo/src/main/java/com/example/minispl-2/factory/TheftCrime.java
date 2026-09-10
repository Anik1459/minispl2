package com.example.oopproject.factory;

public class TheftCrime implements Crime {
    @Override
    public String getType() {
        return "Theft/Burglary";
    }
}
