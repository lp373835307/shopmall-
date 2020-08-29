package controller.front;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lp.domain.User;
import com.lp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController {

    private static final String AAA = "login";

    @Reference
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping("/login")
    public String login() {


        return "login";
    }

    @RequestMapping("/loginConfirm")
    public ModelAndView longin(User user, String verifyCode, HttpSession session) {

        ModelAndView modelAndView = new ModelAndView();
        String failCount = (String) redisTemplate.opsForValue().get(AAA + user.getUserName());
        if (!StringUtils.isEmpty(failCount) && Integer.parseInt(failCount) >= 5) {
            modelAndView.addObject("errorMsg", "登录超限制，1分钟后再试");
            modelAndView.setViewName("login");
            return modelAndView;
        }

        String certCode = (String) session.getAttribute("certCode");

        if (!certCode.equals(verifyCode)) {
            modelAndView.addObject("验证码错误");
            modelAndView.setViewName("login");
            return modelAndView;
        }

        String staus = userService.loginUser(user);
        modelAndView.addObject("errorMsg", staus);

        if ("200".equals(staus)) {
            session.setAttribute("user", user);
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        incrLogin(user);
        modelAndView.setViewName("login");

        return modelAndView;
    }

    @RequestMapping("/logout")
    public ModelAndView logout(HttpSession session) {

        session.removeAttribute("user");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("redirect:/");

        return modelAndView;
    }

    private void incrLogin(User user) {
        String key = AAA + user.getUserName();

        String failCount = (String) redisTemplate.opsForValue().get(key);

        if (StringUtils.isEmpty(failCount)) {
            redisTemplate.opsForValue().set(key, "1");
        } else {
            redisTemplate.opsForValue().increment(key);
        }
        redisTemplate.expire(key, 180L, TimeUnit.SECONDS);
    }
}
