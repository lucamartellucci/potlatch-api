SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `potlatch` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `potlatch` ;

-- -----------------------------------------------------
-- Table `potlatch`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `potlatch`.`user` ;

CREATE TABLE IF NOT EXISTS `potlatch`.`user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `birthdate` DATE NULL,
  `gender` CHAR NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = 'Represents the user of the potlatch application';


-- -----------------------------------------------------
-- Table `potlatch`.`gift`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `potlatch`.`gift` ;

CREATE TABLE IF NOT EXISTS `potlatch`.`gift` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `uuid` VARCHAR(45) NULL,
  `title` VARCHAR(45) NULL,
  `description` VARCHAR(300) NULL,
  `timestamp` TIMESTAMP NULL,
  `parent_id` BIGINT NULL,
  `uri` VARCHAR(300) NULL,
  `user_id` BIGINT NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `number_of_likes` VARCHAR(45) NULL,  
  PRIMARY KEY (`id`),
  INDEX `fk_gift_1_idx` (`user_id` ASC),
  CONSTRAINT `fk_gift_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `potlatch`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'A gift is the base unit of the potlatch application. It represents a photo with a title and an optional description';


-- -----------------------------------------------------
-- Table `potlatch`.`user_action`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `potlatch`.`user_action` ;

CREATE TABLE IF NOT EXISTS `potlatch`.`user_action` (
  `user_id` BIGINT NOT NULL,
  `gift_id` BIGINT NOT NULL,
  `i_like_it` TINYINT(1) NULL,
  `inappropriate` TINYINT(1) NULL,
  PRIMARY KEY (`user_id`, `gift_id`),
  INDEX `fk_like_1_idx` (`gift_id` ASC),
  CONSTRAINT `fk_like_gift`
    FOREIGN KEY (`gift_id`)
    REFERENCES `potlatch`.`gift` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_like_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `potlatch`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'Contains the user likes and inappropriate flags about gifts';


-- -----------------------------------------------------
-- Table `potlatch`.`tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `potlatch`.`tag` ;

CREATE TABLE IF NOT EXISTS `potlatch`.`tag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tag` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `potlatch`.`tag_gift_rel`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `potlatch`.`tag_gift_rel` ;

CREATE TABLE IF NOT EXISTS `potlatch`.`tag_gift_rel` (
  `tag_id` BIGINT NOT NULL,
  `gift_id` BIGINT NOT NULL,
  PRIMARY KEY (`tag_id`, `gift_id`),
  INDEX `fk_tag_gift_rel_1_idx` (`gift_id` ASC),
  CONSTRAINT `fk_tag_gift_rel_1`
    FOREIGN KEY (`gift_id`)
    REFERENCES `potlatch`.`gift` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tag_gift_rel_2`
    FOREIGN KEY (`tag_id`)
    REFERENCES `potlatch`.`tag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `potlatch` ;

-- -----------------------------------------------------
-- Placeholder table for view `potlatch`.`likes_by_gifts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `potlatch`.`likes_by_gifts` (`gift_id` INT, `user_id` INT, `number_of_likes` INT);

-- -----------------------------------------------------
-- View `potlatch`.`likes_by_gifts`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `potlatch`.`likes_by_gifts` ;
DROP TABLE IF EXISTS `potlatch`.`likes_by_gifts`;
USE `potlatch`;
CREATE  OR REPLACE VIEW `likes_by_gifts` AS SELECT g.id as gift_id, g.user_id, sum(ua.i_like_it) as number_of_likes FROM potlatch.user_action ua 
inner join potlatch.gift g on ua.gift_id = g.id where ua.i_like_it = 1 group by g.id;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
