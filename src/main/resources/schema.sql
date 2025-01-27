create table if not exists post
(
    id         bigserial primary key,
    title      varchar(255) not null,
    image_name varchar(1000),
    content    text not null,
    tags       varchar(1000),
    like_count bigint default 0,
    created_at timestamp default current_timestamp,
    constraint post_unique_id unique (id)
);

create sequence if not exists custom_post_id_seq start 1000;
alter table post alter column id set default nextval('custom_post_id_seq');

create table if not exists comment
(
    id         bigserial primary key,
    content    text   not null,
    post_id    bigint not null,
    created_at timestamp default current_timestamp,
    foreign key (post_id) references post (id) on delete cascade
);