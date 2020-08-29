package controller.admin;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lp.domain.RseponseResult;
import com.lp.domain.User;
import com.lp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminUserController {
    @Reference
    UserService userService;


    @RequestMapping("/user/showjson")
    @ResponseBody
    public RseponseResult userShowJson(@RequestParam(defaultValue = "1") Integer page) {

        PageHelper.startPage(page, 10);

        List<User> users = userService.queryUsers();

        PageInfo pageInfo = new PageInfo(users);

        Map<String, Object> info = new HashMap<String, Object>();

        info.put("pageInfo", pageInfo);

        return RseponseResult.success(null,info);
    }

    @RequestMapping("/user/delete/{userId}")
    public RseponseResult deleteUser(@PathVariable(value = "userId") int userId) {

        boolean flag = userService.deleteUser(userId);

        if (flag) {

            return  RseponseResult.success("删除成功",null);
        }

        return RseponseResult.fail("失败", null);
    }


}
