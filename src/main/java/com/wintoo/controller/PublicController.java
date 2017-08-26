package com.wintoo.controller;

import com.wintoo.model.*;
import com.wintoo.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller

public class PublicController extends BaseController {
	
	@Autowired
	private PublicService publicService;

	@RequestMapping(value="gettime.do",method= RequestMethod.POST)
	@ResponseBody
	public  String getTime(){
		return publicService.getTime();
	}

	@RequestMapping(value="getpublicenergy.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<String> getEnergyTypes(){
		return publicService.getEnergyTypes();
	}

	@RequestMapping(value="getpublicbuilds.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<String> getBuilds(){
		return publicService.getBuilds();
	}

    @RequestMapping(value="getpublicorgans.do",method= RequestMethod.POST)
    @ResponseBody
    public  List<String> getOrgans(){
       return publicService.getOrgans();
    }

    @RequestMapping(value="public.do",method= RequestMethod.POST)
    @ResponseBody
    public  DataTable getPublic(){ 	return publicService.getPublic();	}

	@RequestMapping(value = "publicorgan.do",method = RequestMethod.POST)
	@ResponseBody
	public DataTable getPublicOrgan() { return publicService.getPublicOrgan();}

	@RequestMapping(value = "publicbulids.do",method = RequestMethod.POST)
	@ResponseBody
	public DataTable getPublicBulids() { return publicService.getPublicBulids();}
}
