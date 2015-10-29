DROP PROCEDURE IF EXISTS updateBookGenre;

delimiter //
CREATE PROCEDURE updateBookGenre (bookGenreID int(11), nameRU varchar(128), nameEN varchar(128), bookSectID int(11))
BEGIN
	UPDATE bookgenres SET `name_ru` = nameRU, `name_en` = nameEN, bookSectionID = bookSectID
    WHERE id = bookGenreID;

	SELECT bookgenres.id, bookgenres.name_ru, bookgenres.name_en, bookgenres.bookSectionID, 
		booksections.name_ru AS bookSectionNameRU, booksections.name_en AS bookSectionNameEN
	FROM bookgenres
	LEFT JOIN booksections ON bookgenres.bookSectionID = booksections.id
	WHERE bookgenres.id = bookGenreID limit 1;   
END // 
delimiter ;
