package com.wintoo.service;

import com.wintoo.dao.MaintenanceDao;
import com.wintoo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
public class MaintenanceService {
    @Autowired
    private MaintenanceDao maintenanceDao;

    public DataTable getAllGateways(){
        return maintenanceDao.getAllGateways();
    }

    public void updateGateway(Gateway gateway){
        String[] uuid=gateway.getUuid().split(",");
        if(uuid.length == 1)
            maintenanceDao.updateGatewayById(gateway);
        else {
            maintenanceDao.updateGatewaysById(gateway);
        }
    }

    public Gateway getGateway(String id){
        return maintenanceDao.getGatewayById(id);
    }

    public List<Options> getGateways(){
        return maintenanceDao.getGateways();
    }

    public void setGatewayUse(String ids){
        maintenanceDao.setGatewayUse(ids);
    }

    public void gatewayControl(String ids) {
        String[] uuids=ids.split(",");
        if(uuids[0].equals("01F1")){//复位指令
            for(int i=1;i<uuids.length;i++){
                maintenanceDao.gatewayReboot(uuids[i]);
            }
        }
        else if(uuids[0].equals("01F4")){//校时指令
            for(int i=1;i<uuids.length;i++)
                maintenanceDao.gatewayClear(uuids[i]);
        }
        else if(uuids[0].equals("05F31")){//校时指令
            for(int i=1;i<uuids.length;i++)
                maintenanceDao.gatewayTiming(uuids[i]);
        }
    }

    String gatewayuuid="#";

    public DataTable getGatewaychild(){
        return maintenanceDao.getGatewaychild(gatewayuuid);
    }

    public void setGatewayUuid(String uuid) {
        gatewayuuid=uuid;
    }

    public void setGatewayChildUse(String ids) {
        maintenanceDao.setGatewayChildUse(ids);
    }
    /////////////////////////////////////////////////////////////
    public void Refresh_para(String uuids){
        maintenanceDao.Refresh_para(uuids);
    }


    public DataTable getAllAmmeters(){
        return maintenanceDao.getAllAmmeters();
    }

    public void updateAmmeter(Ammeter ammeter){
        String[] uuid=ammeter.getUuid().split(",");
        if(uuid.length == 1)
            maintenanceDao.updateAmmeterById(ammeter);
        else {
            maintenanceDao.updateAmmetersById(ammeter);
        }
    }

    public Ammeter getAmmeter(String id){
        return maintenanceDao.getAmmeterById(id);
    }

    public void setAmmeterUse(String ids){
        maintenanceDao.setAmmeterUse(ids);
    }

    public String set_ON_OFF(String ids){
        return maintenanceDao.set_ON_OFF(ids);
    }

    ///////////////////////////////////////////////////////////
    public DataTable getAllWatermeters(){
        return maintenanceDao.getAllWatermeters();
    }

    public void updateWatermeter(Watermeter watermeter){
        String[] uuid=watermeter.getUuid().split(",");
        if(uuid.length == 1)
            maintenanceDao.updateWatermeterById(watermeter);
        else {
            maintenanceDao.updateWatermetersById(watermeter);
        }

    }

    public Watermeter getWatermeter(String id){
        return maintenanceDao.getWatermeterById(id);
    }

    public void setWatermeterUse(String ids){
        maintenanceDao.setWatermeterUse(ids);
    }

    ///////////////////////////////////////////////////////////////
    public  EnergyChart getEnergyState(EnergySearch energySearch){
        return maintenanceDao.getEnergyState(energySearch);
    }

    public  EnergyChart getWaterState(EnergySearch energySearch){
        return maintenanceDao.getEnergyState(energySearch);
    }

    public WatermeterData getWatermeterData(String id){
        return maintenanceDao.getWatermeterData(id);
    }
    public WatermeterData getWaterinfo(String equipid){
        return maintenanceDao.getWaterInfo(equipid);
    }

    public AmmeterData getAmmeterData(String id){
        return maintenanceDao.getAmmeterData(id);
    }

    public  EnergyChart getEnergyParameter(ParameterSearch parameterSearch){
        return maintenanceDao.getEnergyParameter(parameterSearch);
    }

//    public  EnergyChart getWaterParameter(ParameterSearch parameterSearch){
//        return maintenanceDao.getWaterParameter(parameterSearch);
//    }

    public void inputAmData(String data){
        maintenanceDao.inputAmData(data);
    }

    public void inputWaterData(String data){
        maintenanceDao.inputWaterData(data);
    }

    public void setLastActive(String data) throws ParseException {
        maintenanceDao.setLastActive(data);
    }

    public void setLastWaterActive(String data) throws ParseException {
        maintenanceDao.setLastWaterActive(data);
    }
    ////////////////////////////
    public DataTable getPickuploads(){
        return maintenanceDao.getPickuploads();
    }
    public void pickup(String date,String path){
        maintenanceDao.pickup(date,path);
    }
    public void deletePickuploads(String ids){
        String[] id = ids.split(",");
        int length = id.length;
        for (int i = 0; i < length; i++)
            maintenanceDao.deletePickuploads(id[i]);

    }
    public String getFile(String path,String uuid) throws IOException {
        return maintenanceDao.getFile(path,uuid);
    }

}
