DROP PROCEDURE IF EXISTS `booklibrary`.`updatePublisher`;

delimiter //
CREATE PROCEDURE `booklibrary`.`updatePublisher` (publisherID int(11), nameRU varchar(128), nameEN varchar(128))
BEGIN
	UPDATE `booklibrary`.`publishers` SET `name_ru` = nameRU, `name_en` = nameEN
    WHERE `id` = publisherID;

    SELECT id, name_ru, name_en FROM publishers WHERE id = publisherID limit 1; 
   
END // 
delimiter ;
