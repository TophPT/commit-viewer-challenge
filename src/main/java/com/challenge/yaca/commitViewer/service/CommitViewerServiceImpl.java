package com.challenge.yaca.commitViewer.service;

import com.challenge.yaca.commitViewer.client.CommandLineClient;
import com.challenge.yaca.commitViewer.client.GitHubClient;
import com.challenge.yaca.commitViewer.model.Commit;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.challenge.yaca.commitViewer.client.CommandLineClient.CommandExecuteException;
import static com.challenge.yaca.commitViewer.client.GitHubClient.GitHubClientException;

@Service
public class CommitViewerServiceImpl implements CommitViewerService {

    final GitHubClient gitHubClient;

    final CommandLineClient commandLineClient;

    public CommitViewerServiceImpl(GitHubClient gitHubClient, CommandLineClient commandLineClient) {
        this.gitHubClient = gitHubClient;
        this.commandLineClient = commandLineClient;
    }

    @Override
    public List<Commit> fetchCommitsByRepositoryUrl(@NonNull String owner, @NonNull String repo) throws CommandExecuteException {
        try {
            return gitHubClient.getCommits(owner, repo).collect(Collectors.toList());
        } catch (GitHubClientException e) {
            logger.info("Something went wrong fetching commits via GitHubAPI..");
        }
        return commandLineClient.getCommitLogs(owner, repo);
    }
}
