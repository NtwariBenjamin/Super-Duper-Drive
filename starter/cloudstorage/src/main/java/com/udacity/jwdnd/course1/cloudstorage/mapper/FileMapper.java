package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FileMapper {
    @Insert("insert into files(filename,contenttype,filesize,userid,filedata) values(#{fileName},#{contentType},#{fileSize},#{userId},#{fileData})")
    @Options(useGeneratedKeys = true,keyProperty = "fileId")
    int addFile(File file);

    @Update("update files set filename=#{fileName},contenttype=#{contentType},filesize=#{fileSize},userid=#{userId},filedata=#{fileData} where fileId=#{fileId}")
    int updateFile(@Param("fileName")String fileName, @Param("contentType") String contentType, @Param("fileSize") String fileSize, @Param("userId") Integer userId, @Param("fileData") String fileData);

    @Delete("delete from files where filename=#{fileName}")
    void deleteFile(String fileName);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName}")
    File getFile(String fileName);

    @Select("select filename from files where userid=#{userId}")
    String[] getAllFiles(Integer userId);
}
