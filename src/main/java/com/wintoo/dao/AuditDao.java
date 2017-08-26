package com.wintoo.dao;

import com.wintoo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class AuditDao {
	@Autowired
    @Qualifier("primaryJdbcTemplate")
	private JdbcOperations jdbcTemplate;

    public DataTable getAllReports() {
        String sql = "select a.f_uuid,a.f_groupid,a.f_buildid,c.F_BUILDGROUPNAME||'-'||b.F_BUILDNAME as buildname,a.f_date,a.f_author,a.F_FILENAME from T_BA_AUDIT a,T_BD_BUILD b,T_BD_GROUP c where a.F_BUILDID=b.F_BUILDID and a.F_GROUPID=c.F_BUILDGROUPID" ;
        DataTable dataTable = new DataTable();
        final List<AuditReport> groups = new ArrayList<AuditReport>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                AuditReport auditReport = new AuditReport();
                auditReport.setUuid(rs.getString(1));
                auditReport.setGroup(rs.getString(2));
                auditReport.setBuild(rs.getString(3));
                auditReport.setBuildname(rs.getString(4));
                auditReport.setDate(rs.getString(5));
                auditReport.setAuthor(rs.getString(6));
                auditReport.setFilename(rs.getString(7));
                groups.add(auditReport);
            }
        });
        dataTable.setData(groups);
        return dataTable;
    }
    public boolean addReport(AuditReport auditReport) {
        System.out.println(auditReport.getDate());
        String sql = "insert into T_BA_AUDIT(F_UUID,F_GROUPID,F_BUILDID,F_DATE,F_AUTHOR,F_CREATETIME) values(sys_guid(),?,?,to_date(?,'YYYY/MM'),?,sysdate)";
        Object[] args = { auditReport.getGroup(),auditReport.getBuild(),auditReport.getDate(),auditReport.getAuthor()};
        int i = jdbcTemplate.update(sql, args);
        return i > 0;
    }

    public void deleteReport(String id) {
        String sql = "delete from T_BA_AUDIT where F_UUID=?";
        Object[] args = { id };
        jdbcTemplate.update(sql, args);
    }

    public boolean updateReportById(AuditReport auditReport) {
        String sql = "update T_BA_AUDIT set F_GROUPID=?,F_BUILDID=?,F_DATE=TO_DATE(?,'YYYY/MM'),F_AUTHOR=? where F_UUID=?";
        Object[] args = { auditReport.getGroup(),auditReport.getBuild(),auditReport.getDate(),auditReport.getAuthor(),auditReport.getUuid() };
        int i = jdbcTemplate.update(sql, args);
        return i > 0;
    }

    public void saveFile(final String uuid,String file, final String filename) throws IOException {
        final File binaryFile=new File(file);
        final InputStream is=new FileInputStream(binaryFile);
        final LobHandler lobHandler=new DefaultLobHandler();
        jdbcTemplate.execute("update t_ba_audit set F_FILENAME=?,F_FILE=? WHERE F_UUID=?",
                new AbstractLobCreatingPreparedStatementCallback(lobHandler){
                    @Override
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException, DataAccessException {
                        ps.setString(1, filename);
                        lobCreator.setBlobAsBinaryStream(ps, 2, is, (int) binaryFile.length());
                        ps.setString(3,uuid);
                    }
                });
        is.close();
    }

    public String getFile(final String path,String uuid) throws IOException {
        final LobHandler lobHandler=new DefaultLobHandler();
        Object[] args = { uuid };
        final List<String> filename=new ArrayList<String>();
        jdbcTemplate.query("select f_filename,f_file from t_ba_audit where f_uuid=?",args,new AbstractLobStreamingResultSetExtractor(){
            protected void streamData(ResultSet rs) throws SQLException,IOException,DataAccessException {
                filename.add(0,rs.getString(1));
                final OutputStream os=new FileOutputStream(path+filename.get(0));
                FileCopyUtils.copy(lobHandler.getBlobAsBinaryStream(rs, 2), os);
                os.close();
            }
        });
        return filename.get(0);
    }
}
