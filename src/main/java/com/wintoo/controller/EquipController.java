package com.wintoo.controller;

import com.wintoo.model.*;
import com.wintoo.service.EquipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller

public class EquipController extends BaseController {
	
	@Autowired
	private EquipService equipService;
	
	@RequestMapping(value="getallequiptype.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<String> getEquipType(){
		return equipService.getAllEquipType();
	}
	
	@RequestMapping(value="getequipsubtype.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<Options> getEquipSubtype(@RequestBody String type){
		return equipService.getEquipSubtype(type);
	}
	
	@RequestMapping(value="gettypeparameter.do",method= RequestMethod.POST)
	@ResponseBody
	public  Options getTypeParameter(@RequestBody String typeid){
		return equipService.getTypeParameter(typeid);
	}
	
	@RequestMapping(value="getallequipbatchs.do",method= RequestMethod.POST)
	@ResponseBody
	public  DataTable getEquipBatchs(){
		return equipService.getAllEquipBatchs();
	}

	@RequestMapping(value="addequipbatch.do",method= RequestMethod.POST)
	@ResponseBody
	public void addEquipBatch(EquipBatch equipBatch){
		equipService.addEquipBatch(equipBatch);
	}
	
	@RequestMapping(value="deleteequipbatch.do",method= RequestMethod.POST)
	@ResponseBody
	public void deleteEquipBatch(@RequestBody String ids){
		equipService.deleteEquipBatch(ids);
	}
		
	@RequestMapping(value="updateequipbatch.do",method= RequestMethod.POST)
	@ResponseBody
	public void updateEquipBatch(EquipBatch equipBatch){
		equipService.updateEquipBatch(equipBatch);
	}
	
	@RequestMapping(value="getequipbatch.do",method= RequestMethod.POST)
	@ResponseBody
	public  EquipBatch getEquipBatch(@RequestBody String id){
		EquipBatch gp=equipService.getEquipBatch(id);
		return gp;
	}
/////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="getallequiplists.do",method= RequestMethod.POST)
	@ResponseBody
	public  DataTable getEquipLists(){
		return equipService.getAllEquipLists();
	}
	
	@RequestMapping(value="deleteequipinstall.do",method= RequestMethod.POST)//删除设备的装配信息
	@ResponseBody
	public void deleteEquipList(@RequestBody String id){
		equipService.deleteEquipInstall(id);
	}
	
	
	@RequestMapping(value="deleteequip.do",method= RequestMethod.POST)//删除设备
	@ResponseBody
	public void deleteEquip(EquipList equipList){
		equipService.deleteEquip(equipList);
	}
	
	
	@RequestMapping(value="getequiplistbytypeid.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<EquipList> getEquipListByTypeid(@RequestBody String typeid){
		List<EquipList> gp=equipService.getEquipListByTypeid(typeid);
		return gp;
	}

	@RequestMapping(value="getequiplistbybatchid.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<EquipList> getEquipListByBatchid(@RequestBody String batchid){
		List<EquipList> gp=equipService.getEquipListByBatchid(batchid);
		return gp;
	}

	@RequestMapping(value="installequip.do",method= RequestMethod.POST)
	@ResponseBody
	public void installEquip(EquipList equipList){
		equipService.installEquip(equipList);
	}
////////////////////////////////////////////////////////////////////////////


	@RequestMapping(value="getallmeasures.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<Measure> getAllMeasures(@RequestBody String id){
		return equipService.getAllMeasures(id);
	}


	@RequestMapping(value="getsuperior_meter.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<Superiormeter> getSuperior_meter(@RequestBody String level){
		//System.out.println(level);
		List<Superiormeter> options=equipService.getSuperior_meter(level);
		return options;
	}


	@RequestMapping(value="addmeasure.do",method= RequestMethod.POST)
         @ResponseBody
         public void addMeasure(Measure measure){
        equipService.addMeasure(measure);
    }

    @RequestMapping(value="updatemeasure.do",method= RequestMethod.POST)
    @ResponseBody
    public void updateMeasure(Measure measure){
        equipService.updateMeasure(measure);
    }

	@RequestMapping(value="deletemeasure.do",method= RequestMethod.POST)
	@ResponseBody
	public void deleteMeasure(@RequestBody String id){
		equipService.deleteMeasure(id);
	}

}
