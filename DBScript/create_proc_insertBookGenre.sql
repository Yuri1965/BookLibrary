DROP procedure IF EXISTS `insertBookGenre`;

DELIMITER $$
CREATE PROCEDURE `insertBookGenre`(nameRU varchar(128), nameEN varchar(128), bookSectID int(11))
BEGIN
    INSERT INTO bookgenres (`name_ru`, `name_en`, `bookSectionID`) VALUES (nameRU, nameEN, bookSectID);

	SET @lastID = (SELECT LAST_INSERT_ID());

	SELECT bookgenres.id, bookgenres.name_ru, bookgenres.name_en, bookgenres.bookSectionID, 
		booksections.name_ru AS bookSectionNameRU, booksections.name_en AS bookSectionNameEN
	FROM bookgenres
	LEFT JOIN booksections ON bookgenres.bookSectionID = booksections.id
	WHERE bookgenres.id = @lastID limit 1;   
END$$

DELIMITER ;