package com.wintoo.dao;

import com.wintoo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class AlertDao {
	@Autowired
    @Qualifier("primaryJdbcTemplate")
	private JdbcOperations jdbcTemplate;

	public List<EquipState> getEquipState(EquipSearch equipSearch){
        final List<EquipState> equipStates = new ArrayList<EquipState>();
        String level=equipSearch.getLevel();
        String cond="";
        if(level.equals("group")){
            if(equipSearch.getModel().equals("all")) cond="";
            else  cond="AND a.F_BUILDGROUPID='"+equipSearch.getModel()+"'";
        }
        else
        if(level.equals("build")) cond="AND a.F_BUILDCODE='"+equipSearch.getModel()+"'";
        else
        if(level.equals("floor")) cond="AND a.F_FLOORID='"+equipSearch.getModel()+"'";
        else
        if(level.equals("room")) cond="AND a.F_ROOMID='"+equipSearch.getModel()+"'";
        String sql = "select j.F_UUID,b.F_ENERGYITEMNAME,j.F_NEWEST_DATA,j.F_NEWEST_VALID_DATA,j.F_EQUIPMENT_STATUE,(sysdate-j.F_NEWEST_OPERATE_TIME) AS STATUS " +
                "from T_RR_DEVICERELATION a,T_DT_ENERGYITEMDICT b, T_be_" + equipSearch.getType() + " j WHERE  a.F_ENERGYITEMCODE=b.F_ENERGYITEMCODE and  a.F_DEVICECODE=j.f_uuid  AND j.F_USE=1 "+cond;
        //System.out.println(sql);
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                EquipState equipState = new EquipState();
                equipState.setEquipid(rs.getString("F_UUID"));
                equipState.setEnergytype(rs.getString("F_ENERGYITEMNAME"));
                if(rs.getDouble("F_NEWEST_DATA") == -1&&rs.getInt("F_EQUIPMENT_STATUE")>3)
                    equipState.setStatus(2);//故障
                else
                if((rs.getDouble("STATUS") >= 0.0208))
                    equipState.setStatus(1);//离线
                else
                if(rs.getDouble("F_NEWEST_VALID_DATA")>rs.getDouble("F_NEWEST_DATA")&&rs.getDouble("F_NEWEST_VALID_DATA")!=-1&&rs.getInt("F_EQUIPMENT_STATUE")==0)
                    equipState.setStatus(3);//底度
                equipStates.add(equipState);
            }
        });
		return equipStates;
	}
    public DataTable getAllEquipAlerts(){
        String sql ="SELECT a.f_uuid,b.F_EQUIPID,d.f_batch,a.F_TYPE,c.f_remarkinfo,a.F_STATUS,a.F_REMARK,a.F_INSERTTIME FROM T_EC_EQUIPALERT_RECORD a,T_BE_EQUIPMENTLIST b,T_BE_EQUIPMENTBATCH d,\n" +
                "(SELECT f_uuid,F_REMARKINFO FROM T_BE_AMMETER WHERE F_REMARKINFO is not null UNION SELECT F_UUID,F_REMARKINFO FROM T_BE_WATERMETER WHERE F_REMARKINFO is not null) c\n" +
                "WHERE a.F_EQUIPUUID=b.F_UUID and a.F_EQUIPUUID=c.f_uuid(+)and b.f_batchid=d.f_uuid";
        final List<AlertEquipList> alertEquipLists=new ArrayList<AlertEquipList>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                AlertEquipList alertEquipList=new AlertEquipList();
                alertEquipList.setUuid(rs.getString(1));
                alertEquipList.setEquipid(rs.getString(2));
                alertEquipList.setSubtype(rs.getString(3));
                alertEquipList.setType(rs.getString(4));
                alertEquipList.setPlace(rs.getString(5));
                alertEquipList.setStatus(rs.getString(6));
                alertEquipList.setRemark(rs.getString(7));
                alertEquipList.setTime(rs.getString(8));
                alertEquipLists.add(alertEquipList);
            }
        });
        DataTable dataTable=new DataTable();
        dataTable.setData(alertEquipLists);
        return dataTable;
    }

    public List<ErrorValue> getAmErrorValues(String id){
        String sql="select F_DATATIME,F_ACTIVE from T_BE_BAD_DATA WHERE F_DEVICECODE=? ORDER BY F_DATATIME DESC ";
        final List<ErrorValue> errorValues=new ArrayList<ErrorValue>();
        jdbcTemplate.query(sql,new Object[]{id}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                ErrorValue errorValue=new ErrorValue();
                errorValue.setValue(rs.getDouble("F_ACTIVE"));
                errorValue.setTime(rs.getString("F_DATATIME"));
                errorValues.add(errorValue);
            }
        });
        return errorValues;
    }
    public List<ErrorValue> getWaterErrorValues(String id){
        String sql="select F_DATATIME,F_ACTIVE from T_BE_BAD_DATA WHERE F_DEVICECODE=? ORDER BY F_DATATIME DESC";
        final List<ErrorValue> errorValues=new ArrayList<ErrorValue>();
        jdbcTemplate.query(sql,new Object[]{id}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                ErrorValue errorValue=new ErrorValue();
                errorValue.setValue(rs.getDouble("F_ACTIVE"));
                errorValue.setTime(rs.getString("F_DATATIME"));
                errorValues.add(errorValue);
            }
        });
        return errorValues;
    }
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void saveAlertConf(AlertConf alertConf){
		String sql="update T_EC_REMINDWAY set f_isalert=?,f_alerttype=?,f_percent=?";
		Object[] args={alertConf.getIsalert(),alertConf.getAlerttype(),alertConf.getPercent()};
        jdbcTemplate.update(sql, args);
	}
	public AlertConf getAlertConf(){
        String sql="select f_isalert,f_percent,f_alerttype from T_EC_REMINDWAY";
        final AlertConf alertConf=new AlertConf();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                alertConf.setIsalert(rs.getInt(1));
                alertConf.setPercent(rs.getInt(2));
                alertConf.setAlerttype(rs.getInt(3));
            }
        });
		return alertConf;
	}
	public void saveAlertEquip(AlertEquip alertEquip){
        String sql="update T_EC_EQUIP_RULE set f_amcivicism=?,f_amschool=?,f_amregion=?,f_ambuild=?,f_amfloor=?,f_amroom=?,f_wmcivicism=?,f_wmschool=?,f_wmregion=?,f_wmbuild=?,f_wmfloor=?,f_wmroom=?";
        Object[] args={alertEquip.getAmcivicism(),alertEquip.getAmschool(),alertEquip.getAmgroup(),alertEquip.getAmbuild(),alertEquip.getAmfloor(),alertEquip.getAmroom(),alertEquip.getWmcivicism(),alertEquip.getWmschool(),alertEquip.getWmbuild(),alertEquip.getWmgroup(),alertEquip.getWmfloor(),alertEquip.getWmroom()};
        jdbcTemplate.update(sql, args);
    }
	public AlertEquip getAlertEquip(){
        String sql="select * from T_EC_EQUIP_RULE";
		final AlertEquip alertEquip=new AlertEquip();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                alertEquip.setAmcivicism(rs.getDouble(1));
                alertEquip.setAmschool(rs.getDouble(2));
                alertEquip.setAmgroup(rs.getDouble(3));
                alertEquip.setAmbuild(rs.getDouble(4));
                alertEquip.setAmfloor(rs.getDouble(5));
                alertEquip.setAmroom(rs.getDouble(6));
                alertEquip.setWmcivicism(rs.getDouble(7));
                alertEquip.setWmschool(rs.getDouble(8));
                alertEquip.setWmgroup(rs.getDouble(9));
                alertEquip.setWmbuild(rs.getDouble(10));
                alertEquip.setWmfloor(rs.getDouble(11));
                alertEquip.setWmroom(rs.getDouble(12));
            }
        });
		return alertEquip;
	}
	public void addAlertRule(AlertRule alertRule){
        String sql="insert into T_EC_ENERGY_RULE (F_UUID,F_ITEM1,F_ITEM2,F_ITEM3,F_LEVEL,F_ENERGYITEM,F_TIMEUNIT,F_VALUE) VALUES(sys_guid(),?,?,?,?,?,?,?)";
        Object[] args={alertRule.getFirst(),alertRule.getSecond(),alertRule.getThird(),alertRule.getOrganlevel(),alertRule.getEnergyitem(),alertRule.getTimeunit(),alertRule.getValue()};
        jdbcTemplate.update(sql,args);

    }

    public void updateAlertRule(AlertRule alertRule){
        String sql="update T_EC_ENERGY_RULE set F_ITEM1=?,F_ITEM2=?,F_ITEM3=?,F_LEVEL=?,F_ENERGYITEM=?,F_TIMEUNIT=?,F_VALUE=?,F_TIME=SYSDATE where F_UUID=?";
        Object[] args={alertRule.getFirst(),alertRule.getSecond(),alertRule.getThird(),alertRule.getOrganlevel(),alertRule.getEnergyitem(),alertRule.getTimeunit(),alertRule.getValue(),alertRule.getUuid()};
        jdbcTemplate.update(sql,args);
    }
	public DataTable getAlertRules(){

        String sql = "select a.F_UUID,a.F_LEVEL,a.F_TIMEUNIT,a.F_VALUE,a.F_STATE,b.F_ENERGYITEMNAME,NVL(c.F_NAME,'') ,NVL(d.F_NAME,'') ,NVL(e.F_NAME,''),to_char(a.f_time,'YYYY/MM/DD HH24:MI')" +
                ",a.f_item1,a.f_item2,a.f_item3,a.f_energyitem from T_EC_ENERGY_RULE a,T_DT_ENERGYITEMDICT b,T_BO_ORGAN c,T_BO_ORGAN d,T_BO_ORGAN e "+
                "WHERE  a.F_ENERGYITEM=b.F_ENERGYITEMCODE AND a.F_ITEM1=c.f_id(+) and a.F_ITEM2=d.F_ID(+) and a.F_ITEM3=e.F_ID(+) ";
        final List<AlertRule> alertRules=new ArrayList<AlertRule>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                AlertRule alertRule=new AlertRule();
                alertRule.setUuid(rs.getString(1));
                alertRule.setOrganlevel(rs.getString(2));
                alertRule.setTimeunit(rs.getString(3));
                alertRule.setValue(rs.getDouble(4));
                alertRule.setState(rs.getInt(5));
                alertRule.setEnergyitem(rs.getString(6));
                alertRule.setFirst(rs.getString(7));
                alertRule.setSecond(rs.getString(8));
                alertRule.setThird(rs.getString(9));
                alertRule.setTime(rs.getString(10));
                alertRule.setFirstid(rs.getString(11));
                alertRule.setSecondid(rs.getString(12));
                alertRule.setThirdid(rs.getString(13));
                alertRule.setEnergyitemcode(rs.getString(14));
                alertRules.add(alertRule);
            }
        });
        //System.out.println(alertRules.get(1).getObjecttype());
		DataTable dataTable=new DataTable();
        dataTable.setData(alertRules);
		return dataTable;
	}
	public void deleteAlertRule(String id){
        String sql="delete from T_EC_ENERGY_RULE where f_uuid=?";
        Object[] args={id};
        jdbcTemplate.update(sql,args);
	}
    public void setRuleOnoff(String ids){
        String[] value=ids.split("@");
        String[] id=value[0].split(":");
        String sql="UPDATE T_EC_ENERGY_RULE SET F_STATE=? WHERE F_UUID=?";
        for(String uuid :id){
            Object[] args={value[1],uuid};
            jdbcTemplate.update(sql,args);
        }
    }
    public DataTable getAllAlerts(){
        String sql = "select a.F_UUID,a.F_TIMEUNIT,a.F_DATETIME,a.F_VALUE,a.F_USAGE,a.F_STATUS,b.F_ENERGYITEMNAME,NVL(c.F_NAME,'') ,NVL(d.F_NAME,'') ,NVL(e.F_NAME,''),to_char(a.f_alerttime,'YYYY/MM/DD HH24:MI:SS') " +
                "from T_EC_VIOLATION_RECORD a,T_DT_ENERGYITEMDICT b,T_BO_ORGAN c,T_BO_ORGAN d,T_BO_ORGAN e "+
                "WHERE  a.F_ENERGYITEM=b.F_ENERGYITEMCODE AND a.F_ITEM1=c.f_id(+) and a.F_ITEM2=d.F_ID(+) and a.F_ITEM3=e.F_ID(+) ";
        final List<Alert> alerts=new ArrayList<Alert>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Alert alert=new Alert();
                alert.setUuid(rs.getString(1));
                alert.setTimeunit(rs.getString(2));
                alert.setDatetime(rs.getString(3));
                alert.setValue(rs.getDouble(4));
                alert.setUsage(rs.getDouble(5));
                alert.setStatus(rs.getInt(6));
                alert.setEnergyitem(rs.getString(7));
                alert.setFirst(rs.getString(8));
                alert.setSecond(rs.getString(9));
                alert.setThird(rs.getString(10));
                alert.setAlerttime(rs.getString(11));
                alerts.add(alert);
            }
        });
        DataTable dataTable=new DataTable();
        dataTable.setData(alerts);
        return dataTable;
    }


    public DataTable getAllBaoguanAlerts(){
        String sql = "SELECT F_VIOLATION_ID,F_NAME,F_USAGE,F_GQUSAGE,F_LATITUDE,F_LONGITUDE,to_char(F_TIME,'yyyy-mm-dd hh24:mi'),f_uuid from T_EC_BAOGUAN_RECORD ";
        final List<Baoguan> baoguans=new ArrayList<Baoguan>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Baoguan baoguan =new Baoguan();
                baoguan.setEquipid(rs.getString(1));
                baoguan.setInstall(rs.getString(2));
                baoguan.setUsage(rs.getDouble(3));
                baoguan.setGqusage(rs.getDouble(4));
                baoguan.setLatitude(rs.getDouble(5));
                baoguan.setLongitude(rs.getDouble(6));
                baoguan.setTime(rs.getString(7));
                baoguan.setUuid(rs.getString(8));
                baoguans.add(baoguan);
            }
        });
        DataTable dataTable=new DataTable();
        dataTable.setData(baoguans);
        return dataTable;
    }

    public void deleteAlert(String id){
        String sql="delete from T_EC_VIOLATION_RECORD where f_uuid=?";
        Object[] args={id};
        jdbcTemplate.update(sql,args);
    }
    public void deleteEquipAlert(String id){
        String sql="delete from T_EC_EQUIPALERT_RECORD where f_uuid=?";
        Object[] args={id};
        jdbcTemplate.update(sql,args);
    }
    public void deleteBaoguan(String id){
        String sql="delete from T_EC_BAOGUAN_RECORD where f_uuid=?";
        Object[] args={id};
        jdbcTemplate.update(sql,args);
    }

    public  void setWhiteList(String type,String level,String id){
        //System.out.println(type+"@"+level+"@"+id);
        String sql="INSERT INTO T_EC_WHITELIST(F_ID,F_LEVEL,F_TYPE) VALUES(?,?,?)";
        Object[] args={id,Integer.parseInt(level),Integer.parseInt(type)};
        jdbcTemplate.update(sql,args);
    }

    public  List<WhiteList> getWhiteList(String type){
        final List<WhiteList> whiteLists=new ArrayList<WhiteList>();
        String[] types=type.split(",");
        String sql=null;
        if(types[0].equals("1")){
            if(types[1].equals("0"))
            sql = "SELECT a.F_BUILDGROUPNAME||'-'||b.F_BUILDNAME||'-'||d.F_NAME||'-'||e.F_NAME as buildname,f.F_ID from T_BD_GROUP a,T_BD_BUILD b,T_BD_GROUPBUILDRELA c,T_BD_FLOOR d,T_BD_ROOM e,T_EC_WHITELIST f " +
                    "where a.F_BUILDGROUPID=c.F_BUILDGROUPID and b.F_BUILDID=c.F_BUILDID and b.F_BUILDID=d.F_BUILDID and d.F_ID=e.F_FLOORID and e.F_ID=f.f_id and f_level=0";
            else
            if(types[1].equals("1"))
                sql = "SELECT a.F_BUILDGROUPNAME||'-'||b.F_BUILDNAME||'-'||d.F_NAME as buildname,f.F_ID from T_BD_GROUP a,T_BD_BUILD b,T_BD_GROUPBUILDRELA c,T_BD_FLOOR d,T_EC_WHITELIST f " +
                        "where a.F_BUILDGROUPID=c.F_BUILDGROUPID and b.F_BUILDID=c.F_BUILDID and b.F_BUILDID=d.F_BUILDID and d.F_ID=f.f_id and f_level=1";
            else
                sql = "SELECT a.F_BUILDGROUPNAME||'-'||b.F_BUILDNAME as buildname,f.F_ID from T_BD_GROUP a,T_BD_BUILD b,T_BD_GROUPBUILDRELA c,T_EC_WHITELIST f " +
                        "where a.F_BUILDGROUPID=c.F_BUILDGROUPID and b.F_BUILDID=c.F_BUILDID and b.F_BUILDID=f.f_id and f_level=2";
        }
        else {
            sql = "SELECT a.F_REMARKINFO,a.F_UUID from T_BE_WATERMETER a,T_EC_WHITELIST b where a.F_UUID=b.F_ID";
        }
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                WhiteList whiteList = new WhiteList();
                whiteList.setBuildname(rs.getString(1));
                whiteList.setId(rs.getString(2));
                whiteLists.add(whiteList);
            }
        });
        return whiteLists;
    }

    public void deleteWhiteList(String id){
        String sql="delete from T_EC_WHITELIST where f_id=?";
        Object[] args={id};
        jdbcTemplate.update(sql,args);
    }

    public  List<AmRankList> getAmRankList(String time){
        String[] times=time.split(",");
        String sql =null;
                //System.out.println(times[0]+":"+times[1]);
        if(times[2].equals("0"))
            sql = "SELECT a.F_BUILDGROUPNAME||'-'||b.F_BUILDNAME||'-'||d.F_NAME||'-'||e.F_NAME as buildname,f.* from T_BD_GROUP a,T_BD_BUILD b,T_BD_GROUPBUILDRELA c,T_BD_FLOOR d,T_BD_ROOM e," +
                "(select F_BUILDID,F_ENERGYITEMCODE,F_BUILDLEVEL,sum(F_VALUE) from T_EC_BUILD_HOUR where F_BUILDLEVEL=0 and F_STARTTIME>=to_date(?,'yyyy/mm/dd hh24:mi') " +
                "and F_STARTTIME<to_date(?,'yyyy/mm/dd hh24:mi')  and F_ENERGYITEMCODE like '%00' group by F_BUILDID,F_ENERGYITEMCODE,F_BUILDLEVEL order by F_BUILDID) f " +
                "where a.F_BUILDGROUPID=c.F_BUILDGROUPID and b.F_BUILDID=c.F_BUILDID and b.F_BUILDID=d.F_BUILDID and d.F_ID=e.F_FLOORID and e.F_ID=f.f_buildid and f.f_buildid not in (SELECT f_id from T_EC_WHITELIST)";
        else if(times[2].equals("1"))
            sql = "SELECT a.F_BUILDGROUPNAME||'-'||b.F_BUILDNAME||'-'||d.F_NAME as buildname,f.* from T_BD_GROUP a,T_BD_BUILD b,T_BD_GROUPBUILDRELA c,T_BD_FLOOR d," +
                    "(select F_BUILDID,F_ENERGYITEMCODE,F_BUILDLEVEL,sum(F_VALUE) from T_EC_BUILD_HOUR where F_BUILDLEVEL=1 and F_STARTTIME>=to_date(?,'yyyy/mm/dd hh24:mi') " +
                    "and F_STARTTIME<to_date(?,'yyyy/mm/dd hh24:mi')  and F_ENERGYITEMCODE like '%00' group by F_BUILDID,F_ENERGYITEMCODE,F_BUILDLEVEL order by F_BUILDID) f " +
                    "where a.F_BUILDGROUPID=c.F_BUILDGROUPID and b.F_BUILDID=c.F_BUILDID and b.F_BUILDID=d.F_BUILDID and d.F_ID=f.f_buildid and f.f_buildid not in (SELECT f_id from T_EC_WHITELIST)";
        else if(times[2].equals("2"))
            sql = "SELECT a.F_BUILDGROUPNAME||'-'||b.F_BUILDNAME as buildname,f.* from T_BD_GROUP a,T_BD_BUILD b,T_BD_GROUPBUILDRELA c," +
                    "(select F_BUILDID,F_ENERGYITEMCODE,F_BUILDLEVEL,sum(F_VALUE) from T_EC_BUILD_HOUR where F_BUILDLEVEL=2 and F_STARTTIME>=to_date(?,'yyyy/mm/dd hh24:mi') " +
                    "and F_STARTTIME<to_date(?,'yyyy/mm/dd hh24:mi')  and F_ENERGYITEMCODE like '%00' group by F_BUILDID,F_ENERGYITEMCODE,F_BUILDLEVEL order by F_BUILDID) f " +
                    "where a.F_BUILDGROUPID=c.F_BUILDGROUPID and b.F_BUILDID=c.F_BUILDID and b.F_BUILDID=f.f_buildid and f.f_buildid not in (SELECT f_id from T_EC_WHITELIST)";
        Object[] args={times[0],times[1]};
        final List<AmRankList> amRankLists=new ArrayList<AmRankList>();
        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                if(amRankLists.size()==0||!rs.getString(2).equals(amRankLists.get(amRankLists.size()-1).getBuildid())){
                    AmRankList amRankList=new AmRankList();
                    amRankList.setBuildname(rs.getString(1));
                    amRankList.setBuildid(rs.getString(2));
                    amRankList.setBuildlevel(rs.getInt(4));
                    amRankLists.add(amRankList);
                }

                AmRankList amRankList=amRankLists.get(amRankLists.size()-1);
                if(rs.getString(3).equals("01000"))
                    amRankList.setEnergy(rs.getDouble(5));
                else
                if(rs.getString(3).equals("01A00"))
                    amRankList.setAenergy(rs.getDouble(5));
                else
                if(rs.getString(3).equals("01B00"))
                    amRankList.setBenergy(rs.getDouble(5));
                else
                if(rs.getString(3).equals("01C00"))
                    amRankList.setCenergy(rs.getDouble(5));
                else
                if(rs.getString(3).equals("01D00"))
                    amRankList.setDenergy(rs.getDouble(5));
                else
                    amRankList.setZenergy(rs.getDouble(5));
                amRankLists.set(amRankLists.size()-1,amRankList);
                //System.out.println(rs.getString(3)+amRankList.getEnergy()+'-'+amRankList.getAenergy()+'-'+amRankList.getBenergy()+'-'+amRankList.getCenergy()+'-'+amRankList.getDenergy());

            }
        });
        return amRankLists;
    }
    public List<AmSublist> getAmSublist(String time){
        String[] times=time.split(",");
        String sql=null;
        if(times[0].equals("0"))
            sql = "select a.F_NEWEST_DATA,a.F_NEWEST_VALID_DATA,(sysdate-a.F_NEWEST_OPERATE_TIME) AS TIME_INTERVAL,(sysdate-a.F_NEWEST_VALID_DATA_TIME) AS TIME_INTERVAL_CUT,a.f_cut,a.F_EQUIPMENT_STATUE as STATUE,x.F_ADDRESS AS GATEWAY,b.F_EQUIPID AS F_EQUIP,c.F_MODEL,d.F_SUBTYPE,b.F_REMARK,"+
                "a.f_remarkinfo,a.F_USE,a.F_PN,a.F_UUID,e.F_ENERGYITEMNAME,e.energy from T_BE_AMMETER a,T_BE_EQUIPMENTLIST b,T_BE_EQUIPMENTBATCH c,T_BE_EQUIPMENTTYPE d,T_BE_GATEWAY x, (select a.F_DEVICECODE,b.F_ENERGYITEMNAME,sum(a.F_TIME_INTERVEL_ACTIVE) as energy from T_BE_15_ENERGY_BUFFER a,"+
                "(select a.F_DEVICECODE,b.F_ENERGYITEMNAME from t_RR_devicerelation a,t_dt_energyitemdict b where a.F_ENERGYITEMCODE=b.F_ENERGYITEMCODE and a.F_ROOMID=?) b where a.F_TYPE=1 and a.F_DEVICECODE=b.F_DEVICECODE and F_DATATIME>=to_date(?,'yyyy/mm/dd hh24:mi') and F_DATATIME<to_date(?,'yyyy/mm/dd hh24:mi') "+
                "group by a.F_DEVICECODE,b.F_ENERGYITEMNAME) e where a.F_GATEWAYS_UUID=x.F_UUID(+) AND a.F_UUID=b.F_UUID AND a.F_BATCHID=c.F_UUID AND b.F_TYPEID=d.F_UUID  AND a.F_UUID=e.f_devicecode " ;
        else
        if(times[0].equals("1"))
            sql = "select a.F_NEWEST_DATA,a.F_NEWEST_VALID_DATA,(sysdate-a.F_NEWEST_OPERATE_TIME) AS TIME_INTERVAL,(sysdate-a.F_NEWEST_VALID_DATA_TIME) AS TIME_INTERVAL_CUT,a.f_cut,a.F_EQUIPMENT_STATUE as STATUE,x.F_ADDRESS AS GATEWAY,b.F_EQUIPID AS F_EQUIP,c.F_MODEL,d.F_SUBTYPE,b.F_REMARK,"+
                    "a.f_remarkinfo,a.F_USE,a.F_PN,a.F_UUID,e.F_ENERGYITEMNAME,e.energy from T_BE_AMMETER a,T_BE_EQUIPMENTLIST b,T_BE_EQUIPMENTBATCH c,T_BE_EQUIPMENTTYPE d,T_BE_GATEWAY x, (select a.F_DEVICECODE,b.F_ENERGYITEMNAME,sum(a.F_TIME_INTERVEL_ACTIVE) as energy from T_BE_15_ENERGY_BUFFER a,"+
                    "(select a.F_DEVICECODE,b.F_ENERGYITEMNAME from t_RR_devicerelation a,t_dt_energyitemdict b where a.F_ENERGYITEMCODE=b.F_ENERGYITEMCODE and a.F_FLOORID=?) b where a.F_TYPE=1 and a.F_DEVICECODE=b.F_DEVICECODE and F_DATATIME>=to_date(?,'yyyy/mm/dd hh24:mi') and F_DATATIME<to_date(?,'yyyy/mm/dd hh24:mi') "+
                    "group by a.F_DEVICECODE,b.F_ENERGYITEMNAME) e where a.F_GATEWAYS_UUID=x.F_UUID(+) AND a.F_UUID=b.F_UUID AND a.F_BATCHID=c.F_UUID AND b.F_TYPEID=d.F_UUID  AND a.F_UUID=e.f_devicecode " ;
        else
        if(times[0].equals("2"))
            sql = "select a.F_NEWEST_DATA,a.F_NEWEST_VALID_DATA,(sysdate-a.F_NEWEST_OPERATE_TIME) AS TIME_INTERVAL,(sysdate-a.F_NEWEST_VALID_DATA_TIME) AS TIME_INTERVAL_CUT,a.f_cut,a.F_EQUIPMENT_STATUE as STATUE,x.F_ADDRESS AS GATEWAY,b.F_EQUIPID AS F_EQUIP,c.F_MODEL,d.F_SUBTYPE,b.F_REMARK,"+
                    "a.f_remarkinfo,a.F_USE,a.F_PN,a.F_UUID,e.F_ENERGYITEMNAME,e.energy from T_BE_AMMETER a,T_BE_EQUIPMENTLIST b,T_BE_EQUIPMENTBATCH c,T_BE_EQUIPMENTTYPE d,T_BE_GATEWAY x, (select a.F_DEVICECODE,b.F_ENERGYITEMNAME,sum(a.F_TIME_INTERVEL_ACTIVE) as energy from T_BE_15_ENERGY_BUFFER a,"+
                    "(select a.F_DEVICECODE,b.F_ENERGYITEMNAME from t_RR_devicerelation a,t_dt_energyitemdict b where a.F_ENERGYITEMCODE=b.F_ENERGYITEMCODE and a.F_BUILDCODE=?) b where a.F_TYPE=1 and a.F_DEVICECODE=b.F_DEVICECODE and F_DATATIME>=to_date(?,'yyyy/mm/dd hh24:mi') and F_DATATIME<to_date(?,'yyyy/mm/dd hh24:mi') "+
                    "group by a.F_DEVICECODE,b.F_ENERGYITEMNAME) e where a.F_GATEWAYS_UUID=x.F_UUID(+) AND a.F_UUID=b.F_UUID AND a.F_BATCHID=c.F_UUID AND b.F_TYPEID=d.F_UUID  AND a.F_UUID=e.f_devicecode" ;
        Object[] args={times[1],times[2],times[3]};
        final List<AmSublist> amSublists=new ArrayList<AmSublist>();
        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                AmSublist amSublist=new AmSublist();
                if((rs.getInt("F_USE") == 0)||(rs.getInt("F_CUT")==1&&(rs.getDouble("TIME_INTERVAL_CUT") < 3)))
                    amSublist.setStatus(4);//关闭
                else
                if(rs.getDouble("F_NEWEST_DATA") == -1&&rs.getInt("STATUE")>3)
                    amSublist.setStatus(2);//故障
                else
                if((rs.getDouble("TIME_INTERVAL") >= 0.0208))
                    amSublist.setStatus(1);//离线
                else
                if(rs.getDouble("F_NEWEST_VALID_DATA")>rs.getDouble("F_NEWEST_DATA")&&rs.getDouble("F_NEWEST_VALID_DATA")!=-1&&rs.getInt("STATUE")==0)
                    amSublist.setStatus(3);//底度
                amSublist.setEnergy(rs.getDouble("energy"));
                amSublist.setEnergytype(rs.getString("F_ENERGYITEMNAME"));
                amSublist.setEquip(rs.getString("F_EQUIP"));
                amSublist.setEquipid(rs.getString("F_UUID"));
                amSublist.setGateway(rs.getString("GATEWAY"));
                amSublist.setModel(rs.getString("F_MODEL"));
                amSublist.setPn(rs.getString("F_PN"));
                amSublist.setRemark(rs.getString("F_REMARK"));
                amSublist.setRemarkinfo(rs.getString("F_REMARKINFO"));
                amSublist.setUse(rs.getInt("F_USE"));
                amSublist.setType(rs.getString("F_SUBTYPE"));
                amSublist.setActive(rs.getDouble("F_NEWEST_DATA"));
                amSublists.add(amSublist);
            }
        });
        return amSublists;
    }
    public  List<WaterRankList> getWaterRankList(String time){
        String[] times=time.split(",");
        String sql = "select a.F_NEWEST_DATA,a.F_NEWEST_VALID_DATA,(sysdate-a.F_NEWEST_OPERATE_TIME) AS TIME_INTERVAL,a.F_EQUIPMENT_STATUE as STATUE,x.F_ADDRESS AS GATEWAY,b.F_EQUIPID AS F_EQUIP,c.F_MODEL,d.F_SUBTYPE,b.F_REMARK " +
                ",a.f_remarkinfo,a.F_USE,a.F_PN,a.F_UUID,e.energy from T_BE_WATERMETER a,T_BE_EQUIPMENTLIST b,T_BE_EQUIPMENTBATCH c,T_BE_EQUIPMENTTYPE d,T_BE_GATEWAY x, " +
                "(select a.F_DEVICECODE, sum(a.F_TIME_INTERVEL_ACTIVE) as energy from T_BE_15_ENERGY_BUFFER a,T_BE_WATERMETER b where a.F_TYPE=1 and a.F_DEVICECODE=b.F_UUID and F_DATATIME>=to_date(?,'yyyy/mm/dd hh24:mi') and F_DATATIME<to_date(?,'yyyy/mm/dd hh24:mi') " +
                "group by a.F_DEVICECODE) e where a.F_GATEWAYS_UUID=x.F_UUID(+) AND a.F_UUID=b.F_UUID AND a.F_BATCHID=c.F_UUID AND b.F_TYPEID=d.F_UUID  AND a.F_UUID=e.f_devicecode and e.f_devicecode not in (SELECT f_id from T_EC_WHITELIST)" ;
        Object[] args={times[0],times[1]};
        final List<WaterRankList> waterRankLists=new ArrayList<WaterRankList>();
        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                WaterRankList waterRankList=new WaterRankList();
                if((rs.getInt("F_USE") == 0))
                    return;//关闭
                else
                if(rs.getDouble("F_NEWEST_DATA") == -1&&rs.getInt("STATUE")>3)
                    waterRankList.setStatus(2);//故障
                else
                if((rs.getDouble("F_NEWEST_DATA") == -2)||(rs.getDouble("TIME_INTERVAL") >= 0.0208))
                    waterRankList.setStatus(1);//离线
                waterRankList.setEnergy(rs.getDouble("energy"));
                waterRankList.setEquip(rs.getString("F_EQUIP"));
                waterRankList.setEquipid(rs.getString("F_UUID"));
                waterRankList.setGateway(rs.getString("GATEWAY"));
                waterRankList.setModel(rs.getString("F_MODEL"));
                waterRankList.setPn(rs.getString("F_PN"));
                waterRankList.setRemark(rs.getString("F_REMARK"));
                waterRankList.setRemarkinfo(rs.getString("F_REMARKINFO"));
                waterRankList.setUse(rs.getInt("F_USE"));
                waterRankList.setType(rs.getString("F_SUBTYPE"));
                waterRankList.setActive(rs.getDouble("F_NEWEST_DATA"));
                waterRankLists.add(waterRankList);
            }
        });
        return waterRankLists;
    }
}
