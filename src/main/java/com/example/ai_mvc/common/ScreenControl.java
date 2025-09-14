package com.example.ai_mvc.common;

import jakarta.servlet.http.HttpServletRequest;

public class ScreenControl {
    private HttpServletRequest request;

    public ScreenControl(HttpServletRequest request) {
        this.request = request;
    }

    public String nextScreen(String screenName, Object... objects) {
        for (Object obj : objects) {
            if (obj instanceof MenuBean) {
                request.setAttribute("menuBean", obj);
            } else {
                request.setAttribute("body", obj);
            }
        }
        return screenName;
    }
}