package com.pyjava.daily.service;

import com.pyjava.daily.entity.Note;

import java.util.List;

/**
 * the service interface of note
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/23 0:17
 */
public interface NoteService {
    /**
     * Get the notes under the notebook according to the notebook ID
     * @param notebookId notebook id
     * @return the list of note which parent id is the param
     * and it guarantees that this function returns array (it can not return null)
     */
    List<Note> listByNotebookId(String notebookId);
}
