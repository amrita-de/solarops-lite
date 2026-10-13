package dev.portfolio.solarops;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.*;
@Service public class InventoryService {
 private final StockRepository stocks; private final ReservationRepository reservations;
 public InventoryService(StockRepository s,ReservationRepository r){stocks=s;reservations=r;}
 @Transactional public Reservation reserve(long item,int quantity,String key){
  if(quantity<1 || key==null || key.isBlank() || key.length()>80) throw new ResponseStatusException(BAD_REQUEST,"Invalid reservation");
  // Item lock serializes competing allocations. An idempotency key belongs to one item only.
  var stock=stocks.lock(item).orElseThrow(()->new ResponseStatusException(NOT_FOUND,"Item not found"));
  var prior=reservations.findById(key);
  if(prior.isPresent()) {
   var p=prior.get();
   if(p.itemId!=item || p.quantity!=quantity) throw new ResponseStatusException(CONFLICT,"Key already used for a different request");
   return p;
  }
  if(stock.available<quantity) throw new ResponseStatusException(CONFLICT,"Insufficient stock");
  stock.available-=quantity;
  return reservations.saveAndFlush(new Reservation(key,item,quantity));
 }
}
