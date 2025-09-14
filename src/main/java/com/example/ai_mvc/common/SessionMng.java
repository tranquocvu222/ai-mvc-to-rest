package com.example.ai_mvc.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionMng {
    private HttpServletRequest request;

    public SessionMng(HttpServletRequest request) {
        this.request = request;
    }

    public ManageInfo getMngInfo() {
        HttpSession session = request.getSession();
        ManageInfo manageInfo = (ManageInfo) session.getAttribute("manageInfo");
        if (manageInfo == null) {
            manageInfo = new ManageInfo();
            manageInfo.setUserId("defaultUser");
            session.setAttribute("manageInfo", manageInfo);
        }
        return manageInfo;
    }
}