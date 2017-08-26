package com.wintoo.controller;

import com.wintoo.model.*;
import com.wintoo.service.BaseService;
import com.wintoo.tools.MakePicture;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class BaseInfoController extends BaseController {

	@Autowired
	private BaseService baseService;
	
	@RequestMapping(value = "addbuildpoint.do", method = RequestMethod.POST)
	@ResponseBody
	public void addbuildpoint(@RequestBody String point) {
		baseService.addBuildPoint(point);
	}
    @RequestMapping(value = "addorganpoint.do", method = RequestMethod.POST)
    @ResponseBody
    public void addorganpoint(@RequestBody String point) {
        baseService.addOrganPoint(point);
    }
    @RequestMapping(value = "addwaterpoint.do", method = RequestMethod.POST)
    @ResponseBody
    public void addwaterpoint(@RequestBody String point) {
        baseService.addWaterPoint(point);
    }
	@RequestMapping(value = "deletebuildpoint.do", method = RequestMethod.POST)
	@ResponseBody
	public void deletebuildpoint(@RequestBody String point) {
		baseService.deleteBuildPoint(point);
	}
    @RequestMapping(value = "deleteorganpoint.do", method = RequestMethod.POST)
    @ResponseBody
    public void deleteorganpoint(@RequestBody String point) {
        baseService.deleteOrganPoint(point);
    }
    @RequestMapping(value = "deletewaterpoint.do", method = RequestMethod.POST)
    @ResponseBody
    public void deletewaterpoint(@RequestBody String point) {
        baseService.deleteWaterPoint(point);
    }
	@RequestMapping(value = "getpoints.do", method = RequestMethod.POST)
	@ResponseBody
	public List<Point> getpoints(@RequestBody String type) {
		return baseService.getPoints(type);
	}
	
	@RequestMapping(value = "getallpoints.do", method = RequestMethod.POST)
         @ResponseBody
         public List<Point> getallpoints() {
        return baseService.getAllPoints();
    }

    @RequestMapping(value = "getallorganpoints.do", method = RequestMethod.POST)
         @ResponseBody
         public List<Point> getAllOranPoints() {
        return baseService.getAllOrganPoints();
    }
    @RequestMapping(value = "getorganpoints.do", method = RequestMethod.POST)
    @ResponseBody
    public List<Point> getorganpoints(@RequestBody String id) {
        return baseService.getOrganPoints(id);
    }
    @RequestMapping(value = "getwaterpoints.do", method = RequestMethod.POST)
    @ResponseBody
    public List<Point> getwaterpoints() {
        return baseService.getWaterPoints();
    }
/////////////////////////////////////////////////////////////////
	@RequestMapping(value = "getallgroups.do", method = RequestMethod.POST)
	@ResponseBody
	public DataTable getGroups() {
		return baseService.getAllGroups();
	}

	@RequestMapping(value = "addgroup.do", method = RequestMethod.POST)
	@ResponseBody
	public void addGroup(Group group) {
		baseService.addGroup(group);
	}

	@RequestMapping(value = "deletegroup.do", method = RequestMethod.POST)
	@ResponseBody
	public void deleteGroup(@RequestBody String ids) {
		baseService.deleteGroup(ids);
	}

	@RequestMapping(value = "updategroup.do", method = RequestMethod.POST)
	@ResponseBody
	public void updateGroup(Group group) {
		baseService.updateGroup(group);
	}

	@RequestMapping(value = "getgroup.do", method = RequestMethod.POST)
	@ResponseBody
	public Group getGroup(@RequestBody String id) {
		Group gp = baseService.getGroup(id);
		return gp;
	}

	// //////////////////////////////////////////////////////////////
	@RequestMapping(value = "getallbuildtypes.do", method = RequestMethod.POST)
	@ResponseBody
	public DataTable getAllBuildTypes() {
		return baseService.getAllBuildTypes();
	}

	@RequestMapping(value = "addbuildtype.do", method = RequestMethod.POST)
	@ResponseBody
	public void addBuildType(BuildType buildType) {
		baseService.addBuildType(buildType);
	}

	@RequestMapping(value = "deletebuildtype.do", method = RequestMethod.POST)
	@ResponseBody
	public void deleteBuildType(@RequestBody String codes) {
		baseService.deleteBuildType(codes);
	}

	@RequestMapping(value = "updatebuildtype.do", method = RequestMethod.POST)
	@ResponseBody
	public void updateBuildType(BuildType buildType) {
		baseService.updateBuildType(buildType);
	}

	@RequestMapping(value = "getbuildtype.do", method = RequestMethod.POST)
	@ResponseBody
	public BuildType getBuildType(@RequestBody String code) {
		BuildType buildType = baseService.getBuildType(code);
		return buildType;
	}
	
	@RequestMapping(value = "getbuildfuncs.do", method = RequestMethod.POST)
	@ResponseBody
	public List<Options> getBuildFuncs() {
		List<Options> buildFuncs = baseService.getBuildFuncs();
		return buildFuncs;
	}

	// //////////////////////////////////////////////////////////////////
	@RequestMapping(value = "getallroomtypes.do", method = RequestMethod.POST)
	@ResponseBody
	public DataTable getAllRoomTypes() {
		return baseService.getAllRoomTypes();
	}

	@RequestMapping(value = "addroomtype.do", method = RequestMethod.POST)
	@ResponseBody
	public void addRoomType(RoomType roomType) {
		baseService.addRoomType(roomType);
	}

	@RequestMapping(value = "deleteroomtype.do", method = RequestMethod.POST)
	@ResponseBody
	public void deleteRoomType(@RequestBody String codes) {
		baseService.deleteRoomType(codes);
	}

	@RequestMapping(value = "updateroomtype.do", method = RequestMethod.POST)
	@ResponseBody
	public void updateRoomType(RoomType roomType) {
		baseService.updateRoomType(roomType);
	}

	@RequestMapping(value = "getroomtype.do", method = RequestMethod.POST)
	@ResponseBody
	public RoomType getRoomType(@RequestBody String code) {
		RoomType roomType = baseService.getRoomType(code);
		return roomType;
	}

	// //////////////////////////////////////////////////////////////////
	@RequestMapping(value = "getallorgantypes.do", method = RequestMethod.POST)
	@ResponseBody
	public DataTable getAllOrganTypes() {
		return baseService.getAllOrganTypes();
	}

	@RequestMapping(value = "addorgantype.do", method = RequestMethod.POST)
	@ResponseBody
	public void addOrganType(OrganType organType) {
		baseService.addOrganType(organType);
	}

	@RequestMapping(value = "deleteorgantype.do", method = RequestMethod.POST)
	@ResponseBody
	public void deleteOrganType(@RequestBody String codes) {
		baseService.deleteOrganType(codes);
	}

	@RequestMapping(value = "updateorgantype.do", method = RequestMethod.POST)
	@ResponseBody
	public void updateOrganType(OrganType organType) {
		baseService.updateOrganType(organType);
	}

	@RequestMapping(value = "getorgantype.do", method = RequestMethod.POST)
	@ResponseBody
	public OrganType getOrganType(@RequestBody String code) {
		OrganType organType = baseService.getOrganType(code);
		return organType;
	}
    @RequestMapping(value = "getorgantypes.do", method = RequestMethod.POST)
    @ResponseBody
    public List<Options> getOrganTypes() {
        return baseService.getOrganTypes();
    }
	// //////////////////////////////////////////////////////////////
	@RequestMapping(value = "getallequiptypes.do", method = RequestMethod.POST)
	@ResponseBody
	public DataTable getAllEquipTypes() {
		return baseService.getAllEquipTypes();
	}

	@RequestMapping(value = "addequiptype.do", method = RequestMethod.POST)
	@ResponseBody
	public void addEquipType(EquipType equipType) {
		baseService.addEquipType(equipType);
	}

	@RequestMapping(value = "deleteequiptype.do", method = RequestMethod.POST)
	@ResponseBody
	public void deleteEquipType(@RequestBody String codes) {
		baseService.deleteEquipType(codes);
	}

	@RequestMapping(value = "updateequiptype.do", method = RequestMethod.POST)
	@ResponseBody
	public void updateEquipType(EquipType equipType) {
		baseService.updateEquipType(equipType);
	}

	@RequestMapping(value = "getequiptype.do", method = RequestMethod.POST)
	@ResponseBody
	public EquipType getEquipType(@RequestBody String code) {
		EquipType equipType = baseService.getEquipType(code);
		return equipType;
	}

	/////////////////////////////////////////////////////////////////
	@RequestMapping(value = "getallusers.do", method = RequestMethod.POST)
	@ResponseBody
	public DataTable getUsers() {
		return baseService.getAllUsers();
	}

	@RequestMapping(value = "adduser.do", method = RequestMethod.POST)
	@ResponseBody
	public void addUser(User user) {
		baseService.addUser(user);
	}

	@RequestMapping(value = "deleteuser.do", method = RequestMethod.POST)
	@ResponseBody
	public void deleteUser(@RequestBody String ids) {
		baseService.deleteUser(ids);
	}

	@RequestMapping(value = "updateuser.do", method = RequestMethod.POST)
	@ResponseBody
	public void updateUser(User user) {
		baseService.updateUser(user);
	}

	@RequestMapping(value = "getuser.do", method = RequestMethod.POST)
	@ResponseBody
	public User getUser(@RequestBody String id) {
		return baseService.getUser(id);
	}
    @RequestMapping(value = "getusers.do", method = RequestMethod.POST)
    @ResponseBody
    public List<Options> getUserOptions() {
        return baseService.getUserOptions();
    }
	/////////////////////////////////////////////////////////////////
	@RequestMapping(value = "getallroles.do", method = RequestMethod.POST)
	@ResponseBody
	public DataTable getRoles() {
		return baseService.getAllRoles();
	}

	@RequestMapping(value = "getroles.do", method = RequestMethod.POST)
	@ResponseBody
	public List<Options> getRoleOptions() {
		return baseService.getRoleOptions();
	}

	@RequestMapping(value = "addrole.do", method = RequestMethod.POST)
	@ResponseBody
	public void addRole(Role role) {
		baseService.addRole(role);
	}

	@RequestMapping(value = "deleterole.do", method = RequestMethod.POST)
	@ResponseBody
	public void deleteRole(@RequestBody String ids) {
		baseService.deleteRole(ids);
	}

	@RequestMapping(value = "updaterole.do", method = RequestMethod.POST)
	@ResponseBody
	public void updateRole(Role role) {
		baseService.updateRole(role);
	}

    @RequestMapping(value = "getright.do", method = RequestMethod.POST)
    @ResponseBody
    public List<String> getRight(@RequestBody String id) {
        return baseService.getRight(id);
    }

    @RequestMapping(value = "updateright.do", method = RequestMethod.POST)
    @ResponseBody
    public void updateRight(@RequestBody String ids) {
        baseService.updateRight(ids);
    }
	/////////////////////////////////////////////////////////////////
	@RequestMapping(value = "getallmenus.do", method = RequestMethod.POST)
	@ResponseBody
	public DataTable getMenus() {
		return baseService.getAllMenus();
	}

	@RequestMapping(value = "addmenu.do", method = RequestMethod.POST)
	@ResponseBody
	public void addMenu(Menu menu) {
		baseService.addMenu(menu);
	}

	@RequestMapping(value = "deletemenu.do", method = RequestMethod.POST)
	@ResponseBody
	public void deleteMenu(@RequestBody String ids) {
		baseService.deleteMenu(ids);
	}

	@RequestMapping(value = "updatemenu.do", method = RequestMethod.POST)
	@ResponseBody
	public void updateMenu(Menu menu) {
		baseService.updateMenu(menu);
	}

	@RequestMapping(value = "getmenusbylevel.do", method = RequestMethod.POST)
	@ResponseBody
	public List<Options> getMenu(@RequestBody String level) {
		return baseService.getMenu(level);
	}

    @RequestMapping(value = "getmenutree.do", method = RequestMethod.GET)
    @ResponseBody
    public List<Tree> getMenuTree() {
        return baseService.getMenuTree();
    }

    //////////////////////////////////////////////////////////////////
	@RequestMapping(value = "getallenergytypes.do", method = RequestMethod.POST)
	@ResponseBody
	public DataTable getAllEnergyTypes() {
		return baseService.getAllEnergyTypes();
	}

	@RequestMapping(value = "addenergytype.do", method = RequestMethod.POST)
	@ResponseBody
	public void addenergyType(EnergyType energyType) {
		baseService.addEnergyType(energyType);
	}

	@RequestMapping(value = "deleteenergytype.do", method = RequestMethod.POST)
	@ResponseBody
	public void deleteEnergyType(@RequestBody String codes) {
		baseService.deleteEnergyType(codes);
	}

	@RequestMapping(value = "updateenergytype.do", method = RequestMethod.POST)
	@ResponseBody
	public void updateEnergyType(EnergyType energyType) {
		baseService.updateEnergyType(energyType);
	}

	@RequestMapping(value = "updateenergytypestate.do", method = RequestMethod.POST)
	@ResponseBody
	public void updateEnergyTypestate(@RequestBody String codes) {
		baseService.updateEnergyTypestate(codes);
	}
	
	
	@RequestMapping(value = "getenergytype.do", method = RequestMethod.POST)
	@ResponseBody
	public EnergyType getenergyType(@RequestBody String code) {
		EnergyType energyType = baseService.getEnergyType(code);
		return energyType;
	}
	
	@RequestMapping(value = "getparentinfo.do", method = RequestMethod.POST)
	@ResponseBody
	public   List<Itemcode_name> getparentinfo(@RequestBody String code) {
		System.out.println(code);
		List<Itemcode_name> itemcode_name=baseService.getparentinfo(code);
		return itemcode_name;
	}
	
	@RequestMapping(value="getenergyitems.do",method= RequestMethod.POST)
	@ResponseBody
	public  List<Options> getEnergyItems(){
		List<Options> options=baseService.getEnergyItem();
		return options;
	}
////////////////////////////////学科专业基础信息/////////////////////////////////////
	@RequestMapping(value = "getalldepartments.do", method = RequestMethod.POST)
	@ResponseBody
	public DataTable getAllDepartments() {
		return baseService.getAllDepartments();
	}
	
	@RequestMapping(value="adddepartment.do",method= RequestMethod.POST)
	@ResponseBody
	public void addDepartment(Organ organ){
		baseService.addDepartment(organ);
	}
	
	@RequestMapping(value = "getdepartment.do", method = RequestMethod.POST)
	@ResponseBody
	public Organ getDepartment(@RequestBody String code) {
		Organ organ = baseService.getDepartment(code);
		return organ;
	}
	
	@RequestMapping(value = "updatedepartment.do", method = RequestMethod.POST)
	@ResponseBody
	public void updateDepartment(Organ organ) {
		baseService.updateDepartment(organ);
	}
	
	@RequestMapping(value = "getallmajors.do", method = RequestMethod.POST)
	@ResponseBody
	public DataTable getAllMajors() {
		return baseService.getAllMajors();
	}
	
	//添加专业
	@RequestMapping(value="addmajor.do",method= RequestMethod.POST)
	@ResponseBody
	public void addMajor(Major major){
		baseService.addMajor(major);
	}
	
	@RequestMapping(value="deletemajor.do",method= RequestMethod.POST)
	@ResponseBody
	public void deleteMajor(@RequestBody String ids){
		baseService.deleteMajor(ids);
	}
	
	@RequestMapping(value = "getmajor.do", method = RequestMethod.POST)
	@ResponseBody
	public Major getMajor(@RequestBody String code) {
		Major major = baseService.getMajor(code);
		return major;
	}
	@RequestMapping(value = "updatemajor.do", method = RequestMethod.POST)
	@ResponseBody
	public void updateMajor(Major major) {
		baseService.updateMajor(major);
	}
	
	// //////////////////////////////////////////////////////////////////
	@RequestMapping(value = "getallusertypes.do", method = RequestMethod.POST)
	@ResponseBody
	public DataTable getAllUserTypes() {
		return baseService.getAllUserTypes();
	}

	@RequestMapping(value = "addusertype.do", method = RequestMethod.POST)
	@ResponseBody
	public void addUserType(UserType userType) {
		baseService.addUserType(userType);
	}

	@RequestMapping(value = "deleteusertype.do", method = RequestMethod.POST)
	@ResponseBody
	public void deleteUserType(@RequestBody String codes) {
		baseService.deleteUserType(codes);
	}

	@RequestMapping(value = "updateusertype.do", method = RequestMethod.POST)
	@ResponseBody
	public void updateUserType(UserType userType) {
		baseService.updateUserType(userType);
	}

	@RequestMapping(value = "getusertype.do", method = RequestMethod.POST)
	@ResponseBody
	public UserType getUserType(@RequestBody String code) {
		UserType userType = baseService.getUserTypeByCode(code);
		return userType;
	}
	///////////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value = "createpic.do", method = RequestMethod.POST)
	@ResponseBody
	public String createPic(HttpSession session) {
		String str=MakePicture.drawPicture(70,28,session);
		session.setAttribute("check",str);
		return str;
	}
	// //////////////////////////////////////////////////////////////////
	@RequestMapping(value = "recordsnum.do", method = RequestMethod.POST)
	@ResponseBody
	public RecordsNum getRecordsNum() {
		return baseService.getRecordsNum();
	}

    @RequestMapping(value = "getusermenus.do", method = RequestMethod.POST)
    @ResponseBody
    public List<UserMenu> getUserMenus(HttpSession session){
        return baseService.getUserMenus((String)session.getAttribute("userid"));

    }
}
