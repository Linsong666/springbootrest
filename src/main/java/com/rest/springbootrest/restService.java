package com.rest.springbootrest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@RestController
public class restService {

    @GetMapping (value = "/rest")
    public String rest(String mobile,String gitUser,String gitPwd,String serviceName)
    {
//        return new StringBuffer(mobile+gitUser).reverse().toString();
        String cmds = "/apps/easy.sh"+" "+mobile+" "+gitUser+" "+gitPwd+" "+serviceName;
        String[] callCmd = {"/bin/sh","-c",cmds};
//        System.out.println("exit code:\n" + execCommand(callCmd).get(0).toString());
//        System.out.println();
//        System.out.println("command result:\n" + execCommand(callCmd).get(1).toString());
        return execCommand(callCmd).get(1).toString();
    }

    public static Map execCommand(String...str) {
        Map<Integer, String> map = new HashMap<>();
        ProcessBuilder pb = new ProcessBuilder(str);
        pb.redirectErrorStream(true);
        Process process = null;
        try {
            process = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = null;
        if (process != null) {
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        }

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (reader != null) {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (process != null) {
                process.waitFor();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (process != null) {
            map.put(0, String.valueOf(process.exitValue()));
        }

        try {
            map.put(1, stringBuilder.toString());
        } catch (StringIndexOutOfBoundsException e) {
            if (stringBuilder.toString().length() == 0) {
                return map;
            }
        }
        return map;
    }
}