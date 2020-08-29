package controller.front;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lp.domain.*;
import com.lp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InfoController {

    @Reference
    UserService userService;

    @Reference
    OrderService orderService;

    @Reference
    AddressService addressService;

    @Reference
    CommentService commentService;

    @Reference
    GoodsService goodsService;

    @Reference
    ImagePathService imagePathService;

    @Reference
    CollectionService collectionService;


    @RequestMapping("/information")
    public ModelAndView informationUser(HttpSession session) {

        ModelAndView modelAndView = new ModelAndView();

        User user = (User) session.getAttribute("user");

        if (user == null) {

            modelAndView.setViewName("redirect:login");

            return modelAndView;
        }

        User user1 = userService.queryUser(user);

        modelAndView.addObject(user1);

        modelAndView.setViewName("information");

        return modelAndView;
    }

    @RequestMapping("/info/list")
    public ModelAndView infoList(HttpSession session) {

        ModelAndView modelAndView = new ModelAndView();

        User user = (User) session.getAttribute("user");

        if (user == null) {

            modelAndView.setViewName("login");

            return modelAndView;
        }

        List<Order> orderList = orderService.queryOrderById(user.getUserId());

        modelAndView.addObject("orderList", orderList);

        modelAndView.setViewName("list");

        return modelAndView;

    }

    @RequestMapping("/info/address")
    public ModelAndView infoAddres(HttpSession session) {

        ModelAndView modelAndView = new ModelAndView();

        User user = (User) session.getAttribute("user");

        if (user == null) {

            modelAndView.setViewName("login");

            return modelAndView;
        }

        List<Address> addressList = addressService.queryAdderssByUserId(user.getUserId());

        modelAndView.addObject("addressList", addressList);

        modelAndView.setViewName("address");

        return modelAndView;
    }


    //修改邮箱和电话
    @RequestMapping("/shop/saveInfo")
    @ResponseBody
    public RseponseResult updetUser(HttpSession session, String email, String telephone) {

        User user = (User) session.getAttribute("user");

        if (user == null) {

            return RseponseResult.fail("请登录", null);
        }

        boolean updetUser = userService.updetUser(user.getUserName(), email, telephone);


        if (updetUser) {
            return RseponseResult.success("修改成功", null);
        }
        return RseponseResult.fail("修改失败", null);
    }

    //修改密码
    @RequestMapping(value = "/shop/savePsw", method = RequestMethod.POST)
    @ResponseBody
    public RseponseResult updetUserPassword(HttpSession session, String oldPsw, String Psw) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return RseponseResult.fail("请登录", null);
        }

        User user1 = userService.queryUserId(user.getUserId());

        if (!oldPsw.equals(user1.getPassword())) {

            return RseponseResult.fail("请重新输入", null);
        }
        boolean flag = userService.updetUserPassword(user1.getUserId(), Psw);

        if (flag) {
            return RseponseResult.success("修改成功", null);
        }

        return RseponseResult.fail("修改失败", null);
    }

    //完成订单
    @RequestMapping(value = "/shop/finishList", method = RequestMethod.POST)
    @ResponseBody
    public RseponseResult orderFinishList(HttpSession session, int orderId) {

        User user = (User) session.getAttribute("user");

        if (user == null) {

            return RseponseResult.fail("请登录", null);
        }

        boolean flag = orderService.updateOrder(user.getUserId(), orderId);

        if (flag) {

            return RseponseResult.success("完成订单", null);
        }
        return RseponseResult.fail("失败", null);
    }

    //删除订单
    @RequestMapping(value = "/shop/deleteList", method = RequestMethod.POST)
    @ResponseBody
    public RseponseResult deleteOrder(HttpSession session, int orderId) {

        User user = (User) session.getAttribute("user");

        if (user == null) {

            return RseponseResult.fail("请登录", null);
        }

        boolean flag = orderService.deletOrder(user.getUserId(), orderId);

        if (flag) {

            return RseponseResult.success("删除成功", null);
        }

        return RseponseResult.fail("删除失败", null);
    }

    //评价
    @RequestMapping(value = "/shop/comment", method = RequestMethod.POST)
    @ResponseBody
    public RseponseResult shopComment(HttpSession session, Comment comment) {

        User user = (User) session.getAttribute("user");

        if (user == null) {

            return RseponseResult.fail("请登录", null);
        }
        comment.setUserId(user.getUserId());

        boolean flag = commentService.insterComment(comment);

        if (flag) {
            return RseponseResult.success("评价成功", null);
        }

        return RseponseResult.fail("评价失败", null);
    }

    //收藏
    @RequestMapping("/info/favorite")
    public ModelAndView favoriteGoods(HttpSession session, @RequestParam(value = "page", defaultValue = "1") int page) {

        PageHelper.startPage(page, 5);

        User user = (User) session.getAttribute("user");

        ModelAndView modelAndView = new ModelAndView();

        if (user == null) {

            modelAndView.setViewName("login");

            return modelAndView;
        }
        List<Collect> collects = collectionService.queryFavoriteByuserId(user.getUserId());

        List<Goods> goodsList = new ArrayList<Goods>();

        if (collects != null && collects.size() > 0) {

            for (Collect collect : collects) {

                Goods goods = goodsService.queryGoodsById(collect.getGoodsId());

                List<ImagePath> imagePathList = imagePathService.queryImagePath(goods.getGoodsId());

                goods.setImagePaths(imagePathList);

                if (goods.getGoodsName().length() > 10) {

                    String substring = goods.getGoodsName().substring(0, 10);

                    goods.setGoodsName(substring);
                }
                goodsList.add(goods);
            }
        }

        PageInfo pageInfo = new PageInfo();

        pageInfo.setList(goodsList);

        modelAndView.addObject("pageInfo", pageInfo);

        modelAndView.setViewName("favorite");

        return modelAndView;

    }

    //收藏
    @RequestMapping(value = "/shop/collect", method = RequestMethod.POST)
    @ResponseBody
    public RseponseResult CollectGoods(HttpSession session, int goodsId) {

        User user = (User) session.getAttribute("user");

        if (user == null) {

            return RseponseResult.fail("请登录", null);
        }

        Collect querycollect = collectionService.querycollect(user.getUserId(), goodsId);


        if (querycollect == null) {

            querycollect = new Collect();

            querycollect.setGoodsId(goodsId);

            querycollect.setUserId(user.getUserId());

            boolean flag = collectionService.insertcollect(querycollect);

            if (flag) {

                return RseponseResult.success("收藏成功", null);
            }

        }


        return RseponseResult.fail("已收藏", null);
    }

    @RequestMapping("/shop/deleteCollect")
    @ResponseBody
    public RseponseResult deleteCollect(HttpSession session, int goodsId) {

        User user = (User) session.getAttribute("user");

        if (user == null) {

            return RseponseResult.fail("请登录", null);
        }
        Collect querycollect = collectionService.querycollect(user.getUserId(), goodsId);

        if (querycollect != null) {

            boolean flag = collectionService.updateCollect(querycollect);

            if (flag) {

                return RseponseResult.success("取消成功", null);
            }
        }
        return RseponseResult.fail("取消失败", null);
    }
}

