package com.example.mihail.showtime;

/**
 * Created by mihail on 2.9.16.
 */
public class JsonSanitizer {

    public static String jsonSanitizeOne(String jsonString) {
        String wellFormedJson = com.google.json.JsonSanitizer.sanitize(jsonString);
        return wellFormedJson;
    }

}