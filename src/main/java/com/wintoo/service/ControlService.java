package com.wintoo.service;

import com.wintoo.dao.ControlDao;
import com.wintoo.model.Ktxt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ControlService {
	@Autowired
	private ControlDao controlDao;


    public List<Ktxt> getKtxt(String type){
        return controlDao.getKtxt(type);
    }


}
