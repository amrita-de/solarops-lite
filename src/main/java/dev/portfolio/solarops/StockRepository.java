package dev.portfolio.solarops;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface StockRepository extends JpaRepository<StockItem,Long> {
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select s from StockItem s where s.id = :id") Optional<StockItem> lock(@Param("id") long id);
}
