package com.greenlearner.ticketbookingservice.controller;

import org.apache.tomcat.util.digester.ArrayStack;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */

@RestController
public class TaskTrackerController {

    List<String> pendingTaskList = new ArrayStack<>();

    @GetMapping("/todo/items")
    List<String> getAllPendingTasks(){
        return pendingTaskList;
    }


    @GetMapping("/todo/items")
    @ResponseStatus(HttpStatus.CREATED)
    void addTaskIntoList(@RequestBody String task){
        pendingTaskList.add(task);
    }
}
