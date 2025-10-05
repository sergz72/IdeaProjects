drop table parts;
create table parts (
    id integer not null primary key default NEXTVAL('parts_id_seq'),
    category_id integer not null references categories(id),
    size_id varchar(4) references sizes(id),
    unit_id varchar(4) references units(id),
    precision_id integer references precisions(id),
    name varchar(100) not null unique,
    value numeric,
    quantity smallint not null CHECK (quantity >= 0),
    quantity_in_use smallint not null default 0 CHECK (quantity_in_use >= 0 and quantity_in_use <= quantity),
    quantity_not_in_use smallint GENERATED ALWAYS AS (quantity - quantity_in_use),
    comment varchar(1000)
);
