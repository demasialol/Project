package com.androidapp.youjigom;

import java.util.HashMap;
import java.util.Map;

public class FirebasePost {
    public String Token;
    public String fullName;
    public String country;

    public FirebasePost() {
    }

    public FirebasePost(String Token, String fullName, String country) {
        this.Token = Token;
        this.fullName = fullName;
        this.country = country;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Token",Token);
        result.put("fullName",fullName);
        result.put("country",country);

        return result;
    }

}
