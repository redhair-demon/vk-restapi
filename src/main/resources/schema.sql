create table if not exists
users (
    username varchar(50) not null,
    password varchar(100) not null,
    enabled boolean not null default true,
    primary key (username)
);

create table if not exists
authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    foreign key (username) references users(username)
);

create unique index if not exists
    ix_auth_username on authorities(username, authority);
