package com.ruishengtech.rscc.crm.datamanager.manager.transfer;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ruishengtech.rscc.crm.datamanager.manager.TransferResult;
import com.ruishengtech.rscc.crm.datamanager.manager.node.BatchNode;
import com.ruishengtech.rscc.crm.datamanager.manager.node.IntentNode;
import com.ruishengtech.rscc.crm.datamanager.manager.node.ShareNode;
import com.ruishengtech.rscc.crm.datamanager.manager.node.UserNode;
import com.ruishengtech.rscc.crm.datamanager.manager.transferdata.IntentToShareData;

/**
 * Created by yaoliceng on 2015/8/18.
 */
@Component
@Transactional
//这里IntentNode中存的表名就是部门名，而不是意向名
public class IntentToShareTransfer extends AbstractTransfer<IntentNode, ShareNode, IntentToShareData> {

	@Override
	public TransferResult transfer(IntentNode fromNode, ShareNode toNode, IntentToShareData transferData) {
		
		//从非意向客户直接搬到共享池
		
    	int count = cut(fromNode.getTableName(), transferData.getUserData().getOwnUser(), 
    			transferData.getUserData().getPhoneNumber(), transferData.getUserData().getBatchUuid(), transferData.getUserData().getOwnDepartment());
    	
    	TransferResult tr = new TransferResult();
    	tr.setTransferCount(count);
    	
    	return tr;
	}
	   
	//移动时清除掉意向
	//传入上次的来源，如果是共享池，则加标记
    private int cut(String sourceTable, String userUuid, String phoneNumber, String batchUuid, String lastSource) {
		//二级回收回部门，插入数据
//		String insertSql = "insert into " + ShareNode.tableName
//				+ "(uuid,batch_uuid,phone_number,json,transfer_department,transfer_user,transfer_timestamp,"
//				+ "call_count,last_call_result,last_call_time,intent_type,intent_timestamp) "
//				+ "(select uuid,batch_uuid,phone_number,json,own_department transfer_department,"
//				+ "own_user transfer_user,now() transfer_timestamp,"
//				+ "call_count,last_call_result,last_call_time,intent_type,intent_timestamp from "
//				+ UserNode.tablePrefix + sourceTable + " where phone_number=?)";
		String insertSql = "insert into " + ShareNode.tableName
				+ "(uuid,batch_uuid,phone_number,json,own_user,own_user_timestamp,transfer_department,transfer_user,transfer_timestamp,"
				+ "call_count,last_call_result,last_call_time) "
				+ "(select uuid,batch_uuid,phone_number,json,? own_user,? own_user_timestamp,own_department transfer_department,"
				+ "own_user transfer_user,now() transfer_timestamp,"
				+ "call_count,last_call_result,last_call_time from "
				+ UserNode.tablePrefix + sourceTable + " where phone_number=?)";
		//删除数据
		String deleteSql = "delete from " + UserNode.tablePrefix + sourceTable + " where phone_number=?";
		//更新数据，数据同步原则：保证两个表中选出来的内容一致。时间有可能不一致，在外部定义变量是最好的方法。
		String updateSql = "update " + BatchNode.tablePrefix + batchUuid + " set own_user=null,own_user_timestamp=null,own_department=?"
				+ ",own_department_timestamp=now(),intent_type=null,intent_timestamp=null where phone_number=?";
		boolean needMark = "global_share".equals(lastSource);
		int updateCount = jdbcTemplate.update(insertSql, needMark?"system":null, needMark?new Date():null, phoneNumber);
		if (updateCount == 0) {
			return updateCount;
		}
		jdbcTemplate.update(deleteSql, phoneNumber);
		jdbcTemplate.update(updateSql, ShareNode.name, phoneNumber);
		synchronzineData(batchUuid, sourceTable, userUuid, updateCount);
		return updateCount;
    }
    
	@Override
	public void synchronzineData(String batchUuid, String depUuid, String userUuid, Integer count) {
		addIntent(batchUuid, batchUuid, depUuid, userUuid, -count);
		addShare(batchUuid, batchUuid, depUuid, userUuid, count);
		addOwn(null, batchUuid, depUuid, -count);
		addData(null, batchUuid, depUuid, userUuid, -count);
	}

}
