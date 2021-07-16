package com.challenge.yaca.commitViewer.service;

import com.challenge.yaca.commitViewer.model.Commit;

import java.util.List;

public interface CommitService {
    List<Commit> getCommits(String url);
}
