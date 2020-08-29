package controller.front;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lp.domain.*;
import com.lp.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DetailController {

    @Reference
    GoodsService goodsService;

    @Reference
    CategoryService categoryService;

    @Reference
    AcativityService acativityService;

    @Reference
    ShopCartService shopCartService;

    @Reference
    CommentService commentService;

    @Reference
    UserService userService;


    @RequestMapping("/detail")
    public ModelAndView queryDetail(int goodsId) {

        ModelAndView modelAndView = new ModelAndView();

        Goods goods = goodsService.queryGoodsById(goodsId);

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("goods", goods);

        List<ImagePath> imagePathList = goodsService.queryImagePath(goods.getGoodsId());

        map.put("image", imagePathList);

        Category category = categoryService.queryCategoryId(goods.getCategory());

        map.put("cate", category);

        Acativity acativity = acativityService.queryActivityId(goods.getActivityId());

        goods.setAcativity(acativity);

        List<Comment> comments = commentService.queryComment(goodsId);

        if (comments != null && comments.size() > 0) {

            for (Comment comment : comments) {

                User user = userService.queryUserId(comment.getUserId());

                comment.setUserName(user.getUserName());
            }
        }

        modelAndView.addObject("goodsInfo", map);

        modelAndView.addObject("commentList", comments);

        modelAndView.setViewName("detail");

        return modelAndView;
    }

    @RequestMapping(value = "/addCart", method = RequestMethod.POST)
    public ModelAndView addCart(HttpSession session, int goodsId,int num) {

        ModelAndView modelAndView = new ModelAndView();

        User user = (User) session.getAttribute("user");

        if (user == null) {
            modelAndView.setViewName("login");
            return modelAndView;
        }
        ShopCart shopCart = shopCartService.queryaddCart(user.getUserId(), goodsId);

        if (shopCart == null) {

            shopCart = new ShopCart();

            shopCart.setGoodsId(goodsId);

            shopCart.setCateDate(new Date());

            shopCart.setUserId(user.getUserId());

            shopCart.setGoodsNum(num);

            boolean flag = shopCartService.insertaddCart(shopCart);

            if (flag) {

                modelAndView.setViewName("shopcart");

                return modelAndView;
            }
        }
        shopCart.setGoodsNum(shopCart.getGoodsNum() + num);

        modelAndView.setViewName("shopcart");

        return modelAndView;
    }
}
