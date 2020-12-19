package com.serene.protochat.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.serene.protochat.model.Message;

@Repository
public class MessageDao {

	
	private SimpleJdbcCall getMessagesCall;
	
	private SimpleJdbcCall saveMessageCall;
	
	private SimpleJdbcCall markReadMessagesCall;

	@Autowired
	public MessageDao(DataSource datasource) {

		getMessagesCall = new SimpleJdbcCall(datasource)
				.withSchemaName("chatapp")
				.withProcedureName("getMessages")
				.returningResultSet("messages", new BeanPropertyRowMapper<Message>(Message.class));
		
		saveMessageCall = new SimpleJdbcCall(datasource)
				.withSchemaName("chatapp")
				.withProcedureName("saveMessage");
		
		markReadMessagesCall = new SimpleJdbcCall(datasource)
				.withSchemaName("chatapp")
				.withProcedureName("markMessagesRead");
		
	}
	
	public List<Message> selectAll(int chatRoom_Id,int user_Id){
		
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("chatRoom_Id", chatRoom_Id)
				.addValue("user_Id", user_Id);
		
		Map<String, Object>  resultSet = getMessagesCall.execute(inparams);
		
		return (List<Message>) resultSet.get("messages");
		
	}
	
	public Message  insert(Message message) {
		
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("fromuser_Id", message.getFromUser_Id())
				.addValue("touser_Id", message.getToUser_Id())
				.addValue("content", message.getContent())
				.addValue("timestamp", message.getTimestamp());
		
		Map<String, Object> resultSet = saveMessageCall.execute(inparams);
		
		int message_Id = (int) resultSet.get("message_Id");
		int chatRoom_Id = (int) resultSet.get("chatRoom_Id");
		
		message.setMessage_Id(message_Id);
		message.setChatRoom_Id(chatRoom_Id);
		
		return message;
		
		
	}
	
	public void markMessagesRead(String messageIdList, int user_Id) {
		
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("messageIdList",messageIdList)
				.addValue("user_Id", user_Id);
		
		markReadMessagesCall.execute(inparams);
		
	}
}
