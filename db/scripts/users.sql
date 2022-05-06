/*Схема таблицы users*/
create table if not exists users(
    id serial primary key,
    name varchar(200) not null constraint unique unique_name,
    password varchar(20) not null,
);