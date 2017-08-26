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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Repository
@Transactional
public class AppDao {
	@Autowired
    @Qualifier("primaryJdbcTemplate")
	private JdbcOperations jdbcTemplate;

	private DecimalFormat df = new DecimalFormat("#.00");
	public List<App_timesplitenergy> getTimeSplitEnergy() {
		String sql="select to_char(time_flag,'yy/mm') as times,NVL(electricity,-1) as electricity_value , NVL(water,-1) as water_value "+
					"from (select distinct(F_STARTTIME) as time_flag from t_ec_build_mon where f_buildlevel=2 and f_starttime>(add_months(sysdate,-13))), "+
					"     (select F_STARTTIME as times1,sum(F_VALUE) as electricity from t_ec_build_mon where f_buildlevel=2 and f_starttime>(add_months(sysdate,-13)) and substr(F_ENERGYITEMCODE,1,2)='01' group by f_starttime), "+
					"	  (select F_STARTTIME as times2,sum(F_VALUE) as water from t_ec_build_mon where f_buildlevel=2 and f_starttime>(add_months(sysdate,-13)) and substr(F_ENERGYITEMCODE,1,2)='02' group by f_starttime) "+
					"where time_flag=times1(+) and time_flag=times2(+)";
		final List<App_timesplitenergy> app_timesplitenergys=new ArrayList<App_timesplitenergy>();
		jdbcTemplate.query(sql, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				
				App_timesplitenergy app_timesplitenergy=new App_timesplitenergy();
				app_timesplitenergy.setTimes(rs.getString("times"));
				app_timesplitenergy.setElectricity(rs.getInt("electricity_value"));
				app_timesplitenergy.setWater(rs.getInt("water_value"));
				app_timesplitenergys.add(app_timesplitenergy);
			}
		});
		return app_timesplitenergys;
	}

	public List<App_timesplitenergy> getBuildEnergySort(String flag) {
		String energy_type=flag.split(",")[0];//01表示电，02表示水
		String sort_method=flag.split(",")[1];//排序方法有降序desc 升序asc
		final List<App_timesplitenergy> app_timesplitenergys=new ArrayList<App_timesplitenergy>();
		if(energy_type.equals("01")){//电
			String sql="select * from (select build_id,t2.F_BUILDNAME as buildname , energy ,t2.F_RATED_ELEC as RATED_ELEC ,ROW_NUMBER() OVER(ORDER BY (energy-t2.F_RATED_ELEC)/(t2.F_RATED_ELEC+1) "+sort_method+") as rownumber "+
									   "from T_BD_BUILD t2, (select F_BUILDID as build_id,sum(F_VALUE) as energy from t_ec_build_mon where f_buildlevel=2 and to_char(f_starttime,'yyyy-mm')=to_char(sysdate,'yyyy-mm') and substr(F_ENERGYITEMCODE,1,2)='01' group by F_BUILDID) "+
									   "where build_id=t2.F_BUILDID) "+
								  "where rownumber<4 order by rownumber asc";
			jdbcTemplate.query(sql, new RowCallbackHandler(){
				@Override
				public void processRow(ResultSet rs)throws SQLException{
					
					App_timesplitenergy app_timesplitenergy=new App_timesplitenergy();
					app_timesplitenergy.setBuilduuid(rs.getString("build_id"));
					app_timesplitenergy.setBuildname(rs.getString("buildname"));
					app_timesplitenergy.setElectricity(rs.getInt("energy"));
					app_timesplitenergy.setRatedelec(rs.getInt("RATED_ELEC"));
					app_timesplitenergys.add(app_timesplitenergy);
				}
			});
		}
		else{//电
			String sql="select * from (select build_id,t2.F_BUILDNAME as buildname , energy ,t2.F_RATED_WATER as RATED_WATER ,ROW_NUMBER() OVER(ORDER BY (energy-t2.F_RATED_WATER)/(t2.F_RATED_WATER+1) "+sort_method+") as rownumber "+
					   				  "from T_BD_BUILD t2, (select F_BUILDID as build_id,sum(F_VALUE) as energy from t_ec_build_mon where f_buildlevel=2 and to_char(f_starttime,'yyyy-mm')=to_char(sysdate,'yyyy-mm') and substr(F_ENERGYITEMCODE,1,2)='02' group by F_BUILDID) "+
					   				  "where build_id=t2.F_BUILDID) "+
					   	"where rownumber<4 order by rownumber asc";
			jdbcTemplate.query(sql, new RowCallbackHandler(){
				@Override
				public void processRow(ResultSet rs)throws SQLException{

					App_timesplitenergy app_timesplitenergy=new App_timesplitenergy();
					app_timesplitenergy.setBuilduuid(rs.getString("build_id"));
					app_timesplitenergy.setBuildname(rs.getString("buildname"));
					app_timesplitenergy.setWater(rs.getInt("energy"));
					app_timesplitenergy.setRatedwater(rs.getInt("RATED_WATER"));
					app_timesplitenergys.add(app_timesplitenergy);
				}
			});
		}
	return app_timesplitenergys;
	}

	

	public List<App_timesplitenergy> getBuildEnergy(String flag) throws ParseException {
		String flag_reg[]=flag.split(",");
		String build_energy_type=flag_reg[0];//能耗数据类型：电耗、水耗
		String build_time_type=flag_reg[1];//指明当前搜索的建筑类别
		String build_time=flag_reg[2];//指明当前时间类型，只有年，年月
		String build_level=flag_reg[3];//建筑级别
		String build_uuid=flag_reg[4];//建筑uuid
		
		//能耗类型（电01，水02）+时间类型（年1，月2，日3）+日期（2015-12-1）+建筑级别（间0，层1，栋2，区3）+建筑uuid("-"代表所有建筑)
		String tablename="";//从哪个表搜索数据
		String timeformat="";
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		Calendar cd = Calendar.getInstance();
		//判断从哪个表读数据
		if(build_time_type.equals("1")){
			tablename="T_EC_BUILD_YEAR";
			timeformat="YYYY";
		}
		else if(build_time_type.equals("2")){
			tablename="T_EC_BUILD_MON";
			timeformat="YYYYMM";
		}
		else{
			Date build_date = sdf.parse(build_time);
			Date nowtime=new Date();//获取系统当前时间
			String nowtimeString=sdf.format(nowtime);//将系统当前时间转换为"yyyy-MM-dd"格式
			
			cd.setTime(sdf.parse(nowtimeString));//将字符串转换为日期并放入cd
			cd.add(Calendar.MONTH, -1);//减去一个月  
			Date nowtimesub1month=cd.getTime();//从cd中取出时间
			int compareresualt=build_date.compareTo(nowtimesub1month);//build_date和nowtimesub1month进行比较
			if(compareresualt<0){//build_date小于nowtimesub1month
				tablename="T_EC_BUILD_DAY";
			}
			else{
				tablename="T_EC_BUILD_DAY_BUFFER";
			}
			timeformat="YYYYMMDD";
		}
		
		//根据build_energy_type能耗类型取出对应的额定值
		String rated_type="";
		if(build_energy_type.equals("01")){//电//bug

		rated_type="F_RATED_ELEC";}
		else{
			rated_type="F_RATED_WATER";
		}
		
		//根据建筑级别生成对应的sql语句
		String sql="";
		if(build_level.equals("0")){
			sql="select * from (select build_id,t2.F_NAME as buildname , energy ,t2."+rated_type+" as RATED ,ROW_NUMBER() OVER(ORDER BY (energy-t2.F_RATED_ELEC)/(t2.F_RATED_ELEC+1) desc) as rownumber "+
								"from T_BD_ROOM t2, (select F_BUILDID as build_id,sum(F_VALUE) as energy from "+tablename+" where f_buildlevel=0 and f_starttime=to_date("+build_time.replace("-", "")+",'"+timeformat+"') and substr(F_ENERGYITEMCODE,1,2)='"+build_energy_type+"' group by F_BUILDID) "+
								"where build_id=t2.F_ID) "+
				"order by rownumber asc";

		}
		else if(build_level.equals("1")){
			sql="select * from (select build_id,t2.F_NAME as buildname , energy ,t2."+rated_type+" as RATED ,ROW_NUMBER() OVER(ORDER BY (energy-t2.F_RATED_ELEC)/(t2.F_RATED_ELEC+1) desc) as rownumber "+
								"from T_BD_FLOOR t2, (select F_BUILDID as build_id,sum(F_VALUE) as energy from "+tablename+" where f_buildlevel=1 and f_starttime=to_date("+build_time.replace("-", "")+",'"+timeformat+"') and substr(F_ENERGYITEMCODE,1,2)='"+build_energy_type+"' group by F_BUILDID) "+
								"where build_id=t2.F_ID) "+
					"order by rownumber asc";
		}
		else if(build_level.equals("2")){
			sql="select * from (select build_id,t2.F_BUILDNAME as buildname , energy ,t2."+rated_type+" as RATED ,ROW_NUMBER() OVER(ORDER BY (energy-t2.F_RATED_ELEC)/(t2.F_RATED_ELEC+1) desc) as rownumber "+
								"from T_BD_BUILD t2, (select F_BUILDID as build_id,sum(F_VALUE) as energy from "+tablename+" where f_buildlevel=2 and f_starttime=to_date("+build_time.replace("-", "")+",'"+timeformat+"') and substr(F_ENERGYITEMCODE,1,2)='"+build_energy_type+"' group by F_BUILDID) "+
								"where build_id=t2.F_BUILDID) "+
					"order by rownumber asc";
		}
		else{
			sql="select * from (select build_id,t2.F_BUILDGROUPNAME as buildname , energy ,t2."+rated_type+" as RATED ,ROW_NUMBER() OVER(ORDER BY (energy-t2.F_RATED_ELEC)/(t2.F_RATED_ELEC+1) desc) as rownumber "+
								"from T_BD_GROUP t2, (select F_BUILDID as build_id,sum(F_VALUE) as energy from "+tablename+" where f_buildlevel=3 and f_starttime=to_date("+build_time.replace("-", "")+",'"+timeformat+"') and substr(F_ENERGYITEMCODE,1,2)='"+build_energy_type+"' group by F_BUILDID) "+
								"where build_id=t2.F_BUILDGROUPID) "+
					"order by rownumber asc";
		}
		System.out.println(sql);
		final List<App_timesplitenergy> app_timesplitenergys=new ArrayList<App_timesplitenergy>();
		jdbcTemplate.query(sql, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{

				App_timesplitenergy app_timesplitenergy=new App_timesplitenergy();
				app_timesplitenergy.setBuilduuid(rs.getString("build_id"));
				app_timesplitenergy.setBuildname(rs.getString("buildname"));
				app_timesplitenergy.setEnergy(rs.getInt("energy"));
				app_timesplitenergy.setRated(rs.getInt("RATED"));
				app_timesplitenergy.setRownumber(rs.getInt("rownumber"));
				app_timesplitenergys.add(app_timesplitenergy);
				}
			});
		
	return app_timesplitenergys;
	}
//
//	public App_EnergyChart getEnergy(App_EnergySearch app_energySearch) throws ParseException {
//		//model:build|organ,modellevel:group|build|floor|room,modelid:model's uuid,
//		//energytypeid:energytype's uuid,energytype:energytype's name,
//		//startdate:xxxx/xx/xx,enddate:xxxx/xx/xx,basetime:minutes|hour|day|month|year,
//		//caltype:total|people|area,showtype:yony|notyony(求某个建筑、部分的时间段能耗返回的数据中每次会有环比，yony代表需要计算同比，noyony代表不需要)
//		////////////////////////////////////能耗类型处理//////////////////////////////////////////////
//		System.out.println(app_energySearch.getStartdate()+','+app_energySearch.getEnddate());
//		int energytypeendflag=app_energySearch.getEnergytypeid().indexOf("0", 2);//从指定的索引处开始，返回第一次出现的指定子字符串在此字符串中的索引。
//		energytypeendflag=(energytypeendflag==-1)?(app_energySearch.getEnergytypeid().length()):energytypeendflag;
//		String energytype=app_energySearch.getEnergytypeid().substring(0, energytypeendflag);
//		//如果没有搜索到0，则赋值为能耗类型的总长度
//		/////////////////////////////////////////////////////////////////////////////////////////
//		int	 buildlevel=0;
//		int  basetimetype=0;
//		if(app_energySearch.getModellevel().equals("room")){
//			buildlevel=0;
//		}
//		else if(app_energySearch.getModellevel().equals("floor")){
//			buildlevel=1;
//		}
//		else if(app_energySearch.getModellevel().equals("build")){
//			buildlevel=2;
//		}
//		else {
//			buildlevel=3;
//		}
//
//		///////////////////////////////////////////////////////////////////////////////////////////////////////////
//		String startdateString=app_energySearch.getStartdate().replace("/", "").replace("-", "");//将时间字符串格式化为YYYYMMDD的样式
//		String enddateString=app_energySearch.getEnddate().replace("/", "").replace("-", "");
//		int tablechoose=1;//0-buffer表 ，1-历史表，2-跨越两表
//        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMdd" );
//		Calendar cd = Calendar.getInstance();
//		//判断从哪个表读数据
//
//		Date startdateDate = sdf.parse(startdateString);
//		Date enddateDate = sdf.parse(enddateString);
//		System.out.println(startdateDate);
//		/*
//		SimpleDateFormat test =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
//		Date testDate = test.parse("2015-03-20 15:40:30");
//		long t = testDate.getTime();
//        java.sql.Date d = new java.sql.Date(t);
//        System.out.println(d);
//		*/
//		//同比时间生成
//		cd.setTime(startdateDate);
//		cd.add(Calendar.YEAR, -1);
//		Date yonystartdateDate = cd.getTime();
//		cd.setTime(enddateDate);
//		cd.add(Calendar.YEAR, -1);
//		Date yonyenddateDate = cd.getTime();
//		int  returntimebeginIndex=0;
//		int  returntimeendIndex=0;
//		//环比时间，搜索数据时将startdate往前推一个单位时间,求所有建筑某个时间段的能耗，没有同比，也没有环比
//		String energydatatable[] = {null,null};
//		if(app_energySearch.getBasetime().equals("minutes")){
//			energydatatable[0]="T_EC_BUILD_15_BUFFER";
//			energydatatable[1]="T_EC_BUILD_15";
//			basetimetype=0;
//			returntimebeginIndex=11;
//			returntimeendIndex=16;
//			if(app_energySearch.getModelid()!=null){//如果目标uuid为空，说明当前搜索的数据是所有建筑的数据
//				cd.setTime(startdateDate);
//				cd.add(Calendar.MINUTE, -15);//如果是取单个建筑能耗数据，就直接将起始时间往前推一个时间单位，多取一个数据！用于环比计算
//				startdateDate=cd.getTime();
//			}
//
//		}
//		else if(app_energySearch.getBasetime().equals("hour")){
//			energydatatable[0]="T_EC_BUILD_HOUR_BUFFER";
//			energydatatable[1]="T_EC_BUILD_HOUR";
//			basetimetype=1;
//			returntimebeginIndex=11;
//			returntimeendIndex=16;
//			if(app_energySearch.getModelid()!=null){//如果目标uuid为空，说明当前搜索的数据是所有建筑的数据
//				cd.setTime(startdateDate);
//				cd.add(Calendar.HOUR, -1);//如果是取单个建筑能耗数据，就直接将起始时间往前推一个时间单位，多取一个数据！用于环比计算
//				startdateDate=cd.getTime();
//			}
//		}
//		else if(app_energySearch.getBasetime().equals("day")){
//			energydatatable[0]="T_EC_BUILD_DAY_BUFFER";
//			energydatatable[1]="T_EC_BUILD_DAY";
//			basetimetype=2;
//			returntimebeginIndex=5;
//			returntimeendIndex=10;
//			if(app_energySearch.getModelid()!=null){//如果目标uuid为空，说明当前搜索的数据是所有建筑的数据
//				cd.setTime(startdateDate);
//				cd.add(Calendar.DAY_OF_YEAR, -1);//如果是取单个建筑能耗数据，就直接将起始时间往前推一个时间单位，多取一个数据！用于环比计算
//				startdateDate=cd.getTime();
//			}
//		}
//		else if(app_energySearch.getBasetime().equals("month")){
//			energydatatable[0]="T_EC_BUILD_MON";
//			energydatatable[1]="T_EC_BUILD_MON";
//			basetimetype=3;
//			returntimebeginIndex=0;
//			returntimeendIndex=7;
//			if(app_energySearch.getModelid()!=null){//如果目标uuid为空，说明当前搜索的数据是所有建筑的数据
//				cd.setTime(startdateDate);
//				cd.add(Calendar.MONTH, -1);//如果是取单个建筑能耗数据，就直接将起始时间往前推一个时间单位，多取一个数据！用于环比计算
//				startdateDate=cd.getTime();
//			}
//		}
//		else {
//			energydatatable[0]="T_EC_BUILD_YEAR";
//			energydatatable[1]="T_EC_BUILD_YEAR";
//			basetimetype=4;
//			returntimebeginIndex=0;
//			returntimeendIndex=4;
//			if(app_energySearch.getModelid()!=null){//如果目标uuid为空，说明当前搜索的数据是所有建筑的数据
//				cd.setTime(startdateDate);
//				cd.add(Calendar.YEAR, -1);//如果是取单个建筑能耗数据，就直接将起始时间往前推一个时间单位，多取一个数据！用于环比计算
//				startdateDate=cd.getTime();
//			}
//		}
//
//		if(basetimetype<3){
//			Date nowtime=new Date();//获取系统当前时间
//			String nowtimeString=sdf.format(nowtime);//将系统当前时间转换为"yyyyMMdd"格式
//
//			cd.setTime(sdf.parse(nowtimeString));//将字符串转换为日期并放入cd
//			if(basetimetype==2){
//				cd.add(Calendar.MONTH, -12);//减去12个月  ,day表中存储12个月日数据
//			}
//			else{
//				cd.add(Calendar.MONTH, -1);//减去一个月  ，小时、分钟表中存储1个月数据
//			}
//			Date nowtimesub1month=cd.getTime();//从cd中取出时间
//
//			int compareresualt1=startdateDate.compareTo(nowtimesub1month);//startdateDate和nowtimesub1month进行比较
//			int compareresualt2=enddateDate.compareTo(nowtimesub1month);//enddateDate和nowtimesub1month进行比较
//			if(compareresualt1>0 || compareresualt2>0){//都大，从buffer表中取数据
//				tablechoose=0;
//			}
//			else if(compareresualt1<0 || compareresualt2<0){//起始结束时间都小，从历史表中取数据
//				tablechoose=1;
//			}
//			else {//其他的，跨表取数据
//				tablechoose=2;
//			}
//		}
//
//
//
//
//		String	sql="";
//		String	sql_yony="";
//		Object[] args = null;//=new Object[]{energySearch.getModelid(),energySearch.getEnergytypeid(),sdf.format(energySearch.getStartdate()),sdf.format(energySearch.getEnddate())};
//		Object[] args_yony = null;
//		if(app_energySearch.getModel().equals("build")){//求建筑能耗
//			if(app_energySearch.getModelid()==null){//如果目标uuid为空，说明当前搜索的数据是所有建筑的数据
//				args=new Object[]{buildlevel,startdateDate,enddateDate,energytypeendflag,energytype};
//				//只有分段表才需要重新初始化args，所以这边先统一初始化，在分段表情况下再重新初始化
//				if(basetimetype<3){//分钟0、小时1、日2 需要进行分表
//					if(tablechoose==0){//需要填入的有  建筑级别、起始日期、结束日期、能耗类型截取结束位、能耗类型截取字符
//						sql="select build_id,t2.F_BUILDNAME as buildname , energy " +
//							"from t_be_buildsimpleinfo t2, " +
//							"	  (select F_BUILDID as build_id,sum(F_VALUE) as energy " +
//							"	   from "+energydatatable[0]+" where f_buildlevel=? and f_starttime>=? and f_starttime<=? and substr(F_ENERGYITEMCODE,1,?)=? group by F_BUILDID) where build_id=t2.F_BUILDID group by build_id,t2.F_BUILDNAME,energy order by energy desc";
//
//					}
//					else if(tablechoose==1){//
//						sql="select build_id,t2.F_BUILDNAME as buildname , energy " +
//								"from t_be_buildsimpleinfo t2, " +
//								"	  (select F_BUILDID as build_id,sum(F_VALUE) as energy " +
//								"	   from "+energydatatable[1]+" where f_buildlevel=? and f_starttime>=? and f_starttime<=? and substr(F_ENERGYITEMCODE,1,?)=? group by F_BUILDID) where build_id=t2.F_BUILDID group by build_id,t2.F_BUILDNAME,energy order by energy desc";
//
//					}
//					else{
//						sql="select build_id,t2.F_BUILDNAME as buildname , energy" +
//								"from t_be_buildsimpleinfo t2, " +
//								"	  ((select F_BUILDID as build_id,sum(F_VALUE) as energy " +
//								"	   			from "+energydatatable[1]+" where f_buildlevel=? and f_starttime>=? and substr(F_ENERGYITEMCODE,1,?)=? group by F_BUILDID)" +
//								"		UNION ALL "+
//								"	   (select F_BUILDID as build_id,sum(F_VALUE) as energy " +
//										"	   	from "+energydatatable[0]+" where f_buildlevel=? and f_starttime<=? and substr(F_ENERGYITEMCODE,1,?)=? group by F_BUILDID)" +
//								") where build_id=t2.F_BUILDID group by build_id,t2.F_BUILDNAME order by energy desc ";
//
//						args=new Object[]{buildlevel,startdateDate,energytypeendflag,energytype,buildlevel,enddateDate,energytypeendflag,energytype};
//					}
//				}
//				else{//月、年
//					sql="select build_id,t2.F_BUILDNAME as buildname , energy " +
//							"from t_be_buildsimpleinfo t2, " +
//							"	  (select F_BUILDID as build_id,sum(F_VALUE) as energy " +
//							"	   from "+energydatatable[0]+" where f_buildlevel=? and f_starttime>=? and f_starttime<=? and substr(F_ENERGYITEMCODE,1,?)=? group by F_BUILDID) where build_id=t2.F_BUILDID group by build_id,t2.F_BUILDNAME,energy order by energy desc";
//				}
//			}
//			else{//求某个建筑单位的时间段能耗数据
//				args=new Object[]{startdateDate,enddateDate,energytypeendflag,energytype,app_energySearch.getModelid()};
//				args_yony=new Object[]{yonystartdateDate,yonyenddateDate,energytypeendflag,energytype,app_energySearch.getModelid()};//yony肯定是在历史表里
//				sql_yony="select sum(F_VALUE) as energy,add_months(f_starttime,12) as datatime_add1year	from "+energydatatable[1]+" "+
//                        "where f_starttime>=? and f_starttime<=? and substr(F_ENERGYITEMCODE,1,?)=? and f_buildid=? "+
//                        "group by f_starttime order by f_starttime";
//				if(basetimetype<3){//分钟0、小时1、日2 需要进行分表
//					if(tablechoose==0){
//						sql="select sum(F_VALUE) as energy,f_starttime,to_char(f_starttime,'YYYY/MM/DD HH24:MI') as datatime	from "+energydatatable[0]+" "+
//                            "where f_starttime>=? and f_starttime<=? and substr(F_ENERGYITEMCODE,1,?)=? and f_buildid=? "+
//                            "group by f_starttime order by f_starttime";
//					}
//					else if(tablechoose==1){
//						sql="select sum(F_VALUE) as energy,f_starttime,to_char(f_starttime,'YYYY/MM/DD HH24:MI') as datatime	from "+energydatatable[1]+" "+
//	                            "where f_starttime>=? and f_starttime<=? and substr(F_ENERGYITEMCODE,1,?)=? and f_buildid=? "+
//	                            "group by f_starttime order by f_starttime";
//					}
//					else{
//						sql="select sum(F_VALUE) as energy,f_starttime,to_char(f_starttime,'YYYY/MM/DD HH24:MI') as datatime " +
//							"from ((select F_VALUE,f_starttime	from "+energydatatable[1]+" "+
//	                            	"where f_starttime>=? and substr(F_ENERGYITEMCODE,1,?)=? and f_buildid=?) " +
//	                            	"UNION ALL " +
//	                            	"(select F_VALUE,f_starttime	from "+energydatatable[0]+" "+
//	                            	"where f_starttime<=? and substr(F_ENERGYITEMCODE,1,?)=? and f_buildid=?)) "+
//	                            "group by f_starttime order by f_starttime";
//						args=new Object[]{startdateDate,energytypeendflag,energytype,app_energySearch.getModelid(),enddateDate,energytypeendflag,energytype,app_energySearch.getModelid()};
//					}
//				}
//				else{
//					sql="select sum(F_VALUE) as energy,f_starttime,to_char(f_starttime,'YYYY/MM/DD HH24:MI') as datatime	from "+energydatatable[0]+" "+
//                            "where f_starttime>=? and f_starttime<=? and substr(F_ENERGYITEMCODE,1,?)=? and f_buildid=? "+
//                            "group by f_starttime order by f_starttime";
//				}
//			}
//
//		}
//		System.out.println(sql);
//		final App_EnergyChart app_EnergyChart=new App_EnergyChart();
//		final List<String> modelid=new ArrayList<String>();
//		final List<String> modelname=new ArrayList<String>();
//		final List<Double> energy=new ArrayList<Double>();
//		final List<Double> rated=new ArrayList<Double>();
//		final List<String> datatime=new ArrayList<String>();
//		final List<Date>   datatimedate=new ArrayList<Date>();
//
//		final List<Double> chain=new ArrayList<Double>();//环比
//		final List<Double> chain_data=new ArrayList<Double>();//环比
//		final List<String> chain_datatime=new ArrayList<String>();//环比datatime
//		final List<Date>   chain_datatimedate=new ArrayList<Date>();//环比datatime
//
//		final List<Double> yony=new ArrayList<Double>();//同比
//		final List<Double> yony_data=new ArrayList<Double>();//同比
//		final List<Date>   yony_datatimedate=new ArrayList<Date>();//已经是去年实际时间加上一年后的数据
//        final double       peoarea=getPeopleArea(app_energySearch);
//		if(app_energySearch.getModel().equals("build")){//求建筑能耗
//			modelid.clear();
//			modelname.clear();
//			energy.clear();
//			if(app_energySearch.getModelid()==null){//求所有建筑时间段内的能耗值，没有同比、也没有环比--build_id,buildname,energy
//
//				jdbcTemplate.query(sql,args, new RowCallbackHandler() {
//					@Override
//					public void processRow(ResultSet rs) throws SQLException {
//						modelid.add(rs.getString("build_id"));
//						modelname.add(rs.getString("buildname"));
//						energy.add(rs.getDouble("energy")/peoarea);
//					}
//
//				});
//				app_EnergyChart.setModelid(modelid);
//				app_EnergyChart.setModelname(modelname);
//				app_EnergyChart.setEnergy(energy);
//			}
//			else{//求某建筑的时间段内能耗数据energy，datatime
//				//附加信息在这先填写一下！！
//				modelid.add(app_energySearch.getModelid());
//				app_EnergyChart.setModelid(modelid);
//				app_EnergyChart.setEnergytype(app_energySearch.getEnergytypeid());
//				////////////////////////////////////////////////////////////////////////////////
//
//
//
//////////////////////////////////////////原始数据、环比计算、数据赋值///////////////////////////////////////////////////////
//				jdbcTemplate.query(sql,args, new RowCallbackHandler() {
//					@Override
//					public void processRow(ResultSet rs) throws SQLException {
//						chain_data.add(rs.getDouble("energy"));
//						chain_datatime.add(rs.getString("datatime"));
//						chain_datatimedate.add(rs.getDate("f_starttime"));
//					}
//				});
//				boolean yonycantodo=false;//同比计算使能，有有效数据才能进行计算 否则无需进行同比计算操作
//				if(chain_data.size()>0){//有搜索到数据的时候才计算
//					if(chain_datatimedate.get(0).compareTo(sdf.parse(startdateString))>=0){//如果得到的第一个数据时间比原本想要搜索的第一个数据时间，说明没有找到第一个可用的环比数据
//						//直接进行赋值和环比计算
//						energy.add(chain_data.get(0)/peoarea);
//						chain.add(0.0);
//						datatime.add(chain_datatime.get(0).substring(returntimebeginIndex, returntimeendIndex));//'YYYY/MM/DD HH24:MI'
//						datatimedate.add(chain_datatimedate.get(0));
//						yony.add(0.0);
//						yonycantodo=true;
//					}
//					if(chain_data.size()>1){//多于一个数据才开始计算环比
//						for (int i = 1; i < chain_data.size(); i++) {
//							energy.add(chain_data.get(i)/peoarea);
//							datatime.add(chain_datatime.get(i).substring(returntimebeginIndex, returntimeendIndex));
//							datatimedate.add(chain_datatimedate.get(i));
//							yony.add(0.0);
//							yonycantodo=true;
//							if(chain_data.get(i-1)!=0.0){
//								chain.add(Double.parseDouble(df.format((chain_data.get(i)-chain_data.get(i-1))/chain_data.get(i-1) * 100)));//环比上涨（+）或下降（-）百分比
//							}
//							else {
//								chain.add(0.0);
//							}
//						}
//					}
//				}
//
//				//处理完后赋值
//				app_EnergyChart.setEnergy(energy);
//				app_EnergyChart.setDatatime(datatime);
//				app_EnergyChart.setChain(chain);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
///////////////////////////////////////////同比计算//////////////////////////////////////////////////////////////
//				if(yonycantodo && app_energySearch.getShowtype()!=null ){//需要计算同比的时候才计算，否则就不计算了！这个搜索数据有可能会比较慢，不过近期基本不会有影响
//					jdbcTemplate.query(sql_yony,args_yony, new RowCallbackHandler() {
//						@Override
//						public void processRow(ResultSet rs) throws SQLException {
//							yony_data.add(rs.getDouble("energy"));
//							yony_datatimedate.add(rs.getDate("datatime_add1year"));
//						}
//					});
//					if(yony_data.size()>0){//有搜索到数据的时候才计算同比数据，否则不进行计算，直接赋值0
//						yony.clear();
//						int j=0;
//						for(int i=0;i<energy.size();i++){
//							if(datatimedate.get(i).compareTo(yony_datatimedate.get(j))==0){//如果时间对比相等就可以计算生成计算数据
//								if(yony_data.get(j) != 0.0){
//									yony.add(Double.parseDouble(df.format((energy.get(i)-yony_data.get(j))/yony_data.get(j)*100)));
//								}
//								else {
//									yony.add(0.0);
//								}
//								j++;
//							}
//							else {
//								yony.add(0.0);
//							}
//
//						}
//						app_EnergyChart.setYony(yony);
//					}
//					else{
//						app_EnergyChart.setYony(yony);
//					}
//				}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//			}
//		}
//
//		return app_EnergyChart;
//	}

    public App_EnergyChart getEnergy(App_EnergySearch app_energySearch) throws ParseException {
        //model:build|organ,modellevel:group|build|floor|room,modelid:model's uuid,
        //energytypeid:energytype's uuid,energytype:energytype's name,
        //startdate:xxxx/xx/xx,enddate:xxxx/xx/xx,basetime:minutes|hour|day|month|year,
        //caltype:total|people|area,showtype:yony|notyony(求某个建筑、部分的时间段能耗返回的数据中每次会有环比，yony代表需要计算同比，noyony代表不需要)
        ////////////////////////////////////能耗类型处理//////////////////////////////////////////////
        System.out.println(app_energySearch.getStartdate()+','+app_energySearch.getEnddate());
        int energytypeendflag=app_energySearch.getEnergytypeid().indexOf("0", 2);//从指定的索引处开始，返回第一次出现的指定子字符串在此字符串中的索引。
        energytypeendflag=(energytypeendflag==-1)?(app_energySearch.getEnergytypeid().length()):energytypeendflag;
        String energytype=app_energySearch.getEnergytypeid().substring(0, energytypeendflag);
        //如果没有搜索到0，则赋值为能耗类型的总长度
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        String startdateString=app_energySearch.getStartdate().replace("/", "").replace("-", "");//将时间字符串格式化为YYYYMMDD的样式
        String enddateString=app_energySearch.getEnddate().replace("/", "").replace("-", "");
        int tablechoose=1;//0-buffer表 ，1-历史表，2-跨越两表
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMdd" );
        Calendar cd = Calendar.getInstance();
        //判断从哪个表读数据

        Date startdateDate = sdf.parse(startdateString);
        Date enddateDate = sdf.parse(enddateString);
        System.out.println(app_energySearch.getModel()+":"+app_energySearch.getModelid());
        if(app_energySearch.getModel().equals("organ")&&app_energySearch.getModelid().equals("")) {
            app_energySearch.setModel("build");
            app_energySearch.setModelid("allofsumgroup");
        }

        //同比时间生成
        cd.setTime(startdateDate);
        cd.add(Calendar.YEAR, -1);
        Date yonystartdateDate = cd.getTime();
        cd.setTime(enddateDate);
        cd.add(Calendar.YEAR, -1);
        Date yonyenddateDate = cd.getTime();
        int  returntimebeginIndex=0;
        int  returntimeendIndex=0;
        int  basetimetype=0;
        //环比时间，搜索数据时将startdate往前推一个单位时间,求所有建筑某个时间段的能耗，没有同比，也没有环比
        String energydatatable[] = {null,null};
        if(app_energySearch.getBasetime().equals("minutes")){
            if(app_energySearch.getModel().equals("build")) {
                energydatatable[0] = "T_EC_BUILD_15";
                energydatatable[1] = "T_EC_BUILD_15";
            }
            else{
                energydatatable[0] = "T_EC_ORGAN_15";
                energydatatable[1] = "T_EC_ORGAN_15";
            }
            basetimetype=0;
            returntimebeginIndex=11;
            returntimeendIndex=16;
            if(app_energySearch.getModelid()!=null){//如果目标uuid为空，说明当前搜索的数据是所有建筑的数据
                cd.setTime(startdateDate);
                cd.add(Calendar.MINUTE, -15);//如果是取单个建筑能耗数据，就直接将起始时间往前推一个时间单位，多取一个数据！用于环比计算
                startdateDate=cd.getTime();
            }

        }
        else if(app_energySearch.getBasetime().equals("hour")){
            if(app_energySearch.getModel().equals("build")) {
                energydatatable[0] = "T_EC_BUILD_HOUR";
                energydatatable[1] = "T_EC_BUILD_HOUR";
            }
            else{
                energydatatable[0] = "T_EC_ORGAN_HOUR";
                energydatatable[1] = "T_EC_ORGAN_HOUR";
            }
            basetimetype=1;
            returntimebeginIndex=11;
            returntimeendIndex=16;
            if(app_energySearch.getModelid()!=null){//如果目标uuid为空，说明当前搜索的数据是所有建筑的数据
                cd.setTime(startdateDate);
                cd.add(Calendar.HOUR, -1);//如果是取单个建筑能耗数据，就直接将起始时间往前推一个时间单位，多取一个数据！用于环比计算
                startdateDate=cd.getTime();
            }
        }
        else if(app_energySearch.getBasetime().equals("day")){
            if(app_energySearch.getModel().equals("build")) {
                energydatatable[0] = "T_EC_BUILD_DAY";
                energydatatable[1] = "T_EC_BUILD_DAY";
            }
            else{
                energydatatable[0] = "T_EC_ORGAN_DAY";
                energydatatable[1] = "T_EC_ORGAN_DAY";
            }
            basetimetype=2;
            returntimebeginIndex=5;
            returntimeendIndex=10;
            if(app_energySearch.getModelid()!=null){//如果目标uuid为空，说明当前搜索的数据是所有建筑的数据
                cd.setTime(startdateDate);
                cd.add(Calendar.DAY_OF_YEAR, -1);//如果是取单个建筑能耗数据，就直接将起始时间往前推一个时间单位，多取一个数据！用于环比计算
                startdateDate=cd.getTime();
            }
        }
        else if(app_energySearch.getBasetime().equals("month")){
            if(app_energySearch.getModel().equals("build")) {
                energydatatable[0] = "T_EC_BUILD_MON";
                energydatatable[1] = "T_EC_BUILD_MON";
            }
            else {
                energydatatable[0] = "T_EC_ORGAN_MON";
                energydatatable[1] = "T_EC_ORGAN_MON";
            }
            basetimetype=3;
            returntimebeginIndex=0;
            returntimeendIndex=7;
            if(app_energySearch.getModelid()!=null){//如果目标uuid为空，说明当前搜索的数据是所有建筑的数据
                cd.setTime(startdateDate);
                cd.add(Calendar.MONTH, -1);//如果是取单个建筑能耗数据，就直接将起始时间往前推一个时间单位，多取一个数据！用于环比计算
                startdateDate=cd.getTime();
            }
        }
        else {
            if(app_energySearch.getModel().equals("build")) {
                energydatatable[0] = "T_EC_BUILD_YEAR";
                energydatatable[1] = "T_EC_BUILD_YEAR";
            }
            else{
                energydatatable[0] = "T_EC_ORGAN_YEAR";
                energydatatable[1] = "T_EC_ORGAN_YEAR";
            }
            basetimetype=4;
            returntimebeginIndex=0;
            returntimeendIndex=4;
            if(app_energySearch.getModelid()!=null){//如果目标uuid为空，说明当前搜索的数据是所有建筑的数据
                cd.setTime(startdateDate);
                cd.add(Calendar.YEAR, -1);//如果是取单个建筑能耗数据，就直接将起始时间往前推一个时间单位，多取一个数据！用于环比计算
                startdateDate=cd.getTime();
            }
        }

        if(basetimetype<3){
            Date nowtime=new Date();//获取系统当前时间
            String nowtimeString=sdf.format(nowtime);//将系统当前时间转换为"yyyyMMdd"格式

            cd.setTime(sdf.parse(nowtimeString));//将字符串转换为日期并放入cd
            if(basetimetype==2){
                cd.add(Calendar.MONTH, -12);//减去12个月  ,day表中存储12个月日数据
            }
            else{
                cd.add(Calendar.MONTH, -1);//减去一个月  ，小时、分钟表中存储1个月数据
            }
            Date nowtimesub1month=cd.getTime();//从cd中取出时间

            int compareresualt1=startdateDate.compareTo(nowtimesub1month);//startdateDate和nowtimesub1month进行比较
            int compareresualt2=enddateDate.compareTo(nowtimesub1month);//enddateDate和nowtimesub1month进行比较
            if(compareresualt1>0 || compareresualt2>0){//都大，从buffer表中取数据
                tablechoose=0;
            }
            else if(compareresualt1<0 || compareresualt2<0){//起始结束时间都小，从历史表中取数据
                tablechoose=1;
            }
            else {//其他的，跨表取数据
                tablechoose=2;
            }
        }
        String objectid=null;
        if(app_energySearch.getModel().equals("build")) {
            objectid="f_buildid";
        }
        else
            objectid="f_organid";
        String	sql="";
        String	sql_yony="";
        Object[] args=new Object[]{startdateDate,enddateDate,energytypeendflag,energytype,app_energySearch.getModelid()};
        Object[] args_yony=new Object[]{yonystartdateDate,yonyenddateDate,energytypeendflag,energytype,app_energySearch.getModelid()};//yony肯定是在历史表里
        sql_yony="select sum(F_VALUE) as energy,add_months(f_starttime,12) as datatime_add1year	from "+energydatatable[1]+" "+
                "where f_starttime>=? and f_starttime<=? and substr(F_ENERGYITEMCODE,1,?)=? and "+objectid+"=? "+
                "group by f_starttime order by f_starttime";
        if(basetimetype<3){//分钟0、小时1、日2 需要进行分表
            if(tablechoose==0){
                sql="select sum(F_VALUE) as energy,f_starttime,to_char(f_starttime,'YYYY/MM/DD HH24:MI') as datatime	from "+energydatatable[0]+" "+
                        "where f_starttime>=? and f_starttime<=? and substr(F_ENERGYITEMCODE,1,?)=? and "+objectid+"=? "+
                        "group by f_starttime order by f_starttime";
            }
            else if(tablechoose==1){
                sql="select sum(F_VALUE) as energy,f_starttime,to_char(f_starttime,'YYYY/MM/DD HH24:MI') as datatime	from "+energydatatable[1]+" "+
                        "where f_starttime>=? and f_starttime<=? and substr(F_ENERGYITEMCODE,1,?)=? and "+objectid+"=? "+
                        "group by f_starttime order by f_starttime";
            }
            else{
                sql="select sum(F_VALUE) as energy,f_starttime,to_char(f_starttime,'YYYY/MM/DD HH24:MI') as datatime " +
                        "from ((select F_VALUE,f_starttime	from "+energydatatable[1]+" "+
                        "where f_starttime>=? and substr(F_ENERGYITEMCODE,1,?)=? and "+objectid+"=?) " +
                        "UNION ALL " +
                        "(select F_VALUE,f_starttime	from "+energydatatable[0]+" "+
                        "where f_starttime<=? and substr(F_ENERGYITEMCODE,1,?)=? and "+objectid+"=?)) "+
                        "group by f_starttime order by f_starttime";
                args=new Object[]{startdateDate,energytypeendflag,energytype,app_energySearch.getModelid(),enddateDate,energytypeendflag,energytype,app_energySearch.getModelid()};
            }
        }
        else{
            sql="select sum(F_VALUE) as energy,f_starttime,to_char(f_starttime,'YYYY/MM/DD HH24:MI') as datatime	from "+energydatatable[0]+" "+
                    "where f_starttime>=? and f_starttime<=? and substr(F_ENERGYITEMCODE,1,?)=? and "+objectid+"=? "+
                    "group by f_starttime order by f_starttime";
        }
        System.out.println(sql);
        final App_EnergyChart app_EnergyChart=new App_EnergyChart();
        final List<String> modelid=new ArrayList<String>();
        final List<Double> energy=new ArrayList<Double>();
        final List<String> datatime=new ArrayList<String>();
        final List<Date>   datatimedate=new ArrayList<Date>();

        final List<Double> chain=new ArrayList<Double>();//环比
        final List<Double> chain_data=new ArrayList<Double>();//环比
        final List<String> chain_datatime=new ArrayList<String>();//环比datatime
        final List<Date>   chain_datatimedate=new ArrayList<Date>();//环比datatime

        final List<Double> yony=new ArrayList<Double>();//同比
        final List<Double> yony_data=new ArrayList<Double>();//同比
        final List<Date>   yony_datatimedate=new ArrayList<Date>();//已经是去年实际时间加上一年后的数据
        final double       peoarea=getPeopleArea(app_energySearch);

        modelid.add(app_energySearch.getModelid());
        app_EnergyChart.setModelid(modelid);
        app_EnergyChart.setEnergytype(app_energySearch.getEnergytypeid());
        ////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////原始数据、环比计算、数据赋值///////////////////////////////////////////////////////
        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                chain_data.add(rs.getDouble("energy"));
                chain_datatime.add(rs.getString("datatime"));
                chain_datatimedate.add(rs.getDate("f_starttime"));
            }
        });
        boolean yonycantodo=false;//同比计算使能，有有效数据才能进行计算 否则无需进行同比计算操作
        if(chain_data.size()>0){//有搜索到数据的时候才计算
            if(chain_datatimedate.get(0).compareTo(sdf.parse(startdateString))>=0){//如果得到的第一个数据时间比原本想要搜索的第一个数据时间，说明没有找到第一个可用的环比数据
                //直接进行赋值和环比计算
                energy.add(chain_data.get(0)/peoarea);
                chain.add(0.0);
                datatime.add(chain_datatime.get(0).substring(returntimebeginIndex, returntimeendIndex));//'YYYY/MM/DD HH24:MI'
                datatimedate.add(chain_datatimedate.get(0));
                yony.add(0.0);
                yonycantodo=true;
            }
            if(chain_data.size()>1){//多于一个数据才开始计算环比
                for (int i = 1; i < chain_data.size(); i++) {
                    energy.add(chain_data.get(i)/peoarea);
                    datatime.add(chain_datatime.get(i).substring(returntimebeginIndex, returntimeendIndex));
                    datatimedate.add(chain_datatimedate.get(i));
                    yony.add(0.0);
                    yonycantodo=true;
                    if(chain_data.get(i-1)!=0.0){
                        chain.add(Double.parseDouble(df.format((chain_data.get(i)-chain_data.get(i-1))/chain_data.get(i-1) * 100)));//环比上涨（+）或下降（-）百分比
                    }
                    else {
                        chain.add(0.0);
                    }
                }
            }
        }

        //处理完后赋值
        app_EnergyChart.setEnergy(energy);
        app_EnergyChart.setDatatime(datatime);
        app_EnergyChart.setChain(chain);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////////////////////同比计算//////////////////////////////////////////////////////////////
        if(yonycantodo && app_energySearch.getShowtype()!=null ){//需要计算同比的时候才计算，否则就不计算了！这个搜索数据有可能会比较慢，不过近期基本不会有影响
            jdbcTemplate.query(sql_yony,args_yony, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    yony_data.add(rs.getDouble("energy"));
                    yony_datatimedate.add(rs.getDate("datatime_add1year"));
                }
            });
            if(yony_data.size()>0){//有搜索到数据的时候才计算同比数据，否则不进行计算，直接赋值0
                yony.clear();
                int j=0;
                for(int i=0;i<energy.size();i++){
                    if(datatimedate.get(i).compareTo(yony_datatimedate.get(j))==0){//如果时间对比相等就可以计算生成计算数据
                        if(yony_data.get(j) != 0.0){
                            yony.add(Double.parseDouble(df.format((energy.get(i)-yony_data.get(j))/yony_data.get(j)*100)));
                        }
                        else {
                            yony.add(0.0);
                        }
                        j++;
                    }
                    else {
                        yony.add(0.0);
                    }

                }
                app_EnergyChart.setYony(yony);
            }
            else{
                app_EnergyChart.setYony(yony);
            }
        }

        return app_EnergyChart;
    }

    private double getPeopleArea(App_EnergySearch app_energySearch){
        String sql=null;
        Object[] args={app_energySearch.getModelid()};
        if(app_energySearch.getCaltype().equals("total")){
            return 1;
        }
        else
        if (app_energySearch.getModel().equals("build")) {
            if(app_energySearch.getModellevel().equals("school")){
				sql="select sum(f_people) as people, sum(f_area) as area from t_bd_room where 1=1 or f_id=?";
            }
			else
            if(app_energySearch.getModellevel().equals("group")){
                sql="select f_people as people, f_area as area from T_BD_GROUP  where f_buildgroupid=? ";
            }
            else
            if (app_energySearch.getModellevel().equals("build")) {
                sql="select f_people as people, f_totalarea as area from T_BD_BUILD where f_buildid=?";
            }
            else
            if (app_energySearch.getModellevel().equals("floor")) {
                sql="select f_people as people, f_area as area from t_bd_floor where f_id=?";
            } else {
                sql="select f_people as people, f_area as area from t_bd_room where f_id=?";
            }
			//TsengTest
			System.out.println(app_energySearch.getModellevel()+'\n'+sql);
        }
		else
		if (app_energySearch.getModel().equals("organ")) {//Tseng
			if(app_energySearch.getModellevel().equals("school")){
				sql="select sum(f_people) as people, sum(f_area) as area from t_bd_room where 1=1 or f_id=?";
			}
			else
			if(app_energySearch.getModellevel().equals("first")){
				sql="select f_people as people, f_area as area from t_bo_organ where f_id=?";
			}
			else
			if (app_energySearch.getModellevel().equals("second")) {
				sql="select f_people as people, f_area as area from t_bo_organ where f_id=?";
			}
			else
			if (app_energySearch.getModellevel().equals("third")) {
				sql="select f_people as people, f_area as area from t_bo_organ where f_id=?";
			} else {
				sql="select f_people as people, f_area as area from t_bo_organ where f_id=?";
			}
		}
        else{
            return 1.0;
            //sql="select sum(f_people) as people, sum(f_area) as area from t_bo_organ a,t_rr_organbuildrelation b,t_bd_room c where a.f_id=? and a.f_id=b.f_organid and b.f_id=c.f_id";
        }
        final List<Double> pora = new ArrayList<Double>();
        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pora.add(rs.getDouble("people"));
                pora.add(rs.getDouble("area"));
            }
        });
        if(app_energySearch.getModellevel().equals("school")){
            if (app_energySearch.getCaltype().equals("people")) {
                return 16000;
            } else {
                return 419500;
            }
        }
        if (app_energySearch.getCaltype().equals("people")) {
            return pora.get(0)==0.0?1.0:pora.get(0);
        } else {
            return pora.get(1)==0.0?1.0:pora.get(1);
        }
    }
    public App_EnergyChart App_GetFuncEnergy(App_EnergySearch app_energySearch){
        String sql="select f_name,sum(F_VALUE) as value from T_BD_BUILDTYPE c,T_BD_BUILD a,T_EC_BUILD_YEAR b where a.F_BUILDID=b.F_BUILDID and a.F_BUILDFUNC=c.F_CODE and substr(b.F_ENERGYITEMCODE,1,2) = '01' group by F_NAME ORDER BY value";
        final List<String> label=new ArrayList<String>();
        final List<Double> energy=new ArrayList<Double>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                label.add(rs.getString(1));
                energy.add(rs.getDouble(2));
            }
        });
        App_EnergyChart app_energyChart = new App_EnergyChart();
        app_energyChart.setDatatime(label);
        app_energyChart.setEnergy(energy);
        return app_energyChart;
    }
}
