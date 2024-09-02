package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    private NoteMapper noteMapper;
    private UserMapper userMapper;

    public NoteService(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    //    public void addNote(NoteForm noteForm) {
//        Note newNote=new Note();
//        newNote.setNoteTitle(noteForm.getNoteTitle());
//        newNote.setNoteDescription(noteForm.getNoteDescription());
//        newNote.setUserId(Integer.valueOf(noteForm.getUserId()));
//        noteMapper.addNote(newNote);
//    }

    public Note[] getNoteListings(Integer userId) {
        return noteMapper.getNotesForUser(userId);
    }
    public Note getNote(Integer noteId) {
        return noteMapper.getNote(noteId);
    }

    public void addNote(String newTitle, String newDescription, String username) {
        Integer userId=userMapper.getUser(username).getUserId();
        noteMapper.addNote(new Note(0,newTitle,newDescription,userId));
    }


    public void updateNote(Integer noteId, String newTitle, String newDescription) {
        noteMapper.updateNote(noteId,newTitle,newDescription);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }
}
