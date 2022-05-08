package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Insert("INSERT INTO FILES(filename, contenttype, filesize, filedata, userid) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{fileData}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Select("SELECT fileid, filename FROM FILES WHERE userid=#{userId}")
    List<File> getFiles(int userId);

    @Select("SELECT * FROM FILES WHERE filename=#{fileName}")
    File getFileByFileName(String fileName);

    @Select("SELECT * From FILES WHERE fileId=#{fileId}")
    File getFileByFileId(Integer fileId);

    @Delete("DELETE FROM FILES WHERE fileid=#{fileId}")
    int delete(Integer fileId);
}
