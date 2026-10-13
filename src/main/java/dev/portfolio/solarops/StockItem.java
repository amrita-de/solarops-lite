package dev.portfolio.solarops;
import jakarta.persistence.*;
@Entity @Table(name="stock_item") public class StockItem {
 @Id public Long id; public String name; public int available;
 protected StockItem() {} public StockItem(long id,String name,int available){this.id=id;this.name=name;this.available=available;}
}
