package com.challenge.yaca.commitViewer.service;

import com.challenge.yaca.commitViewer.model.Commit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.challenge.yaca.commitViewer.utils.Commands.CommandExecuteException;

/**
 * Service that used the git cli through cmd.exe to get the list of commits
 */
public interface GitCLIService {

    Logger logger = LoggerFactory.getLogger(GitCLIService.class);

    List<Commit> getCommits(String repoUrl) throws CommandExecuteException;
}
