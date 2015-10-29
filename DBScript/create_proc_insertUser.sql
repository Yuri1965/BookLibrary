DROP PROCEDURE IF EXISTS `booklibrary`.`insertAuthor`;

delimiter //
CREATE PROCEDURE `booklibrary`.`insertAuthor` (fioRU varchar(256), fioEN varchar(256), birthdate date)
BEGIN

	INSERT INTO `booklibrary`.`authors`	(`fio_ru`,`fio_en`,`birhtday`) VALUES (fioRU, fioEN, birthdate);

	SET @lastID = (SELECT LAST_INSERT_ID());

    SELECT id, fio_ru,fio_en, birhtday FROM authors WHERE id =  @lastID limit 1; 
   
END // 
delimiter ;
