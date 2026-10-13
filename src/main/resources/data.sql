INSERT INTO stock_item(id,name,available) SELECT 1,'Solar panel 550W',40 WHERE NOT EXISTS(SELECT 1 FROM stock_item WHERE id=1);
INSERT INTO stock_item(id,name,available) SELECT 2,'Hybrid inverter 5kW',12 WHERE NOT EXISTS(SELECT 1 FROM stock_item WHERE id=2);
INSERT INTO stock_item(id,name,available) SELECT 3,'Mounting rail kit',60 WHERE NOT EXISTS(SELECT 1 FROM stock_item WHERE id=3);
