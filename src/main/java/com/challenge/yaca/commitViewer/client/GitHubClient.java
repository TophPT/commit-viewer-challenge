package com.challenge.yaca.commitViewer.client;

import com.challenge.yaca.commitViewer.model.Commit;
import com.challenge.yaca.commitViewer.service.CommitViewerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Client for making requests to the GitHub API
 */
@Component
public class GitHubClient {

    public static class GitHubClientException extends RestClientException {
        public GitHubClientException(String message) { super(message); }
    }

    static final Logger logger = LoggerFactory.getLogger(CommitViewerService.class);

    private final String COMMITS_URL = "https://api.github.com/repos/%s/%s/commits";
    private final RestTemplate rest;
    private final HttpHeaders headers;

    public GitHubClient() {
        this.rest = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
    }

    /**
     * Fetch commits from the GitHub repository: /owner/repo
     */
    public Stream<Commit> getCommits(String owner, String repo) throws GitHubClientException {
        String url = String.format(COMMITS_URL, owner, repo);
        logger.info("GET {}", url);
        try {
            ResponseEntity<Commit[]> responseEntity = rest.getForEntity(url, Commit[].class);
            return Arrays.stream(Objects.requireNonNull(responseEntity.getBody()));
        } catch (RestClientException e) {
            throw new GitHubClientException(e.getMessage());
        }
    }
}
