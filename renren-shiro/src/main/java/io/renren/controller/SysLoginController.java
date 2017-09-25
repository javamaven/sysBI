package io.renren.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

import io.renren.utils.R;
import io.renren.utils.ShiroUtils;

/**
 * 登录相关
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 下午1:15:31
 */
@Controller
public class SysLoginController {
	@Autowired
	private Producer producer;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
	}
	
	/**
	 * 登录
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public R login(String username, String password, String captcha, HttpServletRequest request)throws IOException {
//		try {
//			String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
//			if(!captcha.equalsIgnoreCase(kaptcha)){
//				return R.error("验证码不正确");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		System.err.println("+++++++++++++登陆用户：" + username + " ,登录时间=" + sdf.format(new Date()));
		try{
			Subject subject = ShiroUtils.getSubject();
			//sha256加密
			password = new Sha256Hash(password).toHex();
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
		}catch (UnknownAccountException e) {
			System.err.println("+++++++UnknownAccountException+++++++" + e.getMessage());
			return R.error(e.getMessage());
		}catch (IncorrectCredentialsException e) {
			System.err.println("+++++++IncorrectCredentialsException+++++++" + e.getMessage());
			return R.error(e.getMessage());
		}catch (LockedAccountException e) {
			System.err.println("+++++++LockedAccountException+++++++" + e.getMessage());
			return R.error(e.getMessage());
		}catch (AuthenticationException e) {
			System.err.println("+++++++AuthenticationException+++++++" + "账户验证失败");
			return R.error("账户验证失败");
		}
	    
		try {
			HttpSession session = request.getSession();
			session.setAttribute("userName", username);
			session.setAttribute("loginTime", sdf.format(new Date()));
		} catch (Exception e) {
			System.err.println("++++++++HttpSession++++++++++" + e.getMessage());
			throw e;
		}
		return R.ok();
	}
	
	/**
	 * 退出
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		ShiroUtils.logout();
		return "redirect:login.html";
	}
	
}
