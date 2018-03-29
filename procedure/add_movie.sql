USE moviedb;
DROP procedure IF EXISTS add_movie;

DELIMITER $$

CREATE PROCEDURE add_movie(
					 	 	IN _title varchar(200), 
					 	 	IN _year int, 
					 	 	IN _director varchar(200), 
						 	IN star_name varchar(200),
					 		IN genre varchar(200),
							OUT output varchar(200) )
BEGIN
    
    DECLARE star_id varchar(200) default -1;
    DECLARE genre_id int default -1;
    DECLARE movie_id varchar(200) default -1;
    DECLARE maxstar varchar(10);
    DECLARE maxgenre varchar(10);
    DECLARE maxmovie varchar(10);
    DECLARE message  varchar(200) DEFAULT "";
    
    
    START TRANSACTION;
    
    -- query for star id 
    SELECT stars.id into star_id from stars where name = star_name limit 1;    
	-- query for genre id
	SELECT genres.id into genre_id from genres where name = genre limit 1;
    -- query for movie id
    SELECT movies.id into movie_id from movies where title = _title and year = _year and director = _director limit 1;
    
    
    -- Add Movie with New Star and New Genre
    IF (star_id = -1 and genre_id = -1 and movie_id = -1 )THEN        
        set maxstar = (select endId from maxid where tablename = 'stars');
        -- set maxstar = cast((SUBSTRING(maxstar,3,8)) as unsigned);
        set maxstar = maxstar +1;
        UPDATE `moviedb`.`maxid` SET `endId`= maxstar WHERE `tablename`='stars';
        SET star_id = concat("nm",cast(maxstar as char));
        -- update sidmax set maxid=star_id where num='1';
        insert into stars(id, name) values (star_id, star_name);
        SET message = "Add new star!";
        
        set maxgenre = (select endId from maxid where tablename = 'genres');
        set maxgenre = maxgenre +1; 
        UPDATE `moviedb`.`maxid` SET `endId`= maxgenre WHERE `tablename`='genres';
		INSERT INTO genres VALUES(maxgenre, genre);
		SET genre_id = maxgenre;
		SET message = "Add new genre!";
			        
        set maxmovie = (select endId from maxid where tablename = 'movies');
		set maxmovie = maxmovie +1;
        UPDATE `moviedb`.`maxid` SET `endId`= maxmovie WHERE `tablename`='movies';
		SET movie_id = concat("tt",cast(maxmovie as char));
		update midmax set maxid=movie_id where num='1';
		insert into movies(id, title, year, director)
		values (movie_id, _title, _year, _director);
		SET message = "Add new movie!";
        
		INSERT INTO genres_in_movies(genreId, movieId) VALUES(genre_id, movie_id);
        INSERT INTO stars_in_movies(starId, movieId) VALUES(star_id, movie_id);
        SET OUTPUT = 'New Star, New Genre and New Movie added successfully!';
    
-- Add Movie with Existing Genre and Star
	ELSEIF (genre_id != -1 and star_id != -1 and movie_id = -1) then
		set maxmovie = (select endId from maxid where tablename = 'movies');
		set maxmovie = maxmovie +1;
        UPDATE `moviedb`.`maxid` SET `endId`= maxmovie WHERE `tablename`='movies';
		SET movie_id = concat("tt",cast(maxmovie as char));
		update midmax set maxid=movie_id where num='1';
		insert into movies(id, title, year, director) values (movie_id, _title, _year, _director);
		INSERT INTO genres_in_movies(genreId, movieId) VALUES(genre_id, movie_id);
		INSERT INTO stars_in_movies(starId, movieId) VALUES(star_id, movie_id);
		SET OUTPUT = 'Movies added successfully. Genre and Star Already Exist!';

-- Movie Already Exist.
	ELSEIF (movie_id != -1) then					
		SET OUTPUT = 'Movies Already Exist! Cannot Add! ';

	ELSE 
		SET output='Invalid Input! Plese Check again!';

    END IF;
    
	COMMIT;
END
$$

DELIMITER ;