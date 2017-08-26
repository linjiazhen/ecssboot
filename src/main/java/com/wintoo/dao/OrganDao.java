package com.wintoo.dao;

import com.wintoo.model.Options;
import com.wintoo.model.Organ;
import com.wintoo.model.OrganBuild;
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
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class OrganDao {
	@Autowired
	@Qualifier("primaryJdbcTemplate")
	private JdbcOperations jdbcTemplate;
	
	public List<Organ> getAllOrgans(){
		String sql="select F_ID,F_PID,F_NAME,F_TYPECODE from T_BO_ORGAN WHERE F_ID !='#' ";
		final List<Organ> organs=new ArrayList<Organ>();
		jdbcTemplate.query(sql, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				Organ organ=new Organ();
				organ.setId(rs.getString("F_ID"));
				organ.setText(rs.getString("F_NAME"));
				organ.setParent(rs.getString("F_PID"));
				organ.setType(rs.getString("F_TYPECODE"));
				organs.add(organ);
			}
		});
		System.out.println(organs.size());
		return organs;
	}
	
	public void addOrgan(Organ organ){
		String sql="insert into T_BO_ORGAN(F_ID,F_NAME,F_PID) values(?,?,?)";
		Object[] args={organ.getId(),organ.getText(),organ.getParent()};
		jdbcTemplate.update(sql, args);
	}
	
	public void deleteOrgan(String id){
		String sql="delete from T_BO_ORGAN where F_ID=?";
        String sql1="DELETE from T_RR_ORGANBUILDRELATION where F_ORGANID=?";
		Object[] args={id};
		jdbcTemplate.update(sql,args);
        jdbcTemplate.update(sql1,args);
	}
	
	public Organ getOrganById(String id){
        String sql="select a.F_ID,a.F_NAME,b.F_CODE,a.F_PEOPLE,a.F_AREA,a.F_MANAGER,a.F_TEL,a.F_EMAIL from T_BO_ORGAN a,T_BO_ORGANTYPE b where a.F_TYPECODE=b.F_CODE and a.F_ID= ?";
        Object[] args={id};
        final Organ organ=new Organ();
        jdbcTemplate.query(sql, args ,new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                organ.setId(rs.getString(1));
                organ.setText(rs.getString(2));
                organ.setType(rs.getString(3));
                organ.setPeople(rs.getInt(4));
                organ.setArea(rs.getDouble(5));
                organ.setManager(rs.getString(6));
                organ.setTel(rs.getString(7));
                organ.setEmail(rs.getString(8));
            }
        });
        return organ;
	}

    public Organ getOrganInfo(String name){
        String sql="select F_ID,a.F_NAME,b.F_NAME from T_BO_ORGAN a,T_BO_ORGANTYPE b where a.F_TYPECODE=b.F_CODE and a.F_NAME= ?";
        Object[] args={name};
        final Organ organ=new Organ();
        jdbcTemplate.query(sql, args ,new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                organ.setId(rs.getString(1));
                organ.setText(rs.getString(2));
                organ.setType(rs.getString(3));
            }
        });
        return organ;
    }
	public List<Options> getOrganByPid(String pid){
		String sql="select F_ID,F_PID,F_NAME from T_BO_ORGAN where F_PID=?";
		Object[] args={pid};
		final List<Options> organs=new ArrayList<Options>();
		jdbcTemplate.query(sql,args, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				Options organ=new Options();
				organ.setId(rs.getString("F_ID"));
				organ.setName(rs.getString("F_NAME"));
				organs.add(organ);
			}
		});
		return organs;
	}

    public List<Options> getOrgansByType(String type){
        String sql="select F_ID,F_NAME from T_BO_ORGAN where F_PID='j4_2' and F_TYPECODE=?";
        Object[] args={type};
        final List<Options> organs=new ArrayList<Options>();
        jdbcTemplate.query(sql,args, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Options organ=new Options();
                organ.setId(rs.getString("F_ID"));
                organ.setName(rs.getString("F_NAME"));
                organs.add(organ);
            }
        });
        return organs;
    }

	public void updateOrganById(Organ organ){
		String sql="update T_BO_ORGAN set F_NAME=?,F_TYPECODE=?,F_MANAGER=?,F_TEL=?,F_EMAIL=?,F_REMARK=?,F_PEOPLE=?,F_AREA=? where F_ID=?";
		Object[] args={organ.getText(),organ.getType(),organ.getManager(),organ.getTel(),organ.getEmail(),organ.getRemark(),organ.getPeople(),organ.getArea(),organ.getId()};
		jdbcTemplate.update(sql,args);
	}
/////////////////////////////////////////////////////////////////
	public List<Options> getBuildNameById(String id){
		String sql;
		String[] ids=id.split(",");
		if(ids[1].equals("#")){
			sql="select distinct F_BUILDID,F_BUILDNAME from T_BD_BUILD where F_BUILDID NOT IN (SELECT F_BUILDID FROM T_RR_ORGANBUILDRELATION WHERE F_ORGANID = ?) order by F_BUILDNAME";
			id="00";
		}
		else{
			if(ids[0].equals("2"))
				sql="select distinct A.F_BUILDID,A.F_BUILDNAME from T_BD_BUILD A,T_RR_ORGANBUILDRELATION B where B.F_ORGANID=? AND A.F_BUILDID=B.F_BUILDID AND B.F_BUILDID NOT IN (SELECT F_BUILDID FROM T_RR_ORGANBUILDRELATION WHERE F_ORGANID LIKE '"+ids[1]+"__')";
			else {
				sql="select distinct A.F_BUILDID,A.F_BUILDNAME from T_BD_BUILD A,T_RR_ORGANBUILDRELATION B where B.F_ORGANID=? AND A.F_BUILDID=B.F_BUILDID AND B.F_BUILDID NOT IN (SELECT F_BUILDID FROM T_RR_ORGANBUILDRELATION WHERE F_ORGANID LIKE '"+ids[1]+"__' AND F_SIGN='2')";
			}
			id=ids[1];
		}
		Object[] args={id};
		final List<Options> options=new ArrayList<Options>();
		jdbcTemplate.query(sql, args, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				Options option=new Options();
				option.setId(rs.getString("F_BUILDID"));
				option.setName(rs.getString("F_BUILDNAME"));
				options.add(option);
			}
		});
		return options;
	}
	
	public List<Options> getFloorNameById(String id){
		String sql;
		String[] ids=id.split(",");
		if(ids[1].equals("#")){
			sql="select distinct F_ID,F_NAME from T_BD_FLOOR where F_ID NOT IN (SELECT F_FLOORID FROM T_RR_ORGANBUILDRELATION WHERE F_ORGANID = ?)";
			id="00";
		}
		else{
			if(ids[0].equals("1")){
				sql="select distinct A.F_ID,A.F_NAME from T_BD_FLOOR A,T_RR_ORGANBUILDRELATION B where  B.F_ORGANID=? AND A.F_ID=B.F_FLOORID AND B.F_FLOORID NOT IN (SELECT F_FLOORID FROM T_RR_ORGANBUILDRELATION WHERE F_ORGANID LIKE '"+ids[0]+"__' )";
			}
			else{
				sql="select distinct A.F_ID,A.F_NAME from T_BD_FLOOR A,T_RR_ORGANBUILDRELATION B where  B.F_ORGANID=? AND A.F_ID=B.F_FLOORID AND B.F_FLOORID NOT IN (SELECT F_FLOORID FROM T_RR_ORGANBUILDRELATION WHERE F_ORGANID LIKE '"+ids[0]+"__' AND F_SIGN='1')";
			}
			id=ids[1];
		}
		Object[] args={id};
		final List<Options> options=new ArrayList<Options>();
		jdbcTemplate.query(sql, args, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				Options option=new Options();
				option.setId(rs.getString("F_ID"));
				option.setName(rs.getString("F_NAME"));
				options.add(option);
			}
		});
		return options;
	}
	
	public List<Options> getRoomNameById(String id){
		String sql;
		sql="select distinct  A.F_ID,A.F_NAME from T_BD_ROOM A,T_RR_ORGANBUILDRELATION B where B.F_ORGANID=? AND A.F_ID=B.F_ROOMID AND B.F_ROOMID NOT IN (SELECT F_ROOMID FROM T_RR_ORGANBUILDRELATION WHERE F_ORGANID LIKE '"+id+"__')";
		Object[] args={id};
		final List<Options> options=new ArrayList<Options>();
		jdbcTemplate.query(sql, args, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				Options option=new Options();
				option.setId(rs.getString("F_ID"));
				option.setName(rs.getString("F_NAME"));
				options.add(option);
			}
		});
		return options;
	}

	//////////////////////////////////////////////////////////////////////////
	public void addOrganBuild(final OrganBuild organbuild){
		System.out.println(organbuild.getBuildid()+organbuild.getOrganid());
		String sql="insert into T_RR_ORGANBUILDRELATION(F_UUID,F_ORGANID,F_BUILDID,F_PERCENT) values(sys_guid(),?,?,100)";
		final String[] buildids=organbuild.getBuildid().split(",");
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, organbuild.getOrganid());
				ps.setString(2, buildids[i]);
			}
			@Override
			public int getBatchSize() {
				return buildids.length;
			}
		});
	}
	public void deleteOrganBuild(OrganBuild organbuild){
		String sql="DELETE FROM T_RR_ORGANBUILDRELATION WHERE F_ORGANID=?";
		Object[] args={organbuild.getOrganid()};
		jdbcTemplate.update(sql,args);
	}
	public void updateOrganBuild(OrganBuild organbuild){
		deleteOrganBuild(organbuild);
		if(!organbuild.getBuildid().equals(""))
			addOrganBuild(organbuild);
	}
	
	public List<String> getBuildByOrgan(String id){
		String sql="select F_BUILDID from T_RR_ORGANBUILDRELATION E WHERE E.F_ORGANID=?";
		Object[] args={id};
		final List<String> buildids=new ArrayList<String>();
		jdbcTemplate.query(sql,args, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				buildids.add(rs.getString(1));
			}
		});
		return buildids;
	}
}
