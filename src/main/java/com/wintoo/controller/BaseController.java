package com.wintoo.controller;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseController {  

    @ExceptionHandler
    public String exception(Exception e) { 
    		System.out.println(e.getMessage());
 //   		e.printStackTrace();
            return "error"; 
    }
    
    @InitBinder
	protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		   dateFormat.setLenient(false);
		   binder.registerCustomEditor(Date.class, new CustomDateEditor(
		     dateFormat, false));
		//initDataBinder(request, binder);
	}
}
