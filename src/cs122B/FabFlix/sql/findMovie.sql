select movies.title, movies.year, movies.director, r.rating, t.genres, h.star
from movies , (select movieId, rating 
from ratings)as r, (select movies.title, group_concat(r.name separator '-') as genres from movies, (select * from genres, genres_in_movies where genres.id = genres_in_movies.genreId) as r
where movies.id = r.movieId group by movies.title) as t, (SELECT movies.title, group_concat(a.name separator '-') as star
FROM movies, (select movieId, stars.name from stars_in_movies, stars where stars_in_movies.starId = stars.id) as a
where movies.id = a.movieId
group by movies.title) as h
where movies.id = r.movieId and movies.title = t.title and h.title = t.title
order by r.rating desc;