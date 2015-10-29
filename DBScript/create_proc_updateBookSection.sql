DROP PROCEDURE IF EXISTS updateBookSection;

delimiter //
CREATE PROCEDURE updateBookSection (bookSectionID int(11), nameRU varchar(128), nameEN varchar(128))
BEGIN
	UPDATE booksections SET `name_ru` = nameRU, `name_en` = nameEN
    WHERE `id` = bookSectionID;

    SELECT id, name_ru, name_en FROM booksections WHERE id = bookSectionID limit 1; 
   
END // 
delimiter ;
