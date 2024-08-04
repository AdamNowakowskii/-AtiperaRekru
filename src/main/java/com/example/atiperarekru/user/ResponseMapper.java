package com.example.atiperarekru.user;

import static java.util.stream.Collectors.toCollection;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;

public class ResponseMapper {

    public static Set<String> toRepositories(JSONArray json) {
        return IntStream.range(0, json.length())
                .mapToObj(json::getJSONObject)
                .map(jsonObject -> jsonObject.getString("name"))
                .collect(toCollection(LinkedHashSet<String>::new));
    }

    public static Map<String, String> toBranchSHA(JSONArray json) {
        return IntStream.range(0, json.length())
                .mapToObj(json::getJSONObject)
                .collect(Collectors.toMap(
                        jsonObject -> jsonObject.getString("name"),
                        jsonObject -> jsonObject.getJSONObject("commit").getString("sha")));
    }

}
