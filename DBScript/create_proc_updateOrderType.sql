DROP PROCEDURE IF EXISTS updateOrderType;

delimiter //
CREATE PROCEDURE updateOrderType (orderTypeID int(11), nameRU varchar(128), nameEN varchar(128))
BEGIN
	UPDATE ordertypes SET `name_ru` = nameRU, `name_en` = nameEN
    WHERE id = orderTypeID;

	SELECT id, name_ru, name_en	FROM ordertypes	WHERE id = orderTypeID limit 1;   
END // 
delimiter ;
