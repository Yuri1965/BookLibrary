INSERT INTO `booklibrary`.`user_roles` (`userID`,`roleID`) 
VALUES((SELECT id FROM users WHERE name_user = 'Yuri1965' limit 1), (SELECT id FROM roles WHERE roleName = 'EMPLOYEE' limit 1));

INSERT INTO `booklibrary`.`user_roles` (`userID`,`roleID`) 
VALUES((SELECT id FROM users WHERE name_user = 'Ivan' limit 1), (SELECT id FROM roles WHERE roleName = 'READER' limit 1));

