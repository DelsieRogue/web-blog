create table if not exists post
(
    id         bigserial primary key,
    title      varchar(255) not null,
    image_name varchar(1000),
    content    text not null,
    tags       varchar(1000),
    like_count bigint default 0,
    created_at timestamp default current_timestamp
);

create table if not exists comment
(
    id         bigserial primary key,
    content    text   not null,
    post_id    bigint not null,
    created_at timestamp default current_timestamp,
    foreign key (post_id) references post (id) on delete cascade
);