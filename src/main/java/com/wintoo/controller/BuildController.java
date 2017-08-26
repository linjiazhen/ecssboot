package com.wintoo.controller;

import com.wintoo.model.*;
import com.wintoo.service.BuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller

public class BuildController extends BaseController {
	
	@Autowired
	private BuildService buildService;
	
	@RequestMapping(value="getallbuilds.do",method= RequestMethod.POST)
	@ResponseBody
	public  DataTable getBuilds(){
		return buildService.getAllBuilds();
	}

	
	@RequestMapping(value="addbuild.do",method= RequestMethod.POST)
	@ResponseBody
	public void addBuild(Build build){
		buildService.addBuild(build);
	}
	
	@RequestMapping(value="deletebuild.do",method= RequestMethod.POST)
	@ResponseBody
	public void deleteBuild(@RequestBody String ids){
		buildService.deleteBuild(ids);
	}
		
	@RequestMapping(value="updatebuild.do",method= RequestMethod.POST)
	@ResponseBody
	public void updateBuild(Build build){
		buildService.updateBuild(build);
	}
	
	@RequestMapping(value="getbuild.do",method= RequestMethod.POST)
	@ResponseBody
	public  Build getBuild(@RequestBody String id){
		Build gp=buildService.getBuild(id);
		return gp;
	}
	@RequestMapping(value="getbuildbyname.do",method= RequestMethod.POST)
	@ResponseBody
	public  Build getBuildByName(@RequestBody String name){
		Build gp=buildService.getBuildByName(name);
		return gp;
	}
//////////////////////////////////////////////////////////////////////	
	@RequestMapping(value="getfloors.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<Floor> getFloors(@RequestBody String id){
		List<Floor> lf=buildService.getFloors(id);
		return lf;
	}
    @RequestMapping(value="getfloor.do",method= RequestMethod.POST)
    @ResponseBody
    public  Floor getFloor(@RequestBody String id){
        Floor floor=buildService.getFloor(id);
        return floor;
    }
	@RequestMapping(value="addfloor.do",method= RequestMethod.POST)
	@ResponseBody
	public void addFloor(Floor floor){
		buildService.addFloor(floor);
	}
	
	@RequestMapping(value="deletefloor.do",method= RequestMethod.POST)
	@ResponseBody
	public void deleteFloor(@RequestBody String ids){
		buildService.deleteFloor(ids);
	}

    @RequestMapping(value="updatefloor.do",method= RequestMethod.POST)
    @ResponseBody
    public void updateFloor(Floor floor){
        buildService.updateFloor(floor);
    }
//////////////////////////////////////////////////////////////////////
	@RequestMapping(value="addroom.do",method= RequestMethod.POST)
	@ResponseBody
	public void addRoom(Room room){
		buildService.addRoom(room);
	}
	
	@RequestMapping(value="deleterooms.do",method= RequestMethod.POST)
	@ResponseBody
	public void deleteRoom(@RequestBody String ids){
		buildService.deleteRoom(ids);
	}
		
	@RequestMapping(value="updateroom.do",method= RequestMethod.POST)
	@ResponseBody
	public void updateRoom(Room room){
		buildService.updateRoom(room);
	}
	
	@RequestMapping(value="getroom.do",method= RequestMethod.POST)
	@ResponseBody
	public  Room getRoom(@RequestBody String id){
		Room gr=buildService.getRoom(id);
		return gr;
	}
	
	@RequestMapping(value="getrooms.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<Room> getRooms(@RequestBody String id){
		List<Room> gr=buildService.getRooms(id);
		return gr;
	}
	
////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="getgroupnames.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<Options> getGroupNames(){
		List<Options> options=buildService.getGroupNames();
		return options;
	}
	
	@RequestMapping(value="getbuildnames.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<Options> getBuildNames(@RequestBody String id){
		List<Options> options=buildService.getBuildNames(id);
		return options;
	}
	
	@RequestMapping(value="getbuildbyfunc.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<Options> getBuildByFunc(@RequestBody String id){
		List<Options> options=buildService.getBuildByFunc(id);
		return options;
	}
	
	@RequestMapping(value="getfloornames.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<Options> getFloorNames(@RequestBody String id){
		List<Options> options=buildService.getFloorNames(id);
		return options;
	}
	
	@RequestMapping(value="getroomnames.do",method= RequestMethod.POST)
         @ResponseBody
         public  List<Options> getRoomNames(@RequestBody String id){
        List<Options> options=buildService.getRoomNames(id);
        return options;
    }
    @RequestMapping(value="getroomnamesbybuild.do",method= RequestMethod.POST)
    @ResponseBody
    public  List<Options> getRoomNamesByBuild(@RequestBody String id){
        List<Options> options=buildService.getRoomNamesByBuild(id);
        return options;
    }
	//////////////////////////////////////////////////////////
	@RequestMapping(value="getschooltree.do",method={RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public  List<Tree> getSchoolTree(){
		List<Tree> buildtree=buildService.getSchoolTree();
		return buildtree;
	}
    @RequestMapping(value="getbuildtree.do",method={RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public  List<Tree> getBuildTree(){
        List<Tree> buildtree=buildService.getBuildTree();
        return buildtree;
    }
}
