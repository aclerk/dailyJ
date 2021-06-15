package com.pyjava.daily.config;

import com.google.inject.AbstractModule;
import com.pyjava.daily.service.NoteService;
import com.pyjava.daily.service.NoteServiceImpl;

/**
 * <p>描述: daily module </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/1 0:12
 */
public class DailyModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(NoteService.class).to(NoteServiceImpl.class);
    }
}
