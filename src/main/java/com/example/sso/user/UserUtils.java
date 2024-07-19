package com.example.sso.user;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserUtils {

    public static Map<String, Object> convertUserToMap(User user) {
        Map<String, Object> userMap = new LinkedHashMap<>();
        userMap.put("id", user.getId());
        userMap.put("name", user.getName());
        userMap.put("email", user.getEmail());
        userMap.put("dob", user.getDob());
        userMap.put("age", user.getAge());
        return userMap;
    }
}
