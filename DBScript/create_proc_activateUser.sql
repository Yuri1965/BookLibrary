DROP PROCEDURE IF EXISTS `booklibrary`.`activateUser`;

delimiter //
CREATE PROCEDURE `booklibrary`.`activateUser` (activCode varchar(50))
BEGIN
    SET @activationDate = now();
    SELECT id FROM users WHERE activationCode = activCode limit 1 INTO @userID;
    
    if @userID > 0 then
		begin
			UPDATE `booklibrary`.`users` SET isActivated = 1, activationDate = @activationDate 
			WHERE id = @userID and isActivated = 0;
            
            SELECT id, uuid_user, name_user, pass_user, email_user, reg_Date, firstName_user, lastName_user,
				isActivated, activationCode, activationDate, isBlocked, blockedDescription
			FROM `booklibrary`.`users` WHERE id = @userID limit 1; 
		end;
	else
		begin
			SELECT 0 AS id, '' AS uuid_user, '' AS name_user, '' AS pass_user, '' AS email_user, 
				null AS reg_Date, '' AS firstName_user, '' AS lastName_user,
				0 AS isActivated, '' AS activationCode, null AS activationDate, 0 AS isBlocked, '' AS blockedDescription;
        end;
	end if;
   
END // 
delimiter ;
