package com.wintoo.controller;

import com.wintoo.model.App_EnergyChart;
import com.wintoo.model.App_EnergySearch;
import com.wintoo.model.App_timesplitenergy;
import com.wintoo.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;

@Controller

public class AppController extends BaseController {
	
	@Autowired
	private AppService appService;
	
	
	@RequestMapping(value="gettimesplitenergy.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<App_timesplitenergy> getTimeSplitEnergy(){
		List<App_timesplitenergy> app_timesplitenergy=appService.getTimeSplitEnergy();
		return app_timesplitenergy;
	}
	
	@RequestMapping(value="getbuildenergysort.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<App_timesplitenergy> getBuildEnergySort(@RequestBody String flag){
		List<App_timesplitenergy> app_timesplitenergy=appService.getBuildEnergySort(flag);
		return app_timesplitenergy;
	}
	
	
	
	
	@RequestMapping(value="app_getenergy.do",method= RequestMethod.POST)
	@ResponseBody
	public  App_EnergyChart App_GetEnergy(App_EnergySearch app_energySearch) throws ParseException{
		return appService.App_GetEnergy(app_energySearch);
	}
	
	
	@RequestMapping(value="getbuildenergy.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<App_timesplitenergy> getBuildEnergy(@RequestBody String flag) throws ParseException{
		List<App_timesplitenergy> app_timesplitenergy=appService.getBuildEnergy(flag);
		return app_timesplitenergy;
	}

    @RequestMapping(value="gettypeenergy.do",method= RequestMethod.POST)
    @ResponseBody
    public  List<Double> App_GetTypeEnergy(App_EnergySearch app_energySearch) throws ParseException{
        return appService.App_GetTypeEnergy(app_energySearch);
    }

    @RequestMapping(value="getfuncenergy.do",method= RequestMethod.POST)
    @ResponseBody
    public  App_EnergyChart App_GetFuncEnergy(App_EnergySearch app_energySearch) throws ParseException{
        return appService.App_GetFuncEnergy(app_energySearch);
    }
	
}

