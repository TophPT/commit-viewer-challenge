package com.challenge.yaca.commitViewer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit implements Comparable<Commit> {

    @JsonProperty("sha")
    private String id;

    @JsonProperty("commit")
    private CommitInfo commitInfo;

    public Commit() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CommitInfo getCommitInfo() {
        return commitInfo;
    }

    public void setCommitInfo(CommitInfo commitInfo) {
        this.commitInfo = commitInfo;
    }

    @Override
    public int compareTo(Commit other) {
        return this.getCommitInfo().getAuthor().getDate()
                .compareTo(other.getCommitInfo().getAuthor().getDate());
    }


}
