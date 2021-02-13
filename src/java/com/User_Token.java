/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

/**
 *
 * @author LAPTOPVTC.VN
 */

import java.util.concurrent.ThreadLocalRandom;


public class User_Token {

    public String generateToken() {
        char[] hexArray = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        String token = "";

        for (int i = 0; i < 64; i++) {
            int rand = ThreadLocalRandom.current().nextInt(0, 36);
            token += hexArray[rand];
        }

        return token;

    }
}

