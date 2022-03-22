create table user (
    id identity NOT NULL constraint user_id_pk primary key,
    name varchar NOT NULL,
    created_date timestamp default now() not null
);
create table conversion_transaction (
    id bigint auto_increment NOT NULL constraint conversion_transaction_pk primary key,
    transaction_id uuid NOT NULL constraint transaction_id_uq unique,
    user_id number NOT NULL,
    destination_currency varchar NOT NULL,
    origin_currency varchar NOT NULL ,
    origin_value numeric NOT NULL,
    exchange_rate numeric not null,
    created_date timestamp with time zone default now() not null,
    FOREIGN key (user_id) REFERENCES user(id)
);

insert into user (name) values ('Rafael');
insert into user (name) values ('Renata');
insert into user (name) values ('João');
insert into user (name) values ('Maria');
insert into user (name) values ('Pedro');
