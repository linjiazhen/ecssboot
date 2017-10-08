package com.wintoo.controller;

import com.wintoo.model.DataTable;
import com.wintoo.model.LoginForm;
import com.wintoo.service.BaseService;
import com.wintoo.service.EmailService;
import com.wintoo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private BaseService baseService;
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public String login(HttpSession session,LoginForm loginForm ){
        //if ((((String)session.getAttribute("check")).toLowerCase()).equals(loginForm.getCheck().toLowerCase())){
            if (baseService.checklogin(loginForm)) {
                session.setAttribute("islogin", true);
                session.setAttribute("userid",loginForm.getId());
                return "0";
            }
            else
                return "2";
//        }
//        else
//            return "1";
    }
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public void logout(HttpSession session){
        session.setAttribute("islogin",false);
    }

    @RequestMapping(value = "checklogin.do", method = RequestMethod.POST)
    @ResponseBody
    public String checklogin(HttpSession session) throws Exception{
        if (session.getAttribute("islogin")!=null&&(Boolean) session.getAttribute("islogin")) {
            return "yes";
        } else {
            return "no";
        }
    }

    @RequestMapping(value = "sendemail.do", method = RequestMethod.POST)
    @ResponseBody
    public void sendEmail(HttpSession session) throws MessagingException {
        emailService.sendAttachmentEmail(session);
    }

    @RequestMapping(value = "getlogs.do", method = RequestMethod.POST)
    @ResponseBody
    public DataTable getLogs() throws MessagingException {
        return loginService.getLogs();
    }

    @RequestMapping(value = "addlog.do", method = RequestMethod.POST)
    @ResponseBody
    public void addLog(HttpSession session, HttpServletRequest request , @RequestBody String operate) throws MessagingException {
       // System.out.println((String)session.getAttribute("userid")+operate+request.getRemoteAddr());
        loginService.addLog((String)session.getAttribute("userid"),operate,request.getRemoteHost());
    }

    @RequestMapping(value = "deletelog.do", method = RequestMethod.POST)
    @ResponseBody
    public void deleteLog(@RequestBody String ids) throws MessagingException {
        loginService.deleteLog(ids);
    }

}
