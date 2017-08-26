package com.wintoo.service;

import com.wintoo.dao.EnergyDao;
import com.wintoo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnergyService {
	@Autowired
	private EnergyDao energyDao;
	
	public List<Tree> getAllEnergyTypes(){
		return energyDao.getAllEnergyTypes();
	}
	
	public List<Tree> getWaterEnergyTypes(){
		return energyDao.getWaterEnergyTypes();
	}
	
	public List<Tree> getElectricEnergyTypes(){
		return energyDao.getElectricEnergyTypes();
	}

	public EnergyChart getEnergy(EnergySearch energySearch){
		return energyDao.getEnergy(energySearch);
	}
    public EnergyChart getWaterEnergyMap(EnergySearch energySearch){
        return energyDao.getWaterEnergyMap(energySearch);
    }
    public EnergyChart getWaterEnergy(EnergySearch energySearch){
        return energyDao.getWaterEnergy(energySearch);
    }

	public List<EnergyTable> getEnergyTable(EnergySearch energySearch){
		return energyDao.getEnergyTable(energySearch);
	}
	
	public double getDayEnergy(String ids){
		return energyDao.getDayEnergy(ids);
	}
	public double getMonthEnergy(String ids){
		return energyDao.getMonthEnergy(ids);
	}
    public double getOrganMonthEnergy(String ids){
        return energyDao.getOrganMonthEnergy(ids);
    }
    public double getWaterMonthEnergy(String ids){
        return energyDao.getWaterMonthEnergy(ids);
    }
    public DataTable getAllWaternets() {
        return energyDao.getAllWaternets();
    }

    public void addWaternet(Waternet waternet) {
        energyDao.addWaternet(waternet);
    }

    public void deleteWaternet(String ids) {
        String[] id = ids.split(",");
        int length = id.length;
        for (int i = 0; i < length; i++) {
            energyDao.deleteWaternet(id[i]);
        }
    }

    public void updateWaternet(Waternet waternet) {
        energyDao.updateWaternet(waternet);
    }
    public List<Options> getGatewaynames(){
        return energyDao.getGatewaynames();
    }
    public List<Options> getEquipnames(String id){
        return energyDao.getEquipnames(id);
    }
    public List<EquipList> getProduce(String id){
        return energyDao.getProduce(id);
    }

    public void addProduce(String ids) {
        energyDao.addProduce(ids);
    }

    public void deleteProduce(String ids) {
        String[] id = ids.split(",");
        int length = id.length;
        for (int i = 0; i < length; i++) {
            energyDao.deleteProduce(id[i]);
        }
    }
    public List<EquipList> getConsume(String id){
        return energyDao.getConsume(id);
    }

    public void addConsume(String ids) {
        energyDao.addConsume(ids);
    }

    public void deleteConsume(String ids) {
        String[] id = ids.split(",");
        int length = id.length;
        for (int i = 0; i < length; i++) {
            energyDao.deleteConsume(id[i]);
        }
    }

    public LeakChart getLeak(LeakSearch leakSearch){
        return energyDao.getLeak(leakSearch);
    }

    public List<Options> getNetNames(){
        return energyDao.getNetNames();
    }

    public List<LeakRank> getProduceRank(String id){
        return energyDao.getProduceRank(id);
    }

    public List<LeakRank> getConsumeRank(String id){
        return energyDao.getConsumeRank(id);
    }
}
