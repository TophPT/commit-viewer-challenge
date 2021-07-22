package com.challenge.yaca.commitViewer.controller;

import com.challenge.yaca.commitViewer.model.Commit;
import com.challenge.yaca.commitViewer.service.CommitViewerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.challenge.yaca.commitViewer.client.CommandLineClient.CommandExecuteException;

/**
 * Commit Viewer endpoints
 */
@RestController("commitViewerController")
@RequestMapping(value = "/api/v1/commit-viewer", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommitViewerController {

    final
    CommitViewerService commitViewerService;

    public CommitViewerController(CommitViewerService commitViewerService) {
        this.commitViewerService = commitViewerService;
    }

    @GetMapping("/")
    public @ResponseBody String getReadMe() {
        return "Welcome to Yet Another Challenge Application!\n" +
                "To setup your development environment:\n" +
                " # Clone the repository:" +
                "\t $ git clone https://github.com/TophPT/commit-viewer-challenge.git\n" +
                "\t $ cd commit-viewer-challenge\n\n" +
                " # Either configure Maven or use your IDE build in Maven:\n" +
                "\t $ mvn clean install\n" +
                "\t $ mvn start\n\n" +
                " # Server is now listening on: http://localhost:8080/api/v1/commit-viewer/\n\n";
    }

    @GetMapping("/{owner}/{repo}")
    public @ResponseBody ResponseEntity<List<Commit>> getCommitsByRepositoryUrl(
            @PathVariable String owner,
            @PathVariable String repo) throws CommandExecuteException {
        List<Commit> commits = commitViewerService.fetchCommitsByRepositoryUrl(owner, repo);
        return ResponseEntity.ok(commits);
    }

}