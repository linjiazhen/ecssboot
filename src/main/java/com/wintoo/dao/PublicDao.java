package com.wintoo.dao;

import com.wintoo.model.DataTable;
import com.wintoo.model.PublicBulidsInfo;
import com.wintoo.model.PublicInfo;
import com.wintoo.model.PublicOrganInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Repository
@Transactional(value = "primaryTransactionManager")
public class PublicDao {
	@Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcOperations jdbcTemplate;


	public void setPublic(PublicInfo publicInfo){

    }
    public void setPublicOrgan(PublicOrganInfo publicOrganInfo){

    }

    public void setPublicBulids(PublicBulidsInfo publicBulidsInfo){

    }

	public String getTime(){
		return "";
	}
	public List<String> getEnergyTypes(){
        List<String> energys=new ArrayList<String>();
		return energys;
	}

    public List<String> getBuilds(){
        List<String> builds=new ArrayList<String>();
        return builds;
    }
    public List<String> getOrgans(){
        List<String> organs=new ArrayList<String>();
        return organs;
    }

    public DataTable getPublic(){
        String sql="SELECT T3.buildcode, T3.buildName, T3.area, VALUE_D, VALUE_S, T3.FUNC, T3.peopleNum, t6.F_BUILDGROUPNAME FROM (select T1.F_BUILDID as ID, T1.F_BUILDcode AS buildcode, T1.F_TOTALAREA as area, T1.F_BUILDNAME as buildName, SUM(T2.F_VALUE) AS VALUE_D, T1.F_BUILDFUNC as FUNC, T1.F_PEOPLE as peopleNum from T_BD_BUILDBASEINFO T1, T_EC_BUILD_YEAR T2 where T2.F_BUILDID = T1.F_BUILDID AND substr(T2.F_ENERGYITEMCODE,1,2) = '01' AND T1.F_BUILDFUNC!='M' GROUP BY T1.F_BUILDID, T1.F_BUILDcode, T1.F_TOTALAREA, T1.F_BUILDNAME, T1.F_BUILDFUNC, T1.F_PEOPLE) T3, "
                +"    (select T1.F_BUILDcode AS buildcode, T1.F_TOTALAREA as area, T1.F_BUILDNAME as buildName, SUM(T2.F_VALUE) AS VALUE_S from T_BD_BUILDBASEINFO T1, T_EC_BUILD_YEAR T2 where T2.F_BUILDID = T1.F_BUILDID AND substr(T2.F_ENERGYITEMCODE,1,2) = '02'  GROUP BY T1.F_BUILDcode, T1.F_TOTALAREA, T1.F_BUILDNAME) T4, T_BD_BUILDGROUPRELAINFO t5, T_BD_BUILDGROUPBASEINFO t6 where t3.buildcode=t4.buildcode(+) and t3.ID = t5.F_BUILDID and t5.F_BUILDGROUPID = t6.F_BUILDGROUPID order by t3.buildcode";
        final List<PublicInfo> list=new LinkedList<PublicInfo>();
        String anothersql="SELECT T1.engery, T2.water FROM "
                +"(select sum(F_VALUE) AS engery from T_EC_BUILD_year where F_BUILDID = 'allofsumgroup' AND substr(F_ENERGYITEMCODE,1,2) = '01') T1, "
                +"(select sum(F_VALUE) AS water from T_EC_BUILD_year where F_BUILDID = 'allofsumgroup' AND substr(F_ENERGYITEMCODE,1,2) = '02') T2";
        jdbcTemplate.query(anothersql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                PublicInfo publicInfo=new PublicInfo();
                publicInfo.setBuildcode("000000");
                publicInfo.setBuildname("福建工程学院-全校");
                publicInfo.setTotalenergy(rs.getDouble(1));
                publicInfo.setTotalwater(rs.getDouble(2));
                publicInfo.setAreaenergy(rs.getDouble(1)/419500);
                publicInfo.setAreawater(rs.getDouble(2)/419500);
                publicInfo.setPeopleenergy(rs.getDouble(1)/16000);
                publicInfo.setPeoplewater(rs.getDouble(2)/16000);
                publicInfo.setFunc("A");
                list.add(publicInfo);
            }
        });
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                PublicInfo publicInfo=new PublicInfo();
                publicInfo.setBuildcode(rs.getString(1));
                publicInfo.setBuildname(rs.getString(8)+"-"+rs.getString(2));
                publicInfo.setTotalenergy(rs.getDouble(4));
                publicInfo.setTotalwater(rs.getDouble(5));
                publicInfo.setAreaenergy(rs.getDouble(4)/rs.getDouble(3));
                publicInfo.setAreawater(rs.getDouble(5)/rs.getDouble(3));
                publicInfo.setPeopleenergy((rs.getDouble(4)/rs.getDouble(7)));
                publicInfo.setPeoplewater(rs.getDouble(5)/rs.getDouble(7));
                publicInfo.setFunc(rs.getString(6));
                list.add(publicInfo);
            }
        });
        DataTable dataTable=new DataTable();
        dataTable.setData(list);
        return dataTable;
    }

    public DataTable getPublicOrgan(){
        String sql="SELECT T3.organID, T3.organName, VALUE_D, VALUE_S, T3.organArea, T3.people FROM"
                +" (select T1.F_ID AS organID, T1.F_AREA AS organArea, T1.F_NAME AS organName, T1.F_PEOPLE AS people, SUM(T2.F_VALUE) AS VALUE_D from T_BO_ORGAN T1, T_EC_ORGAN_YEAR T2"
                +" where T1.F_ID = T2.F_ORGANID AND T1.F_PID = 'j4_2' AND substr(T2.F_ENERGYITEMCODE,1,2) = '01' GROUP BY T1.F_ID, T1.F_AREA, T1.F_NAME, T1.F_PEOPLE) T3,"
                +" (SELECT T1.F_ID AS organID, SUM(T2.F_VALUE) AS VALUE_S from T_BO_ORGAN T1, T_EC_ORGAN_YEAR T2 where T1.F_ID = T2.F_ORGANID AND T1.F_PID = 'j4_2' AND substr(T2.F_ENERGYITEMCODE,1,2) = '02' GROUP BY T1.F_ID) T4"
                +" where t3.organID=t4.organID(+) order by t3.organID";
        final List<PublicOrganInfo> list=new LinkedList<PublicOrganInfo>();
        String anothersql = "SELECT T3.energy, T4.water FROM(SELECT sum(T2.F_VALUE) as energy FROM T_BO_ORGAN t1, T_EC_ORGAN_YEAR t2 where T1.F_ID = T2.F_ORGANID AND T1.F_PID = 'j4_2' AND substr(T2.F_ENERGYITEMCODE,1,2) = '01') T3," +
                " (SELECT sum(T2.F_VALUE) as water FROM T_BO_ORGAN t1, T_EC_ORGAN_YEAR t2 where T1.F_ID = T2.F_ORGANID AND T1.F_PID = 'j4_2' AND substr(T2.F_ENERGYITEMCODE,1,2) = '02') T4";
        jdbcTemplate.query(anothersql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                PublicOrganInfo publicOrganInfo=new PublicOrganInfo();
                publicOrganInfo.setorganid("allgroupsum");
                publicOrganInfo.setorgan("福建工程学院-所有机构");
                publicOrganInfo.setTotalenergy(rs.getDouble(1));
                publicOrganInfo.setTotalwater(rs.getDouble(2));
                //publicOrganInfo.setAreaenergy(rs.getDouble(3)/rs.getDouble(5));
                //publicOrganInfo.setAreawater(rs.getDouble(4)/rs.getDouble(5));
                publicOrganInfo.setAreaenergy(rs.getDouble(1)/888);
                publicOrganInfo.setAreawater(rs.getDouble(2)/888);
                publicOrganInfo.setPeopleenergy(rs.getDouble(1)/16000);
                publicOrganInfo.setPeoplewater(rs.getDouble(2)/16000);
                list.add(publicOrganInfo);
            }
        });
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                PublicOrganInfo publicOrganInfo=new PublicOrganInfo();
                publicOrganInfo.setorganid(rs.getString(1));
                publicOrganInfo.setorgan(rs.getString(2));
                publicOrganInfo.setTotalenergy(rs.getDouble(3));
                publicOrganInfo.setTotalwater(rs.getDouble(4));
                //publicOrganInfo.setAreaenergy(rs.getDouble(3)/rs.getDouble(5));
                //publicOrganInfo.setAreawater(rs.getDouble(4)/rs.getDouble(5));
                publicOrganInfo.setAreaenergy(rs.getDouble(3)/888);
                publicOrganInfo.setAreawater(rs.getDouble(4)/888);
                publicOrganInfo.setPeopleenergy(rs.getDouble(3)/rs.getDouble(6));
                publicOrganInfo.setPeoplewater(rs.getDouble(4)/rs.getDouble(6));
                list.add(publicOrganInfo);
            }
        });
        DataTable dataTable=new DataTable();
        dataTable.setData(list);
        return dataTable;
    }

    public DataTable getPublicBulids(){

        String sql="SELECT  T8.buildcode, T8.buildName, T8.Total_engery, t81.ZMCZ_engery, t82.KTYD_engery, t83.DLYD_engery, t84.TSYD_engery, t6.F_BUILDGROUPNAME FROM "
                +" (select T1.F_BUILDID as ID, T1.F_BUILDcode AS buildcode, T1.F_BUILDNAME as buildName, SUM(T2.F_VALUE) AS Total_engery from T_BD_BUILDBASEINFO T1, T_EC_BUILD_year T2 where T2.F_BUILDID = T1.F_BUILDID AND substr(T2.F_ENERGYITEMCODE,1,2) = '01' AND T1.F_BUILDFUNC!='M' GROUP BY t1.F_BUILDID, T1.F_BUILDcode, T1.F_BUILDNAME) T8,"
                +" (select T1.F_BUILDcode AS buildcode, T1.F_BUILDNAME as buildName, SUM(T2.F_VALUE) AS ZMCZ_engery from T_BD_BUILDBASEINFO T1, T_EC_BUILD_year T2 where T2.F_BUILDID = T1.F_BUILDID AND substr(T2.F_ENERGYITEMCODE,1,3) = '01A' GROUP BY T1.F_BUILDcode, T1.F_BUILDNAME) T81,"
                +" (select T1.F_BUILDcode AS buildcode, T1.F_BUILDNAME as buildName, SUM(T2.F_VALUE) AS KTYD_engery from T_BD_BUILDBASEINFO T1, T_EC_BUILD_year T2 where T2.F_BUILDID = T1.F_BUILDID AND substr(T2.F_ENERGYITEMCODE,1,3) = '01B' GROUP BY T1.F_BUILDcode, T1.F_BUILDNAME) T82,"
                +" (select T1.F_BUILDcode AS buildcode, T1.F_BUILDNAME as buildName, SUM(T2.F_VALUE) AS DLYD_engery from T_BD_BUILDBASEINFO T1, T_EC_BUILD_year T2 where T2.F_BUILDID = T1.F_BUILDID AND substr(T2.F_ENERGYITEMCODE,1,3) = '01C' GROUP BY T1.F_BUILDcode, T1.F_BUILDNAME) T83,"
                +" (select T1.F_BUILDcode AS buildcode, T1.F_BUILDNAME as buildName, SUM(T2.F_VALUE) AS TSYD_engery from T_BD_BUILDBASEINFO T1, T_EC_BUILD_year T2 where T2.F_BUILDID = T1.F_BUILDID AND substr(T2.F_ENERGYITEMCODE,1,3) = '01D' GROUP BY T1.F_BUILDcode, T1.F_BUILDNAME) T84, T_BD_BUILDGROUPRELAINFO t5, T_BD_BUILDGROUPBASEINFO t6"
                +" where t8.ID = t5.F_BUILDID and t5.F_BUILDGROUPID = t6.F_BUILDGROUPID and t8.buildcode = t81.buildcode(+) and t8.buildcode = t82.buildcode(+) and t8.buildcode = t83.buildcode(+) and t8.buildcode = t84.buildcode(+) order by t8.buildcode";
        final List<PublicBulidsInfo> list=new LinkedList<PublicBulidsInfo>();
        String anothersql="SELECT  T8.Total_engery, t81.ZMCZ_engery, t82.KTYD_engery, t83.DLYD_engery, t84.TSYD_engery FROM " +
                "(select SUM(F_VALUE) AS Total_engery from T_EC_BUILD_year where F_BUILDID = 'allofsumgroup' AND substr(F_ENERGYITEMCODE,1,2) = '01') T8, " +
                "(select SUM(F_VALUE) AS ZMCZ_engery from T_EC_BUILD_year where F_BUILDID = 'allofsumgroup' AND substr(F_ENERGYITEMCODE,1,3) = '01A') T81, " +
                "(select SUM(F_VALUE) AS KTYD_engery from T_EC_BUILD_year where F_BUILDID = 'allofsumgroup' AND substr(F_ENERGYITEMCODE,1,3) = '01B') T82, " +
                "(select SUM(F_VALUE) AS DLYD_engery from T_EC_BUILD_year where F_BUILDID = 'allofsumgroup' AND substr(F_ENERGYITEMCODE,1,3) = '01C') T83, " +
                "(select SUM(F_VALUE) AS TSYD_engery from T_EC_BUILD_year where F_BUILDID = 'allofsumgroup' AND substr(F_ENERGYITEMCODE,1,3) = '01D') T84 ";
        jdbcTemplate.query(anothersql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                PublicBulidsInfo publicbuildsInfo = new PublicBulidsInfo();
                publicbuildsInfo.setbulidid("000000000");
                publicbuildsInfo.setbulid("福建工程学院全校");
                publicbuildsInfo.setTotalenergy(rs.getDouble(1));
                publicbuildsInfo.setzmczenergy(rs.getDouble(2));
                publicbuildsInfo.setktydenergy(rs.getDouble(3));
                publicbuildsInfo.setdlydenergy(rs.getDouble(4));
                publicbuildsInfo.settsydenergy(rs.getDouble(5));
                list.add(publicbuildsInfo);
            }
        });
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                PublicBulidsInfo publicbuildsInfo = new PublicBulidsInfo();
                publicbuildsInfo.setbulidid(rs.getString(1));
                publicbuildsInfo.setbulid(rs.getString(8)+"-"+rs.getString(2));
                publicbuildsInfo.setTotalenergy(rs.getDouble(3));
                publicbuildsInfo.setzmczenergy(rs.getDouble(4));
                publicbuildsInfo.setktydenergy(rs.getDouble(5));
                publicbuildsInfo.setdlydenergy(rs.getDouble(6));
                publicbuildsInfo.settsydenergy(rs.getDouble(7));
                list.add(publicbuildsInfo);
            }
        });
        DataTable dataTable=new DataTable();
        dataTable.setData(list);
        return dataTable;
    }

}
