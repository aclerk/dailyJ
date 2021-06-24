package com.pyjava.daily.mapper;

import com.pyjava.daily.entity.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>the mapper of note</p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/22 23:51
 */
@Mapper
public interface NoteMapper {
    /**
     * Get the notes under the notebook according to the notebook ID
     * @param notebookId notebook id
     * @return the list of note which parent id is the param
     *  and it guarantees that this function returns array
     */
    List<Note> listByNotebookId(@Param("notebookId") String notebookId);
}
