USE moviedb;
DROP procedure IF EXISTS add_actors63;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `add_actors63`(
						 	IN star_name varchar(200),
                            IN birth_year int unsigned)
BEGIN
    
    DECLARE star_id varchar(200) default -1;
    DECLARE maxstar varchar(10);
    DECLARE message  varchar(200) DEFAULT "";
    
    START TRANSACTION;
    
    -- query for star id 
    SELECT stars.id into star_id from stars where name = star_name and birthYear = birth_year limit 1;
    
    -- if star_id cannot be found then create a star entry
    IF star_id = -1 THEN        
        set maxstar = (select endId from maxid where tablename = 'stars');
        -- set maxstar = cast((SUBSTRING(maxstar,3,7)) as unsigned);
        set maxstar = maxstar +1;
        UPDATE `moviedb`.`maxid` SET `endId`= maxstar WHERE `tablename`='stars';
        SET star_id = concat("nm",cast(maxstar as char));
        insert into stars(id, name, birthYear)
        values (star_id, star_name, birth_year);
        SET message = "Add new star!";
	else
		set message = "Star Duplicated!";
    END IF;
    --    INSERT INTO genres_in_movies(genreId, movieId) VALUES(genre_id, movie_id);
    --    INSERT INTO stars_in_movies(starId, movieId) VALUES(star_id, movie_id);
	--    SET OUTPUT = 'Movies successfully inserted';
    
    COMMIT;
    
END
$$

DELIMITER ;