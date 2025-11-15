alter table person
    add column name text not null default 'n/a';

insert into person(name)
values ('Bob');
