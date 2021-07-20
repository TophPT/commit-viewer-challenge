package com.challenge.yaca.commitViewer.service;

import com.challenge.yaca.commitViewer.model.Commit;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.challenge.yaca.commitViewer.utils.Commands.CommandExecuteException;
import static com.challenge.yaca.commitViewer.utils.Commands.readCommitsFromCommandLine;

@Service
public class GitCLIServiceImpl implements GitCLIService {

    @Override
    public List<Commit> getCommits(String repoUrl) throws CommandExecuteException {
        logger.info("Using git CLI to get commits, a temporary folder will be created.");
        return readCommitsFromCommandLine(repoUrl);
    }



}
