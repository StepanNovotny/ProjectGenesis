package com.project.ProjectGenesis.service;

import com.project.ProjectGenesis.model.User;
import com.project.ProjectGenesis.model.DetailedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void createUser(DetailedUser detailedUser) throws Exception {
        checkPersonid(detailedUser.getPersonId());
        jdbcTemplate.update("INSERT into users (name, surname, person_id, uuid) VALUES (?, ?, ?, ?)",
                detailedUser.getName(), detailedUser.getSurname(),detailedUser.getPersonId(),UUID.randomUUID().toString());
    }

    public User getUser(long id,boolean detail){
        User user = (User) jdbcTemplate.queryForObject("SELECT * from `users` where id = " + id, new RowMapper<Object>() {
            public User mapRow(ResultSet rs, int rowNum) throws SQLException{
                User user = new User();
                if(detail){
                    DetailedUser detailedUser = new DetailedUser();
                    detailedUser.setPersonId(rs.getString("person_id"));
                    detailedUser.setUuid(rs.getString("uuid"));
                    user = detailedUser;
                }
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                return user;
            }
        });
        return user;
    }

    public List<User> getAllUsers(boolean detail){
        List<User> userList = jdbcTemplate.query("SELECT * from `users`",new RowMapper<User>(){
            public User mapRow(ResultSet rs, int rowNum) throws SQLException{
                User users =new User();
                if(detail){
                    DetailedUser detailedUsers = new DetailedUser();
                    detailedUsers.setPersonId(rs.getString("person_id"));
                    detailedUsers.setUuid(rs.getString("uuid"));
                    users = detailedUsers;
                }
                users.setId(rs.getInt("id"));
                users.setName(rs.getString("name"));
                users.setSurname(rs.getString("surname"));
                return users;
            }
        });
        return userList;
    }

    public void editUser(User user){
        jdbcTemplate.update("UPDATE users SET name = ?, surname = ? WHERE id = ?",
                user.getName(),user.getSurname(),user.getId());

    }

    public void deleteUser(long id){
        jdbcTemplate.update("DELETE from users where id = "+id);

    }

    private void checkPersonid(String personId) throws Exception {
        if(personId.length()!=12){
            throw new Exception("Person ID musi obsahovat 12 znaku");
        }
        List<DetailedUser> userList = jdbcTemplate.query("SELECT person_id from `users`",new RowMapper<DetailedUser>(){
            public DetailedUser mapRow(ResultSet rs, int rowNum) throws SQLException{
                    DetailedUser detailedUsers = new DetailedUser();
                    detailedUsers.setPersonId(rs.getString("person_id"));
                return detailedUsers;
            }
        });
        if(userList.stream().anyMatch(user -> personId.equals(user.getPersonId()))){
            throw new Exception("Person ID musi byt originalni");
        }
    }

}


