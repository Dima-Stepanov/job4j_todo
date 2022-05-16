/*Схема таблицы users*/
create table if not exists users(
    id serial primary key,
    name varchar(200) not null constraint unique_users_name unique,
    password varchar(20) not null
); 