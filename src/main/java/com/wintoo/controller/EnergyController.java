package com.wintoo.controller;

import com.wintoo.model.*;
import com.wintoo.service.EnergyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller

public class EnergyController extends BaseController {
	
	@Autowired
	private EnergyService energyService;
	
	@RequestMapping(value="getallenergyitems.do",method={RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public  List<Tree> getAllEnergyTypes(){
		return energyService.getAllEnergyTypes();
	}
	
	@RequestMapping(value="getallwateritems.do",method={RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public  List<Tree> getWaterEnergyTypes(){
		return energyService.getWaterEnergyTypes();
	}
	
	@RequestMapping(value="getallelectricitems.do",method={RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public  List<Tree> getElectricEnergyTypes(){
		return energyService.getElectricEnergyTypes();
	}

	@RequestMapping(value="getenergy.do",method= RequestMethod.POST)
	@ResponseBody
	public  EnergyChart getEnergy(EnergySearch energySearch){
		return energyService.getEnergy(energySearch);
	}

    @RequestMapping(value="getwaterenergymap.do",method= RequestMethod.POST)
    @ResponseBody
    public  EnergyChart getWaterEnergyMap(EnergySearch energySearch){
        return energyService.getWaterEnergyMap(energySearch);
    }

    @RequestMapping(value="getwaterenergy.do",method= RequestMethod.POST)
    @ResponseBody
    public  EnergyChart getWaterEnergy(EnergySearch energySearch){
        return energyService.getWaterEnergy(energySearch);
    }

	@RequestMapping(value="getenergytable.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<EnergyTable> getEnergyTable(EnergySearch energySearch){
		return energyService.getEnergyTable(energySearch);
	}
	
	@RequestMapping(value="getdayenergy.do",method= RequestMethod.POST)
	@ResponseBody
	public  double getDayEnergy(@RequestBody String ids){
		return energyService.getDayEnergy(ids);
	}
	
	@RequestMapping(value="getmonthenergy.do",method= RequestMethod.POST)
	@ResponseBody
	public  double getMonthEnergy(@RequestBody String ids){
		return energyService.getMonthEnergy(ids);
	}
    @RequestMapping(value="getorganmonthenergy.do",method= RequestMethod.POST)
    @ResponseBody
    public  double getOrganMonthEnergy(@RequestBody String ids){
        return energyService.getOrganMonthEnergy(ids);
    }
    @RequestMapping(value="getwatermonthenergy.do",method= RequestMethod.POST)
    @ResponseBody
    public  double getWaterMonthEnergy(@RequestBody String ids){
        return energyService.getWaterMonthEnergy(ids);
    }
    @RequestMapping(value="getallwaternets.do",method= RequestMethod.POST)
    @ResponseBody
    public DataTable getAllWaternets(){
        return energyService.getAllWaternets();
    }

    @RequestMapping(value="addwaternet.do",method= RequestMethod.POST)
    @ResponseBody
    public void addWaternet(Waternet waternet){
        energyService.addWaternet(waternet);
    }

    @RequestMapping(value="deletewaternet.do",method= RequestMethod.POST)
    @ResponseBody
    public void deleteWaternet(@RequestBody String ids){
        energyService.deleteWaternet(ids);
    }

    @RequestMapping(value="updatewaternet.do",method= RequestMethod.POST)
    @ResponseBody
    public void updateWaternet(Waternet waternet){
        energyService.updateWaternet(waternet);
    }
    @RequestMapping(value="getgatewayinwater.do",method= RequestMethod.POST)
    @ResponseBody
    public List<Options> getGatewaynames(){
        return energyService.getGatewaynames();
    }
    @RequestMapping(value="getequipbygateway.do",method= RequestMethod.POST)
    @ResponseBody
    public List<Options> getEquipnames(@RequestBody String id){
        return energyService.getEquipnames(id);
    }

    @RequestMapping(value="getproduce.do",method= RequestMethod.POST)
    @ResponseBody
    public List<EquipList> getProduce(@RequestBody String id){
        return energyService.getProduce(id);
    }

    @RequestMapping(value="addproduce.do",method= RequestMethod.POST)
    @ResponseBody
    public void addProduce(@RequestBody String ids){
        energyService.addProduce(ids);
    }

    @RequestMapping(value="deleteproduce.do",method= RequestMethod.POST)
    @ResponseBody
    public void deleteProduce(@RequestBody String ids){
        energyService.deleteProduce(ids);
    }

    @RequestMapping(value="getconsume.do",method= RequestMethod.POST)
    @ResponseBody
    public List<EquipList> getConsume(@RequestBody String id){
        return energyService.getConsume(id);
    }
    @RequestMapping(value="addconsume.do",method= RequestMethod.POST)
    @ResponseBody
    public void addConsume(@RequestBody String ids){
        energyService.addConsume(ids);
    }

    @RequestMapping(value="deleteconsume.do",method= RequestMethod.POST)
    @ResponseBody
    public void deleteConsume(@RequestBody String ids){
        energyService.deleteConsume(ids);
    }

    @RequestMapping(value="getleak.do",method= RequestMethod.POST)
    @ResponseBody
    public  LeakChart getLeak(LeakSearch leakSearch){
        return energyService.getLeak(leakSearch);
    }

    @RequestMapping(value = "getnetnames.do", method = RequestMethod.POST)
    @ResponseBody
    public List<Options> getNetNames() {
        return energyService.getNetNames();
    }

    @RequestMapping(value="getproducerank.do",method= RequestMethod.POST)
    @ResponseBody
    public List<LeakRank> getProduceRank(@RequestBody String id){
        return energyService.getProduceRank(id);
    }

    @RequestMapping(value="getconsumerank.do",method= RequestMethod.POST)
    @ResponseBody
    public List<LeakRank> getConsumeRank(@RequestBody String id){
        return energyService.getConsumeRank(id);
    }
}
