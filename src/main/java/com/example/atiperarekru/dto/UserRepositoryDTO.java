package com.example.atiperarekru.dto;

import java.util.Map;

public record UserRepositoryDTO(
        String username,
        String repositoryName,
        Map<String, String> branchWithLatestCommitSHA) {
}