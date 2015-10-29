DROP procedure IF EXISTS `insertBookSection`;

DELIMITER $$
CREATE PROCEDURE `insertBookSection`(nameRU varchar(128), nameEN varchar(128))
BEGIN
    INSERT INTO booksections (`name_ru`, `name_en`) VALUES (nameRU, nameEN);

	SET @lastID = (SELECT LAST_INSERT_ID());

    SELECT id, name_ru, name_en FROM booksections WHERE id =  @lastID limit 1; 
END$$

DELIMITER ;