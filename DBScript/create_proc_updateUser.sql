DROP PROCEDURE IF EXISTS `booklibrary`.`updateUser`;

delimiter //
CREATE PROCEDURE `booklibrary`.`updateUser` (userID int(11), userPass varchar(256), emailUser varchar(255), 
	firstNameUser varchar(128), lastNameUser varchar(128))
BEGIN
	SET @userUUID = '';
	SET @userID = 0;
	SET @userEncryptPass = '';

    SELECT id, uuid_user FROM users where id = userID limit 1 INTO @userID, @userUUID;
    
    if @userID > 0 then
		begin
			SET @userEncryptPass = SHA2(concat(@userUUID, '_', userPass), 256);
            
            UPDATE users SET pass_user = @userEncryptPass, email_user = emailUser, 
				firstName_user = emailUser, lastName_user = lastNameUser WHERE id = @userID;
            
            SELECT id, uuid_user, name_user, pass_user, email_user, reg_Date, firstName_user, lastName_user
                FROM users where id = @userID limit 1; 
		end;
	else
		begin
			SELECT 0 AS id, '' AS uuid_user, '' AS name_user, '' AS pass_user, '' AS email_user, 
				null AS reg_Date, '' AS firstName_user, '' AS lastName_user;
        end;
	end if;
   
END // 
delimiter ;

call updateUser(4, '1234', 'ura747@mail.ru', 'Иван', 'Петров');

call updateUser(4, '123', 'ura747@mail.ru', 'Иван', 'Петров')

 