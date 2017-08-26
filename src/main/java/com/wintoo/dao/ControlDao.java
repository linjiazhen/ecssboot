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
public class ControlDao {
	@Autowired
    @Qualifier("primaryJdbcTemplate")
	private JdbcOperations jdbcTemplate;

    public List<Ktxt> getKtxt(String type){
        final List<Ktxt> list=new ArrayList<Ktxt>();
        String sql="SELECT T1.F_NAME,T1.F_VALUE AS VALUE1 ,T2.F_VALUE AS VALUE2 ,T3.F_MODBUSADDRESS FROM (SELECT F_NAME,F_VALUE FROM T_BC_CONTROLAIR WHERE F_TYPE="+type+"1) T1,(SELECT F_NAME,F_VALUE FROM T_BC_CONTROLAIR WHERE F_TYPE="+type+"2) T2," +
                "(SELECT F_NAME,F_MODBUSADDRESS FROM T_BC_CONTROLAIR WHERE F_TYPE="+type+"3) T3 WHERE T1.F_NAME=T2.F_NAME AND T2.F_NAME=T3.F_NAME";
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Ktxt ktxt=new Ktxt();
                ktxt.setName(rs.getString("F_NAME"));
                ktxt.setState(rs.getInt("VALUE1"));
                ktxt.setControl(rs.getInt("VALUE2"));
                ktxt.setControladdress(rs.getInt("F_MODBUSADDRESS"));
                list.add(ktxt);
            }
        });
        return list;
    }


}
