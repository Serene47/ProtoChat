package com.serene.protochat.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.serene.protochat.model.User;

@Repository
public class UserDao {

	private NamedParameterJdbcTemplate nPjdbcTemplate;
	
	private PasswordEncoder passwordEncoder;

	public UserDao(DataSource datasource, PasswordEncoder passwordEncoder) {
		
		this.passwordEncoder = passwordEncoder;
		
		this.nPjdbcTemplate = new NamedParameterJdbcTemplate(datasource);
	}
	
	public User findUserByUsername(String username) {
		
		String sql = "select user_Id,username,enabled,password,displayName,displayPicture from Users where username =:username";
		
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("username", username);
		
		List<User> users =  nPjdbcTemplate.query(sql, inparams, new BeanPropertyRowMapper(User.class));
		
		
		return users.size() > 0 ? users.get(0) : null;
	
	}
	
	public boolean isUsernameExist(String username) {
		
		String sql = "select count(user_id) from Users where username =:username";
		
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("username", username);
		
		Integer count = nPjdbcTemplate.queryForObject(sql, inparams, Integer.class);
		
		return count > 0;
	}
	
	
	public int insert(User user) {
		
		String hashedPassword = passwordEncoder.encode(user.getPassword());
		
		String sql = "insert into Users (username,password,enabled,displayname) "
				+ "values(:username,:password,:enabled,:displayname)";
		
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("username",user.getUsername())
				.addValue("password", hashedPassword)
				.addValue("enabled", false)
				.addValue("displayname", user.getDisplayName());
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		nPjdbcTemplate.update(sql,inparams,keyHolder);
		
		return keyHolder.getKey().intValue();
		
	}
	
	public void updateDisplayPicture(int user_Id, String displayPicture) {
		
		String sql  = "update Users set displaypicture = :displaypicture " + 
					" where user_id = :user_id";
		
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("displaypicture",displayPicture)
				.addValue("user_id", user_Id);
		
		
		nPjdbcTemplate.update(sql, inparams);
		
	}
	
	public void updateDisplayName(int user_Id, String displayName) {
		
		String sql  = "update Users set displayname = :displayname " + 
				" where user_id = :user_id";
	
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("displayname",displayName)
				.addValue("user_id", user_Id);
		
		
		nPjdbcTemplate.update(sql, inparams);
		
	}
	
	public void updatePassword(int user_Id, String password) {
		
		password = passwordEncoder.encode(password);
		
		String sql  = "update Users set password = :password " + 
				" where user_id = :user_id";
	
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("password",password)
				.addValue("user_id", user_Id);
		
		
		nPjdbcTemplate.update(sql, inparams);
		
	}
	
	public void enableUser(int user_Id) {
		
		String sql  = "update Users set enabled = 1 " + 
				" where user_id = :user_id";
	
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("user_id", user_Id);
		
		
		nPjdbcTemplate.update(sql, inparams);
		
	}
	
	
	
}
