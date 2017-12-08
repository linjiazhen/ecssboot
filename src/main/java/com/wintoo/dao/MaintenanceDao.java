package com.wintoo.dao;

import com.wintoo.model.*;
import com.wintoo.tools.GFP_T_Modbus;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
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
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Repository
@Transactional(value = "primaryTransactionManager")
public class MaintenanceDao {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcOperations jdbcTemplate;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    private JdbcOperations jdbcTemplate1;

    public DataTable getAllGateways(){//yyyyMMddHH24mi oracle 中不区分大小写 因此不能用yyyyMMddHH24mm 否则会出错
        String sql="select a.*,(sysdate - F_HEARTBEAT_TIME) AS TIME_INTERVAL,to_char(F_HEARTBEAT_TIME,'yyyy-MM-dd HH24:mi:SS') AS NEWEST_HEARTBEAT_TIME,"+
                "to_char(F_LOGIN_TIME,'yyyy-MM-dd HH24:mi:SS') AS LOGIN_TIME,to_char(F_LOST_CONNET_TIME,'yyyy-MM-dd HH24:mi:SS') AS LOST_CONNET_TIME,"+
                "b.F_EQUIPID AS F_EQUIP,c.F_BATCH,c.F_MODEL,d.F_TYPE,d.F_SUBTYPE,b.F_INSTALLTYPE,t.F_BUILDGROUPNAME,e.F_BUILDNAME,f.F_NAME AS FLOOR,g.F_NAME AS ROOM,b.F_LONGITUDE,b.F_LATITUDE,b.F_REMARK "+
                "from T_BE_GATEWAY a,T_BE_EQUIPMENTLIST b,T_BE_EQUIPMENTBATCH c,T_BE_EQUIPMENTTYPE d,T_BD_BUILDGROUPRELAINFO h,T_BD_BUILDGROUPBASEINFO t,T_BD_BUILDBASEINFO e,T_BD_FLOOR f,T_BD_ROOM g " +
                "where a.F_UUID=b.F_UUID AND a.F_BATCHID=c.F_UUID AND b.F_TYPEID=d.F_UUID AND b.F_BUILDID=e.F_BUILDID(+) AND b.F_FLOORID=f.F_ID(+) AND b.F_ROOMID=g.F_ID(+) AND h.F_BUILDID(+)=b.F_BUILDID AND t.F_BUILDGROUPID(+)=h.F_BUILDGROUPID";
        DataTable dataTable=new DataTable();
        final List<Gateway> builds=new ArrayList<Gateway>();
        jdbcTemplate.query(sql, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Gateway gateway=new Gateway();
                gateway.setUuid(rs.getString("F_UUID"));
                gateway.setBatch(rs.getString("F_BATCH"));
                gateway.setEquip(rs.getString("F_EQUIP"));
                gateway.setType(rs.getString("F_TYPE"));
                gateway.setSubtype(rs.getString("F_SUBTYPE"));
                gateway.setModel(rs.getString("F_MODEL"));
                gateway.setAddress(rs.getString("F_ADDRESS"));
                gateway.setCode(rs.getString("F_CODE"));
                gateway.setIpmain(rs.getString("F_IP_MAIN"));
                gateway.setPortmain(rs.getString("F_PORT_MAIN"));
                gateway.setIpbackup(rs.getString("F_IP_BACKUP"));
                gateway.setPortbackup(rs.getString("F_PORT_BACKUP"));
                gateway.setApn(rs.getString("F_APN"));
                gateway.setDelay1(rs.getInt("F_DELAY_1"));
                gateway.setDelay2(rs.getInt("F_DELAY_2"));
                gateway.setWaittime(rs.getInt("F_WAIT_TIME"));
                gateway.setFlag(rs.getInt("F_FLAG"));
                gateway.setHeartbeat(rs.getInt("F_HEARTBEAT"));
                gateway.setInstalltype(rs.getInt("F_INSTALLTYPE"));
                gateway.setGroup(rs.getString("F_BUILDGROUPNAME"));
                gateway.setBuild(rs.getString("F_BUILDNAME"));
                gateway.setFloor(rs.getString("FLOOR"));
                gateway.setRoom(rs.getString("ROOM"));
                gateway.setLongitude(rs.getString("F_LONGITUDE"));
                gateway.setLatitude(rs.getString("F_LATITUDE"));
                gateway.setUse(rs.getInt("F_USE"));
                gateway.setRemark(rs.getString("F_REMARK"));
                gateway.setServer_flag(rs.getInt("F_SERVER_FLAG"));
                if(rs.getDate("F_HEARTBEAT_TIME") != null && rs.getInt("F_USE")==1){
                    if(rs.getDouble("TIME_INTERVAL")*1440 <= rs.getInt("F_HEARTBEAT")+1)
                        gateway.setState(1);
                    else{
                        gateway.setState(0);
                    }
                }
                else {
                    gateway.setState(0);
                }

                if(rs.getString("NEWEST_HEARTBEAT_TIME") != null)
                    gateway.setNewest_hearbeat_time(rs.getString("NEWEST_HEARTBEAT_TIME"));
                else {
                    gateway.setNewest_hearbeat_time("-");
                }
                gateway.setGateway_ver(rs.getString("F_GATEWAY_VER"));
                gateway.setLogin_time(rs.getString("LOGIN_TIME"));
                gateway.setLost_connet_time(rs.getString("LOST_CONNET_TIME"));
                gateway.setZd_ip(rs.getString("F_ZD_IP"));
                gateway.setZd_mask(rs.getString("F_ZD_MASK"));
                gateway.setZd_gateway(rs.getString("F_ZD_GATEWAY"));
                gateway.setZd_mac(rs.getString("F_ZD_MAC"));
                gateway.setZd_lcd_password(rs.getString("F_ZD_LCD_PASSWORD"));
                builds.add(gateway);
            }
        });
        dataTable.setData(builds);
        System.out.printf("1234");
        return dataTable;
    }

    public Gateway getGatewayById(String id){
        String sql="select a.*,b.F_EQUIPID AS F_EQUIP,c.F_BATCH,c.F_MODEL,d.F_TYPE,d.F_SUBTYPE,b.F_INSTALLTYPE,t.F_BUILDGROUPNAME,e.F_BUILDNAME,f.F_NAME AS FLOOR,g.F_NAME AS ROOM,b.F_LONGITUDE,b.F_LATITUDE,b.F_REMARK from T_BE_GATEWAY a,T_BE_EQUIPMENTLIST b,T_BE_EQUIPMENTBATCH c,T_BE_EQUIPMENTTYPE d,T_BD_BUILDGROUPRELAINFO h,T_BD_BUILDGROUPBASEINFO t,T_BD_BUILDBASEINFO e,T_BD_FLOOR f,T_BD_ROOM g " +
                "where a.F_UUID=b.F_UUID AND a.F_BATCHID=c.F_UUID AND b.F_TYPEID=d.F_UUID AND b.F_BUILDID=e.F_BUILDID(+) AND b.F_FLOORID=f.F_ID(+) AND b.F_ROOMID=g.F_ID(+) AND h.F_BUILDID(+)=b.F_BUILDID AND t.F_BUILDGROUPID(+)=h.F_BUILDGROUPID AND a.F_UUID=?";
        Object[] args={id};
        final Gateway gateway=new Gateway();
        jdbcTemplate.query(sql, args ,new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                gateway.setUuid(rs.getString("F_UUID"));
                gateway.setBatch(rs.getString("F_BATCH"));
                gateway.setEquip(rs.getString("F_EQUIP"));
                gateway.setType(rs.getString("F_TYPE"));
                gateway.setSubtype(rs.getString("F_SUBTYPE"));
                gateway.setModel(rs.getString("F_MODEL"));
                gateway.setAddress(rs.getString("F_ADDRESS"));
                gateway.setCode(rs.getString("F_CODE"));
                gateway.setIpmain(rs.getString("F_IP_MAIN"));
                gateway.setPortmain(rs.getString("F_PORT_MAIN"));
                gateway.setIpbackup(rs.getString("F_IP_BACKUP"));
                gateway.setPortbackup(rs.getString("F_PORT_BACKUP"));
                gateway.setApn(rs.getString("F_APN"));
                gateway.setDelay1(rs.getInt("F_DELAY_1"));
                gateway.setDelay2(rs.getInt("F_DELAY_2"));
                gateway.setWaittime(rs.getInt("F_WAIT_TIME"));
                gateway.setFlag(rs.getInt("F_FLAG"));
                gateway.setHeartbeat(rs.getInt("F_HEARTBEAT"));
                gateway.setInstalltype(rs.getInt("F_INSTALLTYPE"));
                gateway.setGroup(rs.getString("F_BUILDGROUPNAME"));
                gateway.setBuild(rs.getString("F_BUILDNAME"));
                gateway.setFloor(rs.getString("FLOOR"));
                gateway.setRoom(rs.getString("ROOM"));
                gateway.setLongitude(rs.getString("F_LONGITUDE"));
                gateway.setLatitude(rs.getString("F_LATITUDE"));
                gateway.setUse(rs.getInt("F_USE"));
                gateway.setZd_lcd_password(rs.getString("F_ZD_LCD_PASSWORD"));
                gateway.setZd_ip(rs.getString("F_ZD_IP"));
                gateway.setZd_mask(rs.getString("F_ZD_MASK"));
                gateway.setZd_gateway(rs.getString("F_ZD_GATEWAY"));
                gateway.setZd_mac(rs.getString("F_ZD_MAC"));


            }
        });
        return gateway;
    }

    public void updateGatewayById(Gateway gateway){
        String uuid=gateway.getUuid().split(":")[0];
        String use_flag=gateway.getUuid().split(":")[1];
        String ips="";
        if(gateway.getZd_ip() != null){
            String password=gateway.getZd_lcd_password()+"000000";
            ips="F_ZD_LCD_PASSWORD='"+password.substring(0,6)+"',F_ZD_IP='"+gateway.getZd_ip()+"',F_ZD_MASK='"+gateway.getZd_mask()+"',F_ZD_GATEWAY='"+gateway.getZd_gateway()+"',F_ZD_MAC='"+gateway.getZd_mac()+"',";
        }


        String sql="update T_BE_GATEWAY set "+ips+" F_NEED_OPEN_TRG=0, F_ADDRESS=?,F_CODE=?,F_IP_MAIN=?,F_PORT_MAIN=?,F_IP_BACKUP=?,F_PORT_BACKUP=?,F_APN=?,F_DELAY_1=?,F_DELAY_2=?,F_WAIT_TIME=?,F_FLAG=?,F_HEARTBEAT=? where F_UUID=?";
        Object[] args={gateway.getAddress().toUpperCase(),gateway.getCode(),gateway.getIpmain(),gateway.getPortmain(),gateway.getIpbackup(),gateway.getPortbackup(),gateway.getApn(),gateway.getDelay1(),gateway.getDelay2(),gateway.getWaittime(),gateway.getFlag(),gateway.getHeartbeat(),uuid};
        jdbcTemplate.update(sql,args);
        if(use_flag.equals("1")){
            sql=" insert into t_tasks(F_UUID,F_TYPE) values(SYS_GUID(),4)";
            jdbcTemplate1.update(sql);
            sql=" insert into t_tasks(F_UUID,F_TYPE_BACKUP) values(SYS_GUID(),4)";
            jdbcTemplate1.update(sql);
        }

    }

    public void updateGatewaysById(Gateway gateway) {
        String flag="";
        String addr_old_string="";
        int    addr_old_int=0;
        if(gateway.getAddress() != null){//对传进来的参数进行判断，并生成SQL操作语句所需的信息
            flag=flag+",F_ADDRESS="+"'"+gateway.getAddress().toUpperCase()+"'";
            addr_old_string = "F_ADDRESS="+"'"+gateway.getAddress().toUpperCase()+"'";
            addr_old_int = Integer.parseInt(gateway.getAddress().toUpperCase(), 16);
        }
        if(gateway.getCode() != null){
            flag=flag+",F_CODE="+"'"+gateway.getCode()+"'";
        }
        if(gateway.getIpmain() != null){
            flag=flag+",F_IP_MAIN="+"'"+gateway.getIpmain()+"'";
        }
        if(gateway.getPortmain() != null){
            flag=flag+",F_PORT_MAIN="+"'"+gateway.getPortmain()+"'";
        }
        if(gateway.getIpbackup() != null){
            flag=flag+",F_IP_BACKUP="+"'"+gateway.getIpbackup()+"'";
        }
        if(gateway.getPortbackup() != null){
            flag=flag+",F_PORT_BACKUP="+"'"+gateway.getPortbackup()+"'";
        }
        if(gateway.getApn() != null){
            flag=flag+",F_APN="+"'"+gateway.getApn()+"'";
        }
        if(gateway.getDelay1() != 99999999){
            flag=flag+",F_DELAY_1="+gateway.getDelay1();
        }
        if(gateway.getDelay2() != 99999999){
            flag=flag+",F_DELAY_2="+gateway.getDelay2();
        }
        if(gateway.getWaittime() != 99999999){
            flag=flag+",F_WAIT_TIME="+gateway.getWaittime();
        }
        if(gateway.getFlag() != 99999999){
            flag=flag+",F_FLAG="+gateway.getFlag();
        }
        if(gateway.getHeartbeat() != 99999999){
            flag=flag+",F_HEARTBEAT="+gateway.getHeartbeat();
        }
        flag=flag.substring(1);
        //根据uuid进行数据更新
        String sql="update T_BE_GATEWAY set "+flag+",F_NEED_OPEN_TRG=0  where F_UUID=?";
        String[] uuid=gateway.getUuid().split(",");
        String addr_next_string="";
        int addr_next_int=0;
        String uuid_reg="";
        String use_flag="";
        int    need_set_cmd=0;
        for(int i=0;i<uuid.length;i++){
            if(addr_old_int != 0){
                addr_next_int = addr_old_int + i;
                addr_next_string = "F_ADDRESS="+"'"+String.format("%1$04x", addr_next_int).toUpperCase()+"'";
                sql=sql.replace(addr_old_string, addr_next_string);
                addr_old_string=addr_next_string;
            }
            uuid_reg = uuid[i].split(":")[0];
            use_flag = uuid[i].split(":")[1];
            if(use_flag.equals("1")){
                need_set_cmd = 1;
            }
            Object[] args={uuid_reg};
            jdbcTemplate.update(sql,args);
        }
        if(need_set_cmd == 1){
            sql=" insert into t_tasks(F_UUID,F_TYPE) values(SYS_GUID(),4)";
            jdbcTemplate1.update(sql);
            sql=" insert into t_tasks(F_UUID,F_TYPE_BACKUP) values(SYS_GUID(),4)";
            jdbcTemplate1.update(sql);
        }

    }

    public List<Options> getGateways(){
        String sql="select F_UUID,F_ADDRESS,F_USE FROM T_BE_GATEWAY order by f_address asc";
        final List<Options> options=new ArrayList<Options>();
        jdbcTemplate.query(sql, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Options option=new Options();
                option.setId(rs.getString("F_UUID"));
                option.setName(rs.getString("F_ADDRESS"));
                option.setUse(rs.getInt("F_USE"));
                options.add(option);
            }
        });
        return options;
    }

    public void setGatewayUse(String ids){
        String sql="update T_BE_GATEWAY set F_USE=?,F_IS_FILE_CHANGED=?,F_IS_IP_CHANGED=?,F_IS_TRANS_CHANGED=?  WHERE F_UUID=?";
        String[] regString=ids.split(",")[0].split(":");
        String use_flag=ids.split(",")[1];
        int i=0;
        Object[] args=null;
        for(i=0;i<regString.length;){
            args=new Object[]{use_flag,use_flag,use_flag,use_flag,regString[i]};
            jdbcTemplate.update(sql, args);
            i++;
        }
        if(use_flag.equals("1")){
            sql=" insert into t_tasks(F_UUID,F_TYPE) values(SYS_GUID(),4)";
            jdbcTemplate1.update(sql);
            sql=" insert into t_tasks(F_UUID,F_TYPE_BACKUP) values(SYS_GUID(),4)";
            jdbcTemplate1.update(sql);
        }
    }


    public void gatewayReboot(String uuids) {//暂时用作读取网关时间！！！
        String sql_get_server_flag="select f_server_flag from T_BE_GATEWAY where F_UUID=?";
        Object[] args={uuids};
        final List<Integer> server_flag=new ArrayList<Integer>();
        server_flag.clear();
        jdbcTemplate.query(sql_get_server_flag, args, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                server_flag.add(rs.getInt("F_SERVER_FLAG"));
            }
        });

        String sql_set_cmd= "insert into t_tasks( f_uuid, F_GATEWAY_UUID , f_type, f_flag) values(?,?,?,?)";
        if(server_flag.get(0) == 1)
            sql_set_cmd= "insert into t_tasks( f_uuid, F_GATEWAY_UUID , f_type, f_flag) values(?,?,?,?)";
        else {
            sql_set_cmd= "insert into t_tasks( f_uuid, F_GATEWAY_UUID , f_type_backup, f_flag_backup) values(?,?,?,?)";
        }
        String uuid = UUID.randomUUID().toString();
        Object[] args_set_cmd={uuid,uuids,"2","54"};
        jdbcTemplate1.update(sql_set_cmd, args_set_cmd);//发送数据
		/*//SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		SimpleDateFormat df=new SimpleDateFormat("ssmmhhdd");//设置日期格式,获取秒分时日的BCD码
		//Date nowtime=new Date();
		String time=df.format(new Date());
		String timeflag="01"+time+"00";//1376.1时标
		String cmd_reg=("01"+"E1"+"00000100"+"00000000000000000000000000000000"+timeflag).toUpperCase();//大写
					 // AFN +SEQ+F1+填充16byte+时标
		String sql_set_cmd= "insert into t_tasks( f_uuid, F_GATEWAY_UUID , f_type, f_flag,f_send) values(?,?,?,?,?)";


		String uuid = UUID.randomUUID().toString();
		Object[] args_set_cmd={uuid,uuids,"1","71",cmd_reg};
		jdbcTemplate1.update(sql_set_cmd, args_set_cmd);//发送数据



		String sql_wait_success= "select f_flag,f_recv from t_tasks where f_uuid=?";//获取返回的确认信息
		String sql_delete_cmdString="delete from t_tasks where f_uuid=?";//删除已经操作成功的指令
		Object[] operate_uuid={uuid};


		int times=0;
		final List<Integer> flag=new ArrayList<Integer>();
		final List<String> f_recv=new ArrayList<String>();

		for(times=0;times<10;times++){
			try {
				Thread.sleep(1000);
				flag.clear();
				f_recv.clear();
				jdbcTemplate1.query(sql_wait_success, operate_uuid, new RowCallbackHandler(){
					@Override
					public void processRow(ResultSet rs)throws SQLException{
						flag.add(rs.getInt("f_flag"));
						f_recv.add(rs.getString("f_recv"));
					}
				});
			} catch (Exception e) {
				// TODO: handle exception
			}

			if(flag.get(0)==3){//执行成功

			}
			else if(flag.get(0)==9){//执行失败

			}
			else if(times==9){

			}

		}
		jdbcTemplate1.update(sql_delete_cmdString, operate_uuid);*/
    }

    public void gatewayClear(String uuids) {
        String sql_get_server_flag="select f_server_flag from T_BE_GATEWAY where F_UUID=?";
        Object[] args={uuids};
        final List<Integer> server_flag=new ArrayList<Integer>();
        server_flag.clear();
        jdbcTemplate.query(sql_get_server_flag, args, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                server_flag.add(rs.getInt("F_SERVER_FLAG"));
            }
        });
        String sql_set_cmd= "insert into t_tasks( f_uuid, F_GATEWAY_UUID , f_type, f_flag) values(?,?,?,?)";
        if(server_flag.get(0) == 1)
            sql_set_cmd= "insert into t_tasks( f_uuid, F_GATEWAY_UUID , f_type, f_flag) values(?,?,?,?)";
        else {
            sql_set_cmd= "insert into t_tasks( f_uuid, F_GATEWAY_UUID , f_type_backup, f_flag_backup) values(?,?,?,?)";
        }


        //String sql_set_cmd= "insert into t_tasks( f_uuid, F_GATEWAY_UUID , f_type, f_flag) values(?,?,?,?)";

        String uuid = UUID.randomUUID().toString();
        Object[] args_set_cmd={uuid,uuids,"2","55"};
        jdbcTemplate1.update(sql_set_cmd, args_set_cmd);//发送数据

    }

    public void gatewayTiming(String uuids) {
        String sql_get_server_flag="select f_server_flag from T_BE_GATEWAY where F_UUID=?";
        Object[] args={uuids};
        final List<Integer> server_flag=new ArrayList<Integer>();
        server_flag.clear();
        jdbcTemplate.query(sql_get_server_flag, args, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                server_flag.add(rs.getInt("F_SERVER_FLAG"));
            }
        });
        String sql_set_cmd= "insert into t_tasks( f_uuid, F_GATEWAY_UUID , f_type, f_flag) values(?,?,?,?)";
        if(server_flag.get(0) == 1)
            sql_set_cmd= "insert into t_tasks( f_uuid, F_GATEWAY_UUID , f_type, f_flag) values(?,?,?,?)";
        else {
            sql_set_cmd= "insert into t_tasks( f_uuid, F_GATEWAY_UUID , f_type_backup, f_flag_backup) values(?,?,?,?)";
        }
        String uuid = UUID.randomUUID().toString();
        Object[] args_set_cmd={uuid,uuids,"2","53"};
        jdbcTemplate1.update(sql_set_cmd, args_set_cmd);//发送数据

    }


    public DataTable getGatewaychild(String gatewayuuid) {
        String sqlammeter="select a.*,x.F_ADDRESS AS GATEWAY,b.F_EQUIPID AS F_EQUIP,c.F_BATCH,c.F_MODEL,b.F_TYPEID,d.F_TYPE,d.F_SUBTYPE,b.F_INSTALLTYPE,t.F_BUILDGROUPNAME,e.F_BUILDNAME,f.F_NAME AS FLOOR,g.F_NAME AS ROOM,b.F_LONGITUDE,b.F_LATITUDE,b.F_REMARK from T_BE_AMMETER a,T_BE_EQUIPMENTLIST b,T_BE_EQUIPMENTBATCH c,T_BE_EQUIPMENTTYPE d,T_BD_BUILDGROUPRELAINFO h,T_BD_BUILDGROUPBASEINFO t,T_BD_BUILDBASEINFO e,T_BD_FLOOR f,T_BD_ROOM g,T_BE_GATEWAY x " +
                "where a.F_GATEWAYS_UUID=x.F_UUID(+) AND a.F_UUID=b.F_UUID AND a.F_BATCHID=c.F_UUID AND b.F_TYPEID=d.F_UUID AND b.F_BUILDID=e.F_BUILDID(+) AND b.F_FLOORID=f.F_ID(+) AND b.F_ROOMID=g.F_ID(+) AND h.F_BUILDID(+)=b.F_BUILDID AND t.F_BUILDGROUPID(+)=h.F_BUILDGROUPID AND a.F_GATEWAYS_UUID='"+gatewayuuid+"'";
        String sqlwatermeter="select a.*,x.F_ADDRESS AS GATEWAY,b.F_EQUIPID AS F_EQUIP,c.F_BATCH,c.F_MODEL,b.F_TYPEID,d.F_TYPE,d.F_SUBTYPE,b.F_INSTALLTYPE,t.F_BUILDGROUPNAME,e.F_BUILDNAME,f.F_NAME AS FLOOR,g.F_NAME AS ROOM,b.F_LONGITUDE,b.F_LATITUDE,b.F_REMARK from T_BE_WATERMETER a,T_BE_EQUIPMENTLIST b,T_BE_EQUIPMENTBATCH c,T_BE_EQUIPMENTTYPE d,T_BD_BUILDGROUPRELAINFO h,T_BD_BUILDGROUPBASEINFO t,T_BD_BUILDBASEINFO e,T_BD_FLOOR f,T_BD_ROOM g,T_BE_GATEWAY x " +
                "where a.F_GATEWAYS_UUID=x.F_UUID(+) AND a.F_UUID=b.F_UUID AND a.F_BATCHID=c.F_UUID AND b.F_TYPEID=d.F_UUID AND b.F_BUILDID=e.F_BUILDID(+) AND b.F_FLOORID=f.F_ID(+) AND b.F_ROOMID=g.F_ID(+) AND h.F_BUILDID(+)=b.F_BUILDID AND t.F_BUILDGROUPID(+)=h.F_BUILDGROUPID AND a.F_GATEWAYS_UUID='"+gatewayuuid+"'";

        DataTable dataTable=new DataTable();
        final List<Ammeter> builds=new ArrayList<Ammeter>();
        jdbcTemplate.query(sqlammeter, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Ammeter ammeter=new Ammeter();
                ammeter.setUuid(rs.getString("F_UUID"));
                ammeter.setBatch(rs.getString("F_BATCH"));
                ammeter.setEquip(rs.getString("F_EQUIP"));
                ammeter.setType(rs.getString("F_TYPE"));
                ammeter.setSubtype(rs.getString("F_SUBTYPE"));
                ammeter.setModel(rs.getString("F_MODEL"));
                ammeter.setGateway(rs.getString("GATEWAY"));
                ammeter.setPn(rs.getInt("F_PN"));
                ammeter.setNumber(rs.getInt("F_NUMBER"));
                ammeter.setSpeed(rs.getInt("F_SPEED_PORT")&0xf0);//高4bit为速率
                ammeter.setPort(rs.getInt("F_SPEED_PORT")&0x0f);//低4bit为
                ammeter.setProtocol(rs.getInt("F_PROTOCOL"));
                ammeter.setAddress(rs.getString("F_ADDRESS"));
                ammeter.setPassword(rs.getString("F_PASSWORD"));
                ammeter.setRatecount(rs.getInt("F_RATE_COUNT"));
                ammeter.setDataformat(rs.getInt("F_DATA_FORMAT"));
                ammeter.setClassnumber(rs.getInt("F_CLASS_NUMBER"));
                ammeter.setUse(rs.getInt("F_USE"));
                ammeter.setOn_off(rs.getInt("F_ON_OFF"));
                ammeter.setInstalltype(rs.getInt("F_INSTALLTYPE"));
                ammeter.setGroup(rs.getString("F_BUILDGROUPNAME"));
                ammeter.setBuild(rs.getString("F_BUILDNAME"));
                ammeter.setFloor(rs.getString("FLOOR"));
                ammeter.setRoom(rs.getString("ROOM"));
                ammeter.setLongitude(rs.getString("F_LONGITUDE"));
                ammeter.setLatitude(rs.getString("F_LATITUDE"));
                ammeter.setTypeid(rs.getString("F_TYPEID"));
                ammeter.setRemark(rs.getString("F_REMARK"));
                builds.add(ammeter);
            }
        });
        jdbcTemplate.query(sqlwatermeter, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Ammeter ammeter=new Ammeter();
                ammeter.setUuid(rs.getString("F_UUID"));
                ammeter.setBatch(rs.getString("F_BATCH"));
                ammeter.setEquip(rs.getString("F_EQUIP"));
                ammeter.setType(rs.getString("F_TYPE"));
                ammeter.setSubtype(rs.getString("F_SUBTYPE"));
                ammeter.setModel(rs.getString("F_MODEL"));
                ammeter.setGateway(rs.getString("GATEWAY"));
                ammeter.setPn(rs.getInt("F_PN"));
                ammeter.setNumber(rs.getInt("F_NUMBER"));
                ammeter.setSpeed(rs.getInt("F_SPEED_PORT")&0xf0);//高4bit为速率
                ammeter.setPort(rs.getInt("F_SPEED_PORT")&0x0f);//低4bit为
                ammeter.setProtocol(rs.getInt("F_PROTOCOL"));
                ammeter.setAddress(rs.getString("F_ADDRESS"));
                ammeter.setPassword(rs.getString("F_PASSWORD"));
                ammeter.setUse(rs.getInt("F_USE"));
                ammeter.setOn_off(rs.getInt("F_ON_OFF"));
                ammeter.setInstalltype(rs.getInt("F_INSTALLTYPE"));
                ammeter.setGroup(rs.getString("F_BUILDGROUPNAME"));
                ammeter.setBuild(rs.getString("F_BUILDNAME"));
                ammeter.setFloor(rs.getString("FLOOR"));
                ammeter.setRoom(rs.getString("ROOM"));
                ammeter.setLongitude(rs.getString("F_LONGITUDE"));
                ammeter.setLatitude(rs.getString("F_LATITUDE"));
                ammeter.setTypeid(rs.getString("F_TYPEID"));
                ammeter.setRemark(rs.getString("F_REMARK"));
                builds.add(ammeter);
            }
        });


        dataTable.setData(builds);
        return dataTable;
    }

    public void setGatewayChildUse(String ids){
        String sql="";
        String[] regString=ids.split(",")[0].split(":");
        int i=0;
        Object[] args=null;
        for(i=0;i<regString.length;){
            if(regString[i].split("!")[1].equals("0010"))//如果为水表
                sql="update T_BE_WATERMETER set F_USE=?, F_IS_CHANGED=1 WHERE F_UUID=?";
            else {
                sql="update T_BE_AMMETER set F_USE=?,F_IS_CHANGED=1  WHERE F_UUID=?";
            }
            args=new Object[]{ids.split(",")[1],regString[i].split("!")[0]};
            jdbcTemplate.update(sql, args);
            i++;
        }
        String sql1="insert into t_tasks(F_UUID,F_TYPE,F_FLAG) values(?,?,?)";
        String uuid = UUID.randomUUID().toString();
        Object[] operate_uuid={uuid,"3","1"};
        jdbcTemplate1.update(sql1,operate_uuid);

    }

    public void Refresh_para(String uuids){
        String[] uuidss=uuids.split(",");
        String   sql="update t_nodes set f_is_changed=1 where f_uuid=?";
        int need_set_cmd=0;
        for(int i=0;i<uuidss.length;i++){
            String[] uuidsss=uuidss[i].split(":");
            if(uuidsss[1].equals("1")){
                Object[] operate_uuid={uuidsss[0]};
                jdbcTemplate1.update(sql,operate_uuid);
                need_set_cmd=1;
            }
        }
        if(need_set_cmd == 1){
            sql="insert into t_tasks(F_UUID,F_TYPE,F_FLAG) values(SYS_GUID(),'3','1')";
            jdbcTemplate1.update(sql);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public DataTable getAllAmmeters(){
        String sql="select a.*,to_char(a.F_LAST_15,'YYYY-MM-dd HH24:mi') AS LAST_15,to_char(a.F_LAST_DAY,'YYYY-MM-dd') AS LAST_DAY,to_char(a.F_LAST_MON,'YYYY-MM') AS LAST_MON,to_char(a.F_NEWEST_DATA_TIME,'YYYY-MM-dd HH24:mi:SS') AS NEWEST_DATA_TIME,to_char(a.F_NEWEST_OPERATE_TIME,'YYYY-MM-dd HH24:mi:SS') AS NEWEST_OPERATE_TIME,(sysdate-a.F_NEWEST_OPERATE_TIME) AS TIME_INTERVAL,(sysdate-a.F_NEWEST_VALID_DATA_TIME) AS TIME_INTERVAL_CUT,a.F_EQUIPMENT_STATUE as STATUE," +
                "x.F_ADDRESS AS GATEWAY,x.F_UUID AS GATEWAYID,b.F_EQUIPID AS F_EQUIP,c.F_BATCH,c.F_MODEL,b.F_TYPEID,d.F_TYPE,d.F_SUBTYPE,b.F_INSTALLTYPE,t.F_BUILDGROUPNAME,e.F_BUILDNAME,f.F_NAME AS FLOOR,g.F_NAME AS ROOM,b.F_LONGITUDE,b.F_LATITUDE,b.F_REMARK,a.F_cut from T_BE_AMMETER a,T_BE_EQUIPMENTLIST b,T_BE_EQUIPMENTBATCH c,T_BE_EQUIPMENTTYPE d,T_BD_BUILDGROUPRELAINFO h,T_BD_BUILDGROUPBASEINFO t,T_BD_BUILDBASEINFO e,T_BD_FLOOR f,T_BD_ROOM g,T_BE_GATEWAY x " +
                "where a.F_GATEWAYS_UUID=x.F_UUID(+) AND a.F_UUID=b.F_UUID AND a.F_BATCHID=c.F_UUID AND b.F_TYPEID=d.F_UUID AND b.F_BUILDID=e.F_BUILDID(+) AND b.F_FLOORID=f.F_ID(+) AND b.F_ROOMID=g.F_ID(+) AND h.F_BUILDID(+)=b.F_BUILDID AND t.F_BUILDGROUPID(+)=h.F_BUILDGROUPID AND (x.F_ADDRESS IS NOT NULL OR b.F_INSTALLTYPE IS NOT NULL)";
        DataTable dataTable=new DataTable();
        final SimpleDateFormat df_15=new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        final SimpleDateFormat df_day=new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        final SimpleDateFormat df_mon=new SimpleDateFormat("yyyy-MM");//设置日期格式
        final Calendar   calendar   =   new   GregorianCalendar();
        final List<Ammeter> builds=new ArrayList<Ammeter>();
        jdbcTemplate.query(sql, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Ammeter ammeter=new Ammeter();
                ammeter.setUuid(rs.getString("F_UUID"));
                ammeter.setBatch(rs.getString("F_BATCH"));
                ammeter.setEquip(rs.getString("F_EQUIP"));
                ammeter.setType(rs.getString("F_TYPE"));
                ammeter.setSubtype(rs.getString("F_SUBTYPE"));
                ammeter.setModel(rs.getString("F_MODEL"));
                if(rs.getString("GATEWAY") == null)
                    ammeter.setGateway("未分配");
                else
                    ammeter.setGateway(rs.getString("GATEWAY"));
                ammeter.setGatewayid(rs.getString("GATEWAYID"));
                ammeter.setPn(rs.getInt("F_PN"));
                ammeter.setNumber(rs.getInt("F_NUMBER"));
                ammeter.setSpeed(rs.getInt("F_SPEED_PORT")&0xf0);//高4bit为速率
                ammeter.setPort(rs.getInt("F_SPEED_PORT")&0x0f);//低4bit为
                ammeter.setProtocol(rs.getInt("F_PROTOCOL"));
                ammeter.setAddress(rs.getString("F_ADDRESS"));
                ammeter.setPassword(rs.getString("F_PASSWORD"));
                ammeter.setRatecount(rs.getInt("F_RATE_COUNT"));
                ammeter.setDataformat(rs.getInt("F_DATA_FORMAT"));
                ammeter.setClassnumber(rs.getInt("F_CLASS_NUMBER"));
                ammeter.setUse(rs.getInt("F_USE"));
                ammeter.setOn_off(rs.getInt("F_ON_OFF"));
                ammeter.setInstalltype(rs.getInt("F_INSTALLTYPE"));
                ammeter.setGroup(rs.getString("F_BUILDGROUPNAME"));
                ammeter.setBuild(rs.getString("F_BUILDNAME"));
                ammeter.setFloor(rs.getString("FLOOR"));
                ammeter.setRoom(rs.getString("ROOM"));
                ammeter.setLongitude(rs.getString("F_LONGITUDE"));
                ammeter.setLatitude(rs.getString("F_LATITUDE"));
                ammeter.setTypeid(rs.getString("F_TYPEID"));
                ammeter.setRemark(rs.getString("F_REMARK"));
                ammeter.setNewest_data(rs.getDouble("F_NEWEST_DATA"));
                ammeter.setNewest_data_time(rs.getString("NEWEST_DATA_TIME"));
                ammeter.setNewest_valid_data(rs.getDouble("F_NEWEST_VALID_DATA"));
                ammeter.setNewest_valid_data_time(rs.getString("F_NEWEST_VALID_DATA_TIME"));
                ammeter.setNewest_operate_time(rs.getString("NEWEST_OPERATE_TIME"));
                ammeter.setRemarkinfo(rs.getString("F_REMARKINFO"));
                ammeter.setCut(rs.getInt("F_CUT"));
                if((rs.getInt("F_USE") == 0)||(rs.getInt("F_CUT")==1&&(rs.getDouble("TIME_INTERVAL_CUT") < 3)))
                    ammeter.setStatus(4);//关闭
                else
                if(rs.getDouble("F_NEWEST_DATA") == -1&&rs.getInt("STATUE")>3)
                    ammeter.setStatus(2);//故障
                else
                if((rs.getDouble("TIME_INTERVAL") >= 0.0208))
                    ammeter.setStatus(1);//离线
                else
                if(rs.getDouble("F_NEWEST_VALID_DATA")>rs.getDouble("F_NEWEST_DATA")&&rs.getDouble("F_NEWEST_VALID_DATA")!=-1&&rs.getInt("STATUE")==0)
                    ammeter.setStatus(3);//底度

                Date datenow=new Date();
                Date datenow_mon=new Date();
                if(rs.getString("LAST_15") != null)
                    ammeter.setLast_15(rs.getString("LAST_15"));
                else {
                    String time=df_15.format(datenow);
                    ammeter.setLast_15(time);
                }


                if(rs.getString("LAST_DAY") != null)
                    ammeter.setLast_day(rs.getString("LAST_DAY"));
                else {
                    //减去一天
                    calendar.setTime(datenow);
                    calendar.add(Calendar.DATE,-1);//把日期往前增加一天.整数往后推,负数往前移动
                    datenow=calendar.getTime();   //这个时间就是日期往前推一天的结果
                    String time=df_day.format(datenow);
                    ammeter.setLast_day(time);
                }

                if(rs.getString("LAST_MON") != null)
                    ammeter.setLast_mon(rs.getString("LAST_MON"));
                else {
                    calendar.setTime(datenow_mon);
                    calendar.add(Calendar.MONTH,-1);//把yue期往前增加一天.整数往后推,负数往前移动
                    datenow_mon=calendar.getTime();   //这个时间就是yue期往前推一天的结果
                    String time=df_mon.format(datenow_mon);
                    ammeter.setLast_mon(time);
                }
                builds.add(ammeter);
            }
        });
        dataTable.setData(builds);
        return dataTable;
    }

    public Ammeter getAmmeterById(String id){
        String sql="select a.*,to_char(a.F_LAST_15,'YYYY-MM-dd HH24:mi') AS LAST_15,to_char(a.F_LAST_DAY,'YYYY-MM-dd') AS LAST_DAY," +
                "to_char(a.F_LAST_MON,'YYYY-MM') AS LAST_MON,b.F_EQUIPID AS F_EQUIP,c.F_BATCH,c.F_MODEL,d.F_TYPE,d.F_SUBTYPE,b.F_INSTALLTYPE,t.F_BUILDGROUPNAME,e.F_BUILDNAME,f.F_NAME AS FLOOR,g.F_NAME AS ROOM,b.F_LONGITUDE,b.F_LATITUDE,b.F_REMARK from ECSS.T_BE_AMMETER a,ECSS.T_BE_EQUIPMENTLIST b,ECSS.T_BE_EQUIPMENTBATCH c,ECSS.T_BE_EQUIPMENTTYPE d,ECSS.T_BD_BUILDGROUPRELAINFO h,ECSS.T_BD_BUILDGROUPBASEINFO t,ECSS.T_BD_BUILDBASEINFO e,ECSS.T_BD_FLOOR f,ECSS.T_BD_ROOM g ,ECSSTEMP.T_NODES n  " +
                "where a.F_UUID=b.F_UUID AND a.F_BATCHID=c.F_UUID AND b.F_TYPEID=d.F_UUID AND b.F_BUILDID=e.F_BUILDID(+) AND b.F_FLOORID=f.F_ID(+) AND b.F_ROOMID=g.F_ID(+) AND h.F_BUILDID(+)=b.F_BUILDID AND t.F_BUILDGROUPID(+)=h.F_BUILDGROUPID AND a.F_UUID=? AND a.F_UUID=n.F_UUID";
        Object[] args={id};
        final SimpleDateFormat df_15=new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        final SimpleDateFormat df_day=new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        final SimpleDateFormat df_mon=new SimpleDateFormat("yyyy-MM");//设置日期格式
        final Calendar   calendar   =   new   GregorianCalendar();
        final Ammeter ammeter=new Ammeter();
        jdbcTemplate.query(sql, args ,new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                ammeter.setUuid(rs.getString("F_UUID"));
                ammeter.setBatch(rs.getString("F_BATCH"));
                ammeter.setEquip(rs.getString("F_EQUIP"));
                ammeter.setType(rs.getString("F_TYPE"));
                ammeter.setSubtype(rs.getString("F_SUBTYPE"));
                ammeter.setModel(rs.getString("F_MODEL"));
                ammeter.setGateway(rs.getString("F_GATEWAYS_UUID"));
                ammeter.setPn(rs.getInt("F_PN"));
                ammeter.setNumber(rs.getInt("F_NUMBER"));
                ammeter.setSpeed(rs.getInt("F_SPEED_PORT")&0xf0);//高4bit为速率
                ammeter.setPort(rs.getInt("F_SPEED_PORT")&0x0f);//低4bit为
                ammeter.setProtocol(rs.getInt("F_PROTOCOL"));
                ammeter.setAddress(rs.getString("F_ADDRESS"));
                ammeter.setPassword(rs.getString("F_PASSWORD"));
                ammeter.setRatecount(rs.getInt("F_RATE_COUNT"));
                ammeter.setDataformat(rs.getInt("F_DATA_FORMAT"));
                ammeter.setClassnumber(rs.getInt("F_CLASS_NUMBER"));
                ammeter.setUse(rs.getInt("F_USE"));
                ammeter.setInstalltype(rs.getInt("F_INSTALLTYPE"));
                ammeter.setGroup(rs.getString("F_BUILDGROUPNAME"));
                ammeter.setBuild(rs.getString("F_BUILDNAME"));
                ammeter.setFloor(rs.getString("FLOOR"));
                ammeter.setRoom(rs.getString("ROOM"));
                ammeter.setLongitude(rs.getString("F_LONGITUDE"));
                ammeter.setLatitude(rs.getString("F_LATITUDE"));
                ammeter.setRemarkinfo(rs.getString("F_REMARKINFO"));
                ammeter.setCut(rs.getInt("F_CUT"));
                Date datenow=new Date();
                Date datenow_mon=new Date();
                if(rs.getString("LAST_15") != null)
                    ammeter.setLast_15(rs.getString("LAST_15"));
                else {
                    String time=df_15.format(datenow);
                    ammeter.setLast_15(time);
                }


                if(rs.getString("LAST_DAY") != null)
                    ammeter.setLast_day(rs.getString("LAST_DAY"));
                else {
                    //减去一天
                    calendar.setTime(datenow);
                    calendar.add(Calendar.DATE,-1);//把日期往前增加一天.整数往后推,负数往前移动
                    datenow=calendar.getTime();   //这个时间就是日期往前推一天的结果
                    String time=df_day.format(datenow);
                    ammeter.setLast_day(time);
                }

                if(rs.getString("LAST_MON") != null)
                    ammeter.setLast_mon(rs.getString("LAST_MON"));
                else {
                    calendar.setTime(datenow_mon);
                    calendar.add(Calendar.MONTH,-1);//把yue期往前增加一天.整数往后推,负数往前移动
                    datenow_mon=calendar.getTime();   //这个时间就是yue期往前推一天的结果
                    String time=df_mon.format(datenow_mon);
                    ammeter.setLast_mon(time);
                }

            }
        });
        return ammeter;
    }

    public void updateAmmeterById(Ammeter ammeter){
        //PN和NUMBER一样，相同的数据
        String addrString="00000000000"+ammeter.getAddress();//保证address有12位
        addrString=addrString.substring(addrString.length()-12).toUpperCase();
        String set_last_15="";
        if(ammeter.getLast_15() != null){
            set_last_15 = ","+"F_LAST_15=to_date('"+ammeter.getLast_15()+"','YYYY-MM-dd HH24:mi')";
        }

        String set_last_day="";
        if(ammeter.getLast_day() != null){
            set_last_day = ","+"F_LAST_DAY=to_date('"+ammeter.getLast_day()+"','YYYY-MM-dd')";
        }

        String set_last_mon="";
        if(ammeter.getLast_mon() != null){
            set_last_mon = ","+"F_LAST_MON=to_date('"+ammeter.getLast_mon()+"','YYYY-MM')";
        }

        int speedport=0;
        if(ammeter.getPort()==15){
            speedport =31;
        }
        else {
            speedport = ammeter.getSpeed() + ammeter.getPort();
        }
        String sql="update ECSS.t_be_ammeter set F_NEED_OPEN_TRG=0,F_CUT=?,F_REMARKINFO=?,F_GATEWAYS_UUID=?,F_PN=?,F_NUMBER=?,F_SPEED_PORT=?,F_PROTOCOL=?," +
                "F_ADDRESS=?,F_PASSWORD=?,F_RATE_COUNT=?,F_DATA_FORMAT=?,F_CLASS_NUMBER=?,F_IS_CHANGED=?"+set_last_15+set_last_day+set_last_mon+" where F_UUID=?";
        //需要把时间拷贝到ecsstemp表里去，需要触发触发器！因此将F_NEED_OPEN_TRG放到update下，具体的值无所谓！！！
        Object[] args={ammeter.getCut(),ammeter.getRemarkinfo(),ammeter.getGatewayid(),ammeter.getPn(),ammeter.getPn(),speedport,ammeter.getProtocol(),
                addrString,ammeter.getPassword(),ammeter.getRatecount(),ammeter.getDataformat(),
                ammeter.getClassnumber(),ammeter.getUuid().split(":")[1],ammeter.getUuid().split(":")[0]};
        jdbcTemplate.update(sql,args);
        if(ammeter.getUuid().split(":")[1].equals("1")){
            String sql_terminal = "select count(F_UUID) AS TERMINAL from t_nodes where F_IS_CHANGED=1 AND F_ONLY_LAST_CHANGED=0";//如果只有f_is_change=1则将参数同步到采集服务器和网关
            String sql_server = "select count(F_UUID) AS SERVER from t_nodes where F_IS_CHANGED=1 AND F_ONLY_LAST_CHANGED=1";//如果f_is_change=1和F_ONLY_LAST_CHANGED=1则只将参数同步到采集服务器
            final List<Integer> count=new ArrayList<Integer>();
            jdbcTemplate1.query(sql_terminal ,new RowCallbackHandler(){
                @Override
                public void processRow(ResultSet rs)throws SQLException{
                    count.add(rs.getInt("TERMINAL"));
                }
            });
            if(count.get(0) > 0){//如果需要同步到网关的信息数不少于1，则发送同步指令
                sql="insert into t_tasks(F_UUID,F_TYPE,F_FLAG) values(SYS_GUID(),'3','1')";
                jdbcTemplate1.update(sql);
                sql="insert into t_tasks(F_UUID,F_TYPE_BACKUP,F_FLAG_BACKUP) values(SYS_GUID(),'3','1')";
                jdbcTemplate1.update(sql);
            }
            else{//否则查看是否需要同步到采集服务器软件内的
                count.clear();
                jdbcTemplate1.query(sql_server ,new RowCallbackHandler(){
                    @Override
                    public void processRow(ResultSet rs)throws SQLException{
                        count.add(rs.getInt("SERVER"));
                    }
                });
                if(count.get(0) > 0){
                    sql="insert into t_tasks(F_UUID,F_TYPE,F_FLAG) values(SYS_GUID(),'3','1')";
                    jdbcTemplate1.update(sql);
                    sql="insert into t_tasks(F_UUID,F_TYPE_BACKUP,F_FLAG_BACKUP) values(SYS_GUID(),'3','1')";
                    jdbcTemplate1.update(sql);
                }
            }

        }
    }


    public void updateAmmetersById(Ammeter ammeter) {
        // TODO Auto-generated method stub
        String flag="";
        //System.out.println(String.format("%1$04x", 123));
        String pn_old_string="" , addr_old_string="";
        int    pn_old_int=0 , addr_old_int=0;
        if(ammeter.getGatewayid() != null){
            flag=flag+",F_GATEWAYS_UUID="+"'"+ammeter.getGatewayid()+"'";
        }
        if(ammeter.getPn() != 0){
            flag=flag+",F_PN="+ammeter.getPn()+",F_NUMBER="+ammeter.getPn();
            pn_old_string = "F_PN="+ammeter.getPn()+",F_NUMBER="+ammeter.getPn();
            pn_old_int = ammeter.getPn();
        }
        if(ammeter.getPort() != 0){
            if(ammeter.getSpeed()!=0){
                if(ammeter.getPort() == 15)
                    flag=flag+",F_SPEED_PORT=31";
                else {
                    flag=flag+",F_SPEED_PORT="+(ammeter.getPort()+ammeter.getSpeed());
                }
            }
            else{
                flag=flag+",F_SPEED_PORT=bitand(F_SPEED_PORT,240)+"+ammeter.getPort();//&0xf0后再加上port的值
            }

        }
        else if(ammeter.getSpeed()!=0){//如果当前仅进行设置波特率，此时不需要考虑speed=15 无需设置的情况
            flag=flag+",F_SPEED_PORT=bitand(F_SPEED_PORT,15)+"+ammeter.getSpeed();//&0x0f后再加上speed的值
        }

        if(ammeter.getProtocol() != 0){
            flag=flag+",F_PROTOCOL="+ammeter.getProtocol();
        }
        if(ammeter.getAddress() != null){
            flag=flag+",F_ADDRESS="+"'"+ammeter.getAddress().toUpperCase()+"'";
            addr_old_string = "F_ADDRESS="+"'"+ammeter.getAddress().toUpperCase()+"'";
            addr_old_int = Integer.parseInt(ammeter.getAddress().toUpperCase(), 16);
        }
        if(ammeter.getPassword() != null){
            flag=flag+",F_PASSWORD="+"'"+ammeter.getPassword()+"'";
        }
        if(ammeter.getRatecount() != 99999999){
            flag=flag+",F_RATE_COUNT="+ammeter.getRatecount();
        }
        if(ammeter.getDataformat() != 99999999){
            flag=flag+",F_DATA_FORMAT="+ammeter.getDataformat();
        }
        if(ammeter.getClassnumber() != 99999999){
            flag=flag+",F_CLASS_NUMBER="+ammeter.getClassnumber();
        }
        if(ammeter.getLast_15() != null){
            flag = flag+",F_LAST_15=to_date('"+ammeter.getLast_15()+"','YYYY-MM-dd HH24:mi')";
        }
        if(ammeter.getLast_day() != null){
            flag = flag+",F_LAST_DAY=to_date('"+ammeter.getLast_day()+"','YYYY-MM-dd')";
        }
        if(ammeter.getLast_mon() != null){
            flag = flag+",F_LAST_MON=to_date('"+ammeter.getLast_mon()+"','YYYY-MM')";
        }
        if(ammeter.getRemarkinfo() != null){
            flag = flag+",F_REMARKINFO='"+ammeter.getRemarkinfo()+"'";
        }

        flag=flag.substring(1);
        //根据uuid进行数据更新
        String sql="update T_BE_AMMETER set "+flag+" ,F_IS_CHANGED=?,F_NEED_OPEN_TRG=0 where F_UUID=?";//需要把时间拷贝到ecsstemp表里去，需要触发触发器！因此将F_NEED_OPEN_TRG放到update下，具体的值无所谓！！！
        String[] uuid=ammeter.getUuid().split(",");
        String pn_next_string="" , addr_next_string="";
        int pn_next_int=0 , addr_next_int=0;
        int need_set_cmd=0;
        for(int i=0;i<uuid.length;i++){
            if(pn_old_int != 0){
                pn_next_int = pn_old_int + i;
                pn_next_string = "F_PN="+pn_next_int+",F_NUMBER="+pn_next_int;
                sql=sql.replace(pn_old_string, pn_next_string);
                pn_old_string=pn_next_string;
            }

            if(addr_old_int != 0){
                addr_next_int = addr_old_int + i;
                addr_next_string = "F_ADDRESS="+"'"+String.format("%1$012x", addr_next_int).toUpperCase()+"'";
                sql=sql.replace(addr_old_string, addr_next_string);
                addr_old_string=addr_next_string;
            }
            if(uuid[i].split(":")[1].equals("1")){
                need_set_cmd = 1;
            }
            Object[] args={uuid[i].split(":")[1],uuid[i].split(":")[0]};
            jdbcTemplate.update(sql,args);
        }
        if(need_set_cmd == 1){
            sql="insert into t_tasks(F_UUID,F_TYPE,F_FLAG) values(SYS_GUID(),'3','1')";
            jdbcTemplate1.update(sql);
            sql="insert into t_tasks(F_UUID,F_TYPE_BACKUP,F_FLAG_BACKUP) values(SYS_GUID(),'3','1')";
            jdbcTemplate1.update(sql);
        }

    }

    public void setAmmeterUse(String ids){
        String sql="update T_BE_AMMETER set F_USE=?,F_IS_CHANGED=1 WHERE F_UUID=?";
        String[] regString=ids.split(",")[0].split(":");
        int i=0;
        Object[] args=null;
        for(i=0;i<regString.length;){
            args=new Object[]{ids.split(",")[1],regString[i]};
            jdbcTemplate.update(sql, args);
            i++;
        }
        String sql1="insert into t_tasks(F_UUID,F_TYPE,F_FLAG) values(sys_guid(),?,?)";
        String sql1_backup="insert into t_tasks(F_UUID,F_TYPE_backup,F_FLAG_backup) values(sys_guid(),?,?)";
        String uuid = UUID.randomUUID().toString();
        Object[] operate_uuid={"3","1"};
        jdbcTemplate1.update(sql1,operate_uuid);
        jdbcTemplate1.update(sql1_backup,operate_uuid);
    }

    public String set_ON_OFF(String ids){
        String   back_flag="";//存储执行后的返回标志
        String[] idss=ids.split("@");
        String[] uuids=idss[0].split(",");
        String[] equips=idss[1].split(",");
        String   on_off=idss[2];
        ////////////////////////////判断并生成开关指令数据///////////////////
        int[] cmd=null;
        if(on_off.equals("1")){
            cmd=new int[]{0x10, 0x05, 0x66, 0x00, 0x02, 0x04, 0x10, 0x20, 0x55, 0x55};
        }
        else {
            cmd=new int[]{0x10, 0x05, 0x66, 0x00, 0x02, 0x04, 0x10, 0x20, 0xaa, 0xaa};
        }
        ///////////////////////////////////////////////////////////////////////
        /////////////////////////////开始遍历节点信息并进行开关执行/////////////////////
        int i=0;
        for(i=0;i<uuids.length;i++){//少一次，split后，最后的一个数据是空的，不用执行
            String sql="select F_GATEWAYS_UUID,F_SPEED_PORT,F_ADDRESS from T_BE_AMMETER where F_UUID=?";
            Object[] args={uuids[i]};
            ///////////////获取当前表计所在网关已登陆到哪个主站上！
            String sql_get_server_flag="select F_SERVER_FLAG from T_BE_GATEWAY where F_UUID=(select F_GATEWAYS_UUID from T_BE_AMMETER where F_UUID=?)";
            final List<Integer> server_flag=new ArrayList<Integer>();
            server_flag.clear();
            jdbcTemplate.query(sql_get_server_flag, args, new RowCallbackHandler(){
                @Override
                public void processRow(ResultSet rs)throws SQLException{
                    server_flag.add(rs.getInt("F_SERVER_FLAG"));
                }
            });
            ///////////////////////////////////////////////////////////////////////////////////

            final Ammeter ammeter=new Ammeter();
            jdbcTemplate.query(sql, args ,new RowCallbackHandler(){//获取节点的从属网关、波特率、端口号、地址
                @Override
                public void processRow(ResultSet rs)throws SQLException{
                    ammeter.setGateway(rs.getString("F_GATEWAYS_UUID"));
                    ammeter.setSpeed(rs.getInt("F_SPEED_PORT")&0xf0);//高4bit为速率
                    ammeter.setPort(rs.getInt("F_SPEED_PORT")&0x0f);//低4bit为
                    ammeter.setAddress(rs.getString("F_ADDRESS"));
                }
            });
            String addr=ammeter.getAddress().replace(" ", "");
            addr = addr.substring(addr.length()-2,addr.length());//485地址转成

            String uuid = UUID.randomUUID().toString();
            String sql_set_cmd="";
            if(server_flag.get(0)==1)
                sql_set_cmd= "insert into t_tasks( f_uuid, F_GATEWAY_UUID, f_node_uuid, f_type, f_flag,f_send) values(?,?,?,?,?,?)";
            else {
                sql_set_cmd= "insert into t_tasks( f_uuid, F_GATEWAY_UUID, f_node_uuid, f_type_backup, f_flag_backup,f_send) values(?,?,?,?,?,?)";
            }
            String cmd_reg= GFP_T_Modbus.packing((ammeter.getSpeed()+ammeter.getPort()), ammeter.getAddress(),cmd);//生成指令序列
            Object[] args_set_cmd={uuid,ammeter.getGateway(),ids.split(",")[0],"1","71",cmd_reg};

            String sql_wait_success= "";
            if(server_flag.get(0)==1)
                sql_wait_success= "select f_flag,f_recv from t_tasks where f_uuid=?";
            else {
                sql_wait_success= "select f_flag_backup as f_flag,f_recv from t_tasks where f_uuid=?";
            }
            String sql_delete_cmdString="delete from t_tasks where f_uuid=?";
            Object[] operate_uuid={uuid};

            String sql1="update T_BE_AMMETER set F_ON_OFF=? WHERE F_UUID=?";
            Object[] args1={on_off,uuids[i]};

            jdbcTemplate1.update(sql_set_cmd, args_set_cmd);
            int time=0;
            final List<Integer> flag=new ArrayList<Integer>();
            final List<String> f_recv=new ArrayList<String>();

            for(time=0;time<10;time++){
                try {
                    Thread.sleep(1000);
                    flag.clear();
                    f_recv.clear();
                    jdbcTemplate1.query(sql_wait_success, operate_uuid, new RowCallbackHandler(){
                        @Override
                        public void processRow(ResultSet rs)throws SQLException{
                            flag.add(rs.getInt("f_flag"));
                            f_recv.add(rs.getString("f_recv"));
                        }
                    });
                } catch (Exception e) {
                    // TODO: handle exception
                }

                if(flag.get(0)==3){//执行成功
                    if(f_recv.get(0).length()>=12){
                        if(f_recv.get(0).substring(0, 12).equals(addr+"1005660002")){
                            jdbcTemplate.update(sql1, args1);
                            back_flag=back_flag+","+equips[i]+"@1";

                        }
                    }
                    else {
                        back_flag=back_flag+","+equips[i]+"@0";
                    }
                    break;

                }
                else if(flag.get(0)==9){//执行失败
                    back_flag=back_flag+","+equips[i]+"@0";
                    break;
                }
                else if(time==9)
                    back_flag=back_flag+","+equips[i]+"@2";
            }
            jdbcTemplate1.update(sql_delete_cmdString, operate_uuid);
        }
        return back_flag;
    }


    ///////////////////////////////////////////////////////////////////
    public DataTable getAllWatermeters(){
        String sql="select a.*,to_char(a.F_NEWEST_DATA_TIME,'YYYY-MM-dd HH24:mi:SS') AS NEWEST_DATA_TIME,to_char(a.F_NEWEST_OPERATE_TIME,'YYYY-MM-dd HH24:mi:SS') AS NEWEST_OPERATE_TIME,(sysdate-a.F_NEWEST_OPERATE_TIME) AS TIME_INTERVAL,a.F_EQUIPMENT_STATUE as STATUE, " +
                "x.F_ADDRESS AS GATEWAY,b.F_EQUIPID AS F_EQUIP,c.F_BATCH,c.F_MODEL,d.F_TYPE,d.F_SUBTYPE,b.F_INSTALLTYPE,t.F_BUILDGROUPNAME,e.F_BUILDNAME,f.F_NAME AS FLOOR,g.F_NAME AS ROOM,b.F_LONGITUDE,b.F_LATITUDE,b.F_REMARK from T_BE_WATERMETER a,T_BE_EQUIPMENTLIST b,T_BE_EQUIPMENTBATCH c,T_BE_EQUIPMENTTYPE d,T_BD_BUILDGROUPRELAINFO h,T_BD_BUILDGROUPBASEINFO t,T_BD_BUILDBASEINFO e,T_BD_FLOOR f,T_BD_ROOM g,T_BE_GATEWAY x " +
                "where a.F_GATEWAYS_UUID=x.F_UUID(+) AND a.F_UUID=b.F_UUID AND a.F_BATCHID=c.F_UUID AND b.F_TYPEID=d.F_UUID AND b.F_BUILDID=e.F_BUILDID(+) AND b.F_FLOORID=f.F_ID(+) AND b.F_ROOMID=g.F_ID(+) AND h.F_BUILDID(+)=b.F_BUILDID AND t.F_BUILDGROUPID(+)=h.F_BUILDGROUPID AND (x.F_ADDRESS IS NOT NULL OR b.F_INSTALLTYPE IS NOT NULL)";
        DataTable dataTable=new DataTable();
        final List<Watermeter> builds=new ArrayList<Watermeter>();
        jdbcTemplate.query(sql, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                Watermeter watermeter=new Watermeter();
                watermeter.setUuid(rs.getString("F_UUID"));
                watermeter.setBatch(rs.getString("F_BATCH"));
                watermeter.setEquip(rs.getString("F_EQUIP"));
                watermeter.setType(rs.getString("F_TYPE"));
                watermeter.setSubtype(rs.getString("F_SUBTYPE"));
                watermeter.setModel(rs.getString("F_MODEL"));
                if(rs.getString("GATEWAY") == null)
                    watermeter.setGateway("未分配");
                else
                    watermeter.setGateway(rs.getString("GATEWAY"));
                watermeter.setPn(rs.getInt("F_PN"));
                watermeter.setNumber(rs.getInt("F_NUMBER"));
                watermeter.setSpeed(rs.getInt("F_SPEED_PORT")&0xf0);//高4bit为速率
                watermeter.setPort(rs.getInt("F_SPEED_PORT")&0x0f);//低4bit为
                watermeter.setProtocol(rs.getInt("F_PROTOCOL"));
                watermeter.setAddress(rs.getString("F_ADDRESS"));
                watermeter.setPassword(rs.getString("F_PASSWORD"));
                watermeter.setClassnumber(rs.getInt("F_CLASS_NUMBER"));
                watermeter.setUse(rs.getInt("F_USE"));
                watermeter.setGroup(rs.getString("F_BUILDGROUPNAME"));
                watermeter.setBuild(rs.getString("F_BUILDNAME"));
                watermeter.setFloor(rs.getString("FLOOR"));
                watermeter.setRoom(rs.getString("ROOM"));
                watermeter.setLongitude(rs.getString("F_LONGITUDE"));
                watermeter.setLatitude(rs.getString("F_LATITUDE"));
                watermeter.setRemark(rs.getString("F_REMARK"));
                watermeter.setNewest_data(rs.getDouble("F_NEWEST_DATA"));
                watermeter.setNewest_data_time(rs.getString("NEWEST_DATA_TIME"));
                watermeter.setNewest_valid_data(rs.getDouble("F_NEWEST_VALID_DATA"));
                watermeter.setNewest_valid_data_time(rs.getString("F_NEWEST_VALID_DATA_TIME"));
                watermeter.setNewest_operate_time(rs.getString("NEWEST_OPERATE_TIME"));
                watermeter.setRemarkinfo(rs.getString("F_REMARKINFO"));
                if(rs.getDouble("F_NEWEST_DATA") == -1&&rs.getInt("STATUE")>3)
                    watermeter.setStatus(2);//故障
                else
                if((rs.getDouble("F_NEWEST_DATA") == -2)||(rs.getInt("F_USE") == 0)||(rs.getDouble("TIME_INTERVAL") >= 0.0208))
                    watermeter.setStatus(1);//离线
                builds.add(watermeter);
            }
        });
        dataTable.setData(builds);
        return dataTable;
    }
    public Watermeter getWatermeterById(String id){
        String sql="select a.*,to_char(a.F_LAST_15,'YYYY-MM-dd HH24:mi') AS LAST_15,to_char(a.F_LAST_DAY,'YYYY-MM-dd') AS LAST_DAY," +
                "to_char(a.F_LAST_MON,'YYYY-MM') AS LAST_MON,b.F_EQUIPID AS F_EQUIP,c.F_BATCH,c.F_MODEL,d.F_TYPE,d.F_SUBTYPE,b.F_INSTALLTYPE,t.F_BUILDGROUPNAME,e.F_BUILDNAME,f.F_NAME AS FLOOR,g.F_NAME AS ROOM,b.F_LONGITUDE,b.F_LATITUDE,b.F_REMARK from ECSS.T_BE_WATERMETER a,ECSS.T_BE_EQUIPMENTLIST b,ECSS.T_BE_EQUIPMENTBATCH c,ECSS.T_BE_EQUIPMENTTYPE d,ECSS.T_BD_BUILDGROUPRELAINFO h,ECSS.T_BD_BUILDGROUPBASEINFO t,ECSS.T_BD_BUILDBASEINFO e,ECSS.T_BD_FLOOR f,ECSS.T_BD_ROOM g " +
                "where a.F_UUID=b.F_UUID AND a.F_BATCHID=c.F_UUID AND b.F_TYPEID=d.F_UUID AND b.F_BUILDID=e.F_BUILDID(+) AND b.F_FLOORID=f.F_ID(+) AND b.F_ROOMID=g.F_ID(+) AND h.F_BUILDID(+)=b.F_BUILDID AND t.F_BUILDGROUPID(+)=h.F_BUILDGROUPID AND a.F_UUID=?";
        Object[] args={id};
        final SimpleDateFormat df_15=new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        final SimpleDateFormat df_day=new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        final SimpleDateFormat df_mon=new SimpleDateFormat("yyyy-MM");//设置日期格式
        final Calendar   calendar   =   new   GregorianCalendar();
        final Watermeter watermeter=new Watermeter();
        jdbcTemplate.query(sql, args ,new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                watermeter.setUuid(rs.getString("F_UUID"));
                watermeter.setBatch(rs.getString("F_BATCH"));
                watermeter.setEquip(rs.getString("F_EQUIP"));
                watermeter.setType(rs.getString("F_TYPE"));
                watermeter.setSubtype(rs.getString("F_SUBTYPE"));
                watermeter.setModel(rs.getString("F_MODEL"));
                watermeter.setGateway(rs.getString("F_GATEWAYS_UUID"));
                watermeter.setPn(rs.getInt("F_PN"));
                watermeter.setNumber(rs.getInt("F_NUMBER"));
                watermeter.setSpeed(rs.getInt("F_SPEED_PORT")&0xf0);//高4bit为速率
                watermeter.setPort(rs.getInt("F_SPEED_PORT")&0x0f);//低4bit为
                watermeter.setProtocol(rs.getInt("F_PROTOCOL"));
                watermeter.setClassnumber(rs.getInt("F_CLASS_NUMBER"));
                watermeter.setAddress(rs.getString("F_ADDRESS"));
                watermeter.setPassword(rs.getString("F_PASSWORD"));
                watermeter.setUse(rs.getInt("F_USE"));
                watermeter.setInstalltype(rs.getInt("F_INSTALLTYPE"));
                watermeter.setGroup(rs.getString("F_BUILDGROUPNAME"));
                watermeter.setBuild(rs.getString("F_BUILDNAME"));
                watermeter.setFloor(rs.getString("FLOOR"));
                watermeter.setRoom(rs.getString("ROOM"));
                watermeter.setLongitude(rs.getString("F_LONGITUDE"));
                watermeter.setLatitude(rs.getString("F_LATITUDE"));
                watermeter.setRemarkinfo(rs.getString("F_REMARKINFO"));
                Date datenow=new Date();
                Date datenow_mon=new Date();
                if(rs.getString("LAST_15") != null)
                    watermeter.setLast_15(rs.getString("LAST_15"));
                else {
                    String time = df_15.format(datenow);
                    watermeter.setLast_15(time);
                }


                if(rs.getString("LAST_DAY") != null)
                    watermeter.setLast_day(rs.getString("LAST_DAY"));
                else {
                    //减去一天
                    calendar.setTime(datenow);
                    calendar.add(Calendar.DATE,-1);//把日期往前增加一天.整数往后推,负数往前移动
                    datenow=calendar.getTime();   //这个时间就是日期往前推一天的结果
                    String time=df_day.format(datenow);
                    watermeter.setLast_day(time);
                }

                if(rs.getString("LAST_MON") != null)
                    watermeter.setLast_mon(rs.getString("LAST_MON"));
                else {
                    calendar.setTime(datenow_mon);
                    calendar.add(Calendar.MONTH,-1);//把yue期往前增加一天.整数往后推,负数往前移动
                    datenow_mon=calendar.getTime();   //这个时间就是yue期往前推一天的结果
                    String time=df_mon.format(datenow_mon);
                    watermeter.setLast_mon(time);
                }
            }
        });
        return watermeter;
    }

    public void updateWatermeterById(Watermeter watermeter){


        String set_last_15="";
        if(watermeter.getLast_15() != null){
            set_last_15 = ","+"F_LAST_15=to_date('"+watermeter.getLast_15()+"','YYYY-MM-dd HH24:mi')";
        }

        String set_last_day="";
        if(watermeter.getLast_day() != null){
            set_last_day = ","+"F_LAST_DAY=to_date('"+watermeter.getLast_day()+"','YYYY-MM-dd')";
        }

        String set_last_mon="";
        if(watermeter.getLast_mon() != null){
            set_last_mon = ","+"F_LAST_MON=to_date('"+watermeter.getLast_mon()+"','YYYY-MM')";
        }

        int speedport=0;
        if(watermeter.getPort()==15){
            speedport =31;
        }
        else {
            speedport = watermeter.getSpeed() + watermeter.getPort();
        }

        String sql="update T_BE_WATERMETER set F_NEED_OPEN_TRG=0, F_REMARKINFO=?,F_CLASS_NUMBER=?,F_GATEWAYS_UUID=?,F_PN=?,F_NUMBER=?,F_SPEED_PORT=?,F_PROTOCOL=?,F_ADDRESS=?," +
                "F_PASSWORD=?,F_IS_CHANGED=? "+set_last_15+set_last_day+set_last_mon+" where F_UUID=?";
        Object[] args={watermeter.getRemarkinfo(),watermeter.getClassnumber(),watermeter.getGatewayid(),watermeter.getPn(),watermeter.getPn(),speedport,
                watermeter.getProtocol(),watermeter.getAddress().toUpperCase(),watermeter.getPassword(),watermeter.getUuid().split(":")[1],watermeter.getUuid().split(":")[0]};
        jdbcTemplate.update(sql,args);



        if(watermeter.getUuid().split(":")[1].equals("1")){//先这样放着，目前没有区分是不是只更新到采集软件，等后面修复叶工的软件后再启用
            String sql_terminal = "select count(F_UUID) AS TERMINAL from t_nodes where F_IS_CHANGED=1 AND F_ONLY_LAST_CHANGED=0";//如果只有f_is_change=1则将参数同步到采集服务器和网关
            String sql_server = "select count(F_UUID) AS SERVER from t_nodes where F_IS_CHANGED=1 AND F_ONLY_LAST_CHANGED=1";//如果f_is_change=1和F_ONLY_LAST_CHANGED=1则只将参数同步到采集服务器
            final List<Integer> count=new ArrayList<Integer>();
            jdbcTemplate1.query(sql_terminal ,new RowCallbackHandler(){
                @Override
                public void processRow(ResultSet rs)throws SQLException{
                    count.add(rs.getInt("TERMINAL"));
                }
            });
            if(count.get(0) > 0){//如果需要同步到网关的信息数不少于1，则发送同步指令
                sql="insert into t_tasks(F_UUID,F_TYPE,F_FLAG) values(SYS_GUID(),'3','1')";
                jdbcTemplate1.update(sql);
                sql="insert into t_tasks(F_UUID,F_TYPE_BACKUP,F_FLAG_BACKUP) values(SYS_GUID(),'3','1')";
                jdbcTemplate1.update(sql);
            }
            else{//否则查看是否需要同步到采集服务器软件内的
                count.clear();
                jdbcTemplate1.query(sql_server ,new RowCallbackHandler(){
                    @Override
                    public void processRow(ResultSet rs)throws SQLException{
                        count.add(rs.getInt("SERVER"));
                    }
                });
                if(count.get(0) > 0){
                    sql="insert into t_tasks(F_UUID,F_TYPE,F_FLAG) values(SYS_GUID(),'3','1')";
                    jdbcTemplate1.update(sql);
                    sql="insert into t_tasks(F_UUID,F_TYPE_BACKUP,F_FLAG_BACKUP) values(SYS_GUID(),'3','1')";
                    jdbcTemplate1.update(sql);
                }
            }

        }
    }

    public void updateWatermetersById(Watermeter watermeter) {
        String flag="";
        String pn_old_string="" , addr_old_string="";
        int    pn_old_int=0 , addr_old_int=0;
        if(watermeter.getGatewayid() != null){
            flag=flag+",F_GATEWAYS_UUID="+"'"+watermeter.getGatewayid()+"'";
        }
        if(watermeter.getPn() != 0){
            flag=flag+",F_PN="+watermeter.getPn()+",F_NUMBER="+watermeter.getPn();
            pn_old_string = "F_PN="+watermeter.getPn()+",F_NUMBER="+watermeter.getPn();
            pn_old_int = watermeter.getPn();
        }
        if(watermeter.getPort() != 0){
            if(watermeter.getSpeed()!=0){
                if(watermeter.getPort() == 15)
                    flag=flag+",F_SPEED_PORT=31";
                else {
                    flag=flag+",F_SPEED_PORT="+(watermeter.getPort()+watermeter.getSpeed());
                }
            }
            else{
                flag=flag+",F_SPEED_PORT=bitand(F_SPEED_PORT,240)+"+watermeter.getPort();//&0xf0后再加上port的值
            }

        }
        else if(watermeter.getSpeed()!=0){//如果当前仅进行设置波特率，此时不需要考虑speed=15 无需设置的情况
            flag=flag+",F_SPEED_PORT=bitand(F_SPEED_PORT,15)+"+watermeter.getSpeed();//&0x0f后再加上speed的值
        }




        if(watermeter.getProtocol() != 0){
            flag=flag+",F_PROTOCOL="+watermeter.getProtocol();
        }
        if(watermeter.getClassnumber() != 0){
            flag=flag+",F_CLASS_NUMBER="+watermeter.getClassnumber();
        }
        if(watermeter.getAddress() != null){
            flag=flag+",F_ADDRESS="+"'"+watermeter.getAddress().toUpperCase()+"'";
            addr_old_string = "F_ADDRESS="+"'"+watermeter.getAddress().toUpperCase()+"'";
            addr_old_int = Integer.parseInt(watermeter.getAddress().toUpperCase(), 16);
        }
        if(watermeter.getPassword() != null){
            flag=flag+",F_PASSWORD="+"'"+watermeter.getPassword()+"'";
        }
        if(watermeter.getLast_15() != null){
            flag = flag+",F_LAST_15=to_date('"+watermeter.getLast_15()+"','YYYY-MM-dd HH24:mi')";
        }
        if(watermeter.getLast_day() != null){
            flag = flag+",F_LAST_DAY=to_date('"+watermeter.getLast_day()+"','YYYY-MM-dd')";
        }
        if(watermeter.getLast_mon() != null){
            flag = flag+",F_LAST_MON=to_date('"+watermeter.getLast_mon()+"','YYYY-MM')";
        }
        if(watermeter.getRemarkinfo() != null){
            flag = flag+",F_REMARKINFO='"+watermeter.getRemarkinfo()+"'";
        }

        flag=flag.substring(1);

        int use_flag=0;
        //根据uuid进行数据更新
        String sql="update T_BE_WATERMETER set "+flag+" ,F_IS_CHANGED=?,F_NEED_OPEN_TRG=0  where F_UUID=?";
        String[] uuid=watermeter.getUuid().split(",");
        String pn_next_string="" ,  addr_next_string="";
        int pn_next_int=0 ,  addr_next_int=0;

        for(int i=0;i<uuid.length;i++){
            if(pn_old_int != 0){
                pn_next_int = pn_old_int + i;
                pn_next_string = "F_PN="+pn_next_int+",F_NUMBER="+pn_next_int;
                sql=sql.replace(pn_old_string, pn_next_string);
                pn_old_string=pn_next_string;
            }

			/*if(number_old_int != 0){
				number_next_int = number_old_int + i;
				number_next_string = "F_NUMBER="+number_next_int;
				sql=sql.replace(number_old_string, number_next_string);
				number_old_string=number_next_string;
			}*/

            if(addr_old_int != 0){
                addr_next_int = addr_old_int + i;
                addr_next_string = "F_ADDRESS="+"'"+String.format("%1$012x", addr_next_int).toUpperCase()+"'";
                sql=sql.replace(addr_old_string, addr_next_string);
                addr_old_string=addr_next_string;
            }
            if(uuid[i].split(":")[1].equals("1")){
                use_flag = 1;
            }
            Object[] args={uuid[i].split(":")[1],uuid[i].split(":")[0]};
            jdbcTemplate.update(sql,args);
        }
        if(use_flag == 1){
            sql="insert into t_tasks(F_UUID,F_TYPE,F_FLAG) values(SYS_GUID(),'3','1')";
            jdbcTemplate1.update(sql);
            sql="insert into t_tasks(F_UUID,F_TYPE_BACKUP,F_FLAG_BACKUP) values(SYS_GUID(),'3','1')";
            jdbcTemplate1.update(sql);
        }

    }

    public void setWatermeterUse(String ids){
        String sql="update T_BE_WATERMETER set F_USE=?,F_IS_CHANGED=1  WHERE F_UUID=?";
        String[] regString=ids.split(",")[0].split(":");
        int i=0;
        Object[] args=null;
        for(i=0;i<regString.length;){
            args=new Object[]{ids.split(",")[1],regString[i]};
            jdbcTemplate.update(sql, args);
            i++;
        }
        String sql1="insert into t_tasks(F_UUID,F_TYPE,F_FLAG) values(sys_guid(),?,?)";
        String sql1_backup="insert into t_tasks(F_UUID,F_TYPE_BACKUP,F_FLAG_BACKUP) values(sys_guid(),?,?)";
        String uuid = UUID.randomUUID().toString();
        Object[] operate_uuid={"3","1"};
        jdbcTemplate1.update(sql1,operate_uuid);
        jdbcTemplate1.update(sql1_backup,operate_uuid);

    }

/////////////////////////////////////////////////////////////////////////


    public List<Measure> getAllMeasures(String id){
        String sql="select a.*,b.F_ENERGYITEMNAME,NVL(c.F_BUILDGROUPNAME,'') as GROUPNAME,NVL(d.F_BUILDNAME,'') as BUILDNAME,NVL(e.F_NAME,'') as FLOORNAME,NVL(f.F_NAME,'') AS ROOMNAME "+
                "from T_RR_DEVICERELATION a,T_DT_ENERGYITEMDICT b,T_BD_BUILDGROUPBASEINFO c,T_BD_BUILDBASEINFO d,T_BD_FLOOR e, T_BD_ROOM f  "+
                "WHERE  a.F_ENERGYITEMCODE=b.F_ENERGYITEMCODE AND a.F_DEVICECODE=? AND a.F_BUILDGROUPID=c.f_buildgroupid(+) and a.F_BUILDCODE=d.F_BUILDID(+) and a.F_FLOORID=e.F_ID(+) and a.f_roomid=f.F_ID(+)";
        Object[] args={id};
        final List<Measure> measures=new ArrayList<Measure>();
        jdbcTemplate.query(sql,args, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                final Measure measure=new Measure();
                measure.setUuid(rs.getString("F_UUID"));
                measure.setEnergyitemcode(rs.getString("F_ENERGYITEMNAME"));
                measure.setPercent(rs.getInt("F_PERCENT"));
                measure.setPlusminus(rs.getInt("F_PLUSMINUS"));
                measure.setGroup(rs.getString("GROUPNAME"));
                measure.setBuild(rs.getString("BUILDNAME"));
                measure.setFloor(rs.getString("FLOORNAME"));
                measure.setRoom(rs.getString("ROOMNAME"));
                measure.setRemark(rs.getString("F_REMARK"));
                measures.add(measure);
            }
        });
        return measures;
    }


    public List<Superiormeter> getSuperior_meter(String level){
        String sql="select t.F_BUILDGROUPID,t.F_BUILDCODE,t.F_FLOORID,t.f_roomid,t.F_ENERGYITEMCODE,t.F_DEVICECODE as EQUIPUUID,e.F_EQUIPID AS EQUIPID,a.F_BUILDGROUPNAME AS BUIDGROUP,b.F_BUILDNAME AS BUID,c.F_NAME AS FLOOR ,d.F_NAME AS ROOM ,f.F_ENERGYITEMNAME AS ENERGYITEMNAME "+
                "FROM T_RR_DEVICERELATION t,T_BD_BUILDGROUPBASEINFO a,T_BD_BUILDBASEINFO b,T_BD_FLOOR c, T_BD_ROOM d, T_BE_EQUIPMENTLIST e, T_DT_ENERGYITEMDICT f "+
                "WHERE t.F_LEVEL=? and t.f_devicecode=e.F_UUID and t.F_BUILDGROUPID=a.f_buildgroupid(+) and t.F_BUILDCODE=b.F_BUILDID(+) and t.F_FLOORID=c.F_ID(+) and t.f_roomid=d.F_ID(+) and t.F_ENERGYITEMCODE=f.F_ENERGYITEMCODE "+
                "order by t.F_BUILDGROUPID,t.F_BUILDCODE,t.F_FLOORID,t.f_roomid,t.F_ENERGYITEMCODE,t.F_DEVICECODE";
        Object[] args={level};
        final List<Superiormeter> Superiormeter=new ArrayList<com.wintoo.model.Superiormeter>();
        jdbcTemplate.query(sql, args, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                com.wintoo.model.Superiormeter superiormeter=new Superiormeter();
                superiormeter.setBuildgroup(rs.getString("BUIDGROUP"));
                superiormeter.setBuildgroupid(rs.getString("F_BUILDGROUPID"));
                superiormeter.setBuild(rs.getString("BUID"));
                superiormeter.setBuildid(rs.getString("F_BUILDCODE"));
                superiormeter.setFloor(rs.getString("FLOOR"));
                superiormeter.setFloorid(rs.getString("F_FLOORID"));
                superiormeter.setRoom(rs.getString("ROOM"));
                superiormeter.setRoomid(rs.getString("f_roomid"));
                superiormeter.setEnergyitemname(rs.getString("ENERGYITEMNAME"));
                superiormeter.setEnergyitemcode(rs.getString("F_ENERGYITEMCODE"));
                superiormeter.setEquipid(rs.getString("EQUIPID"));
                superiormeter.setEquipuuid(rs.getString("EQUIPUUID"));
                Superiormeter.add(superiormeter);
            }
        });
        return Superiormeter;
    }





    public void addMeasure(Measure measure){
        int	   superior_meter_level=measure.getSuperior_meter_level();
        String superior_meter="";
        int    direct_superior_meter=1;//设置关联时的上级表时，本变量用于标记上级表是否为本建筑体系内的表，如果不是则需要在这个上级表中做标记,1为直接上级表，0为非直接上级表
        if((measure.getSuperior_meter() != null) && (measure.getSuperior_meter().equals("#")==false)){
            String flag[]=measure.getSuperior_meter().split(",");
            if((superior_meter_level==3)&&(flag[0].equals(measure.getGroup())==false)){
                direct_superior_meter=0;
            }
            else if((superior_meter_level==2)&&(flag[0].equals(measure.getBuild())==false)){
                direct_superior_meter=0;
            }
            else if((superior_meter_level==1)&&(flag[0].equals(measure.getFloor())==false)){
                direct_superior_meter=0;
            }
            else if((superior_meter_level==0)&&(flag[0].equals(measure.getRoom())==false)){
                direct_superior_meter=0;
            }
            else
                direct_superior_meter=1;

            //上级表不是在本系建筑内，则需要到上级表那将F_DIRECT_CHILD_METER置为0（告诉该表有非本系建筑内的子表），0为由非本系建筑内的下级表（包括可能有本系建筑内的下级表），1为仅有本系建筑内的下级表，默认为2无下级表
            String sql_direct_child_meter="update T_RR_DEVICERELATION set F_DIRECT_CHILD_METER=? where F_DEVICECODE=? and F_DIRECT_CHILD_METER != 0";//如果已经被设置为0了，就不能再被设置为1了！
            Object[] args_direct_child_meter={direct_superior_meter,flag[1]};
            jdbcTemplate.update(sql_direct_child_meter, args_direct_child_meter);

            superior_meter=flag[1];
        }

		/*
		String  buildgroup="",build="",floor="",room="";
		if(measure.getLevel()==4){
			buildgroup="null";
			build="1";
			floor="1";
			room="1";
		}
		else{
			buildgroup=measure.getGroup();
			build=measure.getBuild();
			floor=measure.getFloor();
			room=measure.getRoom();
		}*/
        //如果是校级表，就让所有建筑编码全部为NULL，到时候仅需要搜索区域编码为NULL的关联信息就是校级表的关联信息
        String sql="insert into T_RR_DEVICERELATION(F_UUID,F_DEVICECODE,F_ENERGYITEMCODE,F_BUILDGROUPID,F_BUILDCODE,F_FLOORID,F_ROOMID,F_SUPERIOR_METER_LEVEL,F_SUPERIOR_METER,F_DIRECT_SUPERIOR_METER,F_PERCENT,F_PLUSMINUS,F_LEVEL,F_REMARK) VALUES(sys_guid(),?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] args={measure.getDevicecode(),measure.getEnergyitemcode(),measure.getGroup(),measure.getBuild(),measure.getFloor(),measure.getRoom(),measure.getSuperior_meter_level(),superior_meter,direct_superior_meter,measure.getPercent(),measure.getPlusminus(),measure.getLevel(),measure.getRemark()};
        jdbcTemplate.update(sql, args);
    }

    public void deleteMeasure(String id){
        String sql="delete from T_RR_DEVICERELATION where F_UUID=?";
        Object[] args={id};
        jdbcTemplate.update(sql,args);
    }
    public  EnergyChart getEnergyState(EnergySearch energySearch){
        Energy energy= new Energy();
        String sql=null;
        Object[] args=new Object[]{energySearch.getModelid(),energySearch.getStartdate(),energySearch.getModelid(),energySearch.getStartdate()};

        if(energySearch.getBasetime().equals("minutes")){
            sql="(select F_TIME_INTERVEL_ACTIVE,to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE=? AND F_TYPE=1 AND to_char(F_DATATIME,'yyyy/mm/dd')=?)" +
                    "UNION (select F_TIME_INTERVEL_ACTIVE,to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE=? AND F_TYPE=1 AND to_char(F_DATATIME,'yyyy/mm/dd')=?) ORDER BY time";

            energy.setName("15分钟");
        }
        else {
            sql = "(select F_ACTIVE,to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_15_ENERGY WHERE F_DEVICECODE=? AND F_TYPE=1 AND to_char(F_DATATIME,'yyyy/mm/dd')=?)" +
                    "UNION (select F_ACTIVE,to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE=? AND F_TYPE=1 AND to_char(F_DATATIME,'yyyy/mm/dd')=?) ORDER BY time";
            energy.setName("表盘数");
        }
        final List<Double> ldDoubles=new ArrayList<Double>();
        final List<String> categories=new ArrayList<String>();
        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                ldDoubles.add(rs.getDouble(1));
                categories.add(rs.getString(2));
            }
        });
        energy.setData(ldDoubles);
        EnergyChart eChart=new EnergyChart();
        eChart.setEnergy(energy);
        eChart.setCategories(categories);
        return eChart;
    }

//    public EnergyChart getEnergyState(EnergySearch energySearch){
//        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
//        Energy energy= new Energy();
//        String sql=null;
//        Object[] args={energySearch.getModelid(),energySearch.getStartdate(),energySearch.getEnddate()};
//
//        if(energySearch.getBasetime().equals("minutes")){
//            //sql="select F_CONSUM-(lag(F_CONSUM,1,F_CONSUM) over(order by F_DATATIME)),to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_WATERHISTORY WHERE F_DEVICECODE=? AND F_TYPE=1 AND to_char(F_DATATIME,'yyyy-mm-dd hh24:mi:ss')>=? AND to_char(F_DATATIME,'yyyy-mm-dd hh24:mi:ss')<=? ORDER BY time";
//            sql="select F_TIME_INTERVEL_ACTIVE,to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE=? AND F_TYPE=1 AND to_char(F_DATATIME,'yyyy/mm/dd')>=? AND to_char(F_DATATIME,'yyyy/mm/dd')<? ORDER BY time";
//        }
//        else
//        if(energySearch.getBasetime().equals("day")){
//            sql="select F_TIME_INTERVEL_ACTIVE,to_char(F_DATATIME,'yyyy-mm-dd') as day FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE=? AND F_TYPE=2 AND to_char(F_DATATIME,'yyyy/mm/dd')>=? AND to_char(F_DATATIME,'yyyy/mm/dd')<=?  ORDER BY day";
//        }
//        else
//        if(energySearch.getBasetime().equals("month")){
//            sql="select F_TIME_INTERVEL_ACTIVE,to_char(F_DATATIME,'yyyy-mm') as month FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE=? AND F_TYPE=3 AND to_char(F_DATATIME,'yyyy/mm/dd')>=? AND to_char(F_DATATIME,'yyyy/mm/dd')<=?  order by month";
//        }
//        energy.setName("用电量");
//        final List<Double> ldDoubles=new ArrayList<Double>();
//        final List<String> categories=new ArrayList<String>();
//        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
//            @Override
//            public void processRow(ResultSet rs) throws SQLException {
//                ldDoubles.add(rs.getDouble(1));
//                categories.add(rs.getString(2));
//            }
//        });
//        energy.setData(ldDoubles);
//        EnergyChart eChart=new EnergyChart();
//        eChart.setEnergy(energy);
//        eChart.setCategories(categories);
//        return eChart;
//    }
//    public EnergyChart getWaterState(EnergySearch energySearch){
//        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
//        Energy energy= new Energy();
//        String sql=null;
//        Object[] args={energySearch.getModelid(),energySearch.getStartdate(),energySearch.getEnddate()};
//
//        if(energySearch.getBasetime().equals("minutes")){
//            //sql="select F_CONSUM-(lag(F_CONSUM,1,F_CONSUM) over(order by F_DATATIME)),to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_WATERHISTORY WHERE F_DEVICECODE=? AND F_TYPE=1 AND to_char(F_DATATIME,'yyyy-mm-dd hh24:mi:ss')>=? AND to_char(F_DATATIME,'yyyy-mm-dd hh24:mi:ss')<=? ORDER BY time";
//            sql="select F_TIME_INTERVEL_ACTIVE,to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE=? AND F_TYPE=1 AND to_char(F_DATATIME,'yyyy/mm/dd')>=? AND to_char(F_DATATIME,'yyyy/mm/dd')<? ORDER BY time";
//        }
//        else
//        if(energySearch.getBasetime().equals("hour")){
//            sql="select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'hh24') as day FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE=? AND F_TYPE=1 AND to_char(F_DATATIME,'yyyy/mm/dd')>=? AND to_char(F_DATATIME,'yyyy/mm/dd')<?  group by to_char(F_DATATIME,'hh24') ORDER BY day";
//        }
//        else
//        if(energySearch.getBasetime().equals("day")){
//            sql="select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'yyyy-mm-dd') as day FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE=? AND F_TYPE=1 AND to_char(F_DATATIME,'yyyy/mm/dd')>=? AND to_char(F_DATATIME,'yyyy/mm/dd')<?  group by to_char(F_DATATIME,'yyyy-mm-dd') ORDER BY day";
//        }
//        else
//        if(energySearch.getBasetime().equals("month")){
//            sql="select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'yyyy-mm') as month FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE=? AND F_TYPE=1 AND to_char(F_DATATIME,'yyyy/mm/dd')>=? AND to_char(F_DATATIME,'yyyy/mm/dd')<? group by to_char(F_DATATIME,'yyyy-mm') order by month";
//        }
//        energy.setName("用水量");
//        final List<Double> ldDoubles=new ArrayList<Double>();
//        final List<String> categories=new ArrayList<String>();
//        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
//            @Override
//            public void processRow(ResultSet rs) throws SQLException {
//                ldDoubles.add(rs.getDouble(1));
//                categories.add(rs.getString(2));
//            }
//        });
//        energy.setData(ldDoubles);
//        EnergyChart eChart=new EnergyChart();
//        eChart.setEnergy(energy);
//        eChart.setCategories(categories);
//        return eChart;
//    }
//    /////////////////////////////////////////////////////////////
//    public  EnergyChart getEnergyState(EnergySearch energySearch){
//        Energy energy= new Energy();
//        String sql=null;
//        Object[] args={energySearch.getModelid(),energySearch.getStartdate(),energySearch.getEnddate()};
//        System.out.println(energySearch.getStartdate()+"######"+energySearch.getEnddate());
//        if(energySearch.getBasetime().equals("minutes")){
//            sql="select F_TIME_INTERVEL_ACTIVE,to_char(F_DATATIME,'hh24:mi') as time FROM T_AD_15_ENERGY_AMMETER WHERE F_DEVICECODE=?  AND F_DATATIME>=to_date(?,'yyyy/mm/dd') AND F_DATATIME<to_date(?,'yyyy/mm/dd')  ORDER BY time";
//        }
//        else
//        if(energySearch.getBasetime().equals("day")){
//            sql="select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'yyyy-mm-dd') as day FROM T_AD_15_ENERGY_AMMETER WHERE F_DEVICECODE=?  AND F_DATATIME>=to_date(?,'yyyy/mm/dd') AND F_DATATIME<to_date(?,'yyyy/mm/dd')  group by to_char(F_DATATIME,'yyyy-mm-dd') ORDER BY day";
//        }
//        else
//        if(energySearch.getBasetime().equals("month")){
//            sql="select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'yyyy-mm') as month FROM T_AD_15_ENERGY_AMMETER WHERE F_DEVICECODE=?  AND F_DATATIME>=to_date(?,'yyyy/mm/dd') AND F_DATATIME<to_date(?,'yyyy/mm/dd') group by to_char(F_DATATIME,'yyyy-mm') order by month";
//        }
//        energy.setName("用电量");
//        final List<Double> ldDoubles=new ArrayList<Double>();
//        final List<String> categories=new ArrayList<String>();
//        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
//            @Override
//            public void processRow(ResultSet rs) throws SQLException {
//                ldDoubles.add(rs.getDouble(1));
//                categories.add(rs.getString(2));
//            }
//        });
//        energy.setData(ldDoubles);
//        EnergyChart eChart=new EnergyChart();
//        eChart.setEnergy(energy);
//        eChart.setCategories(categories);
//        return eChart;
//    }
//    public  EnergyChart getWaterState(EnergySearch energySearch){
//        Energy energy= new Energy();
//        String sql=null;
//        Object[] args={energySearch.getModelid(),energySearch.getStartdate(),energySearch.getEnddate()};
//        if(energySearch.getBasetime().equals("minutes")){
//            sql="select F_TIME_INTERVEL_ACTIVE,to_char(F_DATATIME,'hh24:mi') as time FROM T_AD_15_ENERGY_WATER WHERE F_DEVICECODE=? AND F_DATATIME>=to_date(?,'yyyy/mm/dd') AND F_DATATIME<to_date(?,'yyyy/mm/dd') ORDER BY time";
//        }
//        else
//        if(energySearch.getBasetime().equals("day")){
//            sql="select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'yyyy-mm-dd') as day FROM T_AD_15_ENERGY_WATER WHERE F_DEVICECODE=? AND F_DATATIME>=to_date(?,'yyyy/mm/dd') AND F_DATATIME<to_date(?,'yyyy/mm/dd')  group by to_char(F_DATATIME,'yyyy-mm-dd') ORDER BY day";
//        }
//        else
//        if(energySearch.getBasetime().equals("month")){
//            sql="select sum(F_TIME_INTERVEL_ACTIVE),to_char(F_DATATIME,'yyyy-mm') as month FROM T_AD_15_ENERGY_WATER WHERE F_DEVICECODE=? AND F_DATATIME>=to_date(?,'yyyy/mm/dd') AND F_DATATIME<to_date(?,'yyyy/mm/dd') group by to_char(F_DATATIME,'yyyy-mm') order by month";
//        }
//        energy.setName("用水量");
//        final List<Double> ldDoubles=new ArrayList<Double>();
//        final List<String> categories=new ArrayList<String>();
//        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
//            @Override
//            public void processRow(ResultSet rs) throws SQLException {
//                ldDoubles.add(rs.getDouble(1));
//                categories.add(rs.getString(2));
//            }
//        });
//        energy.setData(ldDoubles);
//        EnergyChart eChart=new EnergyChart();
//        eChart.setEnergy(energy);
//        eChart.setCategories(categories);
//        return eChart;
//    }
    public EnergyChart getEnergyParameter(ParameterSearch parameterSearch){
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        Energy energy= new Energy();
        Object[] args={parameterSearch.getEquipid(),df.format(parameterSearch.getDatetime())};
        String sql="select F_"+parameterSearch.getParameter()+",to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE=? AND F_TYPE=1 AND to_char(F_DATATIME,'yyyy-mm-dd')=? ORDER BY time";
        final List<Double> ldDoubles=new ArrayList<Double>();
        final List<String> categories=new ArrayList<String>();
        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                ldDoubles.add(rs.getDouble(1));
                categories.add(rs.getString(2));
            }
        });
        energy.setName(parameterSearch.getParameter());
        energy.setData(ldDoubles);
        EnergyChart eChart=new EnergyChart();
        eChart.setEnergy(energy);
        eChart.setCategories(categories);
        return eChart;
    }
    public EnergyChart getWaterParameter(ParameterSearch parameterSearch){
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        Energy energy= new Energy();
        Object[] args={parameterSearch.getEquipid(),df.format(parameterSearch.getDatetime())};
        String sql="select F_"+parameterSearch.getParameter()+",to_char(F_DATATIME,'hh24:mi') as time FROM T_BE_15_ENERGY_BUFFER WHERE F_DEVICECODE=? AND F_TYPE=1 AND to_char(F_DATATIME,'yyyy-mm-dd')=? ORDER BY time";
        final List<Double> ldDoubles=new ArrayList<Double>();
        final List<String> categories=new ArrayList<String>();
        jdbcTemplate.query(sql,args, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                ldDoubles.add(rs.getDouble(1));
                categories.add(rs.getString(2));
            }
        });
        energy.setName(parameterSearch.getParameter());
        energy.setData(ldDoubles);
        EnergyChart eChart=new EnergyChart();
        eChart.setEnergy(energy);
        eChart.setCategories(categories);
        return eChart;
    }
    public AmmeterData getAmmeterData(String id){
        String sql3="select F_SERVER_FLAG from T_BE_GATEWAY where F_UUID=(select F_GATEWAYS_UUID from T_BE_AMMETER where F_UUID=?)";
        Object[] args3={id};
        final List<Integer> server_flag=new ArrayList<Integer>();
        server_flag.clear();
        jdbcTemplate.query(sql3, args3, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                server_flag.add(rs.getInt("F_SERVER_FLAG"));
            }
        });
        String sql= "";
        String sql1="";
        if(server_flag.get(0)==1){
            sql= "insert into t_tasks( f_uuid, f_node_uuid, f_type, f_flag ) values(?,?,?,?)";
            sql1="select f_flag,f_node_data_uuid from t_tasks where f_uuid=?";
        }
        else {
            sql= "insert into t_tasks( f_uuid, f_node_uuid, f_type_backup, f_flag_backup ) values(?,?,?,?)";
            sql1="select f_flag_backup as f_flag,f_node_data_uuid from t_tasks where f_uuid=?";
        }



        String uuid = UUID.randomUUID().toString();
        //String sql2="select F_UUID,F_NODES_UUID,F_ACTIVE,F_REACTIVE,to_char(F_DATA_TIME,'HH24:MI:SS') AS F_TIME,F_TYPE,F_IS_NEW,F_P,F_PA,F_PB,F_PC,F_NP,F_NPA,F_NPB,F_NPC,F_UA,F_UB,F_UC,F_IA,F_IB,F_IC,F_GLYS,F_GLYSA,F_GLYSB,F_GLYSC from t_node_data where f_uuid=?";
        String sql2="select * from t_node_data where f_uuid=?";
        Object[] args={uuid,id,"1","31"};
        Object[] args1={uuid};
        jdbcTemplate1.update(sql,args);

        SimpleDateFormat df=new SimpleDateFormat("MM-dd HH:mm:ss");//设置日期格式
        final String nowtime=df.format(new Date());

        int time=0;
        final List<Integer> flag=new ArrayList<Integer>();
        final List<String> datauuid=new ArrayList<String>();
        while(true){
            try {
                Thread.sleep(1000);
                if(time++<10){
                    flag.clear();
                    datauuid.clear();
                    jdbcTemplate1.query(sql1, args1, new RowCallbackHandler(){
                        @Override
                        public void processRow(ResultSet rs)throws SQLException{
                            flag.add(rs.getInt("f_flag"));
                            datauuid.add(rs.getString("f_node_data_uuid"));
                        }
                    });
                    if(flag.get(0)==3){
                        final AmmeterData ammeterData=new AmmeterData();
                        Object[] args2=new Object[]{datauuid.get(0)};
                        jdbcTemplate1.query(sql2, args2, new RowCallbackHandler(){
                            @Override
                            public void processRow(ResultSet rs)throws SQLException{
                                ammeterData.setActive(rs.getDouble("f_active"));
                                ammeterData.setReactive(rs.getDouble("f_reactive"));
                                ammeterData.setDatatime(nowtime);
                                ammeterData.setUa(rs.getDouble("f_ua"));
                                ammeterData.setUb(rs.getDouble("f_ub"));
                                ammeterData.setUc(rs.getDouble("f_uc"));
                                ammeterData.setIa(rs.getDouble("f_ia"));
                                ammeterData.setIb(rs.getDouble("f_ib"));
                                ammeterData.setIc(rs.getDouble("f_ic"));
                                ammeterData.setP(rs.getDouble("f_p"));
                                ammeterData.setPa(rs.getDouble("f_pa"));
                                ammeterData.setPb(rs.getDouble("f_pb"));
                                ammeterData.setPc(rs.getDouble("f_pc"));
                                ammeterData.setNp(rs.getDouble("f_np"));
                                ammeterData.setNpa(rs.getDouble("f_npa"));
                                ammeterData.setNpb(rs.getDouble("f_npb"));
                                ammeterData.setNpc(rs.getDouble("f_npc"));
                                ammeterData.setGlys(rs.getDouble("f_glys"));
                                ammeterData.setGlysa(rs.getDouble("f_Glysa"));
                                ammeterData.setGlysb(rs.getDouble("f_Glysb"));
                                ammeterData.setGlysc(rs.getDouble("f_Glysc"));
                            }
                        });
                        return ammeterData;
                    }
                }
                else{
                    return null;
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public WatermeterData getWatermeterData(String id){
        String sql3="select F_SERVER_FLAG from T_BE_GATEWAY where F_UUID=(select F_GATEWAYS_UUID from T_BE_WATERMETER where F_UUID=?)";
        Object[] args3={id};
        final List<Integer> server_flag=new ArrayList<Integer>();
        server_flag.clear();
        jdbcTemplate.query(sql3, args3, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                server_flag.add(rs.getInt("F_SERVER_FLAG"));
            }
        });
        String uuid = UUID.randomUUID().toString();
        String sql= "";
        String sql1= "";
        if(server_flag.get(0)==1){
            sql= "insert into t_tasks( f_uuid, f_node_uuid, f_type, f_flag ) values(?,?,?,?)";
            sql1="select f_flag,f_node_data_uuid from t_tasks where f_uuid=?";
        }
        else{
            sql= "insert into t_tasks( f_uuid, f_node_uuid, f_type_backup, f_flag_backup ) values(?,?,?,?)";
            sql1="select f_flag_backup as f_flag,f_node_data_uuid from t_tasks where f_uuid=?";
        }
        String sql2="select * from t_w_node_data where f_uuid=?";
        Object[] args={uuid,id,"1","31"};
        Object[] args1={uuid};
        jdbcTemplate1.update(sql,args);
        SimpleDateFormat df=new SimpleDateFormat("MM-dd HH:mm:ss");//设置日期格式
        final String nowtime=df.format(new Date());
        int time=0;
        final List<Integer> flag=new ArrayList<Integer>();
        final List<String> datauuid=new ArrayList<String>();
        while(true){
            try {
                Thread.sleep(1000);
                if(time++<10){
                    flag.clear();
                    datauuid.clear();
                    jdbcTemplate1.query(sql1, args1, new RowCallbackHandler(){
                        @Override
                        public void processRow(ResultSet rs)throws SQLException{
                            flag.add(rs.getInt("f_flag"));
                            datauuid.add(rs.getString("f_node_data_uuid"));
                        }
                    });
                    if(flag.get(0)==3){
                        final WatermeterData watermeterData=new WatermeterData();
                        Object[] args2=new Object[]{datauuid.get(0)};
                        jdbcTemplate1.query(sql2, args2, new RowCallbackHandler(){
                            @Override
                            public void processRow(ResultSet rs)throws SQLException{
                                watermeterData.setConsum(rs.getDouble("f_consum"));
                                watermeterData.setDatatime(nowtime);
                                watermeterData.setFlow(rs.getDouble("f_flow"));
                                watermeterData.setU(rs.getDouble("f_u"));
                            }
                        });
                        return watermeterData;
                    }
                }
                else{
                    return null;
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public WatermeterData getWaterInfo(String equipid){
        String sql="select F_devicecode,F_ACTIVE,F_P,F_UA,F_DATATIME,F_REMARK from (select a.F_REMARK,b.* from T_BE_EQUIPMENTLIST a,T_BE_15_ENERGY_BUFFER b where a.F_UUID=b.F_DEVICECODE and a.F_EQUIPID=?  order by b.F_DATATIME desc) where rownum=1";
        final WatermeterData watermeterData=new WatermeterData();
        Object[] args=new Object[]{equipid};
        jdbcTemplate.query(sql, args, new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs)throws SQLException{
                watermeterData.setId(rs.getString(1));
                watermeterData.setConsum(rs.getDouble(2));
                watermeterData.setU(rs.getDouble(3));
                watermeterData.setFlow(rs.getDouble(4));
                watermeterData.setDatatime(rs.getString(5)+','+rs.getString(6));
            }
        });
        return watermeterData;
    }

    public void inputAmData(String data) {
        String[] strings=data.split(",");
        String sql="insert into T_NODE_DATA(F_UUID,F_NODES_UUID,F_DATA_TIME,F_ACTIVE,F_TYPE,F_IS_NEW) values(sys_guid(),?,to_date(?,'yyyy/mm/dd hh24:mi'),?,1,1)";
        Object[] args={strings[0],strings[1],strings[2]};
        jdbcTemplate1.update(sql, args);
    }

    public void inputWaterData(String data) {
        String[] strings=data.split(",");
        String sql="insert into T_W_NODE_DATA(F_UUID,F_NODES_UUID,F_DATA_TIME,F_CONSUM,F_TYPE,F_IS_NEW) values(sys_guid(),?,to_date(?,'yyyy/mm/dd hh24:mi'),?,1,1)";
        Object[] args={strings[0],strings[1],strings[2]};
        jdbcTemplate1.update(sql, args);
    }

    public void setLastActive(String data) throws ParseException {
        String[] strings=data.split(",");
        SimpleDateFormat sdf  =   new  SimpleDateFormat( "yyyy/MM/dd HH:mm" );
        SimpleDateFormat hoursdf  =   new  SimpleDateFormat( "yyyy/MM/dd HH" );
        SimpleDateFormat daysdf  =   new  SimpleDateFormat( "yyyy/MM/dd" );
        SimpleDateFormat monthsdf  =   new  SimpleDateFormat( "yyyy/MM" );
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(sdf.parse(strings[1]));
        Calendar day=(Calendar)calendar.clone();
        day.add(Calendar.DAY_OF_MONTH,-1);
        Calendar mon=(Calendar)calendar.clone();
        mon.add(Calendar.MONTH,-1);
        String sql="update T_NODES set F_LOCK_PARA=1 where F_UUID=?";
        Object[] args=new Object[]{strings[0]};
        System.out.println(sql);
        jdbcTemplate1.update(sql, args);
        sql="UPDATE T_BE_AMMETER set F_NEWEST_VALID_DATA=?,F_NEWEST_VALID_DATA_TIME=to_date(?,'yyyy/mm/dd hh24:mi'),F_HOUR_VALID_DATA=?,F_HOUR_VALID_DATA_TIME=to_date(?,'yyyy/mm/dd hh24')," +
                "F_DAY_VALID_DATA=?,F_DAY_VALID_DATA_TIME=to_date(?,'yyyy/mm/dd'),F_MON_VALID_DATA=?,F_MON_VALID_DATA_TIME=to_date(?,'yyyy/mm') WHERE F_UUID=?";
        args=new Object[]{strings[2],sdf.format(calendar.getTime()),strings[2],hoursdf.format(calendar.getTime()),strings[2],daysdf.format(day.getTime()),strings[2],monthsdf.format(mon.getTime()),strings[0]};
        System.out.println(sql);
        jdbcTemplate.update(sql, args);
        sql="insert into ecsstemp.T_NODE_DATA(F_UUID,F_NODES_UUID,F_ACTIVE,F_REACTIVE,F_DATA_TIME,F_TYPE,F_P,F_PA,F_PB,F_PC,F_NP,F_NPA,F_NPB,F_NPC,F_UA,F_UB,F_UC,F_IA,F_IB,F_IC,F_GLYS,F_GLYSA,F_GLYSB,F_GLYSC,F_IS_NEW) " +
                "SELECT F_ID,F_DEVICECODE,F_ACTIVE,F_REACTIVE,F_DATATIME,F_TYPE,F_P,F_PA,F_PB,F_PC,F_NP,F_NPA,F_NPB,F_NPC,F_UA,F_UB,F_UC,F_IA,F_IB,F_IC,F_GLYS,F_GLYSA,F_GLYSB,F_GLYSC,1 from T_BE_BAD_DATA " +
                "where F_DEVICECODE=? and F_DATATIME>to_date(?,'yyyy/mm/dd hh24:mi') AND F_TYPE=1 ORDER BY F_DATATIME";
        args=new Object[]{strings[0],strings[1]};
        System.out.println(sql);
        jdbcTemplate.update(sql, args);
//        sql="DELETE T_BE_BAD_DATA where F_DEVICECODE=? and F_DATATIME>=to_date(?,'yyyy/mm/dd hh24:mi')";
//        args=new Object[]{strings[0],strings[1]};
//        jdbcTemplate.update(sql, args);
        sql="update T_NODES set F_LOCK_PARA=0 where F_UUID=?";
        args=new Object[]{strings[0]};
        System.out.println(sql);
        jdbcTemplate1.update(sql, args);
    }

    public void setLastWaterActive(String data) throws ParseException {
        String[] strings=data.split(",");
        SimpleDateFormat sdf  =   new  SimpleDateFormat( "yyyy/MM/dd HH:mm" );
        SimpleDateFormat hoursdf  =   new  SimpleDateFormat( "yyyy/MM/dd HH" );
        SimpleDateFormat daysdf  =   new  SimpleDateFormat( "yyyy/MM/dd" );
        SimpleDateFormat monthsdf  =   new  SimpleDateFormat( "yyyy/MM" );
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(sdf.parse(strings[1]));
        Calendar day=(Calendar)calendar.clone();
        day.add(Calendar.DAY_OF_MONTH,-1);
        Calendar mon=(Calendar)calendar.clone();
        mon.add(Calendar.MONTH,-1);
        String sql="update T_NODES set F_LOCK_PARA=1 where F_UUID=?";
        Object[] args=new Object[]{strings[0]};
        System.out.println(sql);
        jdbcTemplate1.update(sql, args);
        sql="UPDATE T_BE_WATERMETER set F_NEWEST_VALID_DATA=?,F_NEWEST_VALID_DATA_TIME=to_date(?,'yyyy/mm/dd hh24:mi'),F_HOUR_VALID_DATA=?,F_HOUR_VALID_DATA_TIME=to_date(?,'yyyy/mm/dd hh24')," +
                "F_DAY_VALID_DATA=?,F_DAY_VALID_DATA_TIME=to_date(?,'yyyy/mm/dd'),F_MON_VALID_DATA=?,F_MON_VALID_DATA_TIME=to_date(?,'yyyy/mm') WHERE F_UUID=?";
        args=new Object[]{strings[2],sdf.format(calendar.getTime()),strings[2],hoursdf.format(calendar.getTime()),strings[2],daysdf.format(day.getTime()),strings[2],monthsdf.format(mon.getTime()),strings[0]};
        System.out.println(sql);
        jdbcTemplate.update(sql, args);
        sql="insert into ecsstemp.T_W_NODE_DATA(F_UUID,F_NODES_UUID,F_CONSUM,F_DATA_TIME,F_TYPE,F_U,F_FLOW,F_IS_NEW) " +
                "SELECT F_ID,F_DEVICECODE,F_ACTIVE,F_DATATIME,F_TYPE,F_P,F_UA,1 from T_BE_BAD_DATA " +
                "where F_DEVICECODE=? and F_DATATIME>to_date(?,'yyyy/mm/dd hh24:mi') AND F_TYPE=1 ORDER BY F_DATATIME";
        args=new Object[]{strings[0],strings[1]};
        System.out.println(sql);
        jdbcTemplate.update(sql, args);
//        sql="DELETE T_BE_BAD_DATA where F_DEVICECODE=? and F_DATATIME>=to_date(?,'yyyy/mm/dd hh24:mi')";
//        args=new Object[]{strings[0],strings[1]};
//        jdbcTemplate.update(sql, args);
        sql="update T_NODES set F_LOCK_PARA=0 where F_UUID=?";
        args=new Object[]{strings[0]};
        System.out.println(sql);
        jdbcTemplate1.update(sql, args);
    }
    ///////////////////////////////////////////////////////
    public DataTable getPickuploads(){
        String sql = "select F_DATATIME,F_PICKUPSTATUS,F_UPLOADSTATUS,F_OPERATETIME,F_FILENAME from T_BP_PICKUPLOAD ORDER BY F_DATATIME";
        DataTable dataTable = new DataTable();
        final List<Pickupload> pickuploads = new ArrayList<Pickupload>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Pickupload pickupload = new Pickupload();
                pickupload.setDatatime(rs.getString(1));
                pickupload.setPickstatus(rs.getInt(2));
                pickupload.setUploadstatus(rs.getInt(3));
                pickupload.setOperatetime(rs.getString(4));
                pickupload.setFilename(rs.getString(5));
                pickuploads.add(pickupload);
            }
        });
        dataTable.setData(pickuploads);
        return dataTable;
    }
    public void pickup(final String date, final String path){
        int success=0;
        String filename="";
        try {
            String dataCenter=makeBuildXml(date,path);
            filename=makeEnergyXml(date,dataCenter,path);
            zipFile(path,filename);
        } catch (Exception e) {
            success=1;
        } finally{
            String sql="select count(*) num from T_BP_PICKUPLOAD where to_char(f_datatime,'yyyy/mm/dd')=?";
            int size=((BigDecimal)jdbcTemplate.queryForMap(sql,date).get("num")).intValue();
            if(size==0){
                sql="INSERT INTO T_BP_PICKUPLOAD(F_DATATIME) VALUES(to_date(?,'yyyy/mm/dd'))";
                jdbcTemplate.update(sql, date);
            }
            if(success==0){
                try {
                    final File binaryFile=new File(path+filename+".zip");
                    final InputStream is=new FileInputStream(binaryFile);
                    final LobHandler lobHandler=new DefaultLobHandler();
                    final String finalFilename = filename;
                    jdbcTemplate.execute("UPDATE T_BP_PICKUPLOAD SET F_PICKUPSTATUS=0,F_FILENAME=?,F_FILE=?,F_OPERATETIME=sysdate WHERE TO_CHAR(F_DATATIME,'yyyy/mm/dd')=?",
                            new AbstractLobCreatingPreparedStatementCallback(lobHandler){
                                @Override
                                protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException, DataAccessException {
                                    ps.setString(1, finalFilename+".zip");
                                    lobCreator.setBlobAsBinaryStream(ps, 2, is, (int) binaryFile.length());
                                    ps.setString(3,date);
                                }
                            });
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            else{
                sql="UPDATE T_BP_PICKUPLOAD SET F_PICKUPSTATUS=1,F_OPERATETIME=sysdate WHERE TO_CHAR(F_DATATIME,'yyyy/mm/dd')=?";
                jdbcTemplate.update(sql, date);
            }
        }

    }
    private  String getNowDate() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retStrFormatNowDate = sdFormatter.format(nowTime);
        return retStrFormatNowDate;
    }
    private  String setNull(String str){
        if(str == null ) return "";
        else return str;
    }
    public String makeBuildXml(String date,String path) throws IOException, ParseException {
        String sqlDataCenter = "select * FROM T_DC_DATACENTERBASEINFO ";
        String sqlCity = "select * FROM T_DC_CITYTEMPINFO ";
        String sqlBuild = "select * FROM T_BD_BUILDBASEINFO where F_OPERATION != 'P'";
        String sqlBuildGroup = "SELECT * FROM T_BD_BUILDGROUPBASEINFO where F_OPERATION != 'P'";
        final String sqlGroupBuild = "select F_BUILDID FROM T_BD_BUILDGROUPRELAINFO WHERE F_OPERATION != 'P' AND F_BUILDGROUPID = ?";
        String sqlUpdate1 = "UPDATE T_BD_BUILDBASEINFO SET F_OPERATION = 'P' WHERE F_OPERATION != 'P'";
        String sqlUpdate2 = "UPDATE T_BD_BUILDGROUPBASEINFO SET F_OPERATION = 'P' WHERE F_OPERATION != 'P'";
        String sqlUpdate3 = "UPDATE T_BD_BUILDGROUPRELAINFO SET F_OPERATION = 'P' WHERE F_OPERATION != 'P'";
        //XML格式设置部分，固定的
        Document doc = DocumentHelper.createDocument();
        // 根节点root
        Element root = doc.addElement("root");
        // 子元素
        // 开头的一段common
        Element common = root.addElement("common");
        final Element UploadDataCenterID = common.addElement("UploadDataCenterID");
        final Element CreateTime = common.addElement("CreateTime");
        final Element data = root.addElement("data");
        Element DataCenterBaseInfo = data.addElement("DataCenterBaseInfo");
        final Element F_DataCenterID1 	= DataCenterBaseInfo.addElement("F_DataCenterID");
        final Element F_DataCenterName	= DataCenterBaseInfo.addElement("F_DataCenterName");
        final Element F_DataCenterType	= DataCenterBaseInfo.addElement("F_DataCenterType");
        final Element F_DataCenterManager	= DataCenterBaseInfo.addElement("F_DataCenterManager");
        final Element F_DataCenterDesc	= DataCenterBaseInfo.addElement("F_DataCenterDesc");
        final Element F_DataCenterContact	= DataCenterBaseInfo.addElement("F_DataCenterContact");
        final Element F_DataCenterTel		= DataCenterBaseInfo.addElement("F_DataCenterTel");
        jdbcTemplate.query(sqlDataCenter, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                UploadDataCenterID.setText(rs.getString("F_DATACENTERID"));
                CreateTime.setText(getNowDate());
                F_DataCenterID1.setText(rs.getString("F_DATACENTERID"));
                F_DataCenterName.setText(rs.getString("F_DATACENTERNAME"));
                F_DataCenterType.setText(rs.getString("F_DATACENTERTYPE"));
                F_DataCenterManager.setText(rs.getString("F_DATACENTERMANAGER"));
                F_DataCenterDesc.addCDATA(rs.getString("F_DATACENTERDESC"));
                F_DataCenterContact.addCDATA(rs.getString("F_DATACENTERCONTACT"));
                F_DataCenterTel.addCDATA(rs.getString("F_DATACENTERTEL"));
            }
        });
        jdbcTemplate.query(sqlCity, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Element CityTempInfo = data.addElement("CityTempInfo");
                Element F_DataCenterID2 = CityTempInfo.addElement("F_DataCenterID");
                Element F_DistrictCode1 = CityTempInfo.addElement("F_DistrictCode");
                Element F_StartTime 	= CityTempInfo.addElement("F_StartTime");
                Element F_EndTime 		= CityTempInfo.addElement("F_EndTime");
                Element F_TempValue 	= CityTempInfo.addElement("F_TempValue");

                F_DataCenterID2.setText(rs.getString("F_DATACENTERID"));
                F_DistrictCode1.setText(rs.getString("F_DISTRICTCODE"));
                F_StartTime.setText(rs.getString("F_STARTTIME"));
                F_EndTime.setText(rs.getString("F_ENDTIME"));
                F_TempValue.setText(rs.getString("F_TEMPVALUE"));
            }
        });
        jdbcTemplate.query(sqlBuild, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Element Build = data.addElement("Build");
                Build.addAttribute("id", setNull(rs.getString("F_BUILDID")));
                Element BuildBaseInfo = Build.addElement("BuildBaseInfo");
                BuildBaseInfo.addAttribute("operation", rs.getString("F_OPERATION"));
                Element F_DataCenterID3 = BuildBaseInfo.addElement("F_DataCenterID");
                Element F_BuildName 	= BuildBaseInfo.addElement("F_BuildName");
                Element F_AliasName 	= BuildBaseInfo.addElement("F_AliasName");
                Element F_BuildOwner 	= BuildBaseInfo.addElement("F_BuildOwner");
                Element F_State 		= BuildBaseInfo.addElement("F_State");
                Element F_DistrictCode2 = BuildBaseInfo.addElement("F_DistrictCode");
                Element F_BuildAddr 	= BuildBaseInfo.addElement("F_BuildAddr");
                Element F_BuildLong 	= BuildBaseInfo.addElement("F_BuildLong");
                Element F_BuildLat 		= BuildBaseInfo.addElement("F_BuildLat");
                Element F_BuildYear 	= BuildBaseInfo.addElement("F_BuildYear");
                Element F_UpFloor 		= BuildBaseInfo.addElement("F_UpFloor");
                Element F_DownFloor 	= BuildBaseInfo.addElement("F_DownFloor");
                Element F_BuildFunc 	= BuildBaseInfo.addElement("F_BuildFunc");
                Element F_TotalArea 	= BuildBaseInfo.addElement("F_TotalArea");
                Element F_AirArea 		= BuildBaseInfo.addElement("F_AirArea");
                Element F_HeatArea 		= BuildBaseInfo.addElement("F_HeatArea");
                Element F_AirType 		= BuildBaseInfo.addElement("F_AirType");
                Element F_HeatType 		= BuildBaseInfo.addElement("F_HeatType");
                Element F_BodyCoef 		= BuildBaseInfo.addElement("F_BodyCoef");
                Element F_StruType 		= BuildBaseInfo.addElement("F_StruType");
                Element F_WallMatType 	= BuildBaseInfo.addElement("F_WallMatType");
                Element F_WallWarmType 	= BuildBaseInfo.addElement("F_WallWarmType");
                Element F_WallWinType 	= BuildBaseInfo.addElement("F_WallWinType");
                Element F_GlassType 	= BuildBaseInfo.addElement("F_GlassType");
                Element F_WinFrameType 	= BuildBaseInfo.addElement("F_WinFrameType");
                Element F_IsStandard 	= BuildBaseInfo.addElement("F_IsStandard");
                Element F_DesignDept 	= BuildBaseInfo.addElement("F_DesignDept");
                Element F_WorkDept 		= BuildBaseInfo.addElement("F_WorkDept");
                Element F_CreateTime 	= BuildBaseInfo.addElement("F_CreateTime");
                Element F_CreateUser 	= BuildBaseInfo.addElement("F_CreateUser");
                Element F_MonitorDate 	= BuildBaseInfo.addElement("F_MonitorDate");
                Element F_AcceptDate 	= BuildBaseInfo.addElement("F_AcceptDate");

                F_DataCenterID3.setText(setNull(rs.getString("F_DataCenterID")));
                F_BuildName.setText(setNull(rs.getString("F_BuildName")));
                F_AliasName.setText(setNull(rs.getString("F_AliasName")));
                F_BuildOwner.setText(setNull(rs.getString("F_BuildOwner")));
                F_State.setText(setNull(rs.getString("F_State")));
                F_DistrictCode2.setText(setNull(rs.getString("F_DistrictCode")));
                F_BuildAddr.setText(setNull(rs.getString("F_BuildAddr")));
                F_BuildLong.setText(setNull(rs.getString("F_BuildLong")));
                F_BuildLat.setText(setNull(rs.getString("F_BuildLat")));
                F_BuildYear.setText(setNull(rs.getString("F_BuildYear")));
                F_UpFloor.setText(setNull(rs.getString("F_UpFloor")));
                F_DownFloor.setText(setNull(rs.getString("F_DownFloor")));
                F_BuildFunc.setText(setNull(rs.getString("F_BuildFunc")));
                F_TotalArea.setText(setNull(rs.getString("F_TotalArea")));
                F_AirArea.setText(setNull(rs.getString("F_AirArea")));
                F_HeatArea.setText(setNull(rs.getString("F_HeatArea")));
                F_AirType.setText(setNull(rs.getString("F_AirType")));
                F_HeatType.setText(setNull(rs.getString("F_HeatType")));
                F_BodyCoef.setText(setNull(rs.getString("F_BodyCoef")));
                F_StruType.setText(setNull(rs.getString("F_StruType")));
                F_WallMatType.setText(setNull(rs.getString("F_WallMatType")));
                F_WallWarmType.setText(setNull(rs.getString("F_WallWarmType")));
                F_WallWinType.setText(setNull(rs.getString("F_WallWinType")));
                F_GlassType.setText(setNull(rs.getString("F_GlassType")));
                F_WinFrameType.setText(setNull(rs.getString("F_WinFrameType")));
                F_IsStandard.setText(setNull(rs.getString("F_IsStandard")));
                F_DesignDept.setText(setNull(rs.getString("F_DesignDept")));
                F_WorkDept.setText(setNull(rs.getString("F_WorkDept")));
                F_CreateTime.setText(setNull(rs.getString("F_CreateTime")));
                F_CreateUser.setText(setNull(rs.getString("F_CreateUser")));
                F_MonitorDate.setText(setNull(rs.getString("F_MonitorDate")));
                F_AcceptDate.setText(setNull(rs.getString("F_AcceptDate")));
            }
        });
        jdbcTemplate.query(sqlBuildGroup, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Element BuildGroup = data.addElement("BuildGroup");
                BuildGroup.addAttribute("id", setNull(rs.getString("F_BUILDGROUPID")));
                Element BuildGroupBaseInfo = BuildGroup.addElement("BuildGroupBaseInfo");
                BuildGroupBaseInfo.addAttribute("operation", rs.getString("F_OPERATION"));
                Element F_BuildGroupName = BuildGroupBaseInfo.addElement("F_BuildGroupName");
                Element F_GroupAliasName = BuildGroupBaseInfo.addElement("F_GroupAliasName");
                Element F_GroupDesc 	 = BuildGroupBaseInfo.addElement("F_GroupDesc");
                F_BuildGroupName.setText(setNull(rs.getString("F_BuildGroupName")));
                F_GroupAliasName.setText(setNull(rs.getString("F_GroupAliasName")));
                F_GroupDesc.addCDATA(setNull(rs.getString("F_GroupDesc")));

                final Element BuildGroupRelaInfo = BuildGroup.addElement("BuildGroupRelaInfo");
                BuildGroupRelaInfo.addAttribute("operation", rs.getString("F_OPERATION"));
                jdbcTemplate.query(sqlGroupBuild,new Object[]{rs.getString("F_BUILDGROUPID")}, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        Element F_BuildID = BuildGroupRelaInfo.addElement("F_BuildID");
                        F_BuildID.setText(rs.getString("F_BUILDID"));
                    }
                });
            }
        });
        SimpleDateFormat sdf  =   new  SimpleDateFormat( "yyyy/MM/dd" );
        SimpleDateFormat sdf1  =   new  SimpleDateFormat( "yyyyMMdd" );
        String filename=F_DataCenterID1.getText()+sdf1.format(sdf.parse(date));
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 设置输出编码
        format.setEncoding("UTF-8");
        // 创建需要写入的File对象
        File file = new File(path+filename+"Build.xml");
        // 生成XMLWriter对象，构造函数中的参数为需要输出的文件流和格式
        XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
        // 开始写入，write方法中包含上面创建的Document对象
        writer.write(doc);
        //做完了把一切都关闭
        writer.close();
        jdbcTemplate.update(sqlUpdate1);
        jdbcTemplate.update(sqlUpdate2);
        jdbcTemplate.update(sqlUpdate3);
        return F_DataCenterID1.getText();
    }
    public String makeEnergyXml(final String date,String DateCenterID,String path) throws IOException, ParseException {
        final String sqlBuild = "select F_BUILDID FROM T_BD_BUILDBASEINFO ";
        final String sqlDay = "SELECT * FROM T_EC_BUILD_DAY_BUFFER where F_BUILDID = ? and to_char(F_STARTTIME,'yyyy/mm/dd')=?";
        final String sqlHour = "SELECT * FROM T_EC_BUILD_HOUR_BUFFER where F_BUILDID = ? and to_char(F_STARTTIME,'yyyy/mm/dd')=?";
        //XML格式设置部分，固定的
        Document doc = DocumentHelper.createDocument();
        // 根节点root
        Element root = doc.addElement("root");
        // 子元素
        // 开头的一段common
        Element common = root.addElement("common");
        Element UploadDataCenterID = common.addElement("UploadDataCenterID");
        Element CreateTime = common.addElement("CreateTime");
        // 固有信息
        UploadDataCenterID.setText(DateCenterID);
        CreateTime.setText(getNowDate());

        final Element data = root.addElement("data");
        jdbcTemplate.query(sqlBuild, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                final Element Build = data.addElement("Build");
                String buildid=rs.getString("F_BUILDID");
                Build.addAttribute("id", buildid);
                Object[] args={rs.getString("F_BUILDID"),date};
                final double[] tempVMax = {0.0};
                final double[] tempVMin = {1000000.0};
                final double[] tempEMax = {0.0};
                final double[] tempEMin = {1000000.0};
                jdbcTemplate.query(sqlHour,args, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        Element EnergyItemHourResult= Build.addElement("EnergyItemHourResult");
                        Element F_HourResultID 		= EnergyItemHourResult.addElement("F_HourResultID");
                        Element F_EnergyItemCode1 	= EnergyItemHourResult.addElement("F_EnergyItemCode");
                        Element F_StartHour 		= EnergyItemHourResult.addElement("F_StartHour");
                        Element F_EndHour 			= EnergyItemHourResult.addElement("F_EndHour");
                        Element F_HourValue 		= EnergyItemHourResult.addElement("F_HourValue");
                        Element F_HourEquValue 		= EnergyItemHourResult.addElement("F_HourEquValue");
                        Element F_State1 			= EnergyItemHourResult.addElement("F_State");

                        F_HourResultID.setText(rs.getString("F_RESULTID"));
                        F_EnergyItemCode1.setText(rs.getString("F_ENERGYITEMCODE"));
                        F_StartHour.setText(rs.getString("F_STARTTIME"));
                        F_EndHour.setText(rs.getString("F_ENDTIME"));
                        F_HourValue.setText(rs.getString("F_VALUE"));
                        F_HourEquValue.setText(rs.getString("F_EQUVALUE"));
                        F_State1.setText("1");
                        if (tempVMax[0] < rs.getDouble("F_VALUE")) {
                            tempVMax[0] = rs.getDouble("F_VALUE");
                            tempEMax[0] = rs.getDouble("F_EQUVALUE");
                        }
                        if (tempVMin[0] > rs.getDouble("F_VALUE")) {
                            tempVMin[0] = rs.getDouble("F_VALUE");
                            tempEMin[0] = rs.getDouble("F_EQUVALUE");
                        }
                    }
                });
                jdbcTemplate.query(sqlDay,args, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {Element EnergyItemDayResult = Build.addElement("EnergyItemDayResult");
                        Element F_DayResultID 		= EnergyItemDayResult.addElement("F_DayResultID");
                        Element F_EnergyItemCode2	= EnergyItemDayResult.addElement("F_EnergyItemCode");
                        Element F_StartDay 			= EnergyItemDayResult.addElement("F_StartDay");
                        Element F_EndDay 			= EnergyItemDayResult.addElement("F_EndDay");
                        Element F_DayValue 			= EnergyItemDayResult.addElement("F_DayValue");
                        Element F_DayEquValue 		= EnergyItemDayResult.addElement("F_DayEquValue");
                        Element F_HourMaxValue 		= EnergyItemDayResult.addElement("F_HourMaxValue");
                        Element F_HourMaxEquValue 	= EnergyItemDayResult.addElement("F_HourMaxEquValue");
                        Element F_HourMinValue 		= EnergyItemDayResult.addElement("F_HourMinValue");
                        Element F_HourMinEquValue 	= EnergyItemDayResult.addElement("F_HourMinEquValue");
                        Element F_State2 			= EnergyItemDayResult.addElement("F_State");

                        F_DayResultID.setText(rs.getString("F_RESULTID"));
                        F_EnergyItemCode2.setText(rs.getString("F_ENERGYITEMCODE"));
                        F_StartDay.setText(rs.getString("F_STARTTIME"));
                        F_EndDay.setText(rs.getString("F_ENDTIME"));
                        F_DayValue.setText(rs.getString("F_VALUE"));
                        F_DayEquValue.setText(rs.getString("F_EQUVALUE"));
                        F_HourMaxValue.setText(tempVMax[0]+"");
                        F_HourMaxEquValue.setText(tempEMax[0]+"");
                        F_HourMinValue.setText(tempVMin[0]+"");
                        F_HourMinEquValue.setText(tempEMin[0]+"");
                        F_State2.setText("1");
                    }
                });

            }
        });
        SimpleDateFormat sdf  =   new  SimpleDateFormat( "yyyy/MM/dd" );
        SimpleDateFormat sdf1  =   new  SimpleDateFormat( "yyyyMMdd" );
        String filename=DateCenterID+sdf1.format(sdf.parse(date));
        // 实例化输出格式对象
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 设置输出编码
        format.setEncoding("UTF-8");
        // 创建需要写入的File对象
        File file = new File(path+filename+"Energy.xml");
        // 生成XMLWriter对象，构造函数中的参数为需要输出的文件流和格式
        XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
        // 开始写入，write方法中包含上面创建的Document对象
        writer.write(doc);
        writer.close();
        return filename;
    }
    private void zipFile(String path,String filename) throws IOException {
        byte[] buffer = new byte[1024];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(path+filename+".zip"));
        // 压缩两个文件BuildXML.xml，EnergyXML.xml
        File[] file = { new File(path+filename+"Build.xml"), new File(path+filename+"Energy.xml")};
        for (int i = 0; i < file.length; i++) {
            FileInputStream fis = new FileInputStream(file[i]);
            out.putNextEntry(new ZipEntry(file[i].getName()));
            int len;
            // 读入需要下载的文件的内容，打包到zip文件
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.closeEntry();
            fis.close();
        }
        out.close();
        System.out.println("生成成功"+"=====>>"+filename+".zip");
    }

    public void deletePickuploads(String date){
        String sql="delete T_BP_PICKUPLOAD where to_char(F_DATATIME,'yyyy/mm/dd')=?";
        jdbcTemplate.update(sql, date);
    }


    public String getFile(final String path,String datatime) throws IOException {
        final LobHandler lobHandler = new DefaultLobHandler();
        Object[] args = {datatime};
        final List<String> filename = new ArrayList<String>();
        jdbcTemplate.query("select f_filename,f_file from t_bp_pickupload where to_char(f_datatime,'yyyymmdd')=?", args, new AbstractLobStreamingResultSetExtractor() {
            protected void streamData(ResultSet rs) throws SQLException, IOException, DataAccessException {
                filename.add(0, rs.getString(1));
                final OutputStream os = new FileOutputStream(path + filename.get(0));
                FileCopyUtils.copy(lobHandler.getBlobAsBinaryStream(rs, 2), os);
                os.close();
            }
        });
        return filename.get(0);
    }
}
