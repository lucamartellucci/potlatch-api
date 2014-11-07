/* creazione del database */
CREATE DATABASE IF NOT EXISTS `potlatch` DEFAULT CHARACTER SET latin1;

/* creazione utente applicativo*/
CREATE USER 'potlatch'@'localhost' IDENTIFIED BY 'potlatch';
GRANT SELECT, INSERT, UPDATE, DELETE, LOCK TABLES, CREATE TEMPORARY TABLES ON potlatch.* TO 'potlatch'@'localhost';

CREATE USER 'potlatch'@'%' IDENTIFIED BY 'potlatch';
GRANT SELECT, INSERT, UPDATE, DELETE, LOCK TABLES, CREATE TEMPORARY TABLES ON potlatch.* TO 'potlatch'@'%';

