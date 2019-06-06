package com.pan1024.controller;

import com.pan1024.entity.BiliUser;
import com.pan1024.service.BiliInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName: BiliController
 * @Date: 2019/6/6
 * @describe:
 */
@Slf4j
@Controller
public class BiliController {

    @Autowired
    private BiliInfoService biliSpiderService;

    @GetMapping("/list")
    public String list(Model model){
        List<BiliUser> list = biliSpiderService.list();
        model.addAttribute("list",list);
        return "list";
    }

//    @GetMapping("/search")
//    public String search(@RequestParam(value="cardNumber") String cardNumber, @RequestParam(value="name")String name, Model model){
//        TabuaMember member;
//        String message;
//        List<TabuaMember> list =  new ArrayList<TabuaMember>();
//        if(cardNumber.isEmpty() && name.isEmpty()) {
//            message = "至少输入一个参数 Please entry at least one condition!";
//            model.addAttribute("message",message);
//        }else if(!cardNumber.isEmpty()){
//            member = repository.findByCardNumber(cardNumber.trim());
//            if(member!=null)
//                list.add(member);
//            model.addAttribute("list",list);
//        }else if(!name.isEmpty()){
//            list = repository.findWithPartOfName(name);
//            model.addAttribute("list",list);
//        }
//        return "list";
//    }

    @GetMapping("/start/next")
    public void start(@RequestParam(defaultValue = "100") Integer count){
        biliSpiderService.start(count);
    }

    @GetMapping("/stop")
    public void stop(){
        biliSpiderService.stop();
    }

}
