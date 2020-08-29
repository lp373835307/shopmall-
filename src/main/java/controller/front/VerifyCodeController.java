package controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: ruwenbo
 * @Date: 2020/8/8 15:27
 * @Description:
 */
@Controller
public class VerifyCodeController {

    @RequestMapping("/getVerifyCodeImg")
    public String showGetVerityCodeView() {

        return "verificationcodeimg";
    }
}
