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
@Transactional(value = "primaryTransactionManager")
public class BuildDao {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcOperations jdbcTemplate;

    public DataTable getAllBuilds(){
        String sql="select a.F_BUILDID,a.F_BUILDCODE,a.F_BUILDNAME,b.F_BUILDGROUPNAME,b.F_BUILDGROUPID,d.F_NAME,d.F_CODE,a.F_TOTALAREA,a.F_PEOPLE,a.F_AIRAREA,a.F_BUILDYEAR,a.F_UPFLOOR,a.F_DOWNFLOOR,a.F_ZEROFLOOR from T_BD_BUILDBASEINFO a,T_BD_BUILDGROUPBASEINFO b,T_BD_BUILDGROUPRELAINFO c,T_BD_BUILDTYPE d " +
                "where a.F_BUILDID=c.F_BUILDID AND b.F_BUILDGROUPID=c.F_BUILDGROUPID AND a.F_BUILDFUNC=d.F_CODE";
        DataTable dataTable=new DataTable();
        final List<Build> builds=new ArrayList<Build>();
        jdbcTemplate.query(sql, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Build build=new Build();
                build.setId(rs.getString("F_BUILDID"));
                build.setCode(rs.getString("F_BUILDCODE"));
                build.setName(rs.getString("F_BUILDNAME"));
                build.setGroup(rs.getString("F_BUILDGROUPNAME"));
                build.setGroupid(rs.getString("F_BUILDGROUPID"));
                build.setBuildtype(rs.getString("F_NAME"));
                build.setBuildtypeid(rs.getString("F_CODE"));
                build.setArea(rs.getDouble("F_TOTALAREA"));
                build.setPeople(rs.getInt("F_PEOPLE"));
                build.setAirarea(rs.getDouble("F_AIRAREA"));
                build.setYear(rs.getInt("F_BUILDYEAR"));
                build.setZerofloor(rs.getInt("F_ZEROFLOOR"));
                build.setUpfloor(rs.getInt("F_UPFLOOR"));
                build.setDownfloor(rs.getInt("F_DOWNFLOOR"));
                builds.add(build);
            }
        });
        dataTable.setData(builds);
        return dataTable;
    }

    public void addBuild(Build build){
        System.out.println(build.getUpfloor());
        //先添加关系数据，再添加建筑信息,顺序不能对调
        String sql1="insert into T_BD_BUILDGROUPRELAINFO(F_BUILDID,F_BUILDGROUPID,F_OPERATION) values(?,?,'N')";
        Object[] args1={build.getId(),build.getGroupid()};
        jdbcTemplate.update(sql1, args1);

        String sql="insert into T_BD_BUILDBASEINFO(F_BUILDID,F_BUILDCODE,F_BUILDNAME,F_BUILDFUNC,F_TOTALAREA,F_PEOPLE,F_AIRAREA,F_BUILDYEAR,F_UPFLOOR,F_DOWNFLOOR,F_ZEROFLOOR,F_OPERATION) values(?,?,?,?,?,?,?,?,?,?,'N')";
        Object[] args={build.getId(),build.getCode(),build.getName(),build.getBuildtypeid(),build.getArea(),build.getPeople(),build.getAirarea(),build.getYear(),build.getUpfloor(),build.getDownfloor(),build.getZerofloor()};
        jdbcTemplate.update(sql, args);

    }

    public void deleteBuild(String id){
        //建筑信息先删除，再删除建筑区域关联信息，顺序不可调换
        String sql="delete from T_BD_BUILDBASEINFO where F_BUILDID=?";
        Object[] args={id};
        jdbcTemplate.update(sql,args);
        String sql1="delete from T_BD_BUILDGROUPRELAINFO where F_BUILDID=?";
        Object[] args1={id};
        jdbcTemplate.update(sql1,args1);
        String sql3 = "delete from T_BD_ROOM where F_FLOORID in (select F_ID from T_BD_FLOOR where F_BUILDID=?)";
        Object[] args3 = { id };
        jdbcTemplate.update(sql3, args3);
        String sql2 = "delete from T_BD_FLOOR where F_BUILDID=?";
        Object[] args2 = { id };
        jdbcTemplate.update(sql2, args2);
    }

    public Build getBuildById(String id){
        String sql="select a.F_BUILDID,a.F_BUILDCODE,a.F_BUILDNAME,b.F_BUILDGROUPNAME,b.F_BUILDGROUPID,d.F_CODE,d.F_NAME,a.F_TOTALAREA,a.F_AIRAREA,a.F_BUILDYEAR,a.F_UPFLOOR,a.F_DOWNFLOOR,a.F_ZEROFLOOR from T_BD_BUILDBASEINFO a,T_BD_BUILDGROUPBASEINFO b,T_BD_BUILDGROUPRELAINFO c,T_BD_BUILDTYPE d " +
                "where a.F_BUILDID=? AND a.F_BUILDID=c.F_BUILDID AND b.F_BUILDGROUPID=c.F_BUILDGROUPID AND a.F_BUILDFUNC=d.F_CODE";
        Object[] args={id};
        final Build build=new Build();
        jdbcTemplate.query(sql, args ,new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                build.setId(rs.getString("F_BUILDID"));
                build.setCode(rs.getString("F_BUILDCODE"));
                build.setName(rs.getString("F_BUILDNAME"));
                build.setGroup(rs.getString("F_BUILDGROUPNAME"));
                build.setGroupid(rs.getString("F_BUILDGROUPID"));
                build.setBuildtypeid(rs.getString("F_CODE"));
                build.setBuildtype(rs.getString("F_NAME"));
                build.setArea(rs.getDouble("F_TOTALAREA"));
                build.setAirarea(rs.getDouble("F_AIRAREA"));
                build.setYear(rs.getInt("F_BUILDYEAR"));
                build.setUpfloor(rs.getInt("F_UPFLOOR"));
                build.setZerofloor(rs.getInt("F_ZEROFLOOR"));
                build.setDownfloor(rs.getInt("F_DOWNFLOOR"));
            }
        });
        return build;
    }

    public Build getBuildByName(String name){
        String sql="select a.F_BUILDID,a.F_BUILDNAME,b.F_BUILDGROUPNAME,b.F_BUILDGROUPID,d.F_CODE,d.F_NAME,a.F_TOTALAREA,a.F_AIRAREA,a.F_BUILDYEAR,a.F_UPFLOOR+a.F_DOWNFLOOR+a.F_ZEROFLOOR as FLOOR from T_BD_BUILDBASEINFO a,T_BD_BUILDGROUPBASEINFO b,T_BD_BUILDGROUPRELAINFO c,T_BD_BUILDTYPE d " +
                "where a.F_BUILDNAME=? AND a.F_BUILDID=c.F_BUILDID AND b.F_BUILDGROUPID=c.F_BUILDGROUPID AND a.F_BUILDFUNC=d.F_CODE";
        Object[] args={name};
        final Build build=new Build();
        jdbcTemplate.query(sql, args ,new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                build.setId(rs.getString("F_BUILDID"));
                build.setName(rs.getString("F_BUILDNAME"));
                build.setGroup(rs.getString("F_BUILDGROUPNAME"));
                build.setGroupid(rs.getString("F_BUILDGROUPID"));
                build.setBuildtypeid(rs.getString("F_CODE"));
                build.setBuildtype(rs.getString("F_NAME"));
                build.setArea(rs.getDouble("F_TOTALAREA"));
                build.setAirarea(rs.getDouble("F_AIRAREA"));
                build.setYear(rs.getInt("F_BUILDYEAR"));
                build.setUpfloor(rs.getInt("FLOOR"));
            }
        });
        return build;
    }

    public void updateBuildById(Build build){
        //现更新建筑信息，再更新关系表，顺序不能对调！
        String sql="update T_BD_BUILDBASEINFO set F_BUILDCODE=?,F_BUILDNAME=?,F_BUILDFUNC=?,F_TOTALAREA=?,F_PEOPLE=?,F_AIRAREA=?,F_BUILDYEAR=?,F_UPFLOOR=?,F_DOWNFLOOR=?,F_ZEROFLOOR=?,F_OPERATION='U' where F_BUILDID=?";
        Object[] args={build.getCode(),build.getName(),build.getBuildtypeid(),build.getArea(),build.getPeople(),build.getAirarea(),build.getYear(),build.getUpfloor(),build.getDownfloor(),build.getZerofloor(),build.getId()};
        jdbcTemplate.update(sql,args);
        String sql1="update T_BD_BUILDGROUPRELAINFO set F_BUILDGROUPID=?,F_OPERATION='U' WHERE F_BUILDID=?";
        Object[] args1={build.getGroupid(),build.getId()};
        jdbcTemplate.update(sql1,args1);

    }
    //////////////////////////////////////////////////////////////////////////
    public DataTable getAllFloors() {
        String sql = "select F_ID,F_NAME,F_BUILDID from T_BD_FLOOR";
        DataTable dataTable = new DataTable();
        final List<Floor> floors = new ArrayList<Floor>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Floor floor = new Floor();
                floor.setId(rs.getString(1));
                floor.setName(rs.getString(2));
                floor.setBuildid(rs.getString(3));
                floors.add(floor);
            }
        });
        dataTable.setData(floors);
        return dataTable;
    }

    public void addFloor(Floor floor) {
        String sql = "insert into T_BD_FLOOR(F_ID,F_CODE,F_NAME,F_BUILDID) values(sys_guid(),?,?,?)";
        Object[] args = { floor.getCode(), floor.getName(), floor.getBuildid() };
        jdbcTemplate.update(sql, args);
        if(floor.getCode()>0){
            sql="update T_BD_BUILDBASEINFO set f_upfloor = f_upfloor +1 where f_buildid=?";
        }
        else if(floor.getCode()<0){
            sql="update T_BD_BUILDBASEINFO set f_downfloor = f_downfloor +1 where f_buildid=?";
        }
        else
            sql="update T_BD_BUILDBASEINFO set f_zerofloor = 1 where f_buildid=?";
        Object[] args1 = { floor.getBuildid()};
        jdbcTemplate.update(sql,args1);
    }

    public void Floorinit(Floor floor) {
        String sql = "insert into T_BD_FLOOR(F_ID,F_NAME,F_BUILDID,F_CODE) values(?,?,?,?)";
        Object[] args = { floor.getId(), floor.getName(), floor.getBuildid(),floor.getCode() };
        jdbcTemplate.update(sql, args);
    }

    public void deleteFloor(String ids) {
        String id=ids.split(",")[0];
        String sql;
        if(ids.split(",")[1].equals("upfloor")){
            sql="update T_BD_BUILDBASEINFO set f_upfloor = f_upfloor - 1 where f_buildid=(select f_buildid from T_BD_FLOOR where f_id=?)";
        }
        else
        if(ids.split(",")[1].equals("downfloor")){
            sql="update T_BD_BUILDBASEINFO set f_downfloor = f_downfloor - 1 where f_buildid=(select f_buildid from T_BD_FLOOR where f_id=?)";
        }
        else
            sql="update T_BD_BUILDBASEINFO set f_zerofloor = 0 where f_buildid=(select f_buildid from T_BD_FLOOR where f_id=?)";
        Object[] args2 = {id};
        jdbcTemplate.update(sql,args2);
        sql = "delete from T_BD_FLOOR where F_ID=?";
        Object[] args = { id };
        jdbcTemplate.update(sql, args);
        sql = "delete from T_BD_ROOM where F_FLOORID=?";
        Object[] args1 = { id };
        jdbcTemplate.update(sql, args1);

    }

    public List<Floor> getFloorByBuildId(String id) {
        String sql = "select F_ID,F_NAME,F_BUILDID,F_CODE,F_AREA from T_BD_FLOOR where F_BUILDID=? order by F_CODE DESC";
        Object[] args = { id };
        final List<Floor> floors = new ArrayList<Floor>();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Floor floor=new Floor();
                floor.setId(rs.getString(1));
                floor.setName(rs.getString(2));
                floor.setBuildid(rs.getString(3));
                floor.setCode(rs.getInt(4));
                floor.setArea(rs.getDouble(5));
                floors.add(floor);
            }
        });
        return floors;
    }

    public Floor getFloorById(String id) {
        String sql = "select F_ID,F_NAME,F_BUILDID,F_AREA,F_PEOPLE from T_BD_FLOOR where F_ID=?";
        Object[] args = { id };
        final Floor floor = new Floor();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                floor.setId(rs.getString(1));
                floor.setName(rs.getString(2));
                floor.setBuildid(rs.getString(3));
                floor.setArea(rs.getDouble(4));
                floor.setPeople(rs.getInt(5));
            }
        });
        return floor;
    }
    public boolean updateFloorById(Floor floor) {
        String sql = "update T_BD_FLOOR set F_AREA=?,F_PEOPLE=? where F_ID=?";
        Object[] args = { floor.getArea(),floor.getPeople(), floor.getId() };
        int i = jdbcTemplate.update(sql, args);
        return i > 0;
    }

    // //////////////////////////////////////////////////////////////////////////
    public DataTable getAllRooms() {
        String sql = "select F_ID,F_NAME from T_BD_ROOM";
        DataTable dataTable = new DataTable();
        final List<Room> rooms = new ArrayList<Room>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Room room = new Room();
                room.setId(rs.getString(1));
                room.setName(rs.getString(2));
                rooms.add(room);
            }
        });
        dataTable.setData(rooms);
        return dataTable;
    }

    public void addRoom(Room room) {
        String sql = "insert into T_BD_ROOM(F_ID,F_NAME,F_FLOORID,F_ROOMTYPEID,F_PEOPLE,F_AREA) values(?,?,?,?,?,?)";
        Object[] args = { room.getId(), room.getName(),
                room.getFloorid(),room.getRoomtypeid(),room.getPeople(),room.getArea()};
        jdbcTemplate.update(sql, args);
    }

    public void deleteRoom(String id) {
        String sql = "delete from T_BD_ROOM where F_ID=?";
        Object[] args = { id };
        jdbcTemplate.update(sql, args);
        String sql1 = "update t_be_equipmentlist set f_buildid='',f_floorid='',f_roomid='' where f_roomid=?";
        Object[] args1 = { id };
        jdbcTemplate.update(sql1, args1);
    }

    public Room getRoomById(String id) {
        String sql = "select * from T_BD_ROOM where F_ID=? ";
        Object[] args = { id };
        final Room room = new Room();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                room.setId(rs.getString(1));
                room.setName(rs.getString(2));
                room.setFloorid(rs.getString(3));
                room.setRoomtypeid(rs.getString(4));
                room.setPeople(rs.getInt(5));
                room.setArea(rs.getDouble(6));
            }
        });
        return room;
    }

    public List<Room> getRoomByFloorId(String id) {
        String sql = "select F_ID,F_NAME,F_FLOORID from T_BD_ROOM where F_FLOORID=? order by F_NAME";
        Object[] args = { id };
        final List<Room> rooms = new ArrayList<Room>();
        jdbcTemplate.query(sql, args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Room room=new Room();
                room.setId(rs.getString(1));
                room.setName(rs.getString(2));
                room.setFloorid(rs.getString(3));
                rooms.add(room);
            }
        });
        return rooms;
    }

    public void updateRoomById(Room room) {
        String sql = "update T_BD_ROOM set F_NAME=?,F_FLOORID=?,F_ROOMTYPEID=?,F_PEOPLE=?,F_AREA=? where F_ID=?";
        Object[] args = { room.getName(), room.getFloorid(),room.getRoomtypeid(),room.getPeople(),room.getArea(),
                room.getId() };
        jdbcTemplate.update(sql, args);
    }

    /////////////////////////////////////////////////////
    public List<Options> getGroupNames(){
        String sql="select F_BUILDGROUPID,F_BUILDGROUPNAME FROM T_BD_BUILDGROUPBASEINFO ORDER BY F_BUILDGROUPNAME";
        final List<Options> options=new ArrayList<Options>();
        jdbcTemplate.query(sql, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Options option=new Options();
                option.setId(rs.getString("F_BUILDGROUPID"));
                option.setName(rs.getString("F_BUILDGROUPNAME"));
                options.add(option);
            }
        });
        return options;
    }
    public List<Options> getBuildsByGroupId(String id){
        final List<Options> options = new ArrayList<Options>();
        if(id.equals("all")){
            String sql = "select a.F_BUILDID,a.F_BUILDNAME,c.F_BUILDGROUPNAME FROM T_BD_BUILDBASEINFO a,T_BD_BUILDGROUPRELAINFO b,T_BD_BUILDGROUPBASEINFO c WHERE a.F_BUILDID=b.F_BUILDID AND b.F_BUILDGROUPID=c.F_BUILDGROUPID ORDER BY c.F_BUILDGROUPNAME,a.F_BUILDNAME";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    Options option = new Options();
                    option.setId(rs.getString("F_BUILDID"));
                    option.setName(rs.getString("F_BUILDGROUPNAME")+"-"+rs.getString("F_BUILDNAME"));
                    options.add(option);
                }
            });
        }
        else {
            String sql = "select a.F_BUILDID,a.F_BUILDNAME FROM T_BD_BUILDBASEINFO a,T_BD_BUILDGROUPRELAINFO b,T_BD_BUILDGROUPBASEINFO c WHERE a.F_BUILDID=b.F_BUILDID AND b.F_BUILDGROUPID=c.F_BUILDGROUPID AND c.F_BUILDGROUPID=? ORDER BY F_BUILDNAME";
            Object[] args = {id};
            jdbcTemplate.query(sql, args, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    Options option = new Options();
                    option.setId(rs.getString("F_BUILDID"));
                    option.setName(rs.getString("F_BUILDNAME"));
                    options.add(option);
                }
            });
        }
        return options;
    }

    public List<Options> getBuildsByFunc(String id){
        String sql="select F_BUILDID,F_BUILDNAME FROM T_BD_BUILDBASEINFO  WHERE F_BUILDFUNC=? ORDER BY F_BUILDNAME";
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

    public List<Options> getFloorsByBuildId(String id){
        String sql="select F_ID,F_NAME FROM T_BD_FLOOR WHERE F_BUILDID=? ORDER BY F_CODE DESC ";
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
    public List<Options> getRoomsByFloorId(String id){
        String sql="select F_ID,F_NAME FROM T_BD_ROOM WHERE F_FLOORID=? ORDER BY F_NAME";
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
    public List<Options> getRoomsByBuildId(String id){
        String sql="select a.F_ID,a.F_NAME as room,b.F_NAME as floor FROM T_BD_ROOM a,T_BD_FLOOR b WHERE a.F_FLOORID=b.F_ID and b.F_BUILDID=? ORDER BY FLOOR,room";
        Object[] args={id};
        final List<Options> options=new ArrayList<Options>();
        jdbcTemplate.query(sql, args, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Options option=new Options();
                option.setId(rs.getString("F_ID"));
                option.setName(rs.getString("FLOOR")+"-"+rs.getString("ROOM"));
                options.add(option);
            }
        });
        return options;
    }
    ////////////////////////////////////////////////////////
    public List<Tree> getSchoolTree(){
        String sql4="select b.F_BUILDGROUPID,b.F_BUILDGROUPNAME FROM T_BE_EQUIPMENTLIST a,T_BD_BUILDGROUPBASEINFO b,T_BD_BUILDGROUPRELAINFO c WHERE a.F_BUILDID(+)=c.F_BUILDID AND b.F_BUILDGROUPID=c.F_BUILDGROUPID  GROUP BY b.F_BUILDGROUPID,b.F_BUILDGROUPNAME ORDER BY b.F_BUILDGROUPNAME";
        String sql0="select c.F_BUILDGROUPID,b.F_BUILDNAME,b.F_BUILDID FROM T_BE_EQUIPMENTLIST a,T_BD_BUILDBASEINFO b,T_BD_BUILDGROUPBASEINFO c,T_BD_BUILDGROUPRELAINFO d WHERE a.F_BUILDID(+)=b.F_BUILDID AND b.F_BUILDID=d.F_BUILDID AND c.F_BUILDGROUPID=d.F_BUILDGROUPID GROUP BY c.F_BUILDGROUPID,b.F_BUILDNAME,b.F_BUILDID ORDER BY b.F_BUILDNAME";
        String sql1="select b.F_BUILDID,b.F_NAME,b.F_ID,b.F_CODE FROM T_BE_EQUIPMENTLIST a,T_BD_FLOOR b WHERE a.F_FLOORID(+)=b.F_ID GROUP BY b.F_BUILDID,b.F_NAME,b.F_ID,b.F_CODE ORDER BY b.F_CODE DESC ";
        String sql2="select b.F_FLOORID,b.F_NAME,b.F_ID FROM T_BE_EQUIPMENTLIST a,T_BD_ROOM b WHERE a.F_ROOMID(+)=b.F_ID GROUP BY b.F_FLOORID,b.F_NAME,b.F_ID ORDER BY b.F_NAME";
        final List<Tree> buildTrees=new ArrayList<Tree>();
        Tree buildTree=new Tree();
        buildTree.setId("0");
        buildTree.setText("福建工程学院 " );
        buildTree.setParent("#");
        buildTrees.add(buildTree);
        jdbcTemplate.query(sql4, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Tree buildTree=new Tree();
                buildTree.setId(rs.getString("F_BUILDGROUPID"));
                buildTree.setText(rs.getString("F_BUILDGROUPNAME"));
                buildTree.setParent("0");
                buildTrees.add(buildTree);
            }
        });
        jdbcTemplate.query(sql0, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Tree buildTree=new Tree();
                buildTree.setId(rs.getString("F_BUILDID"));
                buildTree.setText(rs.getString("F_BUILDNAME"));
                buildTree.setParent(rs.getString("F_BUILDGROUPID"));
                buildTrees.add(buildTree);
            }
        });
        jdbcTemplate.query(sql1, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Tree buildTree=new Tree();
                buildTree.setParent(rs.getString("F_BUILDID"));
                buildTree.setId(rs.getString("F_ID"));
                buildTree.setText(rs.getString("F_NAME"));
                buildTrees.add(buildTree);
            }
        });
        jdbcTemplate.query(sql2, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Tree buildTree=new Tree();
                buildTree.setParent(rs.getString("F_FLOORID"));
                buildTree.setId(rs.getString("F_ID"));
                buildTree.setText(rs.getString("F_NAME"));
                buildTrees.add(buildTree);
            }
        });
        return buildTrees;
    }
    public List<Tree> getBuildTree(){
        String sql4="select b.F_BUILDGROUPID,b.F_BUILDGROUPNAME FROM T_BE_EQUIPMENTLIST a,T_BD_BUILDGROUPBASEINFO b,T_BD_BUILDGROUPRELAINFO c WHERE a.F_BUILDID(+)=c.F_BUILDID AND b.F_BUILDGROUPID=c.F_BUILDGROUPID  GROUP BY b.F_BUILDGROUPID,b.F_BUILDGROUPNAME ORDER BY b.F_BUILDGROUPNAME";
        String sql0="select c.F_BUILDGROUPID,b.F_BUILDNAME,b.F_BUILDID FROM T_BE_EQUIPMENTLIST a,T_BD_BUILDBASEINFO b,T_BD_BUILDGROUPBASEINFO c,T_BD_BUILDGROUPRELAINFO d WHERE a.F_BUILDID(+)=b.F_BUILDID AND b.F_BUILDID=d.F_BUILDID AND c.F_BUILDGROUPID=d.F_BUILDGROUPID GROUP BY c.F_BUILDGROUPID,b.F_BUILDNAME,b.F_BUILDID ORDER BY b.F_BUILDNAME";
        final List<Tree> buildTrees=new ArrayList<Tree>();
        Tree buildTree=new Tree();
        buildTree.setId("0");
        buildTree.setText("福建工程学院 " );
        buildTree.setParent("#");
        buildTrees.add(buildTree);
        jdbcTemplate.query(sql4, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Tree buildTree=new Tree();
                buildTree.setId(rs.getString("F_BUILDGROUPID"));
                buildTree.setText(rs.getString("F_BUILDGROUPNAME"));
                buildTree.setParent("0");
                buildTrees.add(buildTree);
            }
        });
        jdbcTemplate.query(sql0, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Tree buildTree=new Tree();
                buildTree.setId(rs.getString("F_BUILDID"));
                buildTree.setText(rs.getString("F_BUILDNAME"));
                buildTree.setParent(rs.getString("F_BUILDGROUPID"));
                buildTrees.add(buildTree);
            }
        });
        return buildTrees;
    }
}
