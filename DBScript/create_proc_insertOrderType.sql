DROP procedure IF EXISTS `insertOrderType`;

DELIMITER $$
CREATE PROCEDURE `insertOrderType`(nameRU varchar(128), nameEN varchar(128))
BEGIN
    INSERT INTO ordertypes (`name_ru`, `name_en`) VALUES (nameRU, nameEN);

	SET @lastID = (SELECT LAST_INSERT_ID());

	SELECT id, name_ru, name_en	FROM ordertypes	WHERE id = @lastID limit 1;   
END$$

DELIMITER ;