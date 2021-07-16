package com.challenge.yaca.commitViewer.controller;

import com.challenge.yaca.commitViewer.model.Commit;
import com.challenge.yaca.commitViewer.service.CommitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/commits", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommitController {

    CommitService commitService;

    @GetMapping("/get")
    public @ResponseBody ResponseEntity<List<Commit>> get(@RequestBody String url) {
        return new ResponseEntity<>(commitService.getCommits(url), HttpStatus.OK);
    }

}