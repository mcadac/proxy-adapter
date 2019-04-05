package com.payu.model;

public class Leader {

    private String host;
    private String leader;


    public Leader(String host, String leader) {
        this.host = host;
        this.leader = leader;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
