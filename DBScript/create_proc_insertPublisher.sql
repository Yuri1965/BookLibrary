DROP procedure IF EXISTS `insertPublisher`;

DELIMITER $$
CREATE PROCEDURE `insertPublisher`(nameRU varchar(128), nameEN varchar(128))
BEGIN
    INSERT INTO publishers (`name_ru`, `name_en`) VALUES (nameRU, nameEN);

	SET @lastID = (SELECT LAST_INSERT_ID());

    SELECT id, name_ru, name_en FROM publishers WHERE id =  @lastID limit 1; 
END$$

DELIMITER ;