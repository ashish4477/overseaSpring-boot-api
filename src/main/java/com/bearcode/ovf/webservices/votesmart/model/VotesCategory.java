package com.bearcode.ovf.webservices.votesmart.model;

/**
 * Date: 09.10.13
 * Time: 18:12
 *
 * @author Leonid Ginzburg
 */
public class VotesCategory {
    private String id;
    private String name;

    public VotesCategory() {
    }

    public VotesCategory(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
