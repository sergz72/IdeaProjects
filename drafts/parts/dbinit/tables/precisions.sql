drop table precisions;
create table precisions (
    id integer not null primary key default NEXTVAL('precisions_id_seq'),
    value numeric not null unique
);
