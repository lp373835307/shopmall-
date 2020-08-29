package controller.front;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lp.domain.*;
import com.lp.service.GoodsService;
import com.lp.service.ImagePathService;
import com.lp.service.ShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ShopCartController {

    @Reference
    private GoodsService goodsService;


    @Reference
    private ShopCartService shopCartService;

    @Reference
    private ImagePathService imagePathService;

    @RequestMapping("/showcart")
    public ModelAndView orderShow(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        if (user == null) {

            modelAndView.setViewName("login");
            return modelAndView;
        }
        modelAndView.setViewName("shopcart");
        return modelAndView;
    }


    @RequestMapping("/cartJson")
    @ResponseBody
    public RseponseResult orderShowCart(HttpSession session) {


        User user = (User) session.getAttribute("user");

        if (user == null) {

            return RseponseResult.fail("swal", null);

        }

        List<ShopCart> shopCarts = shopCartService.queryShopCart(user.getUserId());

        List<Goods> goodsList = new ArrayList<Goods>();

        if (shopCarts != null && shopCarts.size() > 0) {

            for (ShopCart shopCart : shopCarts) {

                Goods goods = goodsService.queryGoodsById(shopCart.getGoodsId());

                List<ImagePath> imagePathList = imagePathService.queryImagePath(goods.getGoodsId());

                goods.setImagePaths(imagePathList);

                goods.setNum(shopCart.getGoodsNum());

                goodsList.add(goods);

            }

        }

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("shopcart", goodsList);

        return RseponseResult.success(map);


    }

    @RequestMapping("/deleteCart/{goodsId}")
    @ResponseBody
    public RseponseResult deleteCart(HttpSession session, @PathVariable(value = "goodsId") int goodsId) {

        User user = (User) session.getAttribute("user");

        if (user == null) {

            return RseponseResult.fail("请登录", null);
        }
        boolean flag = shopCartService.deleteCart(user.getUserId(), goodsId);

        if (flag) {

            return RseponseResult.success("删除成功", null);
        }

        return RseponseResult.fail("删除失败", null);
    }

    @RequestMapping("/shop/update")
    @ResponseBody
    public RseponseResult updateCart(int goodsId, int num) {

        boolean flag = shopCartService.updateCart(goodsId, num);

        if (flag) {

            return RseponseResult.success("修改成功", null);
        }

        return RseponseResult.fail("修改失败", null);
    }

}
