package com.challenge.yaca.commitViewer.service;

import com.challenge.yaca.commitViewer.model.Commit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.List;

import static com.challenge.yaca.commitViewer.client.CommandLineClient.CommandExecuteException;

/**
 * Service that used the git cli through cmd.exe to get the list of commits
 */
public interface CommitViewerService {

    Logger logger = LoggerFactory.getLogger(CommitViewerService.class);

    /**
     * Either fetches commits from the GitHub API or run git log command in terminal.
     * @param owner username of the owner of the repository
     * @param repo  repository name
     * @return      List of Commits for the main branch
     * TODO a different endpoint could be added to fetch commits for a specific branch
     * @throws CommandExecuteException  when ProcessBuilder::start() fails
     */
    List<Commit> fetchCommitsByRepositoryUrl(@NonNull String owner, @NonNull String repo) throws CommandExecuteException;
}
