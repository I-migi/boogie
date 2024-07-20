package team3.boogie.test;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @PostMapping("/test")
    public ResponseEntity<Boolean> processForm(@RequestParam Map<String,String> formData){
        int checkedCount =0;
        boolean status= false;
        for(String key: formData.keySet()){
            if(formData.get(key) != null && formData.get(key).equals("on")){
                checkedCount++;
            }
        }
        if(checkedCount >= 4){
            status = true;
            return ResponseEntity.ok(status);
        }else{
            return ResponseEntity.ok(status);
        }
    }
}
