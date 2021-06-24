package com.pyjava.daily.service.impl;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import com.pyjava.daily.entity.Note;
import com.pyjava.daily.mapper.NoteMapper;
import com.pyjava.daily.service.NoteService;
import com.pyjava.daily.util.JdbcUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/23 0:18
 */
@Singleton
public class NoteServiceImpl implements NoteService {
    @Override
    public List<Note> listByNotebookId(String notebookId) {
        List<Note> query;
        // 1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = JdbcUtil.getSqlSessionFactory();
        // 2、获取sqlSession对象
        try (SqlSession openSession = sqlSessionFactory.openSession()) {
            // 3、获取接口的实现类对象
            //会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
            NoteMapper mapper = openSession.getMapper(NoteMapper.class);
            query = mapper.listByNotebookId(notebookId);
        }
        if(CollectionUtils.isEmpty(query)){
            return Lists.newArrayList();
        }
        return query;
    }
}
