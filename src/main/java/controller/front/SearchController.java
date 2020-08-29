package controller.front;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lp.domain.Goods;
import com.lp.domain.ImagePath;
import com.lp.service.GoodsService;
import com.lp.service.ImagePathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SearchController {

    @Reference
    private GoodsService goodsService;

    @Reference
    private ImagePathService imagePathService;

    @RequestMapping("/search")
    public ModelAndView searchShow(@RequestParam(value = "page", defaultValue = "1") Integer page, String keyword) {

        PageHelper.startPage(page, 5);

        List<Goods> goodsList = goodsService.querySearchByName(keyword);

        if (goodsList.size() > 0 && goodsList != null) {

            for (Goods goods : goodsList) {

                List<ImagePath> imagePathList = imagePathService.queryImagePath(goods.getGoodsId());

                String goodsName = goods.getGoodsName();

                if (goods.getGoodsName().length() > 10) {

                    String substring = goods.getGoodsName().substring(0, 10);

                    goods.setGoodsName(substring);
                }

                goods.setImagePaths(imagePathList);
            }
        }

        PageInfo pageInfo = new PageInfo();

        pageInfo.setList(goodsList);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("pageInfo", pageInfo);

        modelAndView.setViewName("search");

        return modelAndView;
    }
}
