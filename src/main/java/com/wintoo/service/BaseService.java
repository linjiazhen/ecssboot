package com.wintoo.service;

import com.wintoo.dao.BaseDao;
import com.wintoo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

@Service
public class BaseService {
	@Autowired
	private BaseDao baseDao;
	public void addBuildPoint(String point){
	   baseDao.addBuildPoint(point);
	}
    public void addOrganPoint(String point){
        baseDao.addOrganPoint(point);
    }
    public void addWaterPoint(String point){
        baseDao.addWaterPoint(point);
    }
	public void deleteBuildPoint(String point){
		   baseDao.deleteBuildPoint(point);
	}
    public void deleteOrganPoint(String point){
        baseDao.deleteOrganPoint(point);
    }
    public void deleteWaterPoint(String point){
        baseDao.deleteWaterPoint(point);
    }
	public List<Point> getPoints(String type) {
		return baseDao.getPoints(type);
	}
	public List<Point> getAllPoints() {
        return baseDao.getAllPoints();
    }
    public List<Point> getAllOrganPoints() {
        return baseDao.getAllOrganPoints();
    }
    public List<Point> getOrganPoints(String id) {
        return baseDao.getOrganPoints(id);
    }
    public List<Point> getWaterPoints() {
        return baseDao.getWaterPoints();
    }
/////////////////////////////////////////////////
	public DataTable getAllGroups() {
		return baseDao.getAllGroups();
	}

	public void addGroup(Group group) {
		baseDao.addGroup(group);
	}

	public void deleteGroup(String ids) {
		String[] id = ids.split(",");
		int length = id.length;
		for (int i = 0; i < length; i++) {
			baseDao.deleteGroup(id[i]);
		}
	}

	public void updateGroup(Group group) {
		baseDao.updateGroupById(group);
	}

	public Group getGroup(String id) {
		return baseDao.getGroupById(id);
	}

	// ///////////////////////////////////////////////////////
	public DataTable getAllBuildTypes() {
		return baseDao.getAllBuildTypes();
	}

	public void addBuildType(BuildType buildType) {
		baseDao.addBuildType(buildType);
	}

	public boolean deleteBuildType(String codes) {
		String[] code = codes.split(",");
		int length = code.length;
		for (int i = 0; i < length; i++) {
			if (!baseDao.deleteBuildType(code[i]))
				return false;
		}
		return true;
	}

	public void updateBuildType(BuildType buildType) {
		baseDao.updateBuildTypeById(buildType);
	}

	public BuildType getBuildType(String code) {
		return baseDao.getBuildTypeById(code);
	}

	public List<Options> getBuildFuncs() {
		return baseDao.getBuildFuncs();
	}
	// ///////////////////////////////////////////////////////////////
	public DataTable getAllRoomTypes() {
		return baseDao.getAllRoomTypes();
	}

	public void addRoomType(RoomType roomType) {
		baseDao.addRoomType(roomType);
	}

	public void deleteRoomType(String codes) {
		String[] code = codes.split(",");
		int length = code.length;
		for (int i = 0; i < length; i++)
			baseDao.deleteRoomType(code[i]);
	}

	public void updateRoomType(RoomType roomType) {
		baseDao.updateRoomTypeById(roomType);
	}

	public RoomType getRoomType(String code) {
		return baseDao.getRoomTypeById(code);
	}

	// ///////////////////////////////////////////////////////////////
	public DataTable getAllOrganTypes() {
		return baseDao.getAllOrganTypes();
	}

	public void addOrganType(OrganType organType) {
		baseDao.addOrganType(organType);
	}

	public void deleteOrganType(String codes) {
		String[] code = codes.split(",");
		int length = code.length;
		for (int i = 0; i < length; i++)
			baseDao.deleteOrganType(code[i]);
	}

	public void updateOrganType(OrganType organType) {
		baseDao.updateOrganTypeById(organType);
	}

	public OrganType getOrganType(String code) {
		return baseDao.getOrganTypeById(code);
	}
    public List<Options> getOrganTypes(){ return baseDao.getOrganTypes(); }
	/////////////////////////////////////////////////
	public DataTable getAllUsers() {
		return baseDao.getAllUsers();
	}

	public void addUser(User group) {
		baseDao.addUser(group);
	}

	public void deleteUser(String ids) {
		String[] id = ids.split(",");
		int length = id.length;
		for (int i = 0; i < length; i++) {
			baseDao.deleteUser(id[i]);
		}
	}

	public void updateUser(User group) {
		baseDao.updateUserById(group);
	}

	public User getUser(String id){
		return baseDao.getUser(id);
	}

    public List<Options> getUserOptions(){
        return baseDao.getUserOptions();
    }
	public boolean checklogin(LoginForm loginForm){
		return baseDao.checkLogin(loginForm);
	}
	/////////////////////////////////////////////////
	public DataTable getAllRoles() {
		return baseDao.getAllRoles();
	}

	public void addRole(Role group) {
		baseDao.addRole(group);
	}

	public List<Options> getRoleOptions(){
		return baseDao.getRoleOptions();
	}
	public void deleteRole(String ids) {
		String[] id = ids.split(",");
		int length = id.length;
		for (int i = 0; i < length; i++) {
			baseDao.deleteRole(id[i]);
		}
	}

	public void updateRole(Role group) {
		baseDao.updateRoleById(group);
	}

    public List<String> getRight(String id){
        return baseDao.getRight(id);
    }

    public void updateRight(String ids){
        baseDao.updateRight(ids);
    }
	/////////////////////////////////////////////////
	public DataTable getAllMenus() {
		return baseDao.getAllMenus();
	}

	public void addMenu(Menu menu) {
		baseDao.addMenu(menu);
	}

	public void deleteMenu(String ids) {
		String[] id = ids.split(",");
		int length = id.length;
		for (int i = 0; i < length; i++) {
			baseDao.deleteMenu(id[i]);
		}
	}

	public void updateMenu(Menu menu) {
		baseDao.updateMenuById(menu);
	}

	public List<Options> getMenu(String level){ return baseDao.getMenu(level);}

    public List<Tree> getMenuTree(){ return baseDao.getMenuTree();}
	// ///////////////////////////////////////////////////////
	public DataTable getAllEquipTypes() {
		return baseDao.getAllEquipTypes();
	}

	public void addEquipType(EquipType buildType) {
		baseDao.addEquipType(buildType);
	}

	public boolean deleteEquipType(String codes) {
		String[] code = codes.split(",");
		int length = code.length;
		for (int i = 0; i < length; i++) {
			if (!baseDao.deleteEquipType(code[i]))
				return false;
		}
		return true;
	}

	public void updateEquipType(EquipType buildType) {
		baseDao.updateEquipTypeById(buildType);
	}

	public EquipType getEquipType(String code) {
		return baseDao.getEquipTypeById(code);
	}


/////////////////////////////////////////////////////////////////
	public DataTable getAllEnergyTypes() {
		return baseDao.getAllEnergyTypes();
	}

	public void addEnergyType(EnergyType energyType) {
		baseDao.addEnergyType(energyType);
	}

	public void deleteEnergyType(String codes) {
		String[] code = codes.split(",");
		int length = code.length;
		for (int i = 0; i < length; i++)
			baseDao.deleteEnergyType(code[i]);
	}

	public void updateEnergyType(EnergyType energyType) {
		baseDao.updateEnergyTypeByCode(energyType);
	}

	public EnergyType getEnergyType(String code) {
		return baseDao.getEnergyTypeByCode(code);
	}

	public void updateEnergyTypestate(String codes) {
		baseDao.updateEnergyTypestate(codes);
		
	}

	public List<Itemcode_name> getparentinfo(String code) {
		return baseDao.getparentinfo(code);
	}
	
	public List<Options> getEnergyItem(){
		return baseDao.getEnergyItem();
	}
///////////////////////////////////////学科专业基础信息////////////////////////////////////
	public DataTable getAllDepartments() {
		return baseDao.getAllDepartments();
	}

	public void addDepartment(Organ organ) {
		baseDao.addDepartment(organ);
		
	}

	public Organ getDepartment(String code) {
		return baseDao.getDepartmentByID(code);
	}
	public void updateDepartment(Organ organ) {
		baseDao.updateDepartmentById(organ);
	}

	public DataTable getAllMajors() {
		return baseDao.getAllMajors();
	}

	public void addMajor(Major major) {
		baseDao.addMajor(major);
		
	}
	public void deleteMajor(String ids){
		String[] id=ids.split(",");
		int length=id.length;
		for (int i=0;i<length;i++){
			baseDao.deleteMajor(id[i]);
		}
	}

	public Major getMajor(String code) {
		return baseDao.getMajorByCode(code);
	}

	public void updateMajor(Major major) {
		baseDao.updateMajorByCode(major);
		
	}
	
	
	// ///////////////////////////////////////////////////////////////
		public DataTable getAllUserTypes() {
			return baseDao.getAllUserTypes();
		}

		public void addUserType(UserType userType) {
			baseDao.addUserType(userType);
		}

		public void deleteUserType(String codes) {
			String[] code = codes.split(",");
			int length = code.length;
			for (int i = 0; i < length; i++)
				baseDao.deleteUserType(code[i]);
		}

		public void updateUserType(UserType userType) {
			baseDao.updateUserTypeByCode(userType);
		}

		public UserType getUserTypeByCode(String code) {
			return baseDao.getUserTypeByCode(code);
		}
	
	////////////////////////////////////////////////////////////////////
	public RecordsNum getRecordsNum(){
		return baseDao.getRecordsNum();
	}

    public List<UserMenu> getUserMenus(String id){
        return baseDao.getUserMenus(id);
    }

}