package com.wintoo.dao;

import com.wintoo.model.*;
import com.wintoo.tools.Md5;
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
import java.util.*;

@Repository
@Transactional
public class BaseDao {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcOperations jdbcTemplate;
    public void addBuildPoint(String point) {
        String[] points=point.split(",");
        String sql = "update T_BD_BUILD set F_BUILDLONG=?,F_BUILDLAT=? WHERE F_BUILDID=?";
        Object[] args = { points[1],points[2],points[0] };
        jdbcTemplate.update(sql, args);
    }

    public void addOrganPoint(String point) {
        String[] points=point.split(",");
        String sql = "update T_BO_ORGAN set F_LNG=?,F_LAT=? WHERE F_ID=?";
        Object[] args = { points[1],points[2],points[0] };
        jdbcTemplate.update(sql, args);
    }


    public void addWaterPoint(String point) {
        String[] points=point.split(",");
        String sql = "update T_BE_EQUIPMENTLIST set F_MAPLNG=?,F_MAPLAT=? WHERE F_UUID=?";
        Object[] args = { points[1],points[2],points[0] };
        jdbcTemplate.update(sql, args);
    }
    public void deleteBuildPoint(String point) {
        String sql = "update T_BD_BUILD set F_BUILDLONG=?,F_BUILDLAT=? WHERE F_BUILDNAME=?";
        Object[] args = { null,null,point };
        jdbcTemplate.update(sql, args);
    }
    public void deleteOrganPoint(String point) {
        String sql = "update T_BO_ORGAN set F_LNG=?,F_LAT=? WHERE F_NAME=?";
        Object[] args = { null,null,point };
        jdbcTemplate.update(sql, args);
    }
    public void deleteWaterPoint(String point) {
        String sql = "update T_BE_EQUIPMENTLIST set F_MAPLNG=?,F_MAPLAT=? WHERE F_EQUIPID=?";
        Object[] args = { null,null,point };
        jdbcTemplate.update(sql, args);
    }
    public List<Point> getPoints(String type){
        String sql = "select F_BUILDID,F_BUILDNAME,F_BUILDLONG,F_BUILDLAT from T_BD_BUILD where F_BUILDFUNC=? ";
        Object[] args = { type };
        final List<Point> points = new ArrayList<Point>();
        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Point point = new Point();
                point.setId(rs.getString(1));
                point.setName(rs.getString(2));
                point.setLongitude(rs.getDouble(3));
                point.setLatitude(rs.getDouble(4));
                points.add(point);
            }
        });
        return points;
    }

    public List<Point> getAllPoints(){
        String sql = "select F_BUILDID,F_BUILDNAME,F_BUILDLONG,F_BUILDLAT from T_BD_BUILD where F_BUILDLONG!=0.0";
        final List<Point> points = new ArrayList<Point>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Point point = new Point();
                point.setId(rs.getString(1));
                point.setName(rs.getString(2));
                point.setLongitude(rs.getDouble(3));
                point.setLatitude(rs.getDouble(4));
                points.add(point);
            }
        });
        return points;
    }

    public List<Point> getAllOrganPoints(){
        String sql = "select F_ID,F_NAME,F_LNG,F_LAT from T_BO_ORGAN where F_PID='j4_2' and F_LNG!=0.0";
        final List<Point> points = new ArrayList<Point>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Point point = new Point();
                point.setId(rs.getString(1));
                point.setName(rs.getString(2));
                point.setLongitude(rs.getDouble(3));
                point.setLatitude(rs.getDouble(4));
                points.add(point);
            }
        });
        return points;
    }

    public List<Point> getOrganPoints(String id){
        String sql = "select F_ID,F_NAME,F_LNG,F_LAT from T_BO_ORGAN where F_ID=? and F_LNG!=0.0";
        Object[] args = { id };
        final List<Point> points = new ArrayList<Point>();
        jdbcTemplate.query(sql, args,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Point point = new Point();
                point.setId(rs.getString(1));
                point.setName(rs.getString(2));
                point.setLongitude(rs.getDouble(3));
                point.setLatitude(rs.getDouble(4));
                points.add(point);
            }
        });
        return points;
    }

    public List<Point> getWaterPoints(){
        String sql = "select a.F_UUID,b.F_REMARKINFO,a.F_MAPLNG,a.F_MAPLAT from T_BE_EQUIPMENTLIST a,T_BE_WATERMETER  b where a.F_UUID=b.F_UUID and a.F_INSTALLTYPE=0 and a.F_MAPLNG!=0.0";
        final List<Point> points = new ArrayList<Point>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Point point = new Point();
                point.setId(rs.getString(1));
                point.setName(rs.getString(2));
                point.setLongitude(rs.getDouble(3));
                point.setLatitude(rs.getDouble(4));
                points.add(point);
            }
        });
        return points;
    }
    /////////////////////////////////////////////////////
    public DataTable getAllGroups() {
        String sql = "select * from T_BD_GROUP ORDER BY F_BUILDGROUPCODE";
        DataTable dataTable = new DataTable();
        final List<Group> groups = new ArrayList<Group>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Group group = new Group();
                group.setId(rs.getString(1));
                group.setName(rs.getString(2));
                group.setAliasname(rs.getString(3));
                group.setDesc(rs.getString(4));
                group.setCode(rs.getString(5));
                group.setArea(rs.getDouble(8));
                groups.add(group);
            }
        });
        dataTable.setData(groups);
        return dataTable;
    }

    public boolean addGroup(Group group) {
        System.out.println(group.toString());
        String sql = "insert into T_BD_GROUP(F_BUILDGROUPID,F_BUILDGROUPNAME,F_GROUPALIASNAME,F_GROUPDESC,F_BUILDGROUPCODE,F_AREA,F_OPERATION) values(sys_guid(),?,?,?,?,?,'N')";
        Object[] args = { group.getName(), group.getAliasname(),group.getDesc(),group.getCode(),group.getArea() };
        int i = jdbcTemplate.update(sql, args);
        return i > 0;
    }

    public void deleteGroup(String id) {
        String sql = "delete from T_BD_GROUP where F_BUILDGROUPID=?";
        Object[] args = { id };
        jdbcTemplate.update(sql, args);
        String sql1 = "delete from T_BD_GROUPBUILDRELA where F_BUILDGROUPID=?";
        Object[] args1 = { id };
        jdbcTemplate.update(sql1, args1);
    }

    public Group getGroupById(String id) {
        String sql = "select F_BUILDGROUPID,F_BUILDGROUPNAME,F_GROUPALIASNAME,F_GROUPDESC,F_BUILDGROUPCODE from T_BD_GROUP where F_BUILDGROUPID=?";
        Object[] args = { id };
        final Group group = new Group();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                group.setId(rs.getString(1));
                group.setName(rs.getString(2));
                group.setAliasname(rs.getString(3));
                group.setDesc(rs.getString(4));
                group.setCode(rs.getString(5));
            }
        });
        return group;
    }

    public boolean updateGroupById(Group group) {
        String sql = "update T_BD_GROUP set F_BUILDGROUPCODE=?,F_BUILDGROUPNAME=?,F_GROUPALIASNAME=?,F_GROUPDESC=?,F_AREA=?,F_OPERATION='U' where F_BUILDGROUPID=?";
        Object[] args = { group.getCode(),group.getName(), group.getAliasname(),
                group.getDesc(),group.getArea(), group.getId() };
        int i = jdbcTemplate.update(sql, args);
        return i > 0;
    }

    // //////////////////////////////////////////////////////////////////////////
    public DataTable getAllBuildTypes() {
        String sql = "select * from T_BD_BUILDTYPE ORDER BY F_CODE";
        DataTable dataTable = new DataTable();
        final List<BuildType> buildTypes = new ArrayList<BuildType>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                BuildType buildType = new BuildType();
                buildType.setId(rs.getString(1));
                buildType.setName(rs.getString(2));
                buildType.setExtra(rs.getString(3));
                buildTypes.add(buildType);
            }
        });
        dataTable.setData(buildTypes);
        return dataTable;
    }

    public boolean addBuildType(BuildType buildType) {
        String sql = "insert into T_BD_BUILDTYPE(F_CODE,F_NAME,F_EXTRA) values(?,?,?)";
        Object[] args = { buildType.getId(), buildType.getName(),
                buildType.getExtra() };
        int i = jdbcTemplate.update(sql, args);
        return i > 0;
    }

    public boolean deleteBuildType(String code) {
        String sql = "delete from T_BD_BUILDTYPE where F_CODE=?";
        Object[] args = { code };
        int i = jdbcTemplate.update(sql, args);
        return i > 0;
    }

    public BuildType getBuildTypeById(String code) {
        String sql = "select * from T_BD_BUILDTYPE where F_CODE=?";
        Object[] args = { code };
        final BuildType buildType = new BuildType();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                buildType.setId(rs.getString(1));
                buildType.setName(rs.getString(2));
                buildType.setExtra(rs.getString(3));
            }
        });
        return buildType;
    }

    public boolean updateBuildTypeById(BuildType buildType) {
        String sql = "update T_BD_BUILDTYPE set F_NAME=?,F_EXTRA=? where F_CODE=?";
        Object[] args = { buildType.getName(), buildType.getExtra(),
                buildType.getId() };
        int i = jdbcTemplate.update(sql, args);
        return i > 0;
    }

    public List<Options> getBuildFuncs() {
        String sql = "select f_code,f_name from T_BD_BUILDTYPE";
        final List<Options> options=new ArrayList<Options>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Options option = new Options();
                option.setId(rs.getString(1));
                option.setName(rs.getString(2));
                options.add(option);
            }
        });
        return options;
    }

    // //////////////////////////////////////////////////////////////////////////////
    public DataTable getAllRoomTypes() {
        String sql = "select * from T_BD_ROOMTYPE";
        DataTable dataTable = new DataTable();
        final List<RoomType> roomTypes = new ArrayList<RoomType>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                RoomType roomType = new RoomType();
                roomType.setId(rs.getString(1));
                roomType.setName(rs.getString(2));
                roomType.setDesc(rs.getString(3));
                roomTypes.add(roomType);
            }
        });
        dataTable.setData(roomTypes);
        return dataTable;
    }

    public void addRoomType(RoomType roomType) {
        String sql = "insert into T_BD_ROOMTYPE(F_CODE,F_NAME,F_DESC) values(?,?,?)";
        Object[] args = { roomType.getId(), roomType.getName(),
                roomType.getDesc() };
        jdbcTemplate.update(sql, args);
    }

    public void deleteRoomType(String code) {
        String sql = "delete from T_BD_ROOMTYPE where F_CODE=?";
        Object[] args = { code };
        jdbcTemplate.update(sql, args);
    }

    public RoomType getRoomTypeById(String code) {
        String sql = "select * from T_BD_ROOMTYPE where F_CODE=?";
        Object[] args = { code };
        final RoomType roomType = new RoomType();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                roomType.setId(rs.getString(1));
                roomType.setName(rs.getString(2));
                roomType.setDesc(rs.getString(3));
            }
        });
        return roomType;
    }

    public void updateRoomTypeById(RoomType roomType) {
        String sql = "update T_BD_ROOMTYPE set F_NAME=?,F_DESC=? where F_CODE=?";
        Object[] args = { roomType.getName(), roomType.getDesc(),
                roomType.getId() };
        jdbcTemplate.update(sql, args);
    }

    // //////////////////////////////////////////////////////////////////////////////
    public DataTable getAllOrganTypes() {
        String sql = "select * from T_BO_ORGANTYPE";
        DataTable dataTable = new DataTable();
        final List<OrganType> organTypes = new ArrayList<OrganType>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                OrganType organType = new OrganType();
                organType.setId(rs.getString(1));
                organType.setName(rs.getString(2));
                organType.setDesc(rs.getString(3));
                organTypes.add(organType);
            }
        });
        dataTable.setData(organTypes);
        return dataTable;
    }

    public void addOrganType(OrganType organType) {
        String sql = "insert into T_BO_ORGANTYPE(F_CODE,F_NAME,F_DESC) values(?,?,?)";
        Object[] args = { organType.getId(), organType.getName(),
                organType.getDesc() };
        jdbcTemplate.update(sql, args);
    }

    public void deleteOrganType(String code) {
        String sql = "delete from T_BO_ORGANTYPE where F_CODE=?";
        Object[] args = { code };
        jdbcTemplate.update(sql, args);
    }

    public OrganType getOrganTypeById(String code) {
        String sql = "select * from T_BO_ORGANTYPE where F_CODE=?";
        Object[] args = { code };
        final OrganType organType = new OrganType();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                organType.setId(rs.getString(1));
                organType.setName(rs.getString(2));
                organType.setDesc(rs.getString(3));
            }
        });
        return organType;
    }

    public void updateOrganTypeById(OrganType organType) {
        String sql = "update T_BO_ORGANTYPE set F_NAME=?,F_DESC=? where F_CODE=?";
        Object[] args = { organType.getName(), organType.getDesc(),
                organType.getId() };
        jdbcTemplate.update(sql, args);
    }

    public List<Options> getOrganTypes(){
        String sql="select F_CODE,F_NAME FROM T_BO_ORGANTYPE ORDER BY F_NAME";
        final List<Options> options=new ArrayList<Options>();
        jdbcTemplate.query(sql,new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Options option=new Options();
                option.setId(rs.getString("F_CODE"));
                option.setName(rs.getString("F_NAME"));
                options.add(option);
            }
        });
        return options;
    }
    /////////////////////////////////////////////////////
    public DataTable getAllUsers() {
        String sql = "select t1.f_uuid,t1.f_name,t1.f_roleid,t1.f_phone,t1.f_email,t1.f_remark,t2.f_name,t1.f_account from T_BS_USER t1,T_BS_ROLE t2 WHERE t1.F_ROLEID=t2.F_UUID ORDER BY t1.F_NAME";
        DataTable dataTable = new DataTable();
        final List<User> users = new ArrayList<User>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                User user = new User();
                user.setUuid(rs.getString(1));
                user.setName(rs.getString(2));
                user.setRoleid(rs.getString(3));
                user.setPhone(rs.getString(4));
                user.setEmail(rs.getString(5));
                user.setRemark(rs.getString(6));
                user.setRole(rs.getString(7));
                user.setAccount(rs.getString(8));
                users.add(user);
            }
        });
        dataTable.setData(users);
        return dataTable;
    }

    public void addUser(User user) {
        String sql = "insert into T_BS_USER(F_UUID,F_ACCOUNT,F_PASSWD,F_NAME,F_ROLEID,F_PHONE,F_EMAIL,F_REMARK) values(sys_guid(),?,?,?,?,?,?,?)";
        Object[] args = { user.getAccount(), Md5.MD5(user.getPasswd()),user.getName(),user.getRoleid(),user.getPhone(),user.getEmail(),user.getRemark() };
        jdbcTemplate.update(sql, args);
    }

    public void deleteUser(String id) {
        String sql = "delete from T_BS_USER where F_UUID=?";
        Object[] args = { id };
        jdbcTemplate.update(sql, args);
    }

    public void updateUserById(User user) {
        String sql = "update T_BS_USER set F_ACCOUNT=?,F_PASSWD=?,F_NAME=?,F_ROLEID=?,F_PHONE=?,F_EMAIL=?,F_REMARK=? where F_UUID=?";
        Object[] args = { user.getAccount(),Md5.MD5(user.getPasswd()),user.getName(),user.getRoleid(),user.getPhone(),user.getEmail(),user.getRemark(),user.getUuid() };
        jdbcTemplate.update(sql, args);
    }

    public User getUser(String id){
        String sql="select t1.f_uuid,t1.f_account,t1.f_name,t1.f_roleid,t1.f_phone,t1.f_email,t1.f_remark,t2.f_name from T_BS_USER t1,T_BS_ROLE t2 WHERE t1.F_ROLEID=t2.F_UUID AND t1.F_UUID=?";
        Object[] args={id};
        final User user=new User();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                user.setUuid(rs.getString(1));
                user.setAccount(rs.getString(2));
                user.setName(rs.getString(3));
                user.setRoleid(rs.getString(4));
                user.setPhone(rs.getString(5));
                user.setEmail(rs.getString(6));
                user.setRemark(rs.getString(7));
                user.setRole(rs.getString(8));
            }
        });
        return user;
    }
    public List<Options> getUserOptions(){
        String sql="select F_UUID,F_NAME FROM T_BS_USER ORDER BY F_NAME";
        final List<Options> options=new ArrayList<Options>();
        jdbcTemplate.query(sql,new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Options option=new Options();
                option.setId(rs.getString("F_UUID"));
                option.setName(rs.getString("F_NAME"));
                options.add(option);
            }
        });
        return options;
    }
    public boolean checkLogin(LoginForm loginForm) {
        String sql = "SELECT count(*) num FROM t_bs_user WHERE f_account=? and f_passwd=?";
        Object[] args = { loginForm.getId(),Md5.MD5(loginForm.getPasswd())};
        return (Long)jdbcTemplate.queryForMap(sql, args).get("num") != 0L;
    }
    /////////////////////////////////////////////////////
    public DataTable getAllRoles() {
        String sql = "select t1.f_uuid,t1.f_name from T_BS_ROLE t1";
        DataTable dataTable = new DataTable();
        final List<Role> roles = new ArrayList<Role>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Role role= new Role();
                role.setUuid(rs.getString(1));
                role.setName(rs.getString(2));
                roles.add(role);
            }
        });
        dataTable.setData(roles);
        return dataTable;
    }
    public List<Options> getRoleOptions(){
        String sql="select F_UUID,F_NAME FROM T_BS_Role ORDER BY F_NAME";
        final List<Options> options=new ArrayList<Options>();
        jdbcTemplate.query(sql,new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Options option=new Options();
                option.setId(rs.getString("F_UUID"));
                option.setName(rs.getString("F_NAME"));
                options.add(option);
            }
        });
        return options;
    }
    public void addRole(Role role) {
        String sql = "insert into T_BS_ROLE(F_UUID,F_NAME) values(sys_guid(),?)";
        Object[] args = { role.getName() };
        jdbcTemplate.update(sql, args);
    }

    public void deleteRole(String id) {
        String sql = "delete from T_BS_ROLE where F_UUID=?";
        Object[] args = { id };
        jdbcTemplate.update(sql, args);
    }

    public void updateRoleById(Role role) {
        String sql = "update T_BS_ROLE set F_NAME=? where F_UUID=?";
        Object[] args = { role.getName(),role.getUuid() };
        jdbcTemplate.update(sql, args);
    }

    public List<String> getRight(String id){
        String sql="select F_MENUID FROM T_BS_RIGHT WHERE F_ROLEID=?";
        Object[] args={id};
        final List<String> rights=new ArrayList<String>();
        jdbcTemplate.query(sql,args,new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                rights.add(rs.getString(1));
            }
        });
        return rights;
    }

    public void updateRight(String ids){
        String[] s=ids.split("#");
        final String roleid=s[0];
        String sql = "delete from T_BS_Right where F_ROLEID=?";
        Object[] args = { roleid };
        jdbcTemplate.update(sql, args);

        sql="insert into T_BS_Right(F_ROLEID,F_MENUID) values(?,?)";
        final String[] menuids=s[1].split(",");
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public int getBatchSize() {
                return menuids.length;
            }
            @Override
            public void setValues(PreparedStatement ps, int i)throws SQLException {
                ps.setString(1, roleid);
                ps.setString(2, menuids[i]);
            }
        });
    }
    /////////////////////////////////////////////////////
    public DataTable getAllMenus() {
        String sql = "select DISTINCT t1.f_uuid,t1.f_name,t1.f_levels,t1.f_upmenu,t1.f_pagename,t1.f_image,t2.f_name,t1.f_code from T_BS_MENU t1,T_BS_MENU t2 WHERE t1.f_upmenu=t2.f_uuid(+) ORDER BY t1.F_CODE";
        DataTable dataTable = new DataTable();
        final List<Menu> menus = new ArrayList<Menu>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Menu menu = new Menu();
                menu.setUuid(rs.getString(1));
                menu.setName(rs.getString(2));
                menu.setLevels(rs.getString(3));
                menu.setUpmenuid(rs.getString(4));
                menu.setPagename(rs.getString(5));
                menu.setImage(rs.getString(6));
                menu.setUpmenu(rs.getString(7));
                menu.setCode(rs.getString(8));
                menus.add(menu);
            }
        });
        dataTable.setData(menus);
        return dataTable;
    }

    public void addMenu(Menu menu) {
        String sql = "insert into T_BS_MENU(F_UUID,F_NAME,F_LEVELS,F_UPMENU,F_PAGENAME,F_IMAGE,F_CODE) values(sys_guid(),?,?,?,?,?,?)";
        Object[] args = { menu.getName(),menu.getLevels(),menu.getUpmenuid(),menu.getPagename(),menu.getImage(),menu.getCode() };
        jdbcTemplate.update(sql, args);
    }

    public void deleteMenu(String id) {
        String sql = "delete from T_BS_MENU where F_UUID=?";
        Object[] args = { id };
        jdbcTemplate.update(sql, args);
    }

    public void updateMenuById(Menu menu) {
        String sql = "update T_BS_MENU set F_NAME=?,F_LEVELS=?,F_UPMENU=?,F_PAGENAME=?,F_IMAGE=?,F_CODE=? where F_UUID=?";
        Object[] args = { menu.getName(),menu.getLevels(),menu.getUpmenuid(),menu.getPagename(),menu.getImage(),menu.getCode(),menu.getUuid() };
        jdbcTemplate.update(sql, args);
    }

    public List<Options> getMenu(String level){
        String sql="select F_UUID,F_NAME FROM T_BS_MENU WHERE F_LEVELS=? ORDER BY F_Code";
        Object[] args = {level};
        final List<Options> options=new ArrayList<Options>();
        jdbcTemplate.query(sql,args, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Options option=new Options();
                option.setId(rs.getString("F_UUID"));
                option.setName(rs.getString("F_NAME"));
                options.add(option);
            }
        });
        return options;
    }

    public List<Tree> getMenuTree(){
        String sql="select F_UUID,F_NAME,F_UPMENU FROM T_BS_MENU  ORDER BY F_CODE";
        final List<Tree> trees=new ArrayList<Tree>();
        jdbcTemplate.query(sql, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Tree tree=new Tree();
                tree.setId(rs.getString("F_UUID"));
                tree.setText(rs.getString("F_NAME"));
                if(rs.getString("F_UPMENU")!=null)
                    tree.setParent(rs.getString("F_UPMENU"));
                else
                    tree.setParent("#");
                trees.add(tree);
            }
        });
        return trees;
    }
    // //////////////////////////////////////////////////////////////////////////
    public DataTable getAllEquipTypes() {
        String sql = "select * from T_BE_EQUIPMENTTYPE";
        DataTable dataTable = new DataTable();
        final List<EquipType> equipTypes = new ArrayList<EquipType>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                EquipType equipType = new EquipType();
                equipType.setUuid(rs.getString(1));
                equipType.setType(rs.getString(2));
                equipType.setSubtype(rs.getString(3));
                equipType.setParameter(rs.getString(4));
                equipTypes.add(equipType);
            }
        });
        dataTable.setData(equipTypes);
        return dataTable;
    }

    public boolean addEquipType(EquipType equipType) {
        String sql = "insert into T_BE_EQUIPMENTTYPE(F_UUID,F_TYPE,F_SUBTYPE,F_PARAMETER) values(?,?,?,?)";
        Object[] args = { equipType.getUuid(),equipType.getType(), equipType.getSubtype(),
                equipType.getParameter() };
        int i = jdbcTemplate.update(sql, args);
        return i > 0;
    }

    public boolean deleteEquipType(String code) {
        String sql = "delete from T_BE_EQUIPMENTTYPE where F_UUID=?";
        Object[] args = { code };
        int i = jdbcTemplate.update(sql, args);
        return i > 0;
    }

    public EquipType getEquipTypeById(String code) {
        String sql = "select * from T_BE_EQUIPMENTTYPE where F_UUID=?";
        Object[] args = { code };
        final EquipType equipType = new EquipType();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                equipType.setUuid(rs.getString(1));
                equipType.setType(rs.getString(2));
                equipType.setSubtype(rs.getString(3));
                equipType.setParameter(rs.getString(4));
            }
        });
        return equipType;
    }

    public boolean updateEquipTypeById(EquipType equipType) {
        String sql = "update T_BE_EQUIPMENTTYPE set F_TYPE=?,F_SUBTYPE=?,F_PARAMETER=? where F_UUID=?";
        Object[] args = { equipType.getType(),equipType.getSubtype(), equipType.getParameter(),
                equipType.getUuid() };
        int i = jdbcTemplate.update(sql, args);
        return i > 0;
    }




    ////////////////////////////////////////////////////////////////////////////////
    public DataTable getAllEnergyTypes() {
        String sql = "select F_EnergyItemLevel,F_EnergyItemCode,F_EnergyItemName,F_ParentItemCode,F_ParentItemName,F_EnergyItemType,F_EnergyItemUnit,F_EnergyItemFml,F_EnergyItemState from T_DT_ENERGYITEMDICT";
        DataTable dataTable = new DataTable();
        final List<EnergyType> energyTypes = new ArrayList<EnergyType>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                EnergyType energyType = new EnergyType();
                energyType.setItemlevel(rs.getInt(1));
                energyType.setItemcode(rs.getString(2));
                energyType.setItemname(rs.getString(3));
                energyType.setParentitemcode(rs.getString(4));
                energyType.setParentitemname(rs.getString(5));
                energyType.setItemtype(rs.getString(6));
                energyType.setItemunit(rs.getString(7));
                energyType.setItemfml(rs.getDouble(8));
                energyType.setItemstate(rs.getInt(9));
                energyType.setItemname(energyType.getItemname());
                energyTypes.add(energyType);
            }
        });
        dataTable.setData(energyTypes);
        return dataTable;
    }

    public void addEnergyType(EnergyType energyType) {
        String sql1 = "insert into T_DT_ENERGYITEMDICT(F_EnergyItemLevel,F_EnergyItemCode,F_EnergyItemName,F_ParentItemCode,F_ParentItemName,F_EnergyItemType,F_EnergyItemUnit,F_EnergyItemFml,F_EnergyItemState) values(?,?,?,?,?,?,?,?,?)";
        String sql2 = "insert into T_DT_ENERGYITEMDICT(F_EnergyItemLevel,F_EnergyItemCode,F_EnergyItemName,F_EnergyItemType,F_EnergyItemUnit,F_EnergyItemFml,F_EnergyItemState) values(?,?,?,?,?,?,?)";
        String[] parentcode_names = null;
        if(energyType.getItemlevel()!=1)
        {
            String parentcode_name=energyType.getParentitemname();
            parentcode_names=parentcode_name.split(",");
            Object[] args = new Object[] {
                    energyType.getItemlevel(),
                    energyType.getItemcode(),
                    energyType.getItemname(),
                    parentcode_names[0],
                    parentcode_names[1],
                    energyType.getItemtype(),
                    energyType.getItemunit(),
                    energyType.getItemfml(),
                    energyType.getItemstate()
            };
            jdbcTemplate.update(sql1, args);
        }
        else{
            Object[] args = new Object[] {
                    energyType.getItemlevel(),
                    energyType.getItemcode(),
                    energyType.getItemname(),
                    energyType.getItemtype(),
                    energyType.getItemunit(),
                    energyType.getItemfml(),
                    energyType.getItemstate()
            };
            jdbcTemplate.update(sql2, args);
        }
    }

    public void deleteEnergyType(String ItemCode) {
        String sql = "delete from T_DT_ENERGYITEMDICT where F_EnergyItemCode=?";
        Object[] args = { ItemCode };
        jdbcTemplate.update(sql, args);
    }

    public EnergyType getEnergyTypeByCode(String ItemCode) {
        String sql = "select F_EnergyItemLevel,F_EnergyItemCode,F_EnergyItemName,F_ParentItemCode,F_ParentItemName,F_EnergyItemType,F_EnergyItemUnit,F_EnergyItemFml,F_EnergyItemState from T_DT_ENERGYITEMDICT where F_EnergyItemCode=?";
        Object[] args = { ItemCode };
        final EnergyType energyType = new EnergyType();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {

                energyType.setItemlevel(rs.getInt(1));
                energyType.setItemcode(rs.getString(2));
                energyType.setItemname(rs.getString(3));
                energyType.setParentitemcode(rs.getString(4));
                energyType.setParentitemname(rs.getString(5));
                energyType.setItemtype(rs.getString(6));
                energyType.setItemunit(rs.getString(7));
                energyType.setItemfml(rs.getDouble(8));
                energyType.setItemstate(rs.getInt(9));
            }
        });
        return energyType;
    }

    public void updateEnergyTypeByCode(EnergyType energyType) {
        String sql = null;
        String[] parentcode_names = null;
        if(energyType.getItemlevel()!=1)
        {
            sql="update T_DT_ENERGYITEMDICT set F_EnergyItemLevel=?,F_EnergyItemName=?,F_ParentItemCode=?,F_ParentItemName=?,F_EnergyItemType=?,F_EnergyItemUnit=?,F_EnergyItemFml=?,F_EnergyItemState=? where F_EnergyItemCode=?";
            String parentcode_name=energyType.getParentitemname();
            parentcode_names=parentcode_name.split(",");
            Object[] args = new Object[] {
                    energyType.getItemlevel(),
                    energyType.getItemname(),
                    parentcode_names[0],
                    parentcode_names[1],
                    energyType.getItemtype(),
                    energyType.getItemunit(),
                    energyType.getItemfml(),
                    energyType.getItemstate(),
                    energyType.getItemcode()
            };
            jdbcTemplate.update(sql, args);
        }
        else
        {
            sql="update T_DT_ENERGYITEMDICT set F_EnergyItemLevel=?,F_EnergyItemName=?,F_EnergyItemType=?,F_EnergyItemUnit=?,F_EnergyItemFml=?,F_EnergyItemState=? where F_EnergyItemCode=?";
            Object[] args = new Object[] {
                    energyType.getItemlevel(),
                    energyType.getItemname(),
                    energyType.getItemtype(),
                    energyType.getItemunit(),
                    energyType.getItemfml(),
                    energyType.getItemstate(),
                    energyType.getItemcode()
            };
            jdbcTemplate.update(sql, args);
        }

    }


    public void updateEnergyTypestate(String id) {
        String sql = "update T_DT_ENERGYITEMDICT set F_EnergyItemState=? where F_EnergyItemCode=?";
        String[] ids=id.split(",");
        Object[] args = {ids[1] , ids[0]};
        jdbcTemplate.update(sql, args);
        System.out.println(id);
    }

    public List<Itemcode_name> getparentinfo(String codes) {
        String sql = "select F_EnergyItemCode,F_EnergyItemName from T_DT_ENERGYITEMDICT where F_EnergyItemLevel=?";
        Object[] args = { codes };
        final List<Itemcode_name> itemcode_names=new ArrayList<Itemcode_name>();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Itemcode_name itemcode_name=new Itemcode_name();
                itemcode_name.setItem_code(rs.getString("F_EnergyItemCode"));
                itemcode_name.setItem_name(rs.getString("F_EnergyItemName"));
                itemcode_names.add(itemcode_name);
            }
        });
        return itemcode_names;
    }


    public List<Options> getEnergyItem(){
        String sql="select F_ENERGYITEMCODE,F_ENERGYITEMNAME FROM T_DT_ENERGYITEMDICT ORDER BY F_ENERGYITEMCODE";
        final List<Options> options=new ArrayList<Options>();
        jdbcTemplate.query(sql, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Options option=new Options();
                option.setId(rs.getString("F_ENERGYITEMCODE"));
                option.setName(rs.getString("F_ENERGYITEMNAME"));
                options.add(option);
            }
        });
        return options;
    }
    ////////////////////////////////////////学科专业基础信息/////////////////////////////////////////
    public DataTable  getAllDepartments() {
        String sql = "select F_ID,F_CODE,F_NAME from T_BO_ORGAN where F_PID='00' and F_TYPECODE='B'";
        DataTable dataTable = new DataTable();
        final List<Organ> organs = new ArrayList<Organ>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Organ organ = new Organ();
                organ.setId(rs.getString("F_ID"));
                organ.setCode(rs.getString("F_CODE"));
                organ.setText(rs.getString("F_NAME"));
                organs.add(organ);
            }
        });
        dataTable.setData(organs);
        return dataTable;
    }


    public void addDepartment(Organ organ){
        String sql="insert into T_BO_ORGAN(F_CODE,F_ID,F_NAME,F_PID,F_TYPECODE) values(?,?,?,?,?)";
        Object[] args={organ.getCode(),organ.getId(),organ.getText(),organ.getParent(),organ.getType()};
        jdbcTemplate.update(sql, args);
    }

    public Organ getDepartmentByID(String Id) {
        String sql = "select * from T_BO_ORGAN where F_ID=?";
        Object[] args = { Id };
        final Organ organ = new Organ();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                organ.setCode(rs.getString("F_CODE"));
                organ.setText(rs.getString("F_NAME"));
                organ.setId(rs.getString("F_ID"));
            }
        });
        return organ;
    }


    public void updateDepartmentById(Organ organ) {
        String sql="update T_BO_ORGAN set F_CODE=?,F_NAME=? where F_ID=?";
        Object[] args = new Object[] {
                organ.getCode(),
                organ.getText(),
                organ.getId()
        };
        jdbcTemplate.update(sql, args);
    }

    //专业信息列表信息获取
    public DataTable  getAllMajors() {
        String sql = "select * from T_BS_MAJOR";
        DataTable dataTable = new DataTable();
        final List<Major> majors = new ArrayList<Major>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Major major = new Major();
                major.setCode(rs.getString("F_CODE"));
                major.setName(rs.getString("F_NAME"));
                major.setYears(rs.getString("F_YEARS"));
                major.setDepartmentcode(rs.getString("F_DEPARTMENTCODE"));
                major.setDepartmentname(rs.getString("F_DEPARTMENTNAME"));
                majors.add(major);
            }
        });
        dataTable.setData(majors);
        return dataTable;
    }

    //添加专业
    public void addMajor(Major major) {
        String sql="insert into T_BS_MAJOR(F_CODE,F_NAME,F_YEARS,F_DEPARTMENTCODE,F_DEPARTMENTNAME) values(?,?,?,?,?)";
        String[] departments=major.getDepartmentname().split(",");
        Object[] args={major.getCode(),major.getName(),major.getYears(),departments[0],departments[1]};
        jdbcTemplate.update(sql, args);

    }
    //删除专业
    public void deleteMajor(String id){
        String sql="delete from T_BS_MAJOR where F_CODE=?";
        Object[] args = { id };
        jdbcTemplate.update(sql, args);
    }


    public Major getMajorByCode(String Code) {
        String sql = "select * from T_BS_MAJOR where F_CODE=?";
        Object[] args = { Code };
        final Major major = new Major();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {

                major.setCode(rs.getString("F_CODE"));
                major.setName(rs.getString("F_NAME"));
                major.setYears(rs.getString("F_YEARS"));
                major.setDepartmentcode(rs.getString("F_DEPARTMENTCODE"));
                major.setDepartmentname(rs.getString("F_DEPARTMENTNAME"));
            }
        });
        return major;
    }

    public void updateMajorByCode(Major major) {
        String sql="update T_BS_MAJOR set F_NAME=?,F_YEARS=?,F_DEPARTMENTCODE=?,F_DEPARTMENTNAME=? where F_CODE=?";
        String[] departments=major.getDepartmentname().split(",");
        Object[] args={major.getName(),major.getYears(),departments[0],departments[1],major.getCode()};
        //System.out.println(major.getName()+major.getYears()+departments[0]+departments[1]+major.getCode());
        jdbcTemplate.update(sql, args);
    }


    // //////////////////////////////////////////////////////////////////////////////
    public DataTable getAllUserTypes() {
        String sql = "select * from T_BS_USERTYPE";
        DataTable dataTable = new DataTable();
        final List<UserType> userTypes = new ArrayList<UserType>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                UserType userType = new UserType();
                userType.setCode(rs.getString("F_CODE"));
                userType.setName(rs.getString("F_NAME"));
                userTypes.add(userType);
            }
        });
        dataTable.setData(userTypes);
        return dataTable;
    }

    public void addUserType(UserType userType) {
        String sql = "insert into T_BS_USERTYPE(F_CODE,F_NAME) values(?,?)";
        Object[] args = { userType.getCode(), userType.getName()};
        jdbcTemplate.update(sql, args);
    }

    public void deleteUserType(String code) {
        String sql = "delete from T_BS_USERTYPE where F_CODE=?";
        Object[] args = { code };
        jdbcTemplate.update(sql, args);
    }

    public UserType getUserTypeByCode(String code) {
        String sql = "select * from T_BS_USERTYPE where F_CODE=?";
        Object[] args = { code };
        final UserType userType = new UserType();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                userType.setCode(rs.getString("F_CODE"));
                userType.setName(rs.getString("F_NAME"));
            }
        });
        return userType;
    }

    public void updateUserTypeByCode(UserType userType) {
        String sql = "update T_BS_USERTYPE set F_NAME=? where F_CODE=?";
        Object[] args = { userType.getName(),userType.getCode() };
        jdbcTemplate.update(sql, args);
    }
    ////////////////////////////////////////////////////////////////
    public RecordsNum getRecordsNum(){
        RecordsNum recordsNum=new RecordsNum();
        String sql="select count(*) num from T_BD_BUILD";
        recordsNum.setBuild((int)jdbcTemplate.queryForMap(sql).get("num"));
        sql="select count(*) num from t_bo_organ";
        recordsNum.setOrgan((int)jdbcTemplate.queryForMap(sql).get("num"));
        sql="select count(*) num from t_be_gateway";
        recordsNum.setGateway((int)jdbcTemplate.queryForMap(sql).get("num"));
        sql="select count(*) num from t_be_ammeter where f_gateways_uuid!='#'";
        recordsNum.setAmmeter((int)jdbcTemplate.queryForMap(sql).get("num"));
        sql="select count(*) num from t_be_watermeter where f_gateways_uuid!='#'";
        recordsNum.setWatermeter((int)jdbcTemplate.queryForMap(sql).get("num"));
        return recordsNum;
    }

    public List<UserMenu> getUserMenus(String id) {
        String sql = "select t3.f_name,t3.f_pagename,t3.f_image,t3.f_code from t_bs_user t1,t_bs_right t2,t_bs_menu t3 where t1.f_roleid=t2.f_roleid and t2.f_menuid=t3.f_uuid and t1.f_account=? ORDER BY t3.f_code";
        List<UserMenu> userMenus = new ArrayList<UserMenu>();
        Object[] args={ id };
        final Map<String,UserMenu> menuMap = new HashMap<String, UserMenu>();
        jdbcTemplate.query(sql, args,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String code=rs.getString("F_CODE");
                if(code.charAt(1)=='0'){
                    UserMenu userMenu = new UserMenu();
                    userMenu.setTxt(rs.getString("F_NAME"));
                    userMenu.setImage(rs.getString("F_IMAGE"));
                    userMenu.setId(rs.getString("F_PAGENAME"));
                    userMenu.setCode(code);
                    userMenu.setSubmenu(new ArrayList<UserMenu>());
                    menuMap.put(code.substring(0,1),userMenu);
                }
                else{
                    if(menuMap.containsKey(code.substring(0, 1))) {
                        UserMenu userMenu = menuMap.get(code.substring(0, 1));
                        UserMenu subMenu = new UserMenu();
                        subMenu.setTxt(rs.getString("F_NAME"));
                        subMenu.setId(rs.getString("F_PAGENAME"));
                        subMenu.setCode(code);
                        userMenu.getSubmenu().add(subMenu);
                    }
                }
            }
        });
        for(UserMenu userMenu: menuMap.values())
            userMenus.add(userMenu);
        Collections.sort(userMenus);
        return userMenus;
    }

    ////////////////////////////

}
