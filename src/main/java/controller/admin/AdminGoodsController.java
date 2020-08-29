package controller.admin;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lp.domain.Category;
import com.lp.domain.Goods;
import com.lp.domain.ImagePath;
import com.lp.domain.RseponseResult;

import com.lp.service.FileService;
import com.lp.service.GoodsService;
import com.lp.service.ImagePathService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminGoodsController {

    @Reference
    GoodsService goodsService;

    @Reference
    FileService fileService;

    @Reference
    ImagePathService imagePathService;


    @RequestMapping("/admin/goods/showjson")
    @ResponseBody
    public RseponseResult queryGoods(@RequestParam(value = "page", defaultValue = "1") Integer page) {

        PageHelper.startPage(page, 10);

        List<Goods> goods = goodsService.queryGoods();

        PageInfo pageInfo = new PageInfo(goods);

        Map<String, Object> infor = new HashMap<String, Object>();

        infor.put("pageInfo", pageInfo);

        return RseponseResult.success(null, infor);
    }


    @RequestMapping("/admin/goods/delete/{goodsId}")
    @ResponseBody
    public RseponseResult deleteGoods(@PathVariable(value = "goodsId") int goodsId) {

        boolean flag = goodsService.deleteGoods(goodsId);

        if (flag) {

            return RseponseResult.success("删除成功", null);
        }

        return RseponseResult.fail("失败", null);
    }

    @RequestMapping(value = "/admin/goods/update/", method = RequestMethod.POST)
    @ResponseBody
    public RseponseResult updateGoods(Goods goods) {


        boolean flag = goodsService.updateGoods(goods);


        if (flag) {

            return RseponseResult.success("成功", null);
        }

        return RseponseResult.fail("失败", null);

    }

    @RequestMapping("/admin/goods/toAddGoodsView")
    public String addGoods(Map map) {

        List<Category> categories = goodsService.queryGoodsCategory();

        map.put("categoryList", categories);

        return "addGoods";
    }

    @RequestMapping(value = "/admin/goods/addGoodInfo", method = RequestMethod.POST)
    public String addGoodsInfor(Goods goods) {

        goodsService.addGoods(goods);

        //拿到圖片路徑
        String upload = fileService.upload(goods.getFileToUpload());

        ImagePath imagePath = new ImagePath();

        imagePath.setGoodId(goods.getGoodsId());

        imagePath.setPath(upload);

        imagePathService.insert(imagePath);

        return "redirect:/admin/goods/show";

    }
}