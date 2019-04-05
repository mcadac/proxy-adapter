package com.payu.model;

import java.security.*;

import javafx.util.*;

public class GeneralState {

    /**
     *
     */
    private static Pair<String,String> currentLeader;

    /**
     *
     */
    private static Integer priority = Math.abs(new SecureRandom().nextInt());

    /**
     *
     */
    private static boolean isLeader;



    public static Pair<String,String> getCurrentLeader() {
        return currentLeader;
    }

        public static void setCurrentLeader(Pair<String,String> currentLeader) {
        GeneralState.currentLeader = currentLeader;
    }

    public static boolean isIsLeader() {
        return isLeader;
    }

    public static void setIsLeader(boolean isLeader) {
        GeneralState.isLeader = isLeader;
    }

    public static Integer getPriority() {
        return priority;
    }

}
