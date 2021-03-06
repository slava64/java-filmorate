DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS films_genres;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS mpa;
DROP TABLE IF EXISTS users;

create table if not exists users (
    id int generated by default as identity primary key,
    email varchar(50),
    login varchar(200),
    name varchar(50),
    birthday timestamp
);

create table if not exists mpa (
    id int generated by default as identity primary key,
    name varchar(50)
);

create table if not exists genres (
    id int generated by default as identity primary key,
    name varchar(50)
);

create table if not exists films (
    id int generated by default as identity primary key,
    name varchar(50),
    description varchar(200),
    release_date timestamp,
    duration int,
    rate int,
    mpa_id int,
    CONSTRAINT fk_films_mpa
        FOREIGN KEY (mpa_id)
            REFERENCES mpa (id)
);

create table if not exists films_genres (
    film_id int REFERENCES films(id),
    genre_id int REFERENCES genres(id)
);

create table if not exists friends (
    user_id int REFERENCES users(id),
    friend_id int REFERENCES users(id)
);

create table if not exists likes (
    film_id int REFERENCES films(id),
    user_id int REFERENCES users(id)
);
