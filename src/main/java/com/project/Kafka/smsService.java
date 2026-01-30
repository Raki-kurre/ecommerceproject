package com.project.Kafka;

import org.springframework.stereotype.Service;

@Service
public class smsService {

    public void sendSms(String phone, String message) {
        System.out.println("📲 SMS SENT TO " + phone);
        System.out.println(message);
    }
}
