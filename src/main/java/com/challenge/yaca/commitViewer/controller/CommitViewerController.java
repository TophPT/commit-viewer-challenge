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

/**
 * Commit Viewer endpoints
 */
@RestController
@RequestMapping(value = "/api/commits", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommitViewerController {

    final
    GitCLIService gitCLIService;

    private static final Logger logger = LoggerFactory.getLogger(GitCLIService.class);

    public CommitViewerController(GitCLIService gitCLIService) {
        this.gitCLIService = gitCLIService;
    }

    @GetMapping("")
    public @ResponseBody ResponseEntity<List<Commit>> get(
            HttpServletRequest request,
            @RequestHeader(value = "repoUrl", required = false) String optionRepoUrl) {
        logger.info("GET request for {}", request.getRequestURI());
        List<Commit> commits = gitCLIService.getCommits(optionRepoUrl);

        return ResponseEntity.ok(commits);
    }

}