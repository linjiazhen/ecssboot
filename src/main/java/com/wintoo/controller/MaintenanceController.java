package com.wintoo.controller;

import com.wintoo.model.*;
import com.wintoo.service.MaintenanceService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Controller

public class MaintenanceController extends BaseController {

    @Autowired
    private MaintenanceService maintenanceService;

    @RequestMapping(value="getallgateways.do",method= RequestMethod.POST)
    @ResponseBody
    public  DataTable getGateways(){
        return maintenanceService.getAllGateways();
    }

    @RequestMapping(value="updategateway.do",method= RequestMethod.POST)
    @ResponseBody
    public void updateGateway(Gateway gateway){
        maintenanceService.updateGateway(gateway);
    }

    @RequestMapping(value="getgateway.do",method= RequestMethod.POST)
    @ResponseBody
    public  Gateway getGateway(@RequestBody String id){
        Gateway gp=maintenanceService.getGateway(id);
        return gp;
    }

    @RequestMapping(value="getgatewaynames.do",method= RequestMethod.POST)
    @ResponseBody
    public  List<Options> getGatewayNames(){
        List<Options> options=maintenanceService.getGateways();
        return options;
    }

    @RequestMapping(value="setgatewayuse.do",method= RequestMethod.POST)
    @ResponseBody
    public  void setGatewayUse(@RequestBody String ids){
        maintenanceService.setGatewayUse(ids);
    }

    @RequestMapping(value="gatewaycontrol.do",method= RequestMethod.POST)
    @ResponseBody
    public  void gatewayControl(@RequestBody String ids){
        maintenanceService.gatewayControl(ids);
    }


    @RequestMapping(value="getgatewaychild.do",method= RequestMethod.POST)
    @ResponseBody
    public  DataTable getGatewaychild(){
        return maintenanceService.getGatewaychild();
    }
    @RequestMapping(value="setgatewayuuid.do",method= RequestMethod.POST)
    @ResponseBody
    public  void setGatewayUuid(@RequestBody String gatewayuuid){
        maintenanceService.setGatewayUuid(gatewayuuid);
    }

    @RequestMapping(value="setgatewaychilduse.do",method= RequestMethod.POST)
    @ResponseBody
    public  void setGatewayChildUse(@RequestBody String ids){
        maintenanceService.setGatewayChildUse(ids);
    }
    /////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value="refresh_para.do",method= RequestMethod.POST)
    @ResponseBody
    public void Refresh_para(@RequestBody String uuids){
        maintenanceService.Refresh_para(uuids);
    }
    /////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value="getallammeters.do",method= RequestMethod.POST)
    @ResponseBody
    public  DataTable getAmeters(){
        return maintenanceService.getAllAmmeters();
    }

    @RequestMapping(value="updateammeter.do",method= RequestMethod.POST)
    @ResponseBody
    public void updateAmmeter(Ammeter ammeter){
        maintenanceService.updateAmmeter(ammeter);
    }

    @RequestMapping(value="getammeter.do",method= RequestMethod.POST)
    @ResponseBody
    public  Ammeter getAmmeter(@RequestBody String id){
        Ammeter gp=maintenanceService.getAmmeter(id);
        return gp;
    }

    @RequestMapping(value="setammeteruse.do",method= RequestMethod.POST)
    @ResponseBody
    public  void setAmmeterUse(@RequestBody String ids){
        maintenanceService.setAmmeterUse(ids);
    }

    @RequestMapping(value="set_on_off.do",method= RequestMethod.POST)
    @ResponseBody
    public  String set_ON_OFF(@RequestBody String ids){
        return maintenanceService.set_ON_OFF(ids);
    }

    //////////////////////////////////////////////////////////////////////////
    @RequestMapping(value="getallwatermeters.do",method= RequestMethod.POST)
    @ResponseBody
    public  DataTable getWatermeters(){
        return maintenanceService.getAllWatermeters();
    }

    @RequestMapping(value="updatewatermeter.do",method= RequestMethod.POST)
    @ResponseBody
    public void updateWatermeter(Watermeter watermeter){
        maintenanceService.updateWatermeter(watermeter);
    }

    @RequestMapping(value="getwatermeter.do",method= RequestMethod.POST)
    @ResponseBody
    public  Watermeter getWatermeter(@RequestBody String id){
        Watermeter gp=maintenanceService.getWatermeter(id);
        return gp;
    }

    @RequestMapping(value="setwatermeteruse.do",method= RequestMethod.POST)
    @ResponseBody
    public  void setWatermeterUse(@RequestBody String ids){
        maintenanceService.setWatermeterUse(ids);
    }

    /////////////////////////////////////////////////////////
    @RequestMapping(value="getenergystate.do",method= RequestMethod.POST)
    @ResponseBody
    public  EnergyChart getEnergyState(EnergySearch energySearch){
        return maintenanceService.getEnergyState(energySearch);
    }

    @RequestMapping(value="getwaterstate.do",method= RequestMethod.POST)
    @ResponseBody
    public  EnergyChart getWaterState(EnergySearch energySearch){
        return maintenanceService.getWaterState(energySearch);
    }

    @RequestMapping(value="getammeterdata.do",method= RequestMethod.POST)
    @ResponseBody
    public AmmeterData getAmmeterData(@RequestBody String id){
        return maintenanceService.getAmmeterData(id);
    }

    @RequestMapping(value="getwatermeterdata.do",method= RequestMethod.POST)
    @ResponseBody
    public WatermeterData getWatermeterData(@RequestBody String id){
        return maintenanceService.getWatermeterData(id);
    }

    @RequestMapping(value="getwaterinfo.do",method= RequestMethod.POST)
    @ResponseBody
    public WatermeterData getWaterinfo(@RequestBody String equipid){
        return maintenanceService.getWaterinfo(equipid);
    }

    @RequestMapping(value="getenergyparameter.do",method= RequestMethod.POST)
    @ResponseBody
    public  EnergyChart getEnergyParameter(ParameterSearch parameterSearch){
        return maintenanceService.getEnergyParameter(parameterSearch);
    }

//    @RequestMapping(value="getwaterparameter.do",method=RequestMethod.POST)
//    @ResponseBody
//    public  EnergyChart getWaterParameter(ParameterSearch parameterSearch){
//        return maintenanceService.getWaterParameter(parameterSearch);
//    }

    @RequestMapping(value="inputamdata.do",method= RequestMethod.POST)
    @ResponseBody
    public  void inputAmData(@RequestBody String data){
        maintenanceService.inputAmData(data);
    }

    @RequestMapping(value="inputwaterdata.do",method= RequestMethod.POST)
    @ResponseBody
    public  void inputWaterData(@RequestBody String data){
        maintenanceService.inputWaterData(data);
    }

    @RequestMapping(value="setlastactive.do",method= RequestMethod.POST)
    @ResponseBody
    public  void setLastActive(@RequestBody String data) throws ParseException {
        maintenanceService.setLastActive(data);
    }

    @RequestMapping(value="setlastwateractive.do",method= RequestMethod.POST)
    @ResponseBody
    public  void setLastWaterActive(@RequestBody String data) throws ParseException {
        maintenanceService.setLastWaterActive(data);
    }
    /////////////////////////////////////

    @RequestMapping(value="getallpickuploads.do",method= RequestMethod.POST)
    @ResponseBody
    public  DataTable getPickuploads(){
        return maintenanceService.getPickuploads();
    }

    @RequestMapping(value="pickup.do",method= RequestMethod.POST)
    @ResponseBody
    public void pickup(@RequestBody String date, HttpSession session){
        String path=session.getServletContext().getRealPath("/") + "static/files/";
        maintenanceService.pickup(date,path);
    }

    @RequestMapping(value="deletepickuploads.do",method= RequestMethod.POST)
    @ResponseBody
    public void deletePickuploads(@RequestBody String ids){
        maintenanceService.deletePickuploads(ids);
    }

    @RequestMapping(value = "{uuid}/zipfileDownload.do",method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@PathVariable String uuid, HttpSession session) throws IOException {
        String path=session.getServletContext().getRealPath("/") + "static/files/";
        String filename=maintenanceService.getFile(path,uuid);
        File file=new File(path+filename);
        HttpHeaders headers = new HttpHeaders();
        String fileName=new String(filename.getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
    }
}
