/*Схема таблицы ITEMS*/
create table if not exists items(
 id serial primary key,
 name varchar(200) not null,
 description varchar(2000),
 created timestamp not null ,
 done timestamp default null,
 user_id int not null references users(id)
);