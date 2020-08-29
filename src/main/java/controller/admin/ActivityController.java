package controller.admin;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.lp.domain.Acativity;
import com.lp.domain.RseponseResult;
import com.lp.service.AcativityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {


    @Reference
    AcativityService acativityService;

    private Object obj = new Object();


    @RequestMapping(value = "/admin/activity/showjson", method = RequestMethod.POST)
    @ResponseBody
    public RseponseResult queryActivity() {



        List<Acativity> acativities = acativityService.queryActivity();

        Map<String, Object> info = new HashMap<String, Object>();

        info.put("activity", acativities);



        return RseponseResult.success(info);
    }

    @RequestMapping(value = "/admin/activity/update/", method = RequestMethod.POST)
    @ResponseBody
    public RseponseResult updateActivity(int activityId, int goodsId) {

        boolean flag = acativityService.updateActivity(activityId, goodsId);

        if (flag) {
            return RseponseResult.success("成功", null);
        }
        return RseponseResult.fail("失败", null);
    }

    @RequestMapping("/admin/activity/show")
    public ModelAndView queryActivity(@RequestParam(defaultValue = "1") int page) {

        PageHelper.startPage(page, 5);

        List<Acativity> acativities = acativityService.queryActivity();

        PageInfo pageInfo = new PageInfo(acativities);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("pageInfo", pageInfo);

        modelAndView.setViewName("activity");

        return modelAndView;

    }

    @RequestMapping("/admin/activity/delete")
    public String ActivityDelete(int activityId) {

        boolean flag = acativityService.ActivityDelete(activityId);

        return "redirect:/admin/activity/show";
    }

    @RequestMapping("/admin/activity/add")
    public ModelAndView ActivityAdd() {

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("addActivity");

        return modelAndView;
    }

    @RequestMapping("/admin/activity/addResult")
    public ModelAndView ActivityAddAddResult(Acativity acativity) {

        acativityService.ActivityAddAddResult(acativity);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("redirect:/admin/activity/show");

        return modelAndView;
    }
}
