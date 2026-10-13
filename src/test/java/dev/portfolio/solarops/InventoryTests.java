package dev.portfolio.solarops;
import java.util.concurrent.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest class InventoryTests {
 @Autowired InventoryService service; @Autowired StockRepository stocks; @Autowired ReservationRepository reservations;
 @BeforeEach void reset(){reservations.deleteAll();stocks.deleteAll();stocks.save(new StockItem(1,"Panel",10));stocks.save(new StockItem(2,"Inverter",10));}
 @Test void reserve(){service.reserve(1,3,"a");assertEquals(7,stocks.findById(1L).get().available);}
 @Test void replay(){service.reserve(1,3,"a");service.reserve(1,3,"a");assertEquals(7,stocks.findById(1L).get().available);assertEquals(1,reservations.count());}
 @Test void conflictingQuantity(){service.reserve(1,3,"a");assertThrows(ResponseStatusException.class,()->service.reserve(1,4,"a"));assertEquals(7,stocks.findById(1L).get().available);}
 @Test void conflictingItem(){service.reserve(1,3,"a");assertThrows(ResponseStatusException.class,()->service.reserve(2,3,"a"));assertEquals(10,stocks.findById(2L).get().available);}
 @Test void overAllocation(){assertThrows(ResponseStatusException.class,()->service.reserve(1,11,"a"));assertEquals(10,stocks.findById(1L).get().available);assertEquals(0,reservations.count());}
 @Test void invalid(){assertThrows(ResponseStatusException.class,()->service.reserve(1,0,"a"));assertThrows(ResponseStatusException.class,()->service.reserve(1,1,""));}
 @Test void missing(){assertThrows(ResponseStatusException.class,()->service.reserve(9,1,"a"));}
 @Test void concurrentStockSafety()throws Exception{var pool=Executors.newFixedThreadPool(8);try{List<Callable<Boolean>> tasks=new ArrayList<>();for(int i=0;i<20;i++){final int n=i;tasks.add(()->{try{service.reserve(1,1,"key"+n);return true;}catch(ResponseStatusException e){return false;}});}long successes=0;for(var f:pool.invokeAll(tasks))if(f.get())successes++;assertEquals(10,successes);assertEquals(0,stocks.findById(1L).get().available);assertEquals(10,reservations.count());}finally{pool.shutdownNow();}}
 @Test void concurrentReplay()throws Exception{var pool=Executors.newFixedThreadPool(4);try{List<Callable<Reservation>> tasks=new ArrayList<>();for(int i=0;i<8;i++)tasks.add(()->service.reserve(1,2,"same"));for(var f:pool.invokeAll(tasks))assertEquals(2,f.get().quantity);assertEquals(8,stocks.findById(1L).get().available);assertEquals(1,reservations.count());}finally{pool.shutdownNow();}}
}
