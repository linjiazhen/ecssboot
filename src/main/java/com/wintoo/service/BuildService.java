package com.wintoo.service;

import com.wintoo.dao.BuildDao;
import com.wintoo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuildService {
	@Autowired
	private BuildDao buildDao;
	
	public DataTable getAllBuilds(){
		return buildDao.getAllBuilds();
	}
	
	public void addBuild(Build build){
		build.setId(UUID.randomUUID().toString());
		buildDao.addBuild(build);
		for (int i = 1; i <= build.getDownfloor(); i++) {
			Floor floor=new Floor();
			floor.setId(UUID.randomUUID().toString());
			floor.setBuildid(build.getId());
			floor.setName("负"+i+"层");
			floor.setCode(-1*i);
			buildDao.Floorinit(floor);
		}
		if(build.getZerofloor()==1){
			Floor floor=new Floor();
			floor.setId(UUID.randomUUID().toString());
			floor.setBuildid(build.getId());
			floor.setName("架空层");
			floor.setCode(0);
			buildDao.Floorinit(floor);
		}
		for (int i = 1; i <= build.getUpfloor(); i++) {
			Floor floor=new Floor();
			floor.setId(UUID.randomUUID().toString());
			floor.setBuildid(build.getId());
			floor.setName(i+"层");
			floor.setCode(i);
			buildDao.Floorinit(floor);
		}
	}
	
	public void deleteBuild(String ids){
		String[] id=ids.split(",");
		int length=id.length;
		for (int i=0;i<length;i++){
			buildDao.deleteBuild(id[i]);
		}
	}
	
	public void updateBuild(Build build){
		buildDao.updateBuildById(build);
	}
	
	public Build getBuild(String id){
		return buildDao.getBuildById(id);
	}
	
	public Build getBuildByName(String name){
		return buildDao.getBuildByName(name);
	}
////////////////////////////////////////////	
	public void addFloor(Floor floor){
		buildDao.addFloor(floor);

	}
	
	public void deleteFloor(String id){
		buildDao.deleteFloor(id);
	}
	
	public List<Floor> getFloors(String id){
		return buildDao.getFloorByBuildId(id);
	}
	
	public Floor getFloor(String id){
		return buildDao.getFloorById(id);
	}

    public boolean updateFloor(Floor floor) {
        return buildDao.updateFloorById(floor);
    }
///////////////////////////////////////////////
	public void addRoom(Room room){
		int num=room.getRoomnum();
		String name=room.getName();
		if(num >1){//只有在创建大于一间的时候才需要后面自动叠加或者加1
			for(int i=0;i<num;i++){
				room.setId(UUID.randomUUID().toString());
				if(name.matches("[0-9]+"))
					room.setName(String.valueOf(Integer.parseInt(name)+i));
				else {
					room.setName(name+i);
				}
				buildDao.addRoom(room);
			}
		}
		else {
			room.setId(UUID.randomUUID().toString());
			room.setName(name);
			buildDao.addRoom(room);
		}
		
	}
	
	public void deleteRoom(String ids){
		String[] id=ids.split(",");
		int length=id.length;
		for (int i=0;i<length;i++){
			buildDao.deleteRoom(id[i]);
		}
	}
	
	public void updateRoom(Room room){
		buildDao.updateRoomById(room);
	}
	
	public List<Room> getRooms(String id){
		return buildDao.getRoomByFloorId(id);
	}
	
	public Room getRoom(String id){
		return buildDao.getRoomById(id);
	}
	
/////////////////////////////////////////////////////
	public List<Options> getGroupNames(){
		return buildDao.getGroupNames();
	}
	
	public List<Options> getBuildNames(String id){
		return buildDao.getBuildsByGroupId(id);
	}
	
	public List<Options> getBuildByFunc(String id){
		return buildDao.getBuildsByFunc(id);
	}
	
	public List<Options> getFloorNames(String id){
		return buildDao.getFloorsByBuildId(id);
	}
	
	public List<Options> getRoomNames(String id){
		return buildDao.getRoomsByFloorId(id);
	}

    public List<Options> getRoomNamesByBuild(String id){
        return buildDao.getRoomsByBuildId(id);
    }
	///////////////////////////////////////////////
    public List<Tree> getSchoolTree(){
        return buildDao.getSchoolTree();
    }
	public List<Tree> getBuildTree(){
		return buildDao.getBuildTree();
	}
}
