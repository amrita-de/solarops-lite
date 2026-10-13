package dev.portfolio.solarops;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api") public class InventoryController {
 private final StockRepository stocks; private final InventoryService service;
 public InventoryController(StockRepository s,InventoryService i){stocks=s;service=i;}
 public record Request(@Positive long itemId,@Min(1) @Max(100000) int quantity,@NotBlank @Size(max=80) String requestKey){}
 @GetMapping("/csrf") public java.util.Map<String,String> csrf(org.springframework.security.web.csrf.CsrfToken token){return java.util.Map.of("token",token.getToken(),"header",token.getHeaderName());}
 @GetMapping("/inventory") public List<StockItem> inventory(){return stocks.findAll(org.springframework.data.domain.Sort.by("id"));}
 @PostMapping("/reservations") public Reservation reserve(@Valid @RequestBody Request r){return service.reserve(r.itemId,r.quantity,r.requestKey);}
}
