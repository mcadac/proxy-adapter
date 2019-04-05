package com.payu.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.payu.client.*;
import com.payu.model.*;


@RestController
public class SpeakerController {

    @Autowired
    private ProxyService proxyService;


    @GetMapping("/proxy/leader")
    public String getLeader(){

        if(GeneralState.getCurrentLeader() == null){
            return proxyService.getMyHost().split("//")[1];
        }

        System.out.println("The current leader is : " + GeneralState.getCurrentLeader());
        return GeneralState.getCurrentLeader().getValue();
    }


    @GetMapping("/proxy")
    public Integer getPriority(){
        return GeneralState.getPriority();
    }




}
