package com.caiths.caiapiinterface;

import com.caiths.caiapiinterface.client.CaiApiClient;
import com.caiths.caiapiinterface.model.User;

public class Main {
    public static void main(String[] args) {
        CaiApiClient caiApiClient = new CaiApiClient();
        String result1 = caiApiClient.getNameByGet("在虎");
        String result2 = caiApiClient.getNameByPost("在虎");
        User user = new User();
        user.setUsername("哭哭");
        String result3 = caiApiClient.getUsernameByPost(user);
        System.out.println(result1);
        System.out.println(result2);
        System.out.println(result3);
    }
}
