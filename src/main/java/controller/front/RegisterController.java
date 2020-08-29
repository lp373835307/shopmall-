package controller.front;


import com.alibaba.dubbo.config.annotation.Reference;
import com.lp.domain.User;
import com.lp.service.UserService;
import com.lp.util.MyMD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


@Controller
public class RegisterController {



    @Reference
    UserService userService;


    @RequestMapping("/registerView")
    public String registerView() {

        return "register";
    }

    @RequestMapping("/register")
    public ModelAndView Register(User user) {

        ModelAndView modelAndView = new ModelAndView();


        if (!user.getPassword().equals(user.getConfirmPassword())) {

            modelAndView.addObject("errorMsg", "两次密码不一致");

            modelAndView.setViewName("register");

            return modelAndView;

        }
        try {
            String encryptedPwd = MyMD5Util.getEncryptedPwd(user.getPassword());
            user.setPassword(encryptedPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String staus = userService.insertUser(user);

        modelAndView.addObject("errorMsg", staus);

        if ("注册成功".equals(staus)) {

            modelAndView.setViewName("login");

            return modelAndView;
        }

        modelAndView.setViewName("register");

        return modelAndView;
    }
}
