USE moviedb;
-- DROP procedure IF EXISTS add_movie;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `add_main243`(
					 	 	IN _title varchar(200), 
					 	 	IN _year int, 
					 	 	IN _director varchar(200), 
					 		IN genre varchar(200),
							OUT output varchar(200) )
BEGIN
    
    DECLARE genre_id varchar(200) default -1;
    DECLARE movie_id varchar(200) default -1;
    DECLARE maxgenre varchar(10);
    DECLARE maxmovie varchar(10);
    DECLARE message  varchar(200) DEFAULT "";
    
    
    START TRANSACTION;
    
    -- query for star id 
	-- query for genre id
	SELECT genres.id into genre_id from genres where name = genre limit 1;
    -- query for movie id
    SELECT movies.id into movie_id from movies where title = _title and year = _year and director = _director limit 1;
    
    -- Add Movie with New Star and New Genre
    IF (genre_id = -1 and movie_id = -1 )THEN        
        set maxgenre = (select max(id) from genres);
        set maxgenre = maxgenre +1; 
		INSERT INTO genres VALUES(maxgenre, genre);
		SET genre_id = maxgenre;
		SET message = "Add new genre!";
			
		set maxmovie = (select max(id) from movies);
		set maxmovie = cast((SUBSTRING(maxmovie,3,8)) as unsigned);
		set maxmovie = maxmovie +1;
		SET movie_id = concat("tt",cast(maxmovie as char));
		insert into movies(id, title, year, director)
		values (movie_id, _title, _year, _director);
		SET message = "Add new movie!";
        
		INSERT INTO genres_in_movies(genreId, movieId) VALUES(genre_id, movie_id);
        SET OUTPUT = 'New Genre and New Movie added successfully!';

-- Add Movie with Existing Genre and Star
	ELSEIF (genre_id != -1 and movie_id = -1) then
			set maxmovie = (select max(id) from movies);
			set maxmovie = cast((SUBSTRING(maxmovie,3,8)) as unsigned);
			set maxmovie = maxmovie +1;
			SET movie_id = concat("tt",cast(maxmovie as char));
            insert into movies(id, title, year, director)
 			values (movie_id, _title, _year, _director);
			INSERT INTO genres_in_movies(genreId, movieId) VALUES(genre_id, movie_id);
			SET OUTPUT = 'Movies added successfully. Genre and Star Already Exist!';
            
  --       -- Movie Already Exist.
    ELSEIF (movie_id != -1 and genre_id = -1) then
			set maxgenre = (select max(id) from genres);
            set maxgenre = maxgenre +1; 
  
		    INSERT INTO genres VALUES(maxgenre, genre);
			SET genre_id = maxgenre;
			SET message = "Add new genre!";
            
            INSERT INTO genres_in_movies(genreId, movieId) VALUES(genre_id, movie_id);
			SET OUTPUT = 'New Genre added successfully! Movie Exist!';
  
	ELSE
			SET OUTPUT = 'Movies Already Exist! Cannot Add! ';
-- 						-- END IF;
				 	-- ELSE SET output='Invalid Input! Plese Check again!';
    END IF;
    
    COMMIT;
    
END
$$

DELIMITER ;