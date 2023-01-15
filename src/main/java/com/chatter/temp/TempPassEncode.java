package com.chatter.temp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

public class TempPassEncode {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Map<String, Integer> mapsPers = new HashMap<>();
        mapsPers.put("Petr Arsentev pass", 123);
        mapsPers.put("Ivan Sobolev", 123456);
        mapsPers.put("Nikolay Vodin pass", 345);
        mapsPers.put("Svetlana Donovan pass", 456);
        mapsPers.put("Sergei Shirokov pass", 567);
        for (Integer intPas : mapsPers.values()) {
            System.out.println(encoder.encode(Integer.toString(intPas)));
        }

        String password = "123456";
        var crp = encoder.encode(password);
        System.out.println("Password after encoder : " + crp);
    }
}
