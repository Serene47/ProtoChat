package com.serene.protochat.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.serene.protochat.model.Contact;
import com.serene.protochat.model.RecentContact;

@Repository
public class ContactsDao {

	private NamedParameterJdbcTemplate jdbcTemplate;
	
	private SimpleJdbcCall recentContactSelectorCall;

	private SimpleJdbcCall allContactSelectorCall;
	
	@Autowired
	public ContactsDao(DataSource datasource) {
		
		jdbcTemplate = new NamedParameterJdbcTemplate(datasource);
		
		recentContactSelectorCall = new SimpleJdbcCall(datasource)
				.withSchemaName("chatapp")
				.withProcedureName("getRecentContacts")
				.returningResultSet("contacts", new BeanPropertyRowMapper<RecentContact>(RecentContact.class));
	}
	
	public List<RecentContact> selectRecentContacts(int currentuser_Id){
		
		/*String sql = "SELECT user_Id,displayName from Users where user_Id != :currentuser_Id";
		
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("currentuser_Id", currentuser_Id);
		
		return jdbcTemplate.query(sql,inparams,new BeanPropertyRowMapper(Contact.class));*/
		
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("user_Id", currentuser_Id);
		
		Map<String, Object>  resultSet = recentContactSelectorCall.execute(inparams);
		
		return (List<RecentContact>) resultSet.get("contacts");
		
	}
	
	public List<Contact> selectAllContacts(int currentuser_Id) {
		
		String sql = "SELECT Users.user_Id,displayName,displayPicture " + 
				"FROM Users WHERE enabled = 1 and Users.user_Id != :user_Id";
		
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("user_Id", currentuser_Id);
		
		return jdbcTemplate.query(sql, inparams, new BeanPropertyRowMapper<Contact>(Contact.class));
		
		
	}
	
	
	
}
