/* creazione del database */
CREATE DATABASE IF NOT EXISTS `potlatch_test` DEFAULT CHARACTER SET latin1;


/* creazione utente applicativo*/
CREATE USER 'potlatch_test'@'localhost' IDENTIFIED BY 'potlatch';

GRANT ALL PRIVILEGES ON potlatch_test.* TO 'potlatch_test'@'localhost';