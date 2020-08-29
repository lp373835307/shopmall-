package controller.front;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lp.domain.Address;
import com.lp.domain.RseponseResult;
import com.lp.domain.User;
import com.lp.service.AddressService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
public class AddressController {

    @Reference
    AddressService addressService;

    @RequestMapping(value = "/shop/insertAddr", method = RequestMethod.POST)
    @ResponseBody
    public RseponseResult insertAddress(HttpSession session, Address insertAddr) {

        User user = (User) session.getAttribute("user");

        if (user == null) {

            return RseponseResult.fail("请登录", null);
        }

        insertAddr.setUserId(user.getUserId());

        boolean flag = addressService.insertAddress(insertAddr);

        if (flag) {

            return RseponseResult.success("添加成功", null);
        }

        return RseponseResult.fail("添加失败", null);
    }

    //修改地址
    @RequestMapping(value = "/shop/saveAddr", method = RequestMethod.POST)
    @ResponseBody
    public RseponseResult saveAddr(HttpSession session, Address saveAddr) {

        User user = (User) session.getAttribute("user");

        if (user == null) {

            return RseponseResult.fail("请登录", null);
        }
        saveAddr.setUserId(user.getUserId());

        boolean flag = addressService.updateAddress(saveAddr);

        if (flag) {

            return RseponseResult.success("修改成功", null);
        }

        return RseponseResult.fail("修改失败", null);
    }

    //删除地址
    @RequestMapping("/shop/deleteAddr")
    @ResponseBody
    public RseponseResult deleteAddr(HttpSession session, int addressID) {

        User user = (User) session.getAttribute("user");

        if (user == null) {

            return RseponseResult.fail("请登录", null);
        }

        boolean flag = addressService.deteleAddr(user.getUserId(), addressID);

        if (flag) {

            return RseponseResult.success("删除成功", null);
        }

        return RseponseResult.fail("删除失败", null);
    }
}
