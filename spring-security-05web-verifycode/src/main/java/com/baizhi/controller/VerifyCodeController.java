package com.baizhi.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.code.kaptcha.Producer;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
public class VerifyCodeController {
    private final Producer producer;
    @Autowired
    public VerifyCodeController(Producer producer) {
        this.producer = producer;
    }

    @RequestMapping("/vc.jpg")
    public void verifyCode(HttpServletResponse response, HttpSession session) throws IOException {
    	
    	//1.生成验证码
        String verifyCode = producer.createText();
        //2.保存到中 session
        session.setAttribute("kaptcha", verifyCode);
        //3.生成图片
        BufferedImage bi = producer.createImage(verifyCode);
        //4.响应图片
        response.setContentType("image/png");
        ServletOutputStream os = response.getOutputStream();
        ImageIO.write(bi, "jpg", os);
    }
}
