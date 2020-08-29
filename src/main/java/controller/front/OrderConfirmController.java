package controller.front;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lp.domain.*;
import com.lp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class OrderConfirmController {

    @Reference
    private AddressService addressService;

    @Reference
    private ShopCartService shopCartService;

    @Reference
    private GoodsService goodsService;

    @Reference

    private ImagePathService imagePathService;

    @Reference
    private AcativityService acativityService;

    @Reference
    private OrderService orderService;

    @RequestMapping("/order")
    public ModelAndView orderShop(HttpSession session) {

        ModelAndView modelAndView = new ModelAndView();

        User user = (User) session.getAttribute("user");

        if (user == null) {

            modelAndView.setViewName("login");

            return modelAndView;
        }
        List<Address> addressList = addressService.queryAdderssByUserId(user.getUserId());

        modelAndView.addObject("address", addressList);

        List<ShopCart> shopCarts = shopCartService.queryShopCart(user.getUserId());


        int oldTotalPrice = 0;
        int totalPrice = 0;
        if (shopCarts != null && shopCarts.size() > 0) {
            List<Goods> goodsList = new ArrayList<>();
            for (ShopCart shopCart : shopCarts) {
                Goods good = goodsService.queryGoodsById(shopCart.getGoodsId());

                good.setNum(shopCart.getGoodsNum());

                int price = good.getPrice() * good.getNum();
                oldTotalPrice += price;

                // 查活动, 活动比例
                Acativity acativity = acativityService.queryActivityId(good.getActivityId());

                if (acativity == null) {
                    totalPrice = oldTotalPrice;
                } else {
                    totalPrice = oldTotalPrice - acativity.getFullNum();
                }

                List<ImagePath> imagePathList = imagePathService.queryImagePath(good.getGoodsId());

                good.setImagePaths(imagePathList);

                good.setNewPrice(good.getPrice()*shopCart.getGoodsNum());

                goodsList.add(good);

            }
            modelAndView.addObject("goodsAndImage", goodsList);
        }
        modelAndView.addObject("oldTotalPrice", oldTotalPrice);
        modelAndView.addObject("totalPrice", totalPrice);
        modelAndView.setViewName("orderConfirm");

        return modelAndView;
    }

    @RequestMapping(value = "/shop/orderFinish", method = RequestMethod.POST)
    @ResponseBody
    public RseponseResult orderFinish(HttpSession session, Float oldPrice, Float newPrice, int isPay, int addressId) {

        User user = (User) session.getAttribute("user");

        if (user == null) {

            return RseponseResult.fail("请登录", null);
        }

        Address address = addressService.queryAdderssById(addressId);
        Order order = new Order();
        order.setAddress(address);
        order.setUserId(user.getUserId());
        order.setAddressId(addressId);
        order.setIsPay(isPay);
        order.setOrderTime(new Date());
        order.setOldPrice(oldPrice);
        order.setNewPrice(newPrice);
        boolean flag = orderService.insertOrder(order);

        if (flag) {

            return RseponseResult.success("购买成功", null);
        }

        return RseponseResult.fail("失败", null);
    }
}
