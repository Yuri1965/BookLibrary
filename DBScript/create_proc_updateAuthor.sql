DROP PROCEDURE IF EXISTS `booklibrary`.`updateAuthor`;

delimiter //
CREATE PROCEDURE `booklibrary`.`updateAuthor` (authorID int(11), fioRU varchar(256), fioEN varchar(256), birthdate date)
BEGIN
	UPDATE `booklibrary`.`authors` SET `fio_ru` = fioRU, `fio_en` = fioEN, `birhtday` = birthdate
    WHERE `id` = authorID;

    SELECT id, fio_ru, fio_en, birhtday FROM authors WHERE id =  authorID limit 1; 
   
END // 
delimiter ;
