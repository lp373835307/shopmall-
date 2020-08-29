package controller.admin;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lp.domain.Category;
import com.lp.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/index")
    public String adminUser() {

        return "admin";
    }

    @RequestMapping("/user/show")
    public String showUserView() {

        return "userManage";
    }

    @Reference
    GoodsService goodsService;

    @RequestMapping("/goods/show")
    public  String showGoodsView(ModelMap modelMap) {

        List<Category> categories = goodsService.queryGoodsCategory();

        modelMap.addAttribute("categoryList",categories);

        return "adminAllGoods";
    }



    @RequestMapping("/chat")
    public String showChatView() {

        return "adminChat";
    }

    @RequestMapping("/logout")
    public void showLogoutView() {

    }

}
