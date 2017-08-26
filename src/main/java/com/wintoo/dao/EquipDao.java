package com.wintoo.dao;

import com.wintoo.model.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Repository
@Transactional
public class EquipDao {
	@Autowired
	@Qualifier("primaryJdbcTemplate")
	private JdbcOperations jdbcTemplate;
	@Autowired
	@Qualifier("secondaryJdbcTemplate")
	private JdbcOperations jdbcTemplate1;
	public List<String> getAllEquipType(){
		String sql="select DISTINCT F_TYPE from T_BE_EQUIPMENTTYPE";
		final List<String> types=new ArrayList<String>();
		jdbcTemplate.query(sql, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				types.add(rs.getString("F_TYPE"));
			}
		});
		return types;
	}

	public List<Options> getEquipSubtype(String type){
		String sql="select DISTINCT F_UUID,F_SUBTYPE from T_BE_EQUIPMENTTYPE WHERE F_TYPE=?";
		String[] args={type};
		final List<Options> Options=new ArrayList<Options>();
		jdbcTemplate.query(sql, args, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				Options option=new Options();
				option.setId(rs.getString("F_UUID"));
				option.setName(rs.getString("F_SUBTYPE"));
				Options.add(option);
			}
		});
		return Options;
	}
	
	public Options getTypeParameter(String typeid){
		String sql="select F_UUID,F_PARAMETER from T_BE_EQUIPMENTTYPE WHERE F_UUID=?";
		String[] args={typeid};
		final Options options=new Options();
		jdbcTemplate.query(sql, args, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				options.setId(rs.getString("F_UUID"));
				options.setName(rs.getString("F_PARAMETER"));
			}
		});
		return options;
	}
	
	public DataTable getAllEquipBatchs(){
		String sql="select a.*,b.F_TYPE,b.F_SUBTYPE,b.F_UUID AS TYPEID from T_BE_EQUIPMENTBATCH a,T_BE_EQUIPMENTTYPE b where a.F_TYPEID=b.F_UUID";
		DataTable dataTable=new DataTable();
		final List<EquipBatch> builds=new ArrayList<EquipBatch>();
		jdbcTemplate.query(sql, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				EquipBatch equipBatch=new EquipBatch();
				equipBatch.setUuid(rs.getString("F_UUID"));
				equipBatch.setBatch(rs.getString("F_BATCH"));
				equipBatch.setType(rs.getString("F_TYPE"));
				equipBatch.setSubtype(rs.getString("F_SUBTYPE"));
				equipBatch.setTypeid(rs.getString("TYPEID"));
				equipBatch.setModel(rs.getString("F_MODEL"));
				equipBatch.setAmount(rs.getInt("F_AMOUNT"));
				equipBatch.setStock(rs.getInt("F_STOCK"));
				equipBatch.setSupplier(rs.getString("F_SUPPLIER"));
				equipBatch.setProduct(rs.getString("F_PRODUCT"));
				equipBatch.setPrice(rs.getDouble("F_PRICE"));
				equipBatch.setBuydate(rs.getDate("F_BUYDATE"));
				equipBatch.setWarranty(rs.getInt("F_WARRANTY"));
				equipBatch.setContact(rs.getString("F_CONTACT"));
				equipBatch.setPhone(rs.getString("F_PHONE"));
				builds.add(equipBatch);
			}
		});
		dataTable.setData(builds);
		return dataTable;
	}
	
	public void addEquipBatch(EquipBatch equipBatch){
		String sql="insert into T_BE_EQUIPMENTBATCH(F_UUID,F_BATCH,F_MODEL,F_TYPEID,F_AMOUNT,F_STOCK,F_SUPPLIER,F_PRODUCT,F_PRICE,F_BUYDATE,F_WARRANTY,F_CONTACT,F_PHONE,F_PARAMETERS) values(sys_guid(),?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] args={equipBatch.getBatch(),equipBatch.getModel(),equipBatch.getTypeid(),equipBatch.getAmount(),equipBatch.getAmount(),equipBatch.getSupplier(),equipBatch.getProduct(),equipBatch.getPrice(),equipBatch.getBuydate(),equipBatch.getWarranty(),equipBatch.getContact(),equipBatch.getPhone(),equipBatch.getParameters()};
		jdbcTemplate.update(sql, args);
		String sql1="select F_UUID FROM T_BE_EQUIPMENTBATCH WHERE F_BATCH=? AND F_MODEL=? AND F_TYPEID=?";
		Object[] args1={equipBatch.getBatch(),equipBatch.getModel(),equipBatch.getTypeid()};
		final String uuid=jdbcTemplate.queryForObject(sql1, args1, String.class);
		final String typeid=equipBatch.getTypeid();
		final String batch=equipBatch.getBatch();
		final int on_off=equipBatch.getOn_off();
		final int size=equipBatch.getAmount();
		final List<UUID> uuids=new ArrayList<UUID>();
		for(int i=0;i<size;i++)
			uuids.add( UUID.randomUUID());
		String sql2="insert into T_BE_EQUIPMENTLIST(F_UUID,F_EQUIPID,F_BATCHID,F_TYPEID) values(?,?,?,?)";
		jdbcTemplate.batchUpdate(sql2, new BatchPreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps,int i)throws SQLException{
				ps.setString(1, uuids.get(i).toString());
			    ps.setString(2, (batch.length()<=4 ? batch : batch.substring((batch.length()-4),batch.length()))+typeid+String.format("%04d", i+1)); 
			    ps.setString(3, uuid); 
			    ps.setString(4, typeid);
			} 
			@Override
			public int getBatchSize(){ 
			    return size; 
			} 
		}); 
		
		
		if(equipBatch.getTypeid().equals("0030")){// --00代表单相电表，01三相电表，10水表，20控制节点,30智能网关
			String sql3="insert into T_BE_GATEWAY(F_UUID,F_BATCHID,F_USE) values(?,?,0)";
			jdbcTemplate.batchUpdate(sql3, new BatchPreparedStatementSetter(){
				@Override
				public void setValues(PreparedStatement ps,int i)throws SQLException{ 
				    ps.setString(1, uuids.get(i).toString()); 
				    ps.setString(2, uuid); 
				} 
				@Override
				public int getBatchSize(){ 
				    return size; 
				} 
			}); 
		}
		else if(equipBatch.getTypeid().equals("0000") || equipBatch.getTypeid().equals("0001")){
			String sql3="insert into T_BE_AMMETER(F_UUID,F_BATCHID,F_ON_OFF,F_USE) values(?,?,?,0)";
			jdbcTemplate.batchUpdate(sql3, new BatchPreparedStatementSetter(){
				@Override
				public void setValues(PreparedStatement ps,int i)throws SQLException{ 
				    ps.setString(1, uuids.get(i).toString()); 
				    ps.setString(2, uuid); 
				    ps.setInt(3, on_off); 
				} 
				@Override
				public int getBatchSize(){ 
				    return size; 
				} 
			}); 
		}
		else if(equipBatch.getTypeid().equals("0010")){
			String sql3="insert into T_BE_WATERMETER(F_UUID,F_BATCHID,F_USE) values(?,?,0)";
			jdbcTemplate.batchUpdate(sql3, new BatchPreparedStatementSetter(){
				@Override
				public void setValues(PreparedStatement ps,int i)throws SQLException{ 
				    ps.setString(1, uuids.get(i).toString()); 
				    ps.setString(2, uuid); 
				} 
				@Override
				public int getBatchSize(){ 
				    return size; 
				} 
			}); 
		}
		else
			return;
	}
	
	public void deleteEquipBatch(String id){
		String sql="delete from T_BE_EQUIPMENTBATCH where F_UUID=?";
		String sql1="delete from T_BE_EQUIPMENTLIST where F_BATCHID=?";
		String sql2="delete from T_BE_GATEWAY where F_BATCHID=?";
		String sql3="delete from T_BE_AMMETER where F_BATCHID=?";
		String sql4="delete from T_BE_WATERMETER where F_BATCHID=?";
		Object[] args={id};
		jdbcTemplate.update(sql,args);
		jdbcTemplate.update(sql1,args);
		jdbcTemplate.update(sql2,args);
		jdbcTemplate.update(sql3,args);
		jdbcTemplate.update(sql4,args);
	}
	
	public EquipBatch getEquipBatchById(String id){
		String sql="select a.*,b.F_TYPE,b.F_SUBTYPE from T_BE_EQUIPMENTBATCH a,T_BE_EQUIPMENTTYPE b where a.F_TYPEID=b.F_UUID AND a.F_UUID=?";		
		Object[] args={id};
		final EquipBatch equipBatch=new EquipBatch();
		jdbcTemplate.query(sql, args ,new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				equipBatch.setUuid(rs.getString("F_UUID"));
				equipBatch.setBatch(rs.getString("F_BATCH"));
				equipBatch.setType(rs.getString("F_TYPE"));
				equipBatch.setSubtype(rs.getString("F_TYPEID"));
				equipBatch.setModel(rs.getString("F_MODEL"));
				equipBatch.setAmount(rs.getInt("F_AMOUNT"));
				equipBatch.setStock(rs.getInt("F_STOCK"));
				equipBatch.setSupplier(rs.getString("F_SUPPLIER"));
				equipBatch.setProduct(rs.getString("F_PRODUCT"));
				equipBatch.setPrice(rs.getDouble("F_PRICE"));
				equipBatch.setBuydate(rs.getDate("F_BUYDATE"));
				equipBatch.setWarranty(rs.getInt("F_WARRANTY"));
				equipBatch.setContact(rs.getString("F_CONTACT"));
				equipBatch.setPhone(rs.getString("F_PHONE"));
				equipBatch.setParameters(rs.getString("F_PARAMETERS"));
			}
		});
		
		return equipBatch;
	}

	public void updateEquipBatchById(EquipBatch equipBatch){
		String sql="update T_BE_EQUIPMENTBATCH set F_BATCH=?,F_MODEL=?,F_SUPPLIER=?,F_PRODUCT=?,F_PRICE=?,F_BUYDATE=?,F_WARRANTY=?,F_CONTACT=?,F_PHONE=?,F_PARAMETERS=? where F_UUID=?";
		Object[] args={equipBatch.getBatch(),equipBatch.getModel(),equipBatch.getSupplier(),equipBatch.getProduct(),equipBatch.getPrice(),equipBatch.getBuydate(),equipBatch.getWarranty(),equipBatch.getContact(),equipBatch.getPhone(),equipBatch.getParameters(),equipBatch.getUuid()};
		jdbcTemplate.update(sql,args);
		
		if(equipBatch.getTypeid().equals("0000") || equipBatch.getTypeid().equals("0001")){
			String sql1="update T_BE_AMMETER set F_ON_OFF=? where F_BATCHID=?";
			Object[] args1={equipBatch.getOn_off(),equipBatch.getUuid()};
			jdbcTemplate.update(sql1,args1);
		}
		
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public DataTable getAllEquipLists(){
		String sql="select NVL(NVL(gateway1,gateway2),'未配置') as gateway, a.F_UUID,a.F_EQUIPID,a.F_BATCHID,a.F_TYPEID,f.F_BATCH,b.F_TYPE,b.F_SUBTYPE,f.F_MODEL,a.F_INSTALLTYPE,t.F_BUILDGROUPNAME,c.F_BUILDNAME,d.F_NAME AS D_F_NAME,e.F_NAME AS E_F_NAME,a.F_LONGITUDE,a.F_LATITUDE,a.F_REMARK "+
					"from T_BD_GROUPBUILDRELA h,T_BD_GROUP t,T_BE_EQUIPMENTLIST a,T_BE_EQUIPMENTTYPE b,T_BD_BUILD c,T_BD_FLOOR d,T_BD_ROOM e ,T_BE_EQUIPMENTBATCH f,"+
					"(select t2.f_address||'-'||t1.f_use||'-'||lpad(t1.f_pn,3,'0') as gateway1,t3.f_uuid as uuid1 from t_be_ammeter t1,t_be_gateway t2,T_BE_EQUIPMENTLIST t3 where t3.f_uuid=t1.f_uuid and t1.f_gateways_uuid=t2.f_uuid),"+
					"(select t2.f_address||'-'||t1.f_use||'-'||lpad(t1.f_pn,3,'0') as gateway2,t3.f_uuid as uuid2 from t_be_watermeter t1,t_be_gateway t2,T_BE_EQUIPMENTLIST t3 where t3.f_uuid=t1.f_uuid and t1.f_gateways_uuid=t2.f_uuid) "+
					"where a.f_uuid=uuid1(+) and a.f_uuid=uuid2(+) and a.F_BATCHID=f.F_UUID AND a.F_TYPEID=b.F_UUID AND a.F_BUILDID=c.F_BUILDID(+) AND a.F_FLOORID=d.F_ID(+) AND a.F_ROOMID=e.F_ID(+) AND h.F_BUILDID(+)=a.F_BUILDID AND t.F_BUILDGROUPID(+)=h.F_BUILDGROUPID ";
		DataTable dataTable=new DataTable();
		final List<EquipList> equips=new ArrayList<EquipList>();
		jdbcTemplate.query(sql, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				EquipList equipList=new EquipList();
				equipList.setUuid(rs.getString("F_UUID"));
				equipList.setEquipid(rs.getString("F_EQUIPID"));
				equipList.setBatch(rs.getString("F_BATCH"));
				equipList.setBatchid(rs.getString("F_BATCHID"));
				equipList.setTypeid(rs.getString("F_TYPEID"));
				equipList.setType(rs.getString("F_TYPE"));
				equipList.setSubtype(rs.getString("F_SUBTYPE"));
				equipList.setModel(rs.getString("F_MODEL"));
				equipList.setInstalltype(rs.getString("F_INSTALLTYPE"));
				equipList.setGroupid(rs.getString("F_BUILDGROUPNAME"));
				equipList.setBuildid(rs.getString("F_BUILDNAME"));
				equipList.setFloorid(rs.getString("D_F_NAME"));
				equipList.setRoomid(rs.getString("E_F_NAME"));
				equipList.setLongitude(rs.getDouble("F_LONGITUDE"));
				equipList.setLatitude(rs.getDouble("F_LATITUDE"));
				equipList.setRemark(rs.getString("F_REMARK"));
				equipList.setParas(rs.getString("gateway"));
				equips.add(equipList);
			}
		});
		dataTable.setData(equips);
		return dataTable;
		
	}

	public void deleteEquipInstall(String id){

		String sql="update T_BE_EQUIPMENTLIST set F_INSTALLTYPE='',F_BUILDID='',F_FLOORID='',F_ROOMID='',F_LONGITUDE='',F_LATITUDE='',F_REMARK='',F_MAINTENANCE=0 where F_UUID=?";
		String[] ids=id.split(",");
		final List<String> validids=new ArrayList<String>();
		for (int i = 0; i <ids.length ; i++) {
			if(ids!=null)
				validids.add(ids[i]);
		}
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public int getBatchSize() {
				return validids.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, validids.get(i));
			}
		});
		String sql1="update T_BE_EQUIPMENTBATCH set F_STOCK=F_STOCK+1 where F_UUID=(SELECT F_BATCHID from T_BE_EQUIPMENTLIST where f_uuid=?)";
		jdbcTemplate.batchUpdate(sql1, new BatchPreparedStatementSetter() {
			@Override
			public int getBatchSize() {
				return validids.size();
			}
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, validids.get(i));
			}
		});
	}

	
	public void deleteEquip(EquipList equipList) {
		String typeid=equipList.getTypeid();
		//水电表需要将
		String table_name="";
		if((typeid.equals("0010")))
			table_name="T_BE_WATERMETER";
		else
			table_name="T_BE_AMMETER";
		if(typeid.equals("0000")||typeid.equals("0001")||typeid.equals("0010")){
			String sql="update "+table_name+" set F_USE=?  WHERE F_UUID=?";
			Object[] args={"0",equipList.getUuid()};
			String sql1="insert into t_tasks(F_UUID,F_TYPE,F_FLAG) values(?,?,?)";
			String uuid = UUID.randomUUID().toString();
			Object[] operate_uuid={uuid,"3","1"};
			try {
				jdbcTemplate.update(sql, args);
				jdbcTemplate1.update(sql1,operate_uuid);
				Thread.sleep(2000);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//更改批次信息
		String installtype=equipList.getInstalltype();
		String changebatch="";
		if((installtype.equals("0"))||installtype.equals("1")){
			changebatch="update T_BE_EQUIPMENTBATCH set F_AMOUNT=F_AMOUNT-1 where F_UUID=?";
		}
		else {
			changebatch="update T_BE_EQUIPMENTBATCH set F_AMOUNT=F_AMOUNT-1, F_STOCK=F_STOCK-1 where F_UUID=?";
		}
		Object[] batchuuid={equipList.getBatchid()};
		jdbcTemplate.update(changebatch, batchuuid);
		
		//删除设备列表信息
		Object[] uuid={equipList.getUuid()};
		String   delete_equiplist="delete from T_BE_EQUIPMENTLIST where F_UUID=?";
		jdbcTemplate.update(delete_equiplist, uuid);
		
		//删除运维列表信息
		String   delete_cmd;
		if(typeid.equals("0000")||typeid.equals("0001")){
			delete_cmd="delete from T_BE_AMMETER where F_UUID=?";
			jdbcTemplate.update(delete_cmd, uuid);
		}
		else if(typeid.equals("0010")){
			delete_cmd="delete from T_BE_WATERMETER where F_UUID=?";
			jdbcTemplate.update(delete_cmd, uuid);
		}
		else if(typeid.equals("0030")){
			delete_cmd="delete from T_BE_GATEWAY where F_UUID=?";
			jdbcTemplate.update(delete_cmd, uuid);
		}

	}
	

	public List<EquipList> getEquipListByTypeid(String typeid){
		String sql="select a.F_UUID,a.F_EQUIPID,b.F_BATCH from T_BE_EQUIPMENTLIST a,T_BE_EQUIPMENTBATCH b where a.F_TYPEID=? AND a.F_INSTALLTYPE is null AND a.F_BATCHID=b.F_UUID";
		Object[] args={typeid};
		final List<EquipList> equipLists=new ArrayList<EquipList>();
		jdbcTemplate.query(sql, args ,new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				EquipList equipList =new EquipList();
				equipList.setUuid(rs.getString("F_UUID"));
				equipList.setEquipid(rs.getString("F_EQUIPID"));
				equipList.setBatch(rs.getString("F_BATCH"));
				equipLists.add(equipList);
			}
		});
		return equipLists;
	}
	public List<EquipList> getEquipListByBatchid(String batchid){
		String sql="select NVL(NVL(gateway1,gateway2),'未配置') as gateway, NVL(NVL(remarkinfo1,remarkinfo2),'-') as remarkinfo , a.F_UUID,a.F_EQUIPID,a.F_BATCHID,a.F_TYPEID,a.F_CONNECTNUM,f.F_BATCH,b.F_TYPE,b.F_SUBTYPE,f.F_MODEL,a.F_INSTALLTYPE,t.F_BUILDGROUPNAME,c.F_BUILDNAME,d.F_NAME AS D_F_NAME,e.F_NAME AS E_F_NAME,a.F_LONGITUDE,a.F_LATITUDE,a.F_REMARK "+
				"from T_BD_GROUPBUILDRELA h,T_BD_GROUP t,T_BE_EQUIPMENTLIST a,T_BE_EQUIPMENTTYPE b,T_BD_BUILD c,T_BD_FLOOR d,T_BD_ROOM e ,T_BE_EQUIPMENTBATCH f,"+
				"(select t2.f_address||'-'||t1.f_use||'-'||lpad(t1.f_pn,3,'0') as gateway1,t3.f_uuid as uuid1,t1.F_REMARKINFO remarkinfo1 from t_be_ammeter t1,t_be_gateway t2,T_BE_EQUIPMENTLIST t3 where t3.f_uuid=t1.f_uuid and t1.f_gateways_uuid=t2.f_uuid),"+
				"(select t2.f_address||'-'||t1.f_use||'-'||lpad(t1.f_pn,3,'0') as gateway2,t3.f_uuid as uuid2,t1.F_REMARKINFO remarkinfo2 from t_be_watermeter t1,t_be_gateway t2,T_BE_EQUIPMENTLIST t3 where t3.f_uuid=t1.f_uuid and t1.f_gateways_uuid=t2.f_uuid) "+
				"where a.f_uuid=uuid1(+) and a.f_uuid=uuid2(+) and a.F_BATCHID=f.F_UUID AND a.F_TYPEID=b.F_UUID AND a.F_BUILDID=c.F_BUILDID(+) AND a.F_FLOORID=d.F_ID(+) AND a.F_ROOMID=e.F_ID(+) AND h.F_BUILDID(+)=a.F_BUILDID AND t.F_BUILDGROUPID(+)=h.F_BUILDGROUPID AND a.F_BATCHID=?";
		Object[] args={batchid};
		final List<EquipList> equips=new ArrayList<EquipList>();
		jdbcTemplate.query(sql, args,new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				EquipList equipList=new EquipList();
				equipList.setUuid(rs.getString("F_UUID"));
				equipList.setEquipid(rs.getString("F_EQUIPID"));
				equipList.setBatch(rs.getString("F_BATCH"));
				equipList.setBatchid(rs.getString("F_BATCHID"));
				equipList.setTypeid(rs.getString("F_TYPEID"));
				equipList.setType(rs.getString("F_TYPE"));
				equipList.setSubtype(rs.getString("F_SUBTYPE"));
				equipList.setModel(rs.getString("F_MODEL"));
				equipList.setInstalltype(rs.getString("F_INSTALLTYPE"));
				equipList.setGroupid(rs.getString("F_BUILDGROUPNAME"));
				equipList.setBuildid(rs.getString("F_BUILDNAME"));
				equipList.setFloorid(rs.getString("D_F_NAME"));
				equipList.setRoomid(rs.getString("E_F_NAME"));
				equipList.setLongitude(rs.getDouble("F_LONGITUDE"));
				equipList.setLatitude(rs.getDouble("F_LATITUDE"));
				equipList.setRemark(rs.getString("F_REMARK"));
				equipList.setParas(rs.getString("gateway"));
				equipList.setRemarkinfo(rs.getString("remarkinfo"));
                equipList.setConnectnum(rs.getInt("F_CONNECTNUM"));
				equips.add(equipList);
			}
		});
		return equips;
	}
	public void installEquip(final EquipList equipList){
		if(equipList.getInstalltype().equals("1")){
			String sql="update T_BE_EQUIPMENTLIST set F_INSTALLTYPE=?,F_BUILDID=?,F_FLOORID=?,F_ROOMID=?,F_REMARK=? where F_UUID=?";
			final String[] uuids=equipList.getUuid().split(",");
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
	            @Override
				public int getBatchSize() {
	                return uuids.length;
	            }
	            @Override
				public void setValues(PreparedStatement ps, int i)throws SQLException {
	                ps.setString(1, equipList.getInstalltype());
	                ps.setString(2, equipList.getBuildid());
	                ps.setString(3, equipList.getFloorid());
	                ps.setString(4, equipList.getRoomid());
	                ps.setString(5, equipList.getRemark());
	                ps.setString(6, uuids[i]);
	            }
	        });
		}
		else{
			String sql="update T_BE_EQUIPMENTLIST set F_INSTALLTYPE=?,F_BUILDID=?,F_FLOORID=?,F_ROOMID=?,F_LONGITUDE=?,F_LATITUDE=?,F_REMARK=? where F_UUID=?";
			final String[] uuids=equipList.getUuid().split(",");
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
	            @Override
				public int getBatchSize() {
	                return uuids.length;
	            }
	            @Override
				public void setValues(PreparedStatement ps, int i)throws SQLException {
	            	
	                ps.setString(1, equipList.getInstalltype());
	                ps.setString(2, equipList.getBuildid());
	                ps.setString(3, equipList.getFloorid());
	                ps.setString(4, equipList.getRoomid());
	                ps.setDouble(5, equipList.getLongitude());
	                ps.setDouble(6, equipList.getLatitude());
	                ps.setString(7, equipList.getRemark());
	                ps.setString(8, uuids[i]);
	            }
	        });		
		}
		String sql="update T_BE_EQUIPMENTBATCH set F_STOCK=F_STOCK-1 where F_UUID=?";
		final String[] uuids=equipList.getUuid().split("#");		
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
			public int getBatchSize() {
                return uuids.length;
            }
            @Override
			public void setValues(PreparedStatement ps, int i)throws SQLException {
                ps.setString(1, uuids[i]);
            }
        });	

	}
/////////////////////////////////////////////////////////////////////////


	public List<Measure> getAllMeasures(String id){
		String sql="select a.*,b.F_ENERGYITEMCODE,b.F_ENERGYITEMNAME,NVL(c.F_BUILDGROUPNAME,'') as GROUPNAME,NVL(d.F_BUILDNAME,'') as BUILDNAME,NVL(e.F_NAME,'') as FLOORNAME,NVL(f.F_NAME,'') AS ROOMNAME "+
				"from T_RR_DEVICERELATION a,T_DT_ENERGYITEMDICT b,T_BD_GROUP c,T_BD_BUILD d,T_BD_FLOOR e, T_BD_ROOM f  "+
				"WHERE  a.F_ENERGYITEMCODE=b.F_ENERGYITEMCODE AND a.F_DEVICECODE=? AND a.F_BUILDGROUPID=c.f_buildgroupid(+) and a.F_BUILDCODE=d.F_BUILDID(+) and a.F_FLOORID=e.F_ID(+) and a.f_roomid=f.F_ID(+)";
		Object[] args={id};
		final List<Measure> measures=new ArrayList<Measure>();
		jdbcTemplate.query(sql,args, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				final Measure measure=new Measure();
                measure.setDevicecode(rs.getString("F_DEVICECODE"));
				measure.setUuid(rs.getString("F_UUID"));
                measure.setLevel(rs.getInt("F_LEVEL"));
                measure.setEnergyitem(rs.getString("F_ENERGYITEMNAME"));
				measure.setEnergyitemcode(rs.getString("F_ENERGYITEMCODE"));
				measure.setPercent(rs.getInt("F_PERCENT"));
				measure.setPlusminus(rs.getInt("F_PLUSMINUS"));
                measure.setGroupid(rs.getString("F_BUILDGROUPID"));
                measure.setBuildid(rs.getString("F_BUILDCODE"));
                measure.setFloorid(rs.getString("F_FLOORID"));
                measure.setRoomid(rs.getString("F_ROOMID"));
				measure.setGroup(rs.getString("GROUPNAME"));
				measure.setBuild(rs.getString("BUILDNAME"));
				measure.setFloor(rs.getString("FLOORNAME"));
				measure.setRoom(rs.getString("ROOMNAME"));
				measure.setRemark(rs.getString("F_REMARK"));
                measure.setSuperior_meter_level(rs.getInt("F_SUPERIOR_METER_LEVEL"));
				measures.add(measure);
			}
		});
		return measures;
	}


	public List<Superiormeter> getSuperior_meter(String level){
		String sql="select t.F_BUILDGROUPID,t.F_BUILDCODE,t.F_FLOORID,t.f_roomid,t.F_ENERGYITEMCODE,t.F_DEVICECODE as EQUIPUUID,e.F_EQUIPID AS EQUIPID,a.F_BUILDGROUPNAME AS BUIDGROUP,b.F_BUILDNAME AS BUID,c.F_NAME AS FLOOR ,d.F_NAME AS ROOM ,f.F_ENERGYITEMNAME AS ENERGYITEMNAME "+
				"FROM T_RR_DEVICERELATION t,T_BD_GROUP a,T_BD_BUILD b,T_BD_FLOOR c, T_BD_ROOM d, T_BE_EQUIPMENTLIST e, T_DT_ENERGYITEMDICT f "+
				"WHERE t.F_LEVEL=? and t.f_devicecode=e.F_UUID and t.F_BUILDGROUPID=a.f_buildgroupid(+) and t.F_BUILDCODE=b.F_BUILDID(+) and t.F_FLOORID=c.F_ID(+) and t.f_roomid=d.F_ID(+) and t.F_ENERGYITEMCODE=f.F_ENERGYITEMCODE "+
				"order by t.F_BUILDGROUPID,t.F_BUILDCODE,t.F_FLOORID,t.f_roomid,t.F_ENERGYITEMCODE,t.F_DEVICECODE";
		Object[] args={level};
		final List<Superiormeter> Superiormeter=new ArrayList<Superiormeter>();
		jdbcTemplate.query(sql, args, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs)throws SQLException{
				Superiormeter superiormeter=new Superiormeter();
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
		if((measure.getSuperior_meter() != null) & (measure.getSuperior_meter().equals("#")==false)){
			String flag[]=measure.getSuperior_meter().split(",");
			if((superior_meter_level==3)&(flag[0].equals(measure.getGroup())==false)){
				direct_superior_meter=0;
			}
			else if((superior_meter_level==2)&(flag[0].equals(measure.getBuild())==false)){
				direct_superior_meter=0;
			}
			else if((superior_meter_level==1)&(flag[0].equals(measure.getFloor())==false)){
				direct_superior_meter=0;
			}
			else if((superior_meter_level==0)&(flag[0].equals(measure.getRoom())==false)){
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

        String buildgroup=measure.getGroup().equals("#")?"":measure.getGroup();
        String build=measure.getBuild().equals("#")?"":measure.getBuild();
        String floor=measure.getFloor().equals("#")?"":measure.getFloor();
        String room=measure.getRoom().equals("#")?"":measure.getRoom();

		String sql="insert into T_RR_DEVICERELATION(F_UUID,F_DEVICECODE,F_ENERGYITEMCODE,F_BUILDGROUPID,F_BUILDCODE,F_FLOORID,F_ROOMID,F_SUPERIOR_METER_LEVEL,F_SUPERIOR_METER,F_DIRECT_SUPERIOR_METER,F_PERCENT,F_PLUSMINUS,F_LEVEL,F_REMARK) VALUES(sys_guid(),?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] args={measure.getDevicecode(),measure.getEnergyitemcode(),buildgroup,build,floor,room,measure.getSuperior_meter_level(),superior_meter,direct_superior_meter,measure.getPercent(),measure.getPlusminus(),measure.getLevel(),measure.getRemark()};
		jdbcTemplate.update(sql, args);
        String sql1="update T_BE_EQUIPMENTLIST set F_CONNECTNUM=F_CONNECTNUM+1 WHERE F_UUID=?";
        Object[] args1={measure.getDevicecode()};
        jdbcTemplate.update(sql1,args1);
	}
    public void updateMeasure(Measure measure){
        int	   superior_meter_level=measure.getSuperior_meter_level();
        String superior_meter="";
        int    direct_superior_meter=1;//设置关联时的上级表时，本变量用于标记上级表是否为本建筑体系内的表，如果不是则需要在这个上级表中做标记,1为直接上级表，0为非直接上级表
        if((measure.getSuperior_meter() != null) & (measure.getSuperior_meter().equals("#")==false)){
            String flag[]=measure.getSuperior_meter().split(",");
            if((superior_meter_level==3)&(flag[0].equals(measure.getGroup())==false)){
                direct_superior_meter=0;
            }
            else if((superior_meter_level==2)&(flag[0].equals(measure.getBuild())==false)){
                direct_superior_meter=0;
            }
            else if((superior_meter_level==1)&(flag[0].equals(measure.getFloor())==false)){
                direct_superior_meter=0;
            }
            else if((superior_meter_level==0)&(flag[0].equals(measure.getRoom())==false)){
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

        String buildgroup=measure.getGroup().equals("#")?"":measure.getGroup();
        String build=measure.getBuild().equals("#")?"":measure.getBuild();
        String floor=measure.getFloor().equals("#")?"":measure.getFloor();
        String room=measure.getRoom().equals("#")?"":measure.getRoom();

        String sql="UPDATE T_RR_DEVICERELATION SET F_DEVICECODE=?,F_ENERGYITEMCODE=?,F_BUILDGROUPID=?,F_BUILDCODE=?,F_FLOORID=?,F_ROOMID=?,F_SUPERIOR_METER_LEVEL=?,F_SUPERIOR_METER=?,F_DIRECT_SUPERIOR_METER=?,F_PERCENT=?,F_PLUSMINUS=?,F_LEVEL=?,F_REMARK=? WHERE F_UUID=?";
        Object[] args={measure.getDevicecode(),measure.getEnergyitemcode(),buildgroup,build,floor,room,measure.getSuperior_meter_level(),superior_meter,direct_superior_meter,measure.getPercent(),measure.getPlusminus(),measure.getLevel(),measure.getRemark(),measure.getUuid()};
        jdbcTemplate.update(sql, args);

    }
	public void deleteMeasure(String id){
        String sql1="update T_BE_EQUIPMENTLIST b set F_CONNECTNUM=F_CONNECTNUM-1 WHERE b.F_UUID in (select F_DEVICECODE FROM T_RR_DEVICERELATION  a WHERE a.F_UUID=?)";
        Object[] args1={id};
        jdbcTemplate.update(sql1,args1);
        String sql="delete from T_RR_DEVICERELATION where F_UUID=?";
        Object[] args={id};
        jdbcTemplate.update(sql,args);
	}
	
}
