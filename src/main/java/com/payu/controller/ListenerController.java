package com.payu.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.context.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javafx.util.*;

import com.payu.client.*;
import com.payu.model.*;

@RestController
public class ListenerController {

    private ProxyService proxyService;

    private ApplicationContext applicationContext;

    @Autowired
    public ListenerController(ProxyService proxyService, ApplicationContext applicationContext) {
        this.proxyService = proxyService;
        this.applicationContext = applicationContext;
    }

    @PostMapping("/proxy/leader")
    public ResponseEntity setLeader(@RequestBody final Leader requestLeader){

        System.out.println("The new leader is : " + requestLeader.getLeader() + "Host :" + requestLeader.getHost());

        if(requestLeader != null){

            GeneralState.setCurrentLeader(
                    new Pair<>(requestLeader.getHost(), requestLeader.getHost().split("//")[1]));

            return ResponseEntity.accepted().build();
        }

        return ResponseEntity.badRequest().build();
    }


    @GetMapping("/")
    public ResponseEntity execute() throws Exception{
        final String currentLeader = proxyService.getLeader();
        return ResponseEntity.ok(currentLeader);
    }


    @GetMapping("/proxy/down")
    public void shutDown(){
        System.exit(SpringApplication.exit(applicationContext, () -> 0));
    }


}

