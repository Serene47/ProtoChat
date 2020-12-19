package com.serene.protochat.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.serene.protochat.model.UserVerificationToken;

@Repository
public class UserVerificationTokenDao {

	private NamedParameterJdbcTemplate nPjdbcTemplate;
	
	UserVerificationTokenDao(DataSource datasource){
		
		this.nPjdbcTemplate = new NamedParameterJdbcTemplate(datasource);
		
	}
	
	public int insert(UserVerificationToken userVerificationToken) {
		
		String sql = "insert into UserVerificationTokens ( user_Id, token, expiry, type) " + 
				"values (:user_Id,:token,:expiry, :type)";
		
		SqlParameterSource inParams = new MapSqlParameterSource()
				.addValue("user_Id", userVerificationToken.getUser_Id())
				.addValue("token", userVerificationToken.getToken())
				.addValue("expiry", userVerificationToken.getExpiry())
				.addValue("type", userVerificationToken.getType());
		
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		nPjdbcTemplate.update(sql,inParams,keyHolder);
		
		return keyHolder.getKey().intValue();
		
	}
	
	public void delete(int userVerificationToken_Id) {
		
		String sql = "delete from UserVerificationTokens where UserVerificationToken_Id = :userVerificationToken_Id";
		
		SqlParameterSource inParams = new MapSqlParameterSource()
				.addValue("userVerificationToken_Id", userVerificationToken_Id);
		
		
		nPjdbcTemplate.update(sql,inParams);
		
	}
	
	public UserVerificationToken loadDetailsByToken(String token,String type) {
		
		String sql = "select userVerificationToken_Id,user_Id,token,expiry " + 
		"from UserVerificationTokens where token = :token and type =:type ";
		
		SqlParameterSource inParams = new MapSqlParameterSource()
				.addValue("token", token)
				.addValue("type", type);
		
		List<UserVerificationToken>  result= 
				nPjdbcTemplate.query(sql, inParams, new BeanPropertyRowMapper<UserVerificationToken>(UserVerificationToken.class));
		
		return result.size() > 0 ? result.get(0) : null;
		
	}
	
	
}
