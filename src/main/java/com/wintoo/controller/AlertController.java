package com.wintoo.controller;

import com.wintoo.model.*;
import com.wintoo.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller

public class AlertController extends BaseController {
	
	@Autowired
	private AlertService alertService;
	
	@RequestMapping(value="getequipstate.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<EquipState> getEquipState(EquipSearch equipSearch){
		return alertService.getEquipState(equipSearch);
	}
    @RequestMapping(value="getequipalerts.do",method= RequestMethod.POST)
    @ResponseBody
    public  DataTable getAllEquipAlerts(){
        return alertService.getAllEquipAlerts();
    }
    @RequestMapping(value="getamerrorvalues.do",method= RequestMethod.POST)
    @ResponseBody
    public  List<ErrorValue> getAmErrorValues(@RequestBody String id){
        return alertService.getAmErrorValues(id);
    }
    @RequestMapping(value="getwatererrorvalues.do",method= RequestMethod.POST)
    @ResponseBody
    public  List<ErrorValue> getWaterErrorValues(@RequestBody String id){
        return alertService.getWaterErrorValues(id);
    }
	@RequestMapping(value="savealertconf.do",method= RequestMethod.POST)
	 @ResponseBody
	 public  void saveAlertConf(AlertConf alertConf){
		alertService.saveAlertConf(alertConf);
	}
	@RequestMapping(value="getalertconf.do",method= RequestMethod.POST)
	@ResponseBody
	public  AlertConf getAlertConf(){
		return alertService.getAlertConf();
	}
	@RequestMapping(value="savealertequip.do",method= RequestMethod.POST)
	@ResponseBody
	public  void saveAlertEquip(AlertEquip alertEquip){
		alertService.saveAlertEquip(alertEquip);
	}
	@RequestMapping(value="getalertequip.do",method= RequestMethod.POST)
	@ResponseBody
	public  AlertEquip getAlertEquip(){
		return alertService.getAlertEquip();
	}
    @RequestMapping(value="addrule.do",method= RequestMethod.POST)
    @ResponseBody
    public  void addAlertRule(AlertRule alertRule){
       alertService.addAlertRule(alertRule);
    }
    @RequestMapping(value="updaterule.do",method= RequestMethod.POST)
    @ResponseBody
    public  void updateAlertRule(AlertRule alertRule){
        alertService.updateAlertRule(alertRule);
    }
    @RequestMapping(value="getallrules.do",method= RequestMethod.POST)
    @ResponseBody
    public  DataTable getAlertRules(){
        return alertService.getAlertRules();
    }

    @RequestMapping(value="setruleonoff.do",method= RequestMethod.POST)
    @ResponseBody
    public  void setRuleOnoff(@RequestBody String ids){
         alertService.setRuleOnoff(ids);
    }
    @RequestMapping(value="deleterule.do",method= RequestMethod.POST)
    @ResponseBody
    public  void deleteRule(@RequestBody String ids){
        alertService.deleteAlertRule(ids);
    }
    @RequestMapping(value="getallalerts.do",method= RequestMethod.POST)
    @ResponseBody
    public  DataTable getAllAlerts(){
        return alertService.getAllAlerts();
    }

    @RequestMapping(value="getallbaoguanalerts.do",method= RequestMethod.POST)
    @ResponseBody
    public  DataTable getAllBaoguanAlerts(){
        return alertService.getAllBaoguanAlerts();
    }

    @RequestMapping(value="deletealert.do",method= RequestMethod.POST)
    @ResponseBody
    public  void deleteAlert(@RequestBody String ids){
        alertService.deleteAlert(ids);
    }

    @RequestMapping(value="deleteequipalert.do",method= RequestMethod.POST)
    @ResponseBody
    public  void deleteEquipAlert(@RequestBody String ids){
        alertService.deleteEquipAlert(ids);
    }

    @RequestMapping(value="deletebaoguan.do",method= RequestMethod.POST)
    @ResponseBody
    public  void deleteBaoguan(@RequestBody String ids){
        alertService.deleteBaoguan(ids);
    }

    @RequestMapping(value="setwhitelist.do",method= RequestMethod.POST)
    @ResponseBody
    public  void setWhiteList(@RequestBody String pras){
        alertService.setWhiteList(pras);
    }

    @RequestMapping(value="getwhitelist.do",method= RequestMethod.POST)
    @ResponseBody
    public  List<WhiteList> getWaterWhiteList(@RequestBody String type) {
        return alertService.getWhiteList(type);
    }
    @RequestMapping(value="deletewhitelist.do",method= RequestMethod.POST)
    @ResponseBody
    public  void deleteWhiteList(@RequestBody String id){
        alertService.deleteWhiteList(id);
    }
    @RequestMapping(value="getamlist.do",method= RequestMethod.POST)
    @ResponseBody
    public  List<AmRankList> getAmList(@RequestBody String time) {
        return alertService.getAmRankList(time);
    }

    @RequestMapping(value="getamsublist.do",method= RequestMethod.POST)
    @ResponseBody
    public  List<AmSublist> getAmSubist(@RequestBody String time) {
        return alertService.getAmSublist(time);
    }

    @RequestMapping(value="getwaterlist.do",method= RequestMethod.POST)
    @ResponseBody
    public  List<WaterRankList> getWaterList(@RequestBody String time) {
        return alertService.getWaterRankList(time);
    }
}
