package com.wintoo.service;

import com.wintoo.dao.AlertDao;
import com.wintoo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {
	@Autowired
	private AlertDao alertDao;
	
	public List<EquipState> getEquipState(EquipSearch equipSearch){
		return alertDao.getEquipState(equipSearch);
	}
    public List<ErrorValue> getAmErrorValues(String id){return alertDao.getAmErrorValues(id);}
    public List<ErrorValue> getWaterErrorValues(String id){return alertDao.getWaterErrorValues(id);}
	public  void saveAlertConf(AlertConf alertConf){
		alertDao.saveAlertConf(alertConf);
	}
	public  AlertConf getAlertConf(){
		return alertDao.getAlertConf();
	}
	public  void saveAlertEquip(AlertEquip alertEquip){
		alertDao.saveAlertEquip(alertEquip);
	}
	public  AlertEquip getAlertEquip(){
		return alertDao.getAlertEquip();
	}
	public  void addAlertRule(AlertRule alertRule){
		alertDao.addAlertRule(alertRule);
	}
    public  void updateAlertRule(AlertRule alertRule){
        alertDao.updateAlertRule(alertRule);
    }
	public  DataTable getAlertRules(){
		return alertDao.getAlertRules();
	}

	public void setRuleOnoff(String ids){
	    alertDao.setRuleOnoff(ids);
    }
	public void deleteAlertRule(String ids){
		String[] id=ids.split(",");
		int length=id.length;
		for (int i=0;i<length;i++){
			alertDao.deleteAlertRule(id[i]);
		}
	}
    public  DataTable getAllAlerts(){
        return alertDao.getAllAlerts();
    }
    public  DataTable getAllEquipAlerts(){
        return alertDao.getAllEquipAlerts();
    }
    public  DataTable getAllBaoguanAlerts(){
        return alertDao.getAllBaoguanAlerts();
    }

    public void deleteAlert(String ids){
        String[] id=ids.split(",");
        int length=id.length;
        for (int i=0;i<length;i++){
            alertDao.deleteAlert(id[i]);
        }
    }
    public void deleteEquipAlert(String ids){
        String[] id=ids.split(",");
        int length=id.length;
        for (int i=0;i<length;i++){
            alertDao.deleteEquipAlert(id[i]);
        }
    }
    public void deleteBaoguan(String ids){
        String[] id=ids.split(",");
        int length=id.length;
        for (int i=0;i<length;i++){
            alertDao.deleteBaoguan(id[i]);
        }
    }

    public  void setWhiteList(String pras){
        String[] pra=pras.split(",");
        int length=pra.length;
        for (int i=2;i<length;i++){
            alertDao.setWhiteList(pra[0],pra[1],pra[i]);
        }
    }

    public  List<WhiteList> getWhiteList(String type){
        return alertDao.getWhiteList(type);
    }

    public void deleteWhiteList(String id){
        alertDao.deleteWhiteList(id);
    }

    public  List<AmRankList> getAmRankList(String time){
        return alertDao.getAmRankList(time);
    }

    public List<AmSublist> getAmSublist(String time){
        return alertDao.getAmSublist(time);
    }
    public  List<WaterRankList> getWaterRankList(String time){
        return alertDao.getWaterRankList(time);
    }
}
