drop database if exists moviedb;
create database moviedb;
use moviedb;

create table movies(
	id varchar(10) NOT NULL DEFAULT "",
	title varchar(100) NOT NULL DEFAULT "NULL",
	year integer NOT NULL,
	director varchar(100) NOT NULL,
    primary key(id)
);

create table stars(
	id varchar(10) NOT NULL DEFAULT "",
	name varchar(100) NOT NULL, 
	birthYear integer, 
    primary key(id)
);

create table stars_in_movies(
	starId varchar(10) NOT NULL,  
	movieId varchar(10) NOT NULL, 
    foreign key (starId) references stars(id),
    foreign key (movieId) references movies(id)
);

create table genres(
	id integer NOT NULL AUTO_INCREMENT, 
	name varchar(32) NOT NULL,
    primary key (id)
);

create table genres_in_movies(
	genreId integer NOT NULL, 
	movieId varchar(10) NOT NULL,
    foreign key (genreId) references genres(id),
    foreign key (movieId) references movies(id)
);

create table creditcards(
	id varchar(20) NOT NULL,
	firstName varchar(50) NOT NULL,
	lastName varchar(50) NOT NULL,
	expiration date NOT NULL,
    primary key (id)
);

create table customers(
	id integer NOT NULL AUTO_INCREMENT,
	firstName varchar(50) NOT NULL, 
	lastName varchar(50) NOT NULL, 
	ccId varchar(20) NOT NULL,
	address varchar(200) NOT NULL, 
	email varchar(50) NOT NULL,
	password varchar(20) NOT NULL,
    primary key (id),
    foreign key (ccId) references creditcards(id)
);

create table sales(
	id integer NOT NULL AUTO_INCREMENT, 
	customerId integer NOT NULL, 
	movieId varchar(10) NOT NULL, 
	saleDate date NOT NULL,
    primary key (id),
    foreign key (customerId) references customers(id),
    foreign key (movieId) references movies(id)
);

create table ratings(
	movieId varchar(10) NOT NULL,
	rating float NOT NULL,
	numVotes integer NOT NULL,
    foreign key (movieId) references movies(id)
);

create table employees(
	email varchar(50) primary key,
	password varchar(20) not null,
	fullname varchar(100)
);

create table maxid(
	frontId varchar(2),
	endId varchar(10) NOT NULL DEFAULT "0000000",
    tablename varchar(20) NOT NULL,
	primary key(tablename)
);

INSERT INTO employees
VALUES ("classta@email.edu", "classta", "TA CS122B");

INSERT INTO `moviedb`.`maxid` (`frontId`, `endId`, `tablename`) VALUES ('nm', '0', 'stars');
INSERT INTO `moviedb`.`maxid` (`frontId`, `endId`, `tablename`) VALUES ('tt', '0', 'movies');
INSERT INTO `moviedb`.`maxid` (`endId`, `tablename`) VALUES ('0', 'genres');



