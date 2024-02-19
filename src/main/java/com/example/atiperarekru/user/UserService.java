package com.example.atiperarekru.user;


import com.example.atiperarekru.NoDataFoundException;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.atiperarekru.NoDataFoundException.forDataNotFound;
import static java.net.http.HttpClient.newHttpClient;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final static String GITHUB_API_URL = "https://api.github.com/";

    public List<UserRepositoryDTO> getUserRepositoriesData(String username)
            throws URISyntaxException, IOException, NoDataFoundException, InterruptedException {
        List<UserRepositoryDTO> result = new ArrayList<>();

        for (String repositoryName : getRepositories(username)) {
            result.add(new UserRepositoryDTO(
                    username,
                    repositoryName,
                    getBranchesAndLastSHAs(username, repositoryName)));
        }

        return result;
    }

    private HttpResponse<String> sendGetRequest(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        return newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    public List<String> getRepositories(String username)
            throws NoDataFoundException, URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(GITHUB_API_URL + "users/" + username + "/repos");
        HttpResponse<String> response = sendGetRequest(uri);

        if (response.statusCode() == NOT_FOUND.value()) {
            throw new IllegalArgumentException("Provided user could not be found");
        }

        JSONArray jsonResponse = new JSONArray(response.body());

        if (jsonResponse.isEmpty()) {
            forDataNotFound();
        }

        return IntStream.range(0, jsonResponse.length())
                .mapToObj(jsonResponse::getJSONObject)
                .map(jsonObject -> jsonObject.getString("name"))
                .collect(Collectors.toList());
    }

    private Map<String, String> getBranchesAndLastSHAs(String username, String repository)
            throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(GITHUB_API_URL + "repos/" + username + "/" + repository + "/branches");
        JSONArray response = new JSONArray(sendGetRequest(uri).body());
        return IntStream.range(0, response.length())
                .mapToObj(response::getJSONObject)
                .collect(Collectors.toMap(
                        jsonObject -> jsonObject.getString("name"),
                        jsonObject -> jsonObject.getJSONObject("commit").getString("sha")));
    }

    public record UserRepositoryDTO(
            String username,
            String repositoryName,
            Map<String, String> lastBranchNameAndCommitSHA) {
    }

}
