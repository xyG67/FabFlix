USE moviedb;
DROP procedure IF EXISTS add_cast124_fast;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `add_cast124_fast`(
						 	IN _title varchar(200),
                            IN star_name varchar(200) )
BEGIN
    
    DECLARE movie_id varchar(200) default -1;
    DECLARE star_id varchar(200) default -1;
    DECLARE maxstar varchar(10);
    DECLARE maxmovie varchar(10);
    DECLARE message  varchar(200) DEFAULT "";
    
    START TRANSACTION;
    
    -- query for star id 
    SELECT stars.id into star_id from stars where name = star_name limit 1;
    SELECT movies.id into movie_id from movies where title = _title limit 1;
    
    -- if star_id and movie cannot be found then create a star entry
    IF movie_id = -1 and star_id = -1 then
		set maxmovie = (select endId from maxid where tablename = 'movies');
		-- set maxmovie = cast((SUBSTRING(maxmovie,3,8)) as unsigned);
		set maxmovie = maxmovie +1;
        UPDATE `moviedb`.`maxid` SET `endId`= maxmovie WHERE `tablename`='movies';
		SET movie_id = concat("tt",cast(maxmovie as char));
		insert into movies(id, title, year, director) values (movie_id, _title, 0, 'NULL');
		SET message = "Add new movie!";
        
        set maxstar = (select endId from maxid where tablename = 'stars');
        -- set maxstar = cast((SUBSTRING(maxstar,3,7)) as unsigned);
        set maxstar = maxstar +1;
        UPDATE `moviedb`.`maxid` SET `endId`= maxstar WHERE `tablename`='stars';
        SET star_id = concat("nm",cast(maxstar as char));
        insert into stars(id, name) values (star_id, star_name);
        insert into stars_in_movies values (star_id, movie_id);
        SET message = "Add new star!";
        
        INSERT INTO stars_in_movies(starId, movieId) VALUES(star_id, movie_id);
        
		set message = "Movie and star does not exist!";
        
	ELSEIF movie_id !=-1 and star_id = -1 THEN
        set maxmovie = (select endId from maxid where tablename = 'movies');
		-- set maxmovie = cast((SUBSTRING(maxmovie,3,8)) as unsigned);
		SET movie_id = concat("tt",cast(maxmovie as char));
        
		set maxstar = (select endId from maxid where tablename = 'stars');
        -- set maxstar = cast((SUBSTRING(maxstar,3,7)) as unsigned);
        set maxstar = maxstar +1;
        UPDATE `moviedb`.`maxid` SET `endId`= maxstar WHERE `tablename`='stars';
        SET star_id = concat("nm",cast(maxstar as char));
        insert into stars(id, name) values (star_id, star_name);
        insert into stars_in_movies values (star_id, movie_id);
        
        INSERT INTO stars_in_movies(starId, movieId) VALUES(star_id, movie_id);
        
	ELSEIF movie_id = -1 and star_id != -1 then
		set maxmovie = (select endId from maxid where tablename = 'movies');
		-- set maxmovie = cast((SUBSTRING(maxmovie,3,8)) as unsigned);
		set maxmovie = maxmovie +1;
        UPDATE `moviedb`.`maxid` SET `endId`= maxmovie WHERE `tablename`='movies';
		SET movie_id = concat("tt",cast(maxmovie as char));
		insert into movies(id, title, year, director) values (movie_id, _title, 0, 'NULL');
		SET message = "Add new movie!";
        
        set maxstar = (select endId from maxid where tablename = 'stars');
        -- set maxstar = cast((SUBSTRING(maxstar,3,7)) as unsigned);
        SET star_id = concat("nm",cast(maxstar as char));
        
        INSERT INTO stars_in_movies(starId, movieId) VALUES(star_id, movie_id);
        
		set message = "Movie does not exist!";
    
	ELSE
		set message = "exists!";
    END IF;
    
    --    INSERT INTO genres_in_movies(genreId, movieId) VALUES(genre_id, movie_id);
    --    INSERT INTO stars_in_movies(starId, movieId) VALUES(star_id, movie_id);
	--    SET OUTPUT = 'Movies successfully inserted';
    
    COMMIT;
    
END

$$
DELIMITER ;