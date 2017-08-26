package com.wintoo.controller;

import com.wintoo.model.Options;
import com.wintoo.model.Organ;
import com.wintoo.model.OrganBuild;
import com.wintoo.service.OrganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller

public class OrganController extends BaseController {
	
	@Autowired
	private OrganService organService;
	
	@RequestMapping(value="getallorgans.do",method= RequestMethod.GET)
	@ResponseBody
	public  List<Organ> getOrgans(){
		return organService.getAllOrgans();
	}

	
	@RequestMapping(value="addorgan.do",method= RequestMethod.POST)
	@ResponseBody
	public void addOrgan(Organ organ){
		organService.addOrgan(organ);
	}
	
	@RequestMapping(value="deleteorgan.do",method= RequestMethod.POST)
	@ResponseBody
	public void deleteOrgan(@RequestBody String ids){
		organService.deleteOrgan(ids);
	}
		
	@RequestMapping(value="updateorgan.do",method= RequestMethod.POST)
	@ResponseBody
	public void updateOrgan(Organ organ){
		organService.updateOrgan(organ);
	}
	
	@RequestMapping(value="getorganbyid.do",method= RequestMethod.POST)
	@ResponseBody
	public  Organ getOrgan(@RequestBody String id){
		Organ gp=organService.getOrganById(id);
		return gp;
	}

    @RequestMapping(value="getorganinfo.do",method= RequestMethod.POST)
    @ResponseBody
    public  Organ getOrganInfo(@RequestBody String name){
        Organ gp=organService.getOrganInfo(name);
        return gp;
    }

	@RequestMapping(value="getorganbypid.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<Options> getOrganByPid(@RequestBody String pid){
		return organService.getOrganByPid(pid);
	}

    @RequestMapping(value="getorgansbytype.do",method= RequestMethod.POST)
    @ResponseBody
    public  List<Options> getOrgansByType(@RequestBody String type){
        return organService.getOrgansByType(type);
    }
//////////////////////////////////////////////////////////////////////////////	
	
	@RequestMapping(value="getbuildname.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<Options> getBuildNames(@RequestBody String id){
		List<Options> options=organService.getBuildName(id);
		return options;
	}
	
	@RequestMapping(value="getfloorname.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<Options> getFloorNames(@RequestBody String id){
		List<Options> options=organService.getFloorName(id);
		return options;
	}
	
	@RequestMapping(value="getroomname.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<Options> getRoomNames(@RequestBody String id){
		List<Options> options=organService.getRoomName(id);
		return options;
	}

	@RequestMapping(value="addorganbuild.do",method= RequestMethod.POST)
	@ResponseBody
	public void addOrganBuild(OrganBuild organbuild){
		organService.addOrganBuild(organbuild);
	}
	
	@RequestMapping(value="deleteorganbuild.do",method= RequestMethod.POST)
	@ResponseBody
	public void deleteOrganBuild(OrganBuild organbuild){
		organService.deleteOrganBuild(organbuild);
	}
		
	@RequestMapping(value="updateorganbuild.do",method= RequestMethod.POST)
	@ResponseBody
	public void updateOrganBuild(OrganBuild organbuild){
		organService.updateOrganBuild(organbuild);
	}
	
	@RequestMapping(value="getbuildbyorgan.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<String> getBuildByOrgan(@RequestBody String id){
		return organService.getBuildByOrgan(id);
	}
	
}
