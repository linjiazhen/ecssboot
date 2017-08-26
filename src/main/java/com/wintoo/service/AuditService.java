package com.wintoo.service;

import com.wintoo.dao.AuditDao;
import com.wintoo.model.AuditReport;
import com.wintoo.model.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuditService {
    @Autowired
	private AuditDao auditDao;

    public DataTable getAllReports() {
        return auditDao.getAllReports();
    }

    public void addReport(AuditReport group) {
        auditDao.addReport(group);
    }

    public void deleteReport(String ids) {
        String[] id = ids.split(",");
        int length = id.length;
        for (int i = 0; i < length; i++) {
            auditDao.deleteReport(id[i]);
        }
    }

    public void updateReport(AuditReport group) {
        auditDao.updateReportById(group);
    }
    public void saveFile(String uuid,String file,String filename) throws IOException {
        auditDao.saveFile(uuid,file,filename);
    }
    public String getFile(String path,String uuid) throws IOException {
        return auditDao.getFile(path,uuid);
    }
}
