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
import java.util.LinkedList;
import java.util.List;

@Repository
@Transactional
public class LoginDao {
	@Autowired
    @Qualifier("primaryJdbcTemplate")
	private JdbcOperations jdbcTemplate;

    public DataTable getLogs(){
        String sql="SELECT a.F_UUID,a.F_USERID,b.F_NAME,a.F_OPERATE,a.F_IP,a.F_TIME from T_BS_LOG a,T_BS_USER b where a.F_USERID=b.F_ACCOUNT ORDER BY a.f_uuid desc limit 100";
        final List<Log> list=new LinkedList<Log>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Log log=new Log();
                log.setId(rs.getString(1));
                log.setUserid(rs.getString(2));
                log.setUsername(rs.getString(3));
                log.setOperate(rs.getString(4));
                log.setIp(rs.getString(5));
                log.setTime(rs.getString(6));
                list.add(log);
            }
        });
        DataTable dataTable=new DataTable();
        dataTable.setData(list);
        return dataTable;
    }

    public void addLog(String userid,String operate,String ip){
        String sql="insert into T_BS_LOG(F_USERID,F_OPERATE,F_IP) values(?,?,?)";
        Object[] args={userid,operate,ip};
        jdbcTemplate.update(sql, args);
    }
    public void deleteLog(String id){
        String sql="DELETE FROM T_BS_LOG WHERE F_UUID=?";
        Object[] args={id};
        jdbcTemplate.update(sql,args);
    }
}
