package com.wintoo.dao;

import com.wintoo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class EnergyDao {
	@Autowired
	@Qualifier("primaryJdbcTemplate")
	private JdbcOperations jdbcTemplate;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
	private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM");
	private DecimalFormat df = new DecimalFormat("#.00");
	
	public List<Tree> getAllEnergyTypes() {
		String sql = "select F_EnergyItemCode,F_EnergyItemName,F_ParentItemCode from T_DT_ENERGYITEMDICT";
		final List<Tree> energyTypes = new ArrayList<Tree>();
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Tree energyType = new Tree();
				energyType.setId(rs.getString(1));
				energyType.setText(rs.getString(2));
				energyType.setParent(rs.getString(3).trim());
				energyTypes.add(energyType);
			}
		});
		return energyTypes;
	}	
	
	public List<Tree> getElectricEnergyTypes() {
		String sql = "select F_EnergyItemCode,F_EnergyItemName,F_ParentItemCode from T_DT_ENERGYITEMDICT where F_EnergyItemCode like '01%'";
		final List<Tree> energyTypes = new ArrayList<Tree>();
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Tree energyType = new Tree();
				energyType.setId(rs.getString(1));
				energyType.setText(rs.getString(2));
				energyType.setParent(rs.getString(3).trim());
				energyTypes.add(energyType);
			}
		});
		return energyTypes;
	}
	
	public List<Tree> getWaterEnergyTypes() {
		String sql = "select F_EnergyItemCode,F_EnergyItemName,F_ParentItemCode from T_DT_ENERGYITEMDICT where F_EnergyItemCode like '02%'";
		final List<Tree> energyTypes = new ArrayList<Tree>();
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Tree energyType = new Tree();
				energyType.setId(rs.getString(1));
				energyType.setText(rs.getString(2));
				energyType.setParent(rs.getString(3).trim());
				energyTypes.add(energyType);
			}
		});
		return energyTypes;
	}

	public EnergyChart getEnergy(EnergySearch energySearch){
		Energy energy= new Energy();
		String sql=null;
        Object[] args=null;
		if(energySearch.getModel().equals("build")){
			if(energySearch.getBasetime().equals("minutes")){
				sql="(select F_VALUE,to_char(F_STARTTIME,'hh24:mi') as time FROM T_EC_BUILD_15 WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd')) "+
                        "UNION (select F_VALUE,to_char(F_STARTTIME,'hh24:mi') as time FROM T_EC_BUILD_15_BUFFER WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd')) ORDER BY time";
                args=new Object[]{energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate(),energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate()};
            }
			else
				if(energySearch.getBasetime().equals("hour")){
					sql="(select F_VALUE,to_char(F_STARTTIME,'hh24:mi') as hour FROM T_EC_BUILD_HOUR WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd')) "+
                            "UNION (select F_VALUE,to_char(F_STARTTIME,'hh24:mi') as hour FROM T_EC_BUILD_HOUR_BUFFER WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd')) ORDER BY hour ";
                    args=new Object[]{energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate(),energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate()};
                }
				else
					if(energySearch.getBasetime().equals("day")){
						sql="(select F_VALUE,to_char(F_STARTTIME,'yyyy/mm/dd') as day FROM T_EC_BUILD_DAY WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd')) "+
                                "UNION (select F_VALUE,to_char(F_STARTTIME,'yyyy/mm/dd') as day FROM T_EC_BUILD_DAY_BUFFER WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd')) ORDER BY day ";
                        args=new Object[]{energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate(),energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate()};
                    }
					else
						if(energySearch.getBasetime().equals("month")){
							sql="select F_VALUE,to_char(F_STARTTIME,'yyyy/mm') as month FROM T_EC_BUILD_MON WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd') order by month";
                            args=new Object[]{energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate()};
                        }
						else
							if(energySearch.getBasetime().equals("year")){
								sql="select F_VALUE,to_char(F_STARTTIME,'yyyy') as year FROM T_EC_BUILD_YEAR WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd') order by year";
                                args=new Object[]{energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate()};
                            }
		}
		else{
			if(energySearch.getBasetime().equals("minutes")){
				sql="(select F_VALUE,to_char(F_STARTTIME,'hh24:mi') as time FROM T_EC_ORGAN_15 WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd')) " +
                        "UNION (select F_VALUE,to_char(F_STARTTIME,'hh24:mi') as time FROM T_EC_ORGAN_15_BUFFER WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd')) ORDER BY time";
                args=new Object[]{energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate(),energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate()};
            }
			else
				if(energySearch.getBasetime().equals("hour")){
					sql="(select F_VALUE,to_char(F_STARTTIME,'hh24:mi') as hour FROM T_EC_ORGAN_HOUR WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd')) " +
                            "UNION (select F_VALUE,to_char(F_STARTTIME,'hh24:mi') as hour FROM T_EC_ORGAN_HOUR_BUFFER WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd')) ORDER BY hour";
                    args=new Object[]{energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate(),energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate()};
                }
				else
					if(energySearch.getBasetime().equals("day")){
						sql="(select F_VALUE,to_char(F_STARTTIME,'yyyy/mm/dd') as day FROM T_EC_ORGAN_DAY WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd'))" +
                                "UNION (select F_VALUE,to_char(F_STARTTIME,'yyyy/mm/dd') as day FROM T_EC_ORGAN_DAY_BUFFER WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd')) ORDER BY day";
                        args=new Object[]{energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate(),energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate()};
                    }
					else
						if(energySearch.getBasetime().equals("month")){
							sql="select F_VALUE,to_char(F_STARTTIME,'yyyy/mm') as month FROM T_EC_ORGAN_MON WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<to_date(?,'yyyy/mm/dd') order by month";
                            args=new Object[]{energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate()};
                        }
						else
							if(energySearch.getBasetime().equals("year")){
								sql="select F_VALUE,to_char(F_STARTTIME,'yyyy') as year FROM T_EC_ORGAN_YEAR WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND F_STARTTIME>=to_date(?,'yyyy/mm/dd') AND F_STARTTIME<=to_date(?,'yyyy/mm/dd') order by year";
                                args=new Object[]{energySearch.getModelid(),energySearch.getEnergytypeid(),energySearch.getStartdate(),energySearch.getEnddate()};
                            }
		}
		//energy.setName(energySearch.getEnergytype());
		//final double pora=getPeopleArea(energySearch);
        final double pora=0;
		final List<Double> data=new ArrayList<Double>();
		final List<String> categories=new ArrayList<String>();
		jdbcTemplate.query(sql,args, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				data.add(rs.getDouble(1));
				categories.add(rs.getString(2));
			}
		});
		energy.setData(data);
		Energy cEnergy=new Energy();
		//cEnergy.setName(energySearch.getEnergytype());
		cEnergy.setData(getChainData(data));
//		final List<Double> lastdata=new ArrayList<Double>();
//		Calendar start = Calendar.getInstance();
//		start.setTime(energySearch.getStartdate());
//		start.add(Calendar.YEAR, -1);
//		Calendar end = Calendar.getInstance();
//		end.setTime(energySearch.getStartdate());
//		end.add(Calendar.YEAR, -1);
////		args=new Object[]{"1234567890","abcde",sdf.format(start.getTime()),sdf.format(end.getTime())};
//		args=new Object[]{energySearch.getModelid(),energySearch.getEnergytypeid(),sdf.format(energySearch.getStartdate()),sdf.format(energySearch.getEnddate())};
//		jdbcTemplate.query(sql,args, new RowCallbackHandler() {
//			@Override
//			public void processRow(ResultSet rs) throws SQLException {
//				lastdata.add(Double.parseDouble(df.format(rs.getDouble(1)/(pora==0?1:pora))));
//			}
//		});
//		Energy yEnergy=new Energy();
//		yEnergy.setName(energySearch.getEnergytype());
//		yEnergy.setData(getYonyData(data,lastdata));
		EnergyChart eChart=new EnergyChart();
		eChart.setEnergy(energy);
		eChart.setCategories(categories);
		eChart.setChainenergy(cEnergy);
//		eChart.setYonyenergy(yEnergy);
		return eChart;
	}
	List<Double> getChainData(List<Double> data){
		List<Double> chaindata=new ArrayList<Double>();
		chaindata.add(0.0);
		for (int i = 1; i < data.size(); i++) {
			if(data.get(i-1)!=0.0)
				chaindata.add(Double.parseDouble(df.format((data.get(i)-data.get(i-1))/data.get(i-1)*100)));
			else {
				chaindata.add(0.0);
			}
		}
		return chaindata;
	}
	List<Double> getYonyData(List<Double> data,List<Double> lastdata){
		if(data.size()!=lastdata.size()) return null;
		List<Double> yonydata=new ArrayList<Double>();
		for (int i = 0; i < data.size(); i++) {
			if(lastdata.get(i)!=0.0)
				yonydata.add(Double.parseDouble(df.format((data.get(i)-lastdata.get(i))/lastdata.get(i)*100)));
			else {
				yonydata.add(0.0);
			}
		}
		return yonydata;
	}
	private double getPeopleArea(EnergySearch energySearch){
		String sql=null;
		Object[] args={energySearch.getModelid()};
		if(energySearch.getCaltype().equals("total")){
			return 1;
		}
		else
			if (energySearch.getModel().equals("build")) {
                if(energySearch.getModellevel().equals("school")){
                    sql="select sum(f_num) as people, sum(f_area) as area from t_bd_room ";
                }
				if(energySearch.getModellevel().equals("group")){
					sql="select sum(f_num) as people, sum(f_area) as area from T_BD_GROUP a,T_BD_GROUPBUILDRELA b,T_BD_BUILD c,t_bd_floor d,t_bd_room e where a.f_buildgroupid=? and a.f_buildgroupid=b.f_buildgroupid and b.f_buildid=c.f_buildid and c.f_buildid=d.f_buildid and d.f_id=e.f_floorid";
				}
				else 
					if (energySearch.getModellevel().equals("build")) {
						sql="select sum(f_num) as people, sum(f_area) as area from T_BD_BUILD a,t_bd_floor b,t_bd_room c where a.f_buildid=?  and a.f_buildid=b.f_buildid and b.f_id=c.f_floorid";
					}
					else 
						if (energySearch.getModellevel().equals("floor")) {
							sql="select sum(f_num) as people, sum(f_area) as area from t_bd_floor a,t_bd_room b where a.f_id=?  and a.f_id=b.f_floorid";
						} else {
							sql="select sum(f_num) as people, sum(f_area) as area from t_bd_room where f_id=?";
						}
			}
			else{
				sql="select sum(f_num) as people, sum(f_area) as area from t_bo_organ a,t_rr_organbuildrelation b,t_bd_room c where a.f_id=? and a.f_id=b.f_organid and b.f_roomid=c.f_id";
			}
		final List<Double> pora = new ArrayList<Double>();
		jdbcTemplate.query(sql,args, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				pora.add(rs.getDouble("people"));
				pora.add(rs.getDouble("area"));
			}
		});
		if (energySearch.getCaltype().equals("people")) {
			return pora.get(0)==0.0?1.0:pora.get(0);
		} else {
			return pora.get(1)==0.0?1.0:pora.get(1);
		}
	}
    public EnergyChart getWaterEnergyMap(EnergySearch energySearch){
        Energy energy= new Energy();
        String sql=null;
        Object[] args=null;

        if(energySearch.getBasetime().equals("day")){
            sql="select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy-mm-dd') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE =? AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy-mm-dd') ORDER BY time";
        }
        else
        if(energySearch.getBasetime().equals("month")){
            sql="select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy-mm') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE =? AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy-mm') ORDER BY time";
        }
        else
        if(energySearch.getBasetime().equals("year")){
            sql="select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE =? AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy') ORDER BY time";
        }
        args=new Object[]{energySearch.getModelid(),energySearch.getStartdate(),energySearch.getEnddate()};

        energy.setName(energySearch.getEnergytype());
        final List<Double> data=new ArrayList<Double>();
        final List<String> categories=new ArrayList<String>();
        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                data.add(Double.parseDouble(df.format(rs.getDouble(1))));
                categories.add(rs.getString(2));
            }
        });
        energy.setData(data);
        EnergyChart eChart=new EnergyChart();
        eChart.setEnergy(energy);
        eChart.setCategories(categories);
        return eChart;
    }
    public EnergyChart getWaterEnergy(EnergySearch energySearch){
        Energy energy= new Energy();
        String sql=null;
        Object[] args=null;
        if(energySearch.getModelid().equals("allofsumgroup")){
            if(energySearch.getBasetime().equals("minutes")){
                sql="select sum(active),time from (select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN ('c0c88ae8-2291-4414-925e-30574e3711c3','c07e00c4-8363-4ba0-b042-eaa7fd1dcb01','54ed57a4-0645-4572-ab69-911953c40561','d29add02-def7-4f86-8fdf-5730fd80ca74')  AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'hh24:mi') "
                +"UNION select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN ('c0c88ae8-2291-4414-925e-30574e3711c3','c07e00c4-8363-4ba0-b042-eaa7fd1dcb01','54ed57a4-0645-4572-ab69-911953c40561','d29add02-def7-4f86-8fdf-5730fd80ca74') AND F_TYPE=1  AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'hh24:mi')) group by time order by time";
            }
            else
            if(energySearch.getBasetime().equals("hour")){
                sql="select sum(active),time from (select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'hh24') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN ('c0c88ae8-2291-4414-925e-30574e3711c3','c07e00c4-8363-4ba0-b042-eaa7fd1dcb01','54ed57a4-0645-4572-ab69-911953c40561','d29add02-def7-4f86-8fdf-5730fd80ca74') AND F_TYPE=1  AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'hh24') "
                        +"UNION select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'hh24') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN ('c0c88ae8-2291-4414-925e-30574e3711c3','c07e00c4-8363-4ba0-b042-eaa7fd1dcb01','54ed57a4-0645-4572-ab69-911953c40561','d29add02-def7-4f86-8fdf-5730fd80ca74') AND F_TYPE=1  AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'hh24')) group by time ORDER BY time";
            }
            else
            if(energySearch.getBasetime().equals("day")){
                sql="select sum(active),time from (select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy-mm-dd') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN ('c0c88ae8-2291-4414-925e-30574e3711c3','c07e00c4-8363-4ba0-b042-eaa7fd1dcb01','54ed57a4-0645-4572-ab69-911953c40561','d29add02-def7-4f86-8fdf-5730fd80ca74') AND F_TYPE=1  AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy-mm-dd')"
                        +"UNION select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy-mm-dd') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN ('c0c88ae8-2291-4414-925e-30574e3711c3','c07e00c4-8363-4ba0-b042-eaa7fd1dcb01','54ed57a4-0645-4572-ab69-911953c40561','d29add02-def7-4f86-8fdf-5730fd80ca74') AND F_TYPE=1  AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy-mm-dd')) group by time ORDER BY time";
            }
            else
            if(energySearch.getBasetime().equals("month")){
                sql="select sum(active),time from (select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy-mm') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN ('c0c88ae8-2291-4414-925e-30574e3711c3','c07e00c4-8363-4ba0-b042-eaa7fd1dcb01','54ed57a4-0645-4572-ab69-911953c40561','d29add02-def7-4f86-8fdf-5730fd80ca74') AND F_TYPE=1  AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy-mm') "
                        +"UNION select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy-mm') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN ('c0c88ae8-2291-4414-925e-30574e3711c3','c07e00c4-8363-4ba0-b042-eaa7fd1dcb01','54ed57a4-0645-4572-ab69-911953c40561','d29add02-def7-4f86-8fdf-5730fd80ca74') AND F_TYPE=1  AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy-mm')) group by time ORDER BY time";
            }
            else
            if(energySearch.getBasetime().equals("year")){
                sql="select sum(active),time from (select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN ('c0c88ae8-2291-4414-925e-30574e3711c3','c07e00c4-8363-4ba0-b042-eaa7fd1dcb01','54ed57a4-0645-4572-ab69-911953c40561','d29add02-def7-4f86-8fdf-5730fd80ca74') AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy') "
                        +"UNION select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN ('c0c88ae8-2291-4414-925e-30574e3711c3','c07e00c4-8363-4ba0-b042-eaa7fd1dcb01','54ed57a4-0645-4572-ab69-911953c40561','d29add02-def7-4f86-8fdf-5730fd80ca74') AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy')) group by time ORDER BY time";
            }
            args=new Object[]{sdf.format(energySearch.getStartdate()),sdf.format(energySearch.getEnddate()),sdf.format(energySearch.getStartdate()),sdf.format(energySearch.getEnddate())};
        }
        else{
            if(energySearch.getBasetime().equals("minutes")){
                sql="select sum(active),time from (select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN (select f_devicecode from t_rr_devicerelation where f_buildgroupid=? or f_buildcode=? or f_floorid=? or f_roomid=? and f_energyitemcode like '02%') AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'hh24:mi')"
                        +"UNION select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN (select f_devicecode from t_rr_devicerelation where f_buildgroupid=? or f_buildcode=? or f_floorid=? or f_roomid=? and f_energyitemcode like '02%') AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'hh24:mi')) group by time order by time";
            }
            else
            if(energySearch.getBasetime().equals("hour")){
                sql="select sum(active),time from (select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'hh24') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN (select f_devicecode from t_rr_devicerelation where f_buildgroupid=? or f_buildcode=? or f_floorid=? or f_roomid=? and f_energyitemcode like '02%') AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'hh24') "
                        +"UNION select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'hh24') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN (select f_devicecode from t_rr_devicerelation where f_buildgroupid=? or f_buildcode=? or f_floorid=? or f_roomid=? and f_energyitemcode like '02%') AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'hh24')) group by time ORDER BY time";
            }
            else
            if(energySearch.getBasetime().equals("day")){
                sql="select sum(active),time from (select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy-mm-dd') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN (select f_devicecode from t_rr_devicerelation where f_buildgroupid=? or f_buildcode=? or f_floorid=? or f_roomid=? and f_energyitemcode like '02%') AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy-mm-dd')"
                        +"UNION select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy-mm-dd') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN (select f_devicecode from t_rr_devicerelation where f_buildgroupid=? or f_buildcode=? or f_floorid=? or f_roomid=? and f_energyitemcode like '02%') AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy-mm-dd')) group by time ORDER BY time";
            }
            else
            if(energySearch.getBasetime().equals("month")){
                sql="select sum(active),time from (select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy-mm') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN (select f_devicecode from t_rr_devicerelation where f_buildgroupid=? or f_buildcode=? or f_floorid=? or f_roomid=? and f_energyitemcode like '02%') AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy-mm') "
                        +"UNION select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy-mm') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN (select f_devicecode from t_rr_devicerelation where f_buildgroupid=? or f_buildcode=? or f_floorid=? or f_roomid=? and f_energyitemcode like '02%') AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy-mm')) group by time ORDER BY time";
            }
            else
            if(energySearch.getBasetime().equals("year")){
                sql="select sum(active),time from (select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN (select f_devicecode from t_rr_devicerelation where f_buildgroupid=? or f_buildcode=? or f_floorid=? or f_roomid=? and f_energyitemcode like '02%') AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy') "
                        +"UNION select sum(F_TIME_INTERVEL_ACTIVE) as active,to_char(F_DATATIME,'yyyy') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN (select f_devicecode from t_rr_devicerelation where f_buildgroupid=? or f_buildcode=? or f_floorid=? or f_roomid=? and f_energyitemcode like '02%') AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy')) group by time ORDER BY time";
            }
            args=new Object[]{energySearch.getModelid(),energySearch.getModelid(),energySearch.getModelid(),energySearch.getModelid(),sdf.format(energySearch.getStartdate()),sdf.format(energySearch.getEnddate()),energySearch.getModelid(),energySearch.getModelid(),energySearch.getModelid(),energySearch.getModelid(),sdf.format(energySearch.getStartdate()),sdf.format(energySearch.getEnddate())};
        }
        energy.setName(energySearch.getEnergytype());
        final List<Double> data=new ArrayList<Double>();
        final List<String> categories=new ArrayList<String>();
        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                data.add(Double.parseDouble(df.format(rs.getDouble(1))));
                categories.add(rs.getString(2));
            }
        });
        energy.setData(data);
        EnergyChart eChart=new EnergyChart();
        eChart.setEnergy(energy);
        eChart.setCategories(categories);
        return eChart;
    }
	public List<EnergyTable> getEnergyTable(EnergySearch energySearch){
		String[] strs=energySearch.getModelid().split(";");
		List<EnergyTable> leTables=new ArrayList<EnergyTable>();
		for (String item : strs) {
			System.out.println(item);
			if(!item.equals("")){
				String[] id=item.split(",");
				energySearch.setModelid(id[0]);
				EnergyTable eTable=new EnergyTable();
				eTable.setName(id[1]);
				eTable.setData(getEnergyItem(energySearch));
				leTables.add(eTable);
			}
		}
		return leTables;
	}
	public List<Double> getEnergyItem(EnergySearch energySearch){

		String sql=null;
//		Object[] args={"1234567890","abcde",sdf.format(energySearch.getStartdate()),sdf.format(energySearch.getEnddate())};
		Object[] args={energySearch.getModelid(),energySearch.getEnergytypeid(),df.format(energySearch.getStartdate()),df.format(energySearch.getEnddate())};
		if(energySearch.getCaltype().equals("common")){
			if(energySearch.getModel().equals("build")){
				if(energySearch.getBasetime().equals("minutes")){
					sql="select F_VALUE,to_char(F_ENDTIME,'hh24:mi') as time FROM T_EC_ENERGYITEMRESULT WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTTIME,'yyyy-mm-dd')=? AND to_char(F_STARTTIME,'yyyy-mm-dd')=? ORDER BY F_STARTTIME";
				}
				else
					if(energySearch.getBasetime().equals("hour")){
						sql="select F_HOURVALUE,to_char(F_ENDHOUR,'hh24:mi') as hour FROM T_EC_ENERGYITEMHOURRESULT WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTHOUR,'yyyy-mm-dd')=? AND to_char(F_STARTHOUR,'yyyy-mm-dd')=? ORDER BY hour";
					}
					else
						if(energySearch.getBasetime().equals("day")){
							sql="select F_DAYVALUE,to_char(F_ENDDAY,'yyyy-mm-dd') as day FROM T_EC_ENERGYITEMDAYRESULT WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTDAY,'yyyy-mm-dd')>=? AND to_char(F_STARTDAY,'yyyy-mm-dd')<=? ORDER BY day";
						}
						else
							if(energySearch.getBasetime().equals("month")){
								sql="select sum(f_dayvalue),to_char(F_ENDDAY,'yyyy-mm') as month FROM T_EC_ENERGYITEMDAYRESULT WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTDAY,'yyyy-mm-dd')>=? AND to_char(F_STARTDAY,'yyyy-mm-dd')<=? group by to_char(F_STARTDAY,'yyyy-mm') order by month";
							}
							else
								if(energySearch.getBasetime().equals("year")){
									sql="select sum(f_dayvalue),to_char(F_ENDDAY,'yyyy') as year FROM T_EC_ENERGYITEMDAYRESULT WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTDAY,'yyyy-mm-dd')>=? AND to_char(F_STARTDAY,'yyyy-mm-dd')<=? group by to_char(F_STARTDAY,'yyyy') order by year";
								}
			}
			else{
				if(energySearch.getBasetime().equals("minutes")){
					sql="select F_VALUE,to_char(F_ENDTIME,'hh24:mi') as time FROM T_EC_ORGANENERGYITEMRESULT WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTTIME,'yyyy-mm-dd')=? AND to_char(F_STARTTIME,'yyyy-mm-dd')=? ORDER BY time";
				}
				else
					if(energySearch.getBasetime().equals("hour")){
						sql="select F_HOURVALUE,to_char(F_ENDHOUR,'hh24:mi') as hour FROM T_EC_ORGANENERGYITEMHOURRESULT WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTHOUR,'yyyy-mm-dd')=? AND to_char(F_STARTHOUR,'yyyy-mm-dd')=? ORDER BY hour";
					}
					else
						if(energySearch.getBasetime().equals("day")){
							sql="select F_DAYVALUE,to_char(F_ENDDAY,'yyyy-mm-dd') as day FROM T_EC_ORGANENERGYITEMDAYRESULT WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTDAY,'yyyy-mm-dd')>=? AND to_char(F_STARTDAY,'yyyy-mm-dd')<=? ORDER BY day";
						}
						else
							if(energySearch.getBasetime().equals("month")){
								sql="select sum(f_dayvalue),to_char(F_ENDDAY,'yyyy-mm') as month FROM T_EC_ORGANENERGYITEMDAYRESULT WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTDAY,'yyyy-mm-dd')>=? AND to_char(F_STARTDAY,'yyyy-mm-dd')<=? group by to_char(F_STARTDAY,'yyyy-mm') order by month";
							}
							else
								if(energySearch.getBasetime().equals("year")){
									sql="select sum(f_dayvalue),to_char(F_ENDDAY,'yyyy') as year FROM T_EC_ORGANENERGYITEMDAYRESULT WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTDAY,'yyyy-mm-dd')>=? AND to_char(F_STARTDAY,'yyyy-mm-dd')<=? group by to_char(F_STARTDAY,'yyyy') order by year";
								}		
			}
		}
		else{
			if(energySearch.getModel().equals("build")){
				if(energySearch.getBasetime().equals("minutes")){
					sql="select F_EQUVALUE,to_char(F_ENDTIME,'hh24:mi') as time FROM T_EC_ENERGYITEMRESULT WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTTIME,'yyyy-mm-dd')=? AND to_char(F_STARTTIME,'yyyy-mm-dd')=? ORDER BY time";
				}
				else
					if(energySearch.getBasetime().equals("hour")){
						sql="select F_HOUREQUVALUE,to_char(F_ENDHOUR,'hh24:mi') as hour FROM T_EC_ENERGYITEMHOURRESULT WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTHOUR,'yyyy-mm-dd')=? AND to_char(F_STARTHOUR,'yyyy-mm-dd')=? ORDER BY hour";
					}
					else
						if(energySearch.getBasetime().equals("day")){
							sql="select F_DAYEQUVALUE,to_char(F_ENDDAY,'yyyy-mm-dd') as day FROM T_EC_ENERGYITEMDAYRESULT WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTDAY,'yyyy-mm-dd')>=? AND to_char(F_STARTDAY,'yyyy-mm-dd')<=? ORDER BY day";
						}
						else
							if(energySearch.getBasetime().equals("month")){
								sql="select sum(f_dayEQUvalue),to_char(F_ENDDAY,'yyyy-mm') as month FROM T_EC_ENERGYITEMDAYRESULT WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTDAY,'yyyy-mm-dd')>=? AND to_char(F_STARTDAY,'yyyy-mm-dd')<=? group by to_char(F_STARTDAY,'yyyy-mm') order by month";
							}
							else
								if(energySearch.getBasetime().equals("year")){
									sql="select sum(f_dayEQUvalue),to_char(F_ENDDAY,'yyyy') as year FROM T_EC_ENERGYITEMDAYRESULT WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTDAY,'yyyy-mm-dd')>=? AND to_char(F_STARTDAY,'yyyy-mm-dd')<=? group by to_char(F_STARTDAY,'yyyy') order by year";
								}
			}
			else{
				if(energySearch.getBasetime().equals("minutes")){
					sql="select F_EQUVALUE,to_char(F_ENDTIME,'hh24:mi') as time FROM T_EC_ORGANENERGYITEMRESULT WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTTIME,'yyyy-mm-dd')=? AND to_char(F_STARTTIME,'yyyy-mm-dd')=? ORDER BY time";
				}
				else
					if(energySearch.getBasetime().equals("hour")){
						sql="select F_HOUREQUVALUE,to_char(F_ENDHOUR,'hh24:mi') as hour FROM T_EC_ORGANENERGYITEMHOURRESULT WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTHOUR,'yyyy-mm-dd')=? AND to_char(F_STARTHOUR,'yyyy-mm-dd')=? ORDER BY hour";
					}
					else
						if(energySearch.getBasetime().equals("day")){
							sql="select F_DAYEQUVALUE,to_char(F_ENDDAY,'yyyy-mm-dd') as day FROM T_EC_ORGANENERGYITEMDAYRESULT WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTDAY,'yyyy-mm-dd')>=? AND to_char(F_STARTDAY,'yyyy-mm-dd')<=? ORDER BY day";
						}
						else
							if(energySearch.getBasetime().equals("month")){
								sql="select sum(f_dayEQUvalue),to_char(F_ENDDAY,'yyyy-mm') as month FROM T_EC_ORGANENERGYITEMDAYRESULT WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTDAY,'yyyy-mm-dd')>=? AND to_char(F_STARTDAY,'yyyy-mm-dd')<=? group by to_char(F_STARTDAY,'yyyy-mm') order by month";
							}
							else
								if(energySearch.getBasetime().equals("year")){
									sql="select sum(f_dayEQUvalue),to_char(F_ENDDAY,'yyyy') as year FROM T_EC_ORGANENERGYITEMDAYRESULT WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTDAY,'yyyy-mm-dd')>=? AND to_char(F_STARTDAY,'yyyy-mm-dd')<=? group by to_char(F_STARTDAY,'yyyy') order by year";
								}		
			}
		}
		final List<Double> value=new ArrayList<Double>();
		jdbcTemplate.query(sql,args, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				value.add(Double.parseDouble(df.format(rs.getDouble(1))));
			}
		});
		if(value.size()>0){
			double sum=0;
			for (Double val : value) {
				sum+=val;
			}
			value.add(Double.parseDouble(df.format(sum)));
		}
		return value;
	}
	public double getDayEnergy(String ids){
		String[] id=ids.split(",");
		String sql="select sum(F_EQUVALUE) FROM T_EC_BUILD_DAY WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTTIME,'yyyy-mm-dd')=?";
		Object[] args={id[0],id[1],sdf.format(new Date())};
		double sum=0;
		try {
			sum=jdbcTemplate.queryForObject(sql, args, Double.class);
		} catch (Exception e) {
		}
		return sum;
	}
	public double getMonthEnergy(String ids){
        String[] id=ids.split(",");
        String sql="select sum(F_EQUVALUE) FROM T_EC_BUILD_MON WHERE F_BUILDID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTTIME,'yyyy-mm')=?";
        Object[] args={id[0],id[1],sdf1.format(new Date())};
        double sum=0;
        try {
            sum=jdbcTemplate.queryForObject(sql, args, Double.class);
        } catch (Exception e) {
        }
        return sum;
    }
    public double getOrganMonthEnergy(String ids){
        String[] id=ids.split(",");
        String sql="select sum(F_EQUVALUE) FROM T_EC_ORGAN_MON WHERE F_ORGANID=? AND F_ENERGYITEMCODE=? AND to_char(F_STARTTIME,'yyyy-mm')=?";
        Object[] args={id[0],id[1],sdf1.format(new Date())};
        double sum=0;
        try {
            sum=jdbcTemplate.queryForObject(sql, args, Double.class);
        } catch (Exception e) {
        }
        return sum;
    }
    public double getWaterMonthEnergy(String ids){
        String[] id=ids.split(",");

        String sql="select sum(F_TIME_INTERVEL_ACTIVE) FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE=?  AND F_DATATIME>=to_date(?,'yyyy-mm')";
        Object[] args={id[0],id[1]};
        double sum=0;
        try {
            sum=jdbcTemplate.queryForObject(sql, args, Double.class);
        } catch (Exception e) {
        }
        return sum;
    }
    private int getEquipNum(String uuid,int type){
        String sql="select count(*) from T_EE_NETEQUIP WHERE F_WATERNET=? AND F_TYPE=?";
        Object[] args={uuid,type};
        return (int)jdbcTemplate.queryForMap(sql,args).get("num");
    }
/*    private double getleak(String uuid){

    }*/
    public DataTable getAllWaternets() {
        String sql = "select F_UUID,F_CODE,F_NAME,F_LEVEL from T_EE_WATERNET ORDER BY F_CODE";
        DataTable dataTable = new DataTable();
        final List<Waternet> waternets = new ArrayList<Waternet>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Waternet waternet = new Waternet();
                waternet.setUuid(rs.getString(1));
                waternet.setCode(rs.getString(2));
                waternet.setName(rs.getString(3));
                waternet.setLevel(rs.getString(4));
                waternet.setPronum(getEquipNum(waternet.getUuid(),1));
                waternet.setConnum(getEquipNum(waternet.getUuid(), 2));

                waternets.add(waternet);
            }
        });
        dataTable.setData(waternets);
        return dataTable;
    }

    public void addWaternet(Waternet waternet) {
        String sql = "insert into T_EE_WATERNET(F_UUID,F_CODE,F_NAME,F_LEVEL) values(sys_guid(),?,?,?)";
        Object[] args = { waternet.getCode(),waternet.getName(), waternet.getLevel()};
        jdbcTemplate.update(sql, args);
    }

    public void deleteWaternet(String id) {
        String sql = "delete from T_EE_WATERNET where F_UUID=?";
        Object[] args = { id };
        jdbcTemplate.update(sql, args);
    }

    public void updateWaternet(Waternet waternet) {
        String sql = "update T_EE_WATERNET set F_CODE=?,F_NAME=?,F_LEVEL=? where F_UUID=?";
        Object[] args = { waternet.getCode(),waternet.getName(),waternet.getLevel(),waternet.getUuid()};
        jdbcTemplate.update(sql, args);
    }
    public List<EquipList> getProduce(String id){
        String sql="select NVL(NVL(gateway1,gateway2),'未配置') as gateway, g.F_UUID,a.F_EQUIPID,a.F_BATCHID,a.F_TYPEID,f.F_BATCH,b.F_TYPE,b.F_SUBTYPE,f.F_MODEL,a.F_INSTALLTYPE,t.F_BUILDGROUPNAME,c.F_BUILDNAME,d.F_NAME AS D_F_NAME,e.F_NAME AS E_F_NAME,a.F_LONGITUDE,a.F_LATITUDE,a.F_REMARK "+
                "from T_BD_GROUPBUILDRELA h,T_BD_GROUP t,T_BE_EQUIPMENTLIST a,T_BE_EQUIPMENTTYPE b,T_BD_BUILD c,T_BD_FLOOR d,T_BD_ROOM e ,T_BE_EQUIPMENTBATCH f,T_EE_NETEQUIP g,"+
                "(select t2.f_address||'-'||t1.f_use||'-'||lpad(t1.f_pn,3,'0') as gateway1,t3.f_uuid as uuid1 from t_be_ammeter t1,t_be_gateway t2,T_BE_EQUIPMENTLIST t3 where t3.f_uuid=t1.f_uuid and t1.f_gateways_uuid=t2.f_uuid),"+
                "(select t2.f_address||'-'||t1.f_use||'-'||lpad(t1.f_pn,3,'0') as gateway2,t3.f_uuid as uuid2 from t_be_watermeter t1,t_be_gateway t2,T_BE_EQUIPMENTLIST t3 where t3.f_uuid=t1.f_uuid and t1.f_gateways_uuid=t2.f_uuid) "+
                "where a.f_uuid=uuid1(+) and a.f_uuid=uuid2(+) and a.F_BATCHID=f.F_UUID AND a.F_TYPEID=b.F_UUID AND a.F_BUILDID=c.F_BUILDID(+) AND a.F_FLOORID=d.F_ID(+) AND a.F_ROOMID=e.F_ID(+) AND h.F_BUILDID(+)=a.F_BUILDID AND t.F_BUILDGROUPID(+)=h.F_BUILDGROUPID AND g.F_EQUIPID=a.F_UUID AND g.F_WATERNET=? AND g.F_TYPE=1";
        Object[] args = { id };
        final List<EquipList> equips=new ArrayList<EquipList>();
        jdbcTemplate.query(sql, args,new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                EquipList equipList=new EquipList();
                equipList.setUuid(rs.getString("F_UUID"));
                equipList.setEquipid(rs.getString("F_EQUIPID"));
                equipList.setBatch(rs.getString("F_BATCH"));
                equipList.setBatchid(rs.getString("F_BATCHID"));
                equipList.setTypeid(rs.getString("F_TYPEID"));
                equipList.setType(rs.getString("F_TYPE"));
                equipList.setSubtype(rs.getString("F_SUBTYPE"));
                equipList.setModel(rs.getString("F_MODEL"));
                equipList.setInstalltype(rs.getString("F_INSTALLTYPE"));
                equipList.setGroupid(rs.getString("F_BUILDGROUPNAME"));
                equipList.setBuildid(rs.getString("F_BUILDNAME"));
                equipList.setFloorid(rs.getString("D_F_NAME"));
                equipList.setRoomid(rs.getString("E_F_NAME"));
                equipList.setLongitude(rs.getDouble("F_LONGITUDE"));
                equipList.setLatitude(rs.getDouble("F_LATITUDE"));
                equipList.setRemark(rs.getString("F_REMARK"));
                equipList.setParas(rs.getString("gateway"));
                equips.add(equipList);
            }
        });
        return equips;

    }

    public void addProduce(String ids) {
        final String[] name=ids.split(":");
        final String[] equips=name[1].split(",");
        String sql="insert into T_EE_NETEQUIP(F_UUID,F_WATERNET,F_TYPE,F_EQUIPID) values(sys_guid(),?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter(){
            @Override
            public void setValues(PreparedStatement ps,int i)throws SQLException{
                ps.setString(1, name[0]);
                ps.setInt(2, 1);
                ps.setString(3, equips[i]);
            }
            @Override
            public int getBatchSize(){
                return equips.length;
            }
        });
    }

    public void deleteProduce(String id) {
        String sql = "delete from T_EE_NETEQUIP where F_UUID=?";
        Object[] args = { id };
        jdbcTemplate.update(sql, args);
    }
    public List<EquipList> getConsume(String id){
        String sql="select NVL(NVL(gateway1,gateway2),'未配置') as gateway, g.F_UUID,a.F_EQUIPID,a.F_BATCHID,a.F_TYPEID,f.F_BATCH,b.F_TYPE,b.F_SUBTYPE,f.F_MODEL,a.F_INSTALLTYPE,t.F_BUILDGROUPNAME,c.F_BUILDNAME,d.F_NAME AS D_F_NAME,e.F_NAME AS E_F_NAME,a.F_LONGITUDE,a.F_LATITUDE,a.F_REMARK "+
                "from T_BD_GROUPBUILDRELA h,T_BD_GROUP t,T_BE_EQUIPMENTLIST a,T_BE_EQUIPMENTTYPE b,T_BD_BUILD c,T_BD_FLOOR d,T_BD_ROOM e ,T_BE_EQUIPMENTBATCH f,T_EE_NETEQUIP g,"+
                "(select t2.f_address||'-'||t1.f_use||'-'||lpad(t1.f_pn,3,'0') as gateway1,t3.f_uuid as uuid1 from t_be_ammeter t1,t_be_gateway t2,T_BE_EQUIPMENTLIST t3 where t3.f_uuid=t1.f_uuid and t1.f_gateways_uuid=t2.f_uuid),"+
                "(select t2.f_address||'-'||t1.f_use||'-'||lpad(t1.f_pn,3,'0') as gateway2,t3.f_uuid as uuid2 from t_be_watermeter t1,t_be_gateway t2,T_BE_EQUIPMENTLIST t3 where t3.f_uuid=t1.f_uuid and t1.f_gateways_uuid=t2.f_uuid) "+
                "where a.f_uuid=uuid1(+) and a.f_uuid=uuid2(+) and a.F_BATCHID=f.F_UUID AND a.F_TYPEID=b.F_UUID AND a.F_BUILDID=c.F_BUILDID(+) AND a.F_FLOORID=d.F_ID(+) AND a.F_ROOMID=e.F_ID(+) AND h.F_BUILDID(+)=a.F_BUILDID AND t.F_BUILDGROUPID(+)=h.F_BUILDGROUPID AND g.F_EQUIPID=a.F_UUID AND g.F_WATERNET=? AND g.F_TYPE=2";
        Object[] args = { id };
        final List<EquipList> equips=new ArrayList<EquipList>();
        jdbcTemplate.query(sql, args,new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                EquipList equipList=new EquipList();
                equipList.setUuid(rs.getString("F_UUID"));
                equipList.setEquipid(rs.getString("F_EQUIPID"));
                equipList.setBatch(rs.getString("F_BATCH"));
                equipList.setBatchid(rs.getString("F_BATCHID"));
                equipList.setTypeid(rs.getString("F_TYPEID"));
                equipList.setType(rs.getString("F_TYPE"));
                equipList.setSubtype(rs.getString("F_SUBTYPE"));
                equipList.setModel(rs.getString("F_MODEL"));
                equipList.setInstalltype(rs.getString("F_INSTALLTYPE"));
                equipList.setGroupid(rs.getString("F_BUILDGROUPNAME"));
                equipList.setBuildid(rs.getString("F_BUILDNAME"));
                equipList.setFloorid(rs.getString("D_F_NAME"));
                equipList.setRoomid(rs.getString("E_F_NAME"));
                equipList.setLongitude(rs.getDouble("F_LONGITUDE"));
                equipList.setLatitude(rs.getDouble("F_LATITUDE"));
                equipList.setRemark(rs.getString("F_REMARK"));
                equipList.setParas(rs.getString("gateway"));
                equips.add(equipList);
            }
        });
        return equips;

    }
    public void addConsume(String ids) {
        final String[] name=ids.split(":");
        final String[] equips=name[1].split(",");
        String sql="insert into T_EE_NETEQUIP(F_UUID,F_WATERNET,F_TYPE,F_EQUIPID) values(sys_guid(),?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter(){
            @Override
            public void setValues(PreparedStatement ps,int i)throws SQLException{
                ps.setString(1, name[0]);
                ps.setInt(2, 2);
                ps.setString(3, equips[i]);
            }
            @Override
            public int getBatchSize(){
                return equips.length;
            }
        });
    }

    public void deleteConsume(String id) {
        String sql = "delete from T_EE_NETEQUIP where F_UUID=?";
        Object[] args = { id };
        jdbcTemplate.update(sql, args);
    }
/*
    public LeakChart getLeak(LeakSearch leakSearch){
        String sql=null;
        if(leakSearch.getBasetime().equals("minutes")){
            sql="(select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN (select f_equipid from t_ee_netequip where f_waternet=? and f_type=? ) AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'hh24:mi') ) "
                    +"UNION select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN (select f_equipid from t_ee_netequip where f_waternet=? and f_type=?) AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'hh24:mi') order by time";
        }
        else
        if(leakSearch.getBasetime().equals("hour")){
            sql="select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'hh24') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN (select f_equipid from t_ee_netequip where f_waternet=? and f_type=?) AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'hh24') "
                    +"UNION select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'hh24') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN (select f_equipid from t_ee_netequip where f_waternet=? and f_type=?) AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'hh24') ORDER BY time";
        }
        else
        if(leakSearch.getBasetime().equals("day")){
            sql="select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'yyyy-mm-dd') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN (select f_equipid from t_ee_netequip where f_waternet=? and f_type=?) AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy-mm-dd')"
                    +"UNION select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'yyyy-mm-dd') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN (select f_equipid from t_ee_netequip where f_waternet=? and f_type=?) AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy-mm-dd') ORDER BY time";
        }
        else
        if(leakSearch.getBasetime().equals("month")){
            sql="select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'yyyy-mm') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN (select f_equipid from t_ee_netequip where f_waternet=? and f_type=?) AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy-mm') "
                    +"UNION select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'yyyy-mm') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN (select f_equipid from t_ee_netequip where f_waternet=? and f_type=?) AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy-mm') ORDER BY time";
        }
        else
        if(leakSearch.getBasetime().equals("year")){
            sql="select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'yyyy') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE IN (select f_equipid from t_ee_netequip where f_waternet=? and f_type=?) AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy') "
                    +"UNION select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'yyyy') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE IN (select f_equipid from t_ee_netequip where f_waternet=? and f_type=?) AND F_TYPE=1 AND F_DATATIME>=to_date(?,'yyyy-mm-dd')  AND F_DATATIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_DATATIME,'yyyy') ORDER BY time";
        }
        System.out.println(sql);
        Object[] args={leakSearch.getNetid(),1,sdf.format(leakSearch.getStartdate()),sdf.format(leakSearch.getEnddate()),leakSearch.getNetid(),1,sdf.format(leakSearch.getStartdate()),sdf.format(leakSearch.getEnddate())};
        final List<Double> in=new ArrayList<Double>();
        final List<String> datatime=new ArrayList<String>();
        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                in.add(Double.parseDouble(df.format(rs.getDouble(1))));
                datatime.add(rs.getString(2));
            }
        });
        Object[] args1={leakSearch.getNetid(),2,sdf.format(leakSearch.getStartdate()),sdf.format(leakSearch.getEnddate()),leakSearch.getNetid(),2,sdf.format(leakSearch.getStartdate()),sdf.format(leakSearch.getEnddate())};
        final List<Double> out=new ArrayList<Double>();
        jdbcTemplate.query(sql,args1, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                out.add(Double.parseDouble(df.format(rs.getDouble(1))));
            }
        });
        List<Double> leak=new ArrayList<Double>();
        for(int i=0;i<in.size();i++){
            double s=in.get(i)-out.get(i);
            leak.add(Double.parseDouble(df.format(s)));
        }
        LeakChart leakChart=new LeakChart();
        leakChart.setTotalin(in);
        leakChart.setTotalout(out);
        leakChart.setLeak(leak);
        leakChart.setDatatime(datatime);
        return leakChart;
    }
*/
public LeakChart getLeak(LeakSearch leakSearch){
    String sql=null;
    if(leakSearch.getBasetime().equals("minutes")){
        sql="select sum(F_VALUE),to_char(F_TIME,'yyyy/mm/dd hh24:mi') as time FROM T_EE_LEAK WHERE F_WATERNET=? AND F_TYPE=? AND F_TIME>=to_date(?,'yyyy-mm-dd')  AND F_TIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_TIME,'yyyy/mm/dd hh24:mi') ORDER BY time";
    }
    else
    if(leakSearch.getBasetime().equals("hour")){
        sql="select sum(F_VALUE),to_char(F_TIME,'yyyy/mm/dd hh24')||':00' as time FROM T_EE_LEAK WHERE F_WATERNET=? AND F_TYPE=? AND F_TIME>=to_date(?,'yyyy-mm-dd')  AND F_TIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_TIME,'yyyy/mm/dd hh24') ORDER BY time";
    }
    else
    if(leakSearch.getBasetime().equals("day")){
        sql="select sum(F_VALUE),to_char(F_TIME,'yyyy/mm/dd') as time FROM T_EE_LEAK WHERE F_WATERNET=? AND F_TYPE=? AND F_TIME>=to_date(?,'yyyy-mm-dd')  AND F_TIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_TIME,'yyyy/mm/dd') ORDER BY time";
    }
    else
    if(leakSearch.getBasetime().equals("month")){
        sql="select sum(F_VALUE),to_char(F_TIME,'yyyy/mm') as time FROM T_EE_LEAK WHERE F_WATERNET=? AND F_TYPE=? AND F_TIME>=to_date(?,'yyyy-mm-dd')  AND F_TIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_TIME,'yyyy/mm') ORDER BY time";
    }
    else
    if(leakSearch.getBasetime().equals("year")){
        sql="select sum(F_VALUE),to_char(F_TIME,'yyyy') as time FROM T_EE_LEAK WHERE F_WATERNET=? AND F_TYPE=? AND F_TIME>=to_date(?,'yyyy-mm-dd')  AND F_TIME<to_date(?,'yyyy-mm-dd') GROUP BY to_char(F_TIME,'yyyy') ORDER BY time";
    }
    System.out.println(sql);
    Object[] args={leakSearch.getNetid(),1,sdf.format(leakSearch.getStartdate()),sdf.format(leakSearch.getEnddate())};
    final List<Double> in=new ArrayList<Double>();
    final List<String> datatime=new ArrayList<String>();
    jdbcTemplate.query(sql,args, new RowCallbackHandler() {
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            in.add(Double.parseDouble(df.format(rs.getDouble(1))));
            datatime.add(rs.getString(2));
        }
    });
    Object[] args1={leakSearch.getNetid(),2,sdf.format(leakSearch.getStartdate()),sdf.format(leakSearch.getEnddate())};
    final List<Double> out=new ArrayList<Double>();
    jdbcTemplate.query(sql,args1, new RowCallbackHandler() {
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            out.add(Double.parseDouble(df.format(rs.getDouble(1))));
        }
    });
    final List<Double> leak=new ArrayList();
    leak.add(10000000.0);
    leak.add(10000000.0);
    String sql2="select sum(F_VALUE),to_char(F_TIME,'hh24:mi') as time FROM T_EE_LEAK WHERE F_WATERNET=? AND F_TYPE=?  AND F_TIME>=to_date(?,'yyyy-mm-dd')  AND F_TIME<to_date(?,'yyyy-mm-dd')-18/24 GROUP BY to_char(F_TIME,'hh24:mi')";
    Object[] args2={leakSearch.getNetid(),1,sdf.format(leakSearch.getStartdate()),sdf.format(leakSearch.getEnddate())};
    jdbcTemplate.query(sql2,args2, new RowCallbackHandler() {
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            double value=rs.getDouble(1);
            if(leak.get(0)>value) leak.set(0,value);
        }
    });
    String sql3="select sum(F_VALUE),to_char(F_TIME,'hh24:mi') as time FROM T_EE_LEAK WHERE F_WATERNET=? AND F_TYPE=? AND F_TIME>=to_date(?,'yyyy-mm-dd')  AND F_TIME<to_date(?,'yyyy-mm-dd')-18/24 GROUP BY to_char(F_TIME,'hh24:mi')";
    Object[] args3={leakSearch.getNetid(),2,sdf.format(leakSearch.getStartdate()),sdf.format(leakSearch.getEnddate())};
    jdbcTemplate.query(sql3,args3, new RowCallbackHandler() {
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            double value=rs.getDouble(1);
            if(leak.get(1)>value) leak.set(1,value);
        }
    });
    LeakChart leakChart=new LeakChart();
    leakChart.setTotalin(in);
    leakChart.setTotalout(out);
    leakChart.setLeak(leak);
    leakChart.setDatatime(datatime);
    return leakChart;
}
    public List<Options> getGatewaynames(){
        String sql="select DISTINCT b.F_UUID,b.F_ADDRESS from T_BE_WATERMETER a,T_BE_GATEWAY b WHERE a.F_GATEWAYS_UUID=b.F_UUID ";
        final List<Options> Options=new ArrayList<Options>();
        jdbcTemplate.query(sql, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Options option=new Options();
                option.setId(rs.getString("F_UUID"));
                option.setName(rs.getString("F_ADDRESS"));
                Options.add(option);
            }
        });
        return Options;
    }
    public List<Options> getEquipnames(String id){
        String sql="select DISTINCT b.F_UUID,b.F_EQUIPID,b.f_remark from T_BE_WATERMETER a,T_BE_EQUIPMENTLIST b WHERE a.F_UUID=b.F_UUID AND a.F_GATEWAYS_UUID=? order by b.F_equipid";
        String[] args={id};
        final List<Options> Options=new ArrayList<Options>();
        jdbcTemplate.query(sql, args, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Options option=new Options();
                option.setId(rs.getString("F_UUID"));
                option.setName(rs.getString("F_EQUIPID") +"-"+rs.getString("F_REMARK"));
                Options.add(option);
            }
        });
        return Options;
    }
    public List<Options> getNetNames(){
        String sql="select f_uuid,f_name from T_EE_WATERNET order by f_code";
        final List<Options> Options=new ArrayList<Options>();
        jdbcTemplate.query(sql,  new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Options option=new Options();
                option.setId(rs.getString("F_UUID"));
                option.setName(rs.getString("F_NAME") );
                Options.add(option);
            }
        });
        return Options;
    }

    public List<LeakRank> getProduceRank(String id){
        String[] ids=id.split(",");
        String sql=null;
        if(ids[0].equals("minutes"))
            sql="select a.f_equipid,c.f_pn,c.F_REMARKINFO,sum(b.F_TIME_INTERVEL_ACTIVE) as value from (select F_EQUIPID from T_EE_NETEQUIP where F_WATERNET=? and F_TYPE=1) a," +
                    "(select F_DEVICECODE,F_TIME_INTERVEL_ACTIVE from T_BE_15_ENERGY_BUFFER where F_DATATIME>=to_date(?,'yyyy/mm/dd hh24:mi') and F_DATATIME<to_date(?,'yyyy/mm/dd hh24:mi')+15/(24*60) and F_TYPE=1) b,T_BE_WATERMETER c " +
                    "where  a.f_equipid=b.F_DEVICECODE and a.f_equipid=c.F_UUID group by a.f_equipid,c.f_pn,c.F_REMARKINFO order by value desc";
        else
        if(ids[0].equals("hour"))
            sql="select a.f_equipid,c.f_pn,c.F_REMARKINFO,sum(b.F_TIME_INTERVEL_ACTIVE) as value from (select F_EQUIPID from T_EE_NETEQUIP where F_WATERNET=? and F_TYPE=1) a," +
                    "(select F_DEVICECODE,F_TIME_INTERVEL_ACTIVE from T_BE_15_ENERGY_BUFFER where F_DATATIME>=to_date(?,'yyyy/mm/dd hh24:mi') and F_DATATIME<to_date(?,'yyyy/mm/dd hh24:mi')+1/24 and F_TYPE=1) b,T_BE_WATERMETER c " +
                    "where  a.f_equipid=b.F_DEVICECODE and a.f_equipid=c.F_UUID group by a.f_equipid,c.f_pn,c.F_REMARKINFO order by value desc";
        else
        if(ids[0].equals("day"))
            sql="select a.f_equipid,c.f_pn,c.F_REMARKINFO,sum(b.F_TIME_INTERVEL_ACTIVE) as value from (select F_EQUIPID from T_EE_NETEQUIP where F_WATERNET=? and F_TYPE=1) a," +
                    "(select F_DEVICECODE,F_TIME_INTERVEL_ACTIVE from T_BE_15_ENERGY_BUFFER where F_DATATIME>=to_date(?,'yyyy/mm/dd') and F_DATATIME<to_date(?,'yyyy/mm/dd')+1 and F_TYPE=1) b,T_BE_WATERMETER c " +
                    "where  a.f_equipid=b.F_DEVICECODE and a.f_equipid=c.F_UUID group by a.f_equipid,c.f_pn,c.F_REMARKINFO order by value desc";
        else
        if(ids[0].equals("month"))
            sql="select a.f_equipid,c.f_pn,c.F_REMARKINFO,sum(b.F_TIME_INTERVEL_ACTIVE) as value from (select F_EQUIPID from T_EE_NETEQUIP where F_WATERNET=? and F_TYPE=1) a," +
                    "(select F_DEVICECODE,F_TIME_INTERVEL_ACTIVE from T_BE_15_ENERGY_BUFFER where F_DATATIME>=to_date(?,'yyyy/mm') and F_DATATIME<add_months(to_date(?,'yyyy/mm'),1) and F_TYPE=1) b,T_BE_WATERMETER c " +
                    "where  a.f_equipid=b.F_DEVICECODE and a.f_equipid=c.F_UUID group by a.f_equipid,c.f_pn,c.F_REMARKINFO order by value desc";
        else
        if(ids[0].equals("year"))
            sql="select a.f_equipid,c.f_pn,c.F_REMARKINFO,sum(b.F_TIME_INTERVEL_ACTIVE) as value from (select F_EQUIPID from T_EE_NETEQUIP where F_WATERNET=? and F_TYPE=1) a," +
                    "(select F_DEVICECODE,F_TIME_INTERVEL_ACTIVE from T_BE_15_ENERGY_BUFFER where F_DATATIME>=to_date(?,'yyyy') and F_DATATIME<add_months(to_date(?,'yyyy'),12) and F_TYPE=1) b,T_BE_WATERMETER c " +
                    "where  a.f_equipid=b.F_DEVICECODE and a.f_equipid=c.F_UUID group by a.f_equipid,c.f_pn,c.F_REMARKINFO order by value desc";
        String[] args={ids[1],ids[2],ids[2]};
        final List<LeakRank> leakRanks=new ArrayList<LeakRank>();
        jdbcTemplate.query(sql ,args, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                LeakRank leakRank=new LeakRank();
                leakRank.setAddress(rs.getString("f_pn"));
                leakRank.setRemarkinfo(rs.getString("f_remarkinfo"));
                leakRank.setValue(rs.getDouble("value"));
                leakRanks.add(leakRank);
            }
        });
        return leakRanks;
    }

    public List<LeakRank> getConsumeRank(String id){
        String[] ids=id.split(",");
        String sql=null;
        if(ids[0].equals("minutes"))
            sql="select a.f_equipid,c.f_pn,c.F_REMARKINFO,sum(b.F_TIME_INTERVEL_ACTIVE) as value from (select F_EQUIPID from T_EE_NETEQUIP where F_WATERNET=? and F_TYPE=2) a," +
                    "(select F_DEVICECODE,F_TIME_INTERVEL_ACTIVE from T_BE_15_ENERGY_BUFFER where F_DATATIME>=to_date(?,'yyyy/mm/dd hh24:mi') and F_DATATIME<to_date(?,'yyyy/mm/dd hh24:mi')+15/(24*60) and F_TYPE=1) b,T_BE_WATERMETER c " +
                    "where  a.f_equipid=b.F_DEVICECODE and a.f_equipid=c.F_UUID group by a.f_equipid,c.f_pn,c.F_REMARKINFO order by value desc";
        else
        if(ids[0].equals("hour"))
            sql="select a.f_equipid,c.f_pn,c.F_REMARKINFO,sum(b.F_TIME_INTERVEL_ACTIVE) as value from (select F_EQUIPID from T_EE_NETEQUIP where F_WATERNET=? and F_TYPE=2) a," +
                    "(select F_DEVICECODE,F_TIME_INTERVEL_ACTIVE from T_BE_15_ENERGY_BUFFER where F_DATATIME>=to_date(?,'yyyy/mm/dd hh24:mi') and F_DATATIME<to_date(?,'yyyy/mm/dd hh24:mi')+1/24 and F_TYPE=1) b,T_BE_WATERMETER c " +
                    "where  a.f_equipid=b.F_DEVICECODE and a.f_equipid=c.F_UUID group by a.f_equipid,c.f_pn,c.F_REMARKINFO order by value desc";
        else
        if(ids[0].equals("day"))
            sql="select a.f_equipid,c.f_pn,c.F_REMARKINFO,sum(b.F_TIME_INTERVEL_ACTIVE) as value from (select F_EQUIPID from T_EE_NETEQUIP where F_WATERNET=? and F_TYPE=2) a," +
                    "(select F_DEVICECODE,F_TIME_INTERVEL_ACTIVE from T_BE_15_ENERGY_BUFFER where F_DATATIME>=to_date(?,'yyyy/mm/dd') and F_DATATIME<to_date(?,'yyyy/mm/dd')+1 and F_TYPE=1) b,T_BE_WATERMETER c " +
                    "where  a.f_equipid=b.F_DEVICECODE and a.f_equipid=c.F_UUID group by a.f_equipid,c.f_pn,c.F_REMARKINFO order by value desc";
        else
        if(ids[0].equals("month"))
            sql="select a.f_equipid,c.f_pn,c.F_REMARKINFO,sum(b.F_TIME_INTERVEL_ACTIVE) as value from (select F_EQUIPID from T_EE_NETEQUIP where F_WATERNET=? and F_TYPE=2) a," +
                    "(select F_DEVICECODE,F_TIME_INTERVEL_ACTIVE from T_BE_15_ENERGY_BUFFER where F_DATATIME>=to_date(?,'yyyy/mm') and F_DATATIME<add_months(to_date(?,'yyyy/mm'),1) and F_TYPE=1) b,T_BE_WATERMETER c " +
                    "where  a.f_equipid=b.F_DEVICECODE and a.f_equipid=c.F_UUID group by a.f_equipid,c.f_pn,c.F_REMARKINFO order by value desc";
        else
        if(ids[0].equals("year"))
            sql="select a.f_equipid,c.f_pn,c.F_REMARKINFO,sum(b.F_TIME_INTERVEL_ACTIVE) as value from (select F_EQUIPID from T_EE_NETEQUIP where F_WATERNET=? and F_TYPE=2) a," +
                    "(select F_DEVICECODE,F_TIME_INTERVEL_ACTIVE from T_BE_15_ENERGY_BUFFER where F_DATATIME>=to_date(?,'yyyy') and F_DATATIME<add_months(to_date(?,'yyyy'),12) and F_TYPE=1) b,T_BE_WATERMETER c " +
                    "where  a.f_equipid=b.F_DEVICECODE and a.f_equipid=c.F_UUID group by a.f_equipid,c.f_pn,c.F_REMARKINFO order by value desc";
        String[] args={ids[1],ids[2],ids[2]};
        final List<LeakRank> leakRanks=new ArrayList<LeakRank>();
        jdbcTemplate.query(sql ,args, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                LeakRank leakRank=new LeakRank();
                leakRank.setAddress(rs.getString("f_pn"));
                leakRank.setRemarkinfo(rs.getString("f_remarkinfo"));
                leakRank.setValue(rs.getDouble("value"));
                leakRanks.add(leakRank);
            }
        });
        return leakRanks;
    }
}
