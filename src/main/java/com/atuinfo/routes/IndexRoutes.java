package com.atuinfo.routes;

import com.atuinfo.controller.IndexController;
import com.jfinal.config.Routes;

public class IndexRoutes extends Routes {
    @Override
    public void config() {
        add("/index", IndexController.class);
    }
}
