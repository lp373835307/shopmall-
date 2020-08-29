package controller.admin;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class CategoryController {

    @Reference
    CategoryService categoryService;

    @RequestMapping("/admin/goods/toAddCategoryView")
    public String addCategoryView() {

        return "addCategory";
    }

    @RequestMapping(value = "/admin/goods/addCategoryResult", method = RequestMethod.POST)
    public String addCategoryResult(String cateName) {

        boolean flag = categoryService.addCategoryResult(cateName);

        return "redirect:/admin/goods/show";
    }
}