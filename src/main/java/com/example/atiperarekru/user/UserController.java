package com.example.atiperarekru.user;

import static java.net.http.HttpResponse.BodyHandlers;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.atiperarekru.dto.MembershipType;
import com.example.atiperarekru.dto.PagePagination;
import com.example.atiperarekru.dto.UserRepositoryDTO;
import com.example.atiperarekru.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Value("${uri.api.github}")
    private String githubUri;

    private final HttpClient httpClient;

    @GetMapping(value = "/{username}/repositories", produces = APPLICATION_JSON_VALUE)
    public Set<UserRepositoryDTO> fetchUserRepositoriesData(
            @PathVariable String username,
            @RequestParam MembershipType type,
            PagePagination pagePagination)
            throws URISyntaxException, IOException, InterruptedException {
        Set<String> repositories = fetchRepositories(username, type, pagePagination);
        Set<UserRepositoryDTO> repositoriesData = new LinkedHashSet<>();

        for (String repository : repositories) {
            var branchWithLatestCommitSHA = fetchLatestBranchSHA(username, repository);
            var repositoryInfo = new UserRepositoryDTO(username, repository, branchWithLatestCommitSHA);
            repositoriesData.add(repositoryInfo);
        }

        return repositoriesData;
    }

    private Set<String> fetchRepositories(
            String username, MembershipType type, PagePagination pagePagination)
            throws IOException, InterruptedException {
        URI uri = getRepositoriesURI(username, type, pagePagination);
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

        if (response.statusCode() == NOT_FOUND.value())
            throw new UserNotFoundException();

        JSONArray jsonResponse = new JSONArray(response.body());
        return ResponseMapper.toRepositories(jsonResponse);
    }

    private URI getRepositoriesURI(String username, MembershipType type, PagePagination pagePagination) {
        return fromUriString(githubUri)
                .path("/users/" + username + "/repos")
                .queryParam(type.name())
                .queryParam("page", pagePagination.page())
                .queryParam("per_page", pagePagination.size())
                .build().toUri();
    }

    private Map<String, String> fetchLatestBranchSHA(String username, String repository)
            throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(githubUri + "/repos/" + username + "/" + repository + "/branches");
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        return ResponseMapper.toBranchSHA(new JSONArray(response.body()));
    }

}