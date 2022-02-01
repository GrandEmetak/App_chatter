/*Схема таблиц для авторизации table person содержит пользователей, table role - это роли.*/
CREATE TABLE role
(
    id        serial primary key,
    authority VARCHAR(50) NOT NULL unique
);

create table person
(
    id       serial primary key,
    enabled  boolean,
    password varchar(255),
    username varchar(255),
    role_id  int references role (id),
    room_id  int references room (id)
);
insert into role (authority)
values ('ROLE_USER');
insert into role (authority)
values ('ROLE_ADMIN');

insert into person(enabled, password, username, role_id, room_id)
VALUES (true, '$2a$10$1LvfibSCmvbDVRB/zNJfteJzSbkA4TJqUNKqk4hrNewdvAq4QC5Di', 'Petr Arsentev', 1, 1);
insert into person(enabled, password, username, role_id, room_id)
VALUES (true, '$2a$10$d8caLcgVFCa9itdmDrv4ueA41QCFwbHHd9CSHFyApwsq7JrmaQbje', 'Ivan Sobolev', 1, 1);
insert into person(enabled, password, username, role_id, room_id)
VALUES (true, '$2a$10$5bBRrJJO6Go/yJxY7PeOMO3ETtAUCeTBxtZLOvyhOKs3AvZym/.Ei', 'Nikolay Vodin', 1, 3);
insert into person(enabled, password, username, role_id, room_id)
VALUES (true, '$2a$10$lHTUyc1gGu2T4.sHgldw5e8ywUXoRjsnazKbzc9SuicaugoNkaOQC', 'Svetlana Donovan', 1, 3);
insert into person(enabled, password, username, role_id, room_id)
VALUES (true, '$2a$10$9hxGrSo10ruUgT4PuoCAtOZcoYzyCMhzp9/v/4TFoCGCfsUT1E0qu', 'Sergei Shirokov', 1, 3);

