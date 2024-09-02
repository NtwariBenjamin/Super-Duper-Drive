package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CredentialMapper {
    @Insert("insert into credentials(url,username,key,password,userid) values(#{url},#{username},#{key},#{password},#{userId})")
    @Options(useGeneratedKeys = true,keyProperty = "credentialId")
    int addCredentials(Credential credential);

    @Update("update credentials set url=#{url},username=#{username},key=#{key},password=#{password} where credentialid=#{credentialId}")
    int updateCredentials(Integer credentialId,@Param("url") String url, @Param("username") String username, @Param("key") String key,@Param("password") String password);

    @Delete("delete from credentials where credentialid=#{credentialId}")
    void deleteCredentials(Integer credentialId);

    @Select("select * from credentials where userid=#{userId}")
    Credential[] getCredentialListing(Integer userId);

    @Select("select * from credentials where credentialid=#{credentialId}")
    Credential getCredential(Integer credentialId);
}

