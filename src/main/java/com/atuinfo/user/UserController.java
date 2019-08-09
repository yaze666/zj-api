package com.atuinfo.user;

import com.atuinfo.common.model.User;
import com.atuinfo.core.Result;
import com.atuinfo.core.ResultGenerator;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

import java.util.List;
import java.util.Map;


@Before(UserInterceptor.class)
public class UserController extends Controller {
    @Inject
    UserService service;

    public void index() {
        setAttr("userPage", service.paginate(getParaToInt(0, 1), 10).getList());
        List<User> ls =  service.getAll();
        for(int i = 0 ;i < ls.size() ;i++){
            System.out.println(ls.get(i));
        }
        Map result = ResultGenerator.genSuccessResult();
        renderJson(result);
    }

    public void add() {
    }

    /**
     * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
     * 并要对数据进正确性进行验证，在此仅为了偷懒
     */
    @Before(UserValidator.class)
    public void save() {
        getBean(User.class).save();
        redirect("/blog");
    }

    public void edit() {
        setAttr("blog", service.findById(getParaToInt()));
    }

    /**
     * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
     * 并要对数据进正确性进行验证，在此仅为了偷懒
     */
    @Before(UserValidator.class)
    public void update() {
        getBean(User.class).update();
        redirect("/blog");
    }

    public void delete() {
        service.deleteById(getParaToInt());
        redirect("/blog");
    }
}
