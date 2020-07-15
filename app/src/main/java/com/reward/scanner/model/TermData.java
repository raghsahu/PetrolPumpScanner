package com.reward.scanner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TermData {
    @SerializedName("term_id")
    @Expose
    private String termId;
    @SerializedName("term")
    @Expose
    private String term;
    @SerializedName("sort_order")
    @Expose
    private String sortOrder;

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
