/*Схема таблицы CATEGORY*/
create table if not exists category(
    id serial primary key,
    name varchar(100) unique not null
);