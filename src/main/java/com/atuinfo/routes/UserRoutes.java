package com.atuinfo.routes;

import com.atuinfo.user.UserController;
import com.jfinal.config.Routes;

public class UserRoutes extends Routes {

    @Override
    public void config() {
        add("/user", UserController.class);
    }
}


