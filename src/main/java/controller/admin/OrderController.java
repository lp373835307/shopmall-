package controller.admin;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lp.domain.Address;
import com.lp.domain.Goods;
import com.lp.domain.Order;
import com.lp.domain.OrderItem;
import com.lp.service.AddressService;
import com.lp.service.GoodsService;
import com.lp.service.OrderItemService;
import com.lp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @Reference
    OrderService orderService;

    @Reference
    AddressService addressService;

    @Reference
    OrderItemService orderItemService;

    @Reference
    GoodsService goodsService;


    @RequestMapping("/admin/order/send")
    public ModelAndView queryOrderSend(@RequestParam(defaultValue = "1") int page) {

        PageHelper.startPage(page, 5);

        List<Order> orders = orderService.queryOrderSend();

        ModelAndView modelAndView = getModelAndView(orders);

        return modelAndView;
    }

    @RequestMapping("/admin/order/receiver")
    public ModelAndView queryOderReceiver(@RequestParam(defaultValue = "1") int page) {

        PageHelper.startPage(page, 5);

        List<Order> orders = orderService.queryOrderReceiver();

        ModelAndView modelAndView = getModelAndView(orders);

        return modelAndView;
    }

    private ModelAndView getModelAndView(List<Order> orders) {
        if (orders.size() > 0 && orders != null) {

            for (Order order : orders) {

                Address address = addressService.queryAdderssById(order.getAddressId());

                order.setAddress(address);

                List<OrderItem> orderItems = orderItemService.queryOrderItemById(order.getOrderId());

                for (OrderItem orderItem : orderItems) {

                    Goods goods = goodsService.queryGoodsById(orderItem.getGoodsId());

                    List<Goods> goodsInfo = new ArrayList<Goods>();

                    goodsInfo.add(goods);

                    order.setGoodsInfo(goodsInfo);
                }


            }
        }
        PageInfo pageInfo = new PageInfo(orders);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("pageInfo", pageInfo);

        modelAndView.setViewName("adminAllOrder");
        return modelAndView;
    }

    @RequestMapping("/admin/order/complete")
    public ModelAndView queryOrderComplete(@RequestParam(defaultValue = "1") int page) {

        PageHelper.startPage(page, 5);

       List<Order> orders =  orderService.queryOrderComplete();

        ModelAndView modelAndView = getModelAndView(orders);

        return modelAndView;
    }
}
