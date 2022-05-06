insert into items(name, description, created, user_id)
values ('Заявка1', 'Описание заявки 1 подробно', current_timestamp(0), 1);
insert into items(name, description, created, done, user_id)
values ('Заявка2', 'Описание заявки 2 подробно', current_timestamp(0) - interval '30 day', current_timestamp(0), 2);