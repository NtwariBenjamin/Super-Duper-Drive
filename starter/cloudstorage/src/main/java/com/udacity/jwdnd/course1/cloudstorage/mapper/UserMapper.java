package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

    @Update("UPDATE users SET username = #{username}, salt = #{salt}, password = #{password}, firstname = #{firstName}, lastname = #{lastName} WHERE userid = #{userId}")
    int updateUser(@Param("username") String username, @Param("salt") String salt, @Param("password") String password, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("userId") Integer userId);

    @Delete("Delete from users where userid=#{userId}")
    void deleteUser(Integer userId);
    }

