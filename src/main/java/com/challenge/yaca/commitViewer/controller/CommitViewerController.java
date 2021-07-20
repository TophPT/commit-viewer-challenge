package com.challenge.yaca.commitViewer.controller;

import com.challenge.yaca.commitViewer.model.Commit;
import com.challenge.yaca.commitViewer.service.GitCLIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.challenge.yaca.commitViewer.utils.Commands.CommandExecuteException;

/**
 * Commit Viewer endpoints
 */
@RestController
@RequestMapping(value = "/api/commit-viewer", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommitViewerController {

    final
    GitCLIService gitCLIService;

    static final Logger logger = LoggerFactory.getLogger(GitCLIService.class);

    public CommitViewerController(GitCLIService gitCLIService) {
        this.gitCLIService = gitCLIService;
    }

    @GetMapping("")
    public @ResponseBody ResponseEntity<List<Commit>> getCommitsByRepositoryUrl(
            HttpServletRequest request,
            @RequestHeader(value = "repoUrl", required = false) String optionRepoUrl) throws CommandExecuteException {
        logger.info("GET request for {}", request.getRequestURI());

        // read commits using CLI
        List<Commit> commits = gitCLIService.getCommits(optionRepoUrl);
        return ResponseEntity.ok(commits);
    }

}