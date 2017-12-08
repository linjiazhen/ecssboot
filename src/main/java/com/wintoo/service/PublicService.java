package com.wintoo.service;

import com.wintoo.dao.PublicDao;
import com.wintoo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicService {
	@Autowired
	private PublicDao publicDao;

    public String getTime(){
        return publicDao.getTime();
    }
    public List<String> getEnergyTypes(){
        return publicDao.getEnergyTypes();
    }

    public List<String> getBuilds(){
        return publicDao.getBuilds();
    }
    public List<String> getOrgans(){
        return publicDao.getOrgans();
    }

    public DataTable getPublicBuild(PublicCondition publicCondition){ return publicDao.getPublicBuild(publicCondition); }
    public DataTable getPublicOrgan(PublicCondition publicCondition){ return publicDao.getPublicOrgan(publicCondition); }
    public DataTable getPublicBuildItem(PublicCondition publicCondition){ return publicDao.getPublicBuildItem(publicCondition); }
    public DataTable getPublicOrganItem(PublicCondition publicCondition){ return publicDao.getPublicOrganItem(publicCondition); }
}
