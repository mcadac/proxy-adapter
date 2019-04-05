package com.payu.process;

import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import com.payu.client.*;

@Component
public class LeaderPingSchedule {

    private ProxyService proxyService;

    @Autowired
    public LeaderPingSchedule(ProxyService proxyService) {
        this.proxyService = proxyService;
    }


    @Scheduled(fixedRate = 5000, initialDelay = 30000)
    public void pingLeader(){
         proxyService.getPriority();
    }



}
