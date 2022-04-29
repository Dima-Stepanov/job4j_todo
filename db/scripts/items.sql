/*Схема таблицы ITEMS*/
create table if not exists items(
 item_id serial primary key,
 item_name varchar(200) not null constraint unique_item_name unique,
 description varchar(2000),
 created timestamp not null ,
 done timestamp default null
)