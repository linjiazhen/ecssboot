package com.wintoo.service;

import com.wintoo.dao.OrganDao;
import com.wintoo.model.Options;
import com.wintoo.model.Organ;
import com.wintoo.model.OrganBuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganService {
	@Autowired
	private OrganDao organDao;
	
	public List<Organ> getAllOrgans(){
		return organDao.getAllOrgans();
	}
	
	public void addOrgan(Organ organ){
		organDao.addOrgan(organ);
	}
	
	public void deleteOrgan(String ids){
		String[] id=ids.split(",");
		int length=id.length;
		for (int i=0;i<length;i++){
			organDao.deleteOrgan(id[i]);
		}
	}
	
	public void updateOrgan(Organ organ){
		organDao.updateOrganById(organ);
	}
	
	public Organ getOrganById(String id){
		return organDao.getOrganById(id);
	}
    public List<Options> getOrgansByType(String type){return  organDao.getOrgansByType(type);}
	public List<Options> getOrganByPid(String pid){
		return organDao.getOrganByPid(pid);
	}
    public Organ getOrganInfo(String name){
        return organDao.getOrganInfo(name);
    }
/////////////////////////////////////////////////	
	public List<Options> getBuildName(String id){
		return organDao.getBuildNameById(id);
	}
	
	public List<Options> getFloorName(String id){
		return organDao.getFloorNameById(id);
	}
	
	public List<Options> getRoomName(String id){
		return organDao.getRoomNameById(id);
	}
	
	public void addOrganBuild(OrganBuild organbuild){
		organDao.addOrganBuild(organbuild);
	}
	
	public void deleteOrganBuild(OrganBuild organbuild){
		organDao.deleteOrganBuild(organbuild);		
	}
	
	public void updateOrganBuild(OrganBuild organbuild){
		organDao.updateOrganBuild(organbuild);
	}
	
	public List<String> getBuildByOrgan(String id){
		return organDao.getBuildByOrgan(id);
	}
}
