package org.lovesoa.calledweb.web;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api") // все REST-URL будут начинаться с /api
public class RestApplication extends Application {
    // пусто, достаточно аннотации
}

