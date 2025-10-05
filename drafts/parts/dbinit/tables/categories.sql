drop table categories;
create table categories (
    id integer not null primary key default NEXTVAL('categories_id_seq'),
    name varchar(50) not null unique
);
