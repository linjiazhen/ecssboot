package com.wintoo.service;

import com.wintoo.dao.AppDao;
import com.wintoo.model.App_EnergyChart;
import com.wintoo.model.App_EnergySearch;
import com.wintoo.model.App_timesplitenergy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AppService {
	@Autowired
	private AppDao appDao;

	public List<App_timesplitenergy> getTimeSplitEnergy() {
		return appDao.getTimeSplitEnergy();
	}

	public List<App_timesplitenergy> getBuildEnergySort(String flag) {
		return appDao.getBuildEnergySort(flag);
	}


	public List<App_timesplitenergy> getBuildEnergy(String flag) throws ParseException {
		return appDao.getBuildEnergy(flag);
	}

	public App_EnergyChart App_GetEnergy(App_EnergySearch app_energySearch) throws ParseException {
		 
		return appDao.getEnergy(app_energySearch);
	}
	
	public List<Double> App_GetTypeEnergy(App_EnergySearch app_energySearch) throws ParseException {
        List<Double> energy=new ArrayList<Double>(Arrays.asList(0.0,0.0,0.0,0.0,0.0,0.0,0.0));
        app_energySearch.setEnergytypeid("01000");
        App_EnergyChart app_energyChart=appDao.getEnergy(app_energySearch);
        for(double data : app_energyChart.getEnergy()){
            energy.set(6,energy.get(6)+data);
        }
        app_energySearch.setEnergytypeid("01A00");
        App_EnergyChart app_energyChart0=appDao.getEnergy(app_energySearch);
        for(double data : app_energyChart0.getEnergy()){
            energy.set(0,energy.get(0)+data);
        }
        app_energySearch.setEnergytypeid("01B00");
        App_EnergyChart app_energyChart1=appDao.getEnergy(app_energySearch);
        for(double data : app_energyChart1.getEnergy()){
            energy.set(1,energy.get(1)+data);
        }
        app_energySearch.setEnergytypeid("01C00");
        App_EnergyChart app_energyChart2=appDao.getEnergy(app_energySearch);
        for(double data : app_energyChart2.getEnergy()){
            energy.set(2,energy.get(2)+data);
        }
        app_energySearch.setEnergytypeid("01D00");
        App_EnergyChart app_energyChart3=appDao.getEnergy(app_energySearch);
        for(double data : app_energyChart3.getEnergy()){
            energy.set(3,energy.get(3)+data);
        }
        energy.set(4,energy.get(0)+energy.get(1)+energy.get(2)+energy.get(3));
        energy.set(5,energy.get(6)-energy.get(4));
        return energy;
    }

    public App_EnergyChart App_GetFuncEnergy(App_EnergySearch app_energySearch){
        return appDao.App_GetFuncEnergy(app_energySearch);
    }
}
