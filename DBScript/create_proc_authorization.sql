DROP PROCEDURE IF EXISTS `booklibrary`.`authorization`;

delimiter //
CREATE PROCEDURE `booklibrary`.`authorization` (userName varchar(50), userPass varchar(50))
BEGIN

	SET @userID = 0;
	SET @userOriginalPass = '';

    SELECT id, pass_user FROM users where name_user = userName limit 1 INTO @userID, @userOriginalPass;
    
    if @userID > 0 then
		begin
			SET @userPass = '';
			SELECT SHA2(concat(uuid_user, '_', userPass), 256) FROM users where id = @userID limit 1 INTO @userPass;

            if @userOriginalPass = @userPass then
				begin
				SELECT id, uuid_user, name_user, pass_user, email_user, reg_Date, firstName_user, lastName_user
                FROM users where id = @userID limit 1;
				end;
            else
				begin
				SELECT 0 AS id, '' AS uuid_user, '' AS name_user, '' AS pass_user, '' AS email_user, 
					null AS reg_Date, '' AS firstName_user, '' AS lastName_user;
                end;
			end if;
		end;
	else
		begin
			SELECT 0 AS id, '' AS uuid_user, '' AS name_user, '' AS pass_user, '' AS email_user, 
				null AS reg_Date, '' AS firstName_user, '' AS lastName_user;
        end;
	end if;
   
END // 
delimiter ;

call authorization('Ivan', '123')


