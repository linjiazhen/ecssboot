package com.wintoo.service;

import com.wintoo.dao.EquipDao;
import com.wintoo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipService {
	@Autowired
	private EquipDao equipDao;

	public List<String> getAllEquipType(){
		return equipDao.getAllEquipType();
	}

	public List<Options> getEquipSubtype(String type){
		return equipDao.getEquipSubtype(type);
	}

	public Options getTypeParameter(String typeid){
		return equipDao.getTypeParameter(typeid);
	}

	public DataTable getAllEquipBatchs(){
		return equipDao.getAllEquipBatchs();
	}

	public void addEquipBatch(EquipBatch equipBatch){
		equipDao.addEquipBatch(equipBatch);
	}

	public void deleteEquipBatch(String ids){
		String[] id=ids.split(",");
		int length=id.length;
		for (int i=0;i<length;i++){
			equipDao.deleteEquipBatch(id[i]);
		}
	}

	public void updateEquipBatch(EquipBatch equipBatch){
		equipDao.updateEquipBatchById(equipBatch);
	}

	public EquipBatch getEquipBatch(String id){
		return equipDao.getEquipBatchById(id);
	}
/////////////////////////////////////////////////////////////
	public DataTable getAllEquipLists(){
		return equipDao.getAllEquipLists();
	}

	public void deleteEquipInstall(String id){
		equipDao.deleteEquipInstall(id);
	}


	public void deleteEquip(EquipList equipList) {
		equipDao.deleteEquip(equipList);

	}

	public List<EquipList> getEquipListByTypeid(String typeid){
		return equipDao.getEquipListByTypeid(typeid);
	}

	public List<EquipList> getEquipListByBatchid(String batchid){
		return equipDao.getEquipListByBatchid(batchid);
	}

	public void installEquip(EquipList equipList){
		equipDao.installEquip(equipList);
	}

	//////////////////////////////////////////////////////////////



	public List<Measure> getAllMeasures(String id){
		return equipDao.getAllMeasures(id);
	}


	public List<Superiormeter> getSuperior_meter(String level){
		return equipDao.getSuperior_meter(level);
	}

	public void addMeasure(Measure measure){
		equipDao.addMeasure(measure);
	}

    public void updateMeasure(Measure measure) {equipDao.updateMeasure(measure);}

	public void deleteMeasure(String ids){
		String[] id=ids.split(",");
		int length=id.length;
		for (int i=0;i<length;i++){
			equipDao.deleteMeasure(id[i]);
		}
	}

}
