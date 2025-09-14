package com.example.ai_mvc.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonController {

    @Autowired
    protected HttpServletRequest context;
}
