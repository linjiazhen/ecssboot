package com.wintoo.service;

import com.wintoo.dao.LoginDao;
import com.wintoo.model.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
	@Autowired
	private LoginDao loginDao;

    public DataTable getLogs(){
        return loginDao.getLogs();
    }
    public void addLog(String userid,String operate,String ip){
        loginDao.addLog(userid,operate,ip);
    }
    public void deleteLog(String ids){
        String[] id = ids.split(",");
        int length = id.length;
        for (int i = 0; i < length; i++) {
            loginDao.deleteLog(id[i]);
        }
    }
}
