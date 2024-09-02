package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Insert("insert into notes(notetitle,notedescription,userid) values(#{noteTitle},#{noteDescription},#{userId})")
    @Options(useGeneratedKeys = true,keyProperty = "noteId")
    int addNote(Note note);

    @Update("update notes set notetitle=#{noteTitle},notedescription=#{noteDescription} where noteid=#{noteId}")
    int updateNote(@Param("noteId") Integer noteId,@Param("noteTitle") String noteTitle, @Param("noteDescription") String noteDescription);

    @Delete("delete from notes where noteid=#{noteId}")
    void deleteNote(Integer noteId);

    @Select("select * from notes")
    Note[] getNoteListings();
    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    Note[] getNotesForUser(Integer userId);
    @Select("select * from notes where noteid=#{noteId}")
    Note getNote(Integer noteId);
}
