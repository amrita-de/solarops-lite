package dev.portfolio.solarops;
import jakarta.persistence.*;
@Entity public class Reservation {
 @Id @Column(name="request_key") public String requestKey;
 @Column(name="item_id") public Long itemId; public int quantity;
 protected Reservation() {} public Reservation(String key,long item,int quantity){this.requestKey=key;this.itemId=item;this.quantity=quantity;}
}
