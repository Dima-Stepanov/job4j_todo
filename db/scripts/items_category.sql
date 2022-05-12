/*Undirect связь ManyToMany между сущностями Item и Category*/
create table if not exists items_category(
    items_id int not null references items(id),
    category_id int not null references category(id)
);