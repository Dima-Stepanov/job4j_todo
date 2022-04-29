/*Схема таблицы ITEMS*/
create table if not exists items(
 item_id serial primary key,
 item_name varchar(200),
 description varchar(2000),
 created timestamp,
 done timestamp
)