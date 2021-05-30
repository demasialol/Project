package com.androidapp.youjigom;

<<<<<<< HEAD
=======
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

>>>>>>> origin/master
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
