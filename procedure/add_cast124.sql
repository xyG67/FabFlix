USE moviedb;
-- DROP procedure IF EXISTS add_movie;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `add_cast124`(
						 	IN _title varchar(200),
                            IN star_name varchar(200), 
							OUT output varchar(200) )
BEGIN
    
    DECLARE movie_id varchar(200) default -1;
    DECLARE star_id varchar(200) default -1;
    DECLARE maxstar varchar(10);
    DECLARE message  varchar(200) DEFAULT "";
    
    
    START TRANSACTION;
    
    -- query for star id 
    SELECT stars.id into star_id from stars where name = star_name limit 1;
    SELECT movies.id into movie_id from movies where title = _title limit 1;
    
    -- if star_id and movie cannot be found then create a star entry
    IF movie_id != -1 and star_id != -1 THEN
		 SET OUTPUT = "exists!";
    
    ELSEIF star_id = -1 and movie_id != -1 THEN        
        set maxstar = (select max(id) from stars);
		set maxstar = cast((SUBSTRING(maxstar,3,7)) as unsigned);
        set maxstar = maxstar +1;
        SET star_id = concat("nm",cast(maxstar as char));
        insert into stars(id, name) values (star_id, star_name);
        insert into stars_in_movies values (star_id, movie_id);
        SET OUTPUT = "Add new star!";
	ELSE
		set OUTPUT = "Movie does not exist!";
    END IF;
    
    --    INSERT INTO genres_in_movies(genreId, movieId) VALUES(genre_id, movie_id);
    --    INSERT INTO stars_in_movies(starId, movieId) VALUES(star_id, movie_id);
	--    SET OUTPUT = 'Movies successfully inserted';
    
    COMMIT;
    
END

$$
DELIMITER ;