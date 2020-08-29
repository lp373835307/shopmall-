package controller.front;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lp.domain.Goods;
import com.lp.domain.User;
import com.lp.service.CategoryService;
import com.lp.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainController {

    @Reference
    GoodsService goodsService;

    @Reference
    CategoryService categoryService;


    @RequestMapping("/")
    public ModelAndView showMainView(HttpSession session) {

        ModelAndView modelAndView = new ModelAndView();


        User user = (User) session.getAttribute("user");


        modelAndView.addObject("digGoods", goodsService.queryCategoryName("数码", user));

        modelAndView.addObject("houseGoods", goodsService.queryCategoryName("家电", user));

        modelAndView.addObject("colGoods", goodsService.queryCategoryName("服饰", user));

        modelAndView.addObject("bookGoods", goodsService.queryCategoryName("书籍", user));

        modelAndView.setViewName("main");

        return modelAndView;
    }

    @RequestMapping("/category")
    public ModelAndView queryCate(HttpSession session , @RequestParam(value = "page", defaultValue = "1") int page, String cate) {

        ModelAndView modelAndView = new ModelAndView();

        User user = (User) session.getAttribute("user");


        PageHelper.startPage(page, 5);


        List<Goods> goodsList = goodsService.queryCategoryName(cate,user);

        PageInfo pageInfo = new PageInfo(goodsList);

        modelAndView.addObject("pageInfo", pageInfo);

        modelAndView.setViewName("category");

        return modelAndView;
    }
}
