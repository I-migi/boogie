package team3.boogie.Test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class TestController {

    @PostMapping("/processForm")
    public String processForm(@RequestParam Map<String,String> formData){
        int checkedCount =0;
        for(String key: formData.keySet()){
            if(formData.get(key) != null && formData.get(key).equals("on")){
                checkedCount++;
            }
        }
        if(checkedCount >= 3){
            return "redirect:/page1";
        }else{
            return "redirect:/page2";
        }
    }
}
