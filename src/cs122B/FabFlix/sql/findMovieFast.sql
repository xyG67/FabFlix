select Idmain, title, year, director, rating, group_concat(distinct genres.name separator '; ') as gener, group_concat(distinct stars.name separator'; ') as star
from (select movies.id as Idmain, movies.title, movies.year, movies.director, r.rating
from movies , (select movieId, rating 
from ratings)as r
where movies.id = r.movieId
order by r.rating desc
limit 0, 20) as tempmovies, genres_in_movies, genres, stars_in_movies, stars
where genres_in_movies.movieId = Idmain and genres.id = genres_in_movies.genreId and stars_in_movies.movieId = Idmain and stars.id = stars_in_movies.starId
group by  Idmain, title, year, director, rating
order by rating desc;
