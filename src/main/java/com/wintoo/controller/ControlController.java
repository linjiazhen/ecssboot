package com.wintoo.controller;

import com.wintoo.model.Ktxt;
import com.wintoo.service.ControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller

public class ControlController extends BaseController {
	
	@Autowired
	private ControlService controlService;

	@RequestMapping(value="getktxt.do",method= RequestMethod.POST)
	 @ResponseBody
	 public  List<Ktxt> getKtxt(@RequestBody String type){
		return controlService.getKtxt(type);
	}

}
