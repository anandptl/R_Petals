package as.r_petals.controller;

import as.r_petals.entities.Shops;
import as.r_petals.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerShop(
            @RequestBody Shops shop) {

        Map<String, Object> response = shopService.registerShop(shop);

        if ((Boolean) response.get("success")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }
}
