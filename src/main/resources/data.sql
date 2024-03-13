insert into users(username, password, enabled)
values -- password = 'pass'
    (
     'user1',
     '$2a$10$Q.mIu9QQP/mK1ik2SiNvv.dvmmvGsAIsgE17KXx6XKsuMawTlvRDK',
     true
    ),
    (
     'user21',
     '$2a$10$Q.mIu9QQP/mK1ik2SiNvv.dvmmvGsAIsgE17KXx6XKsuMawTlvRDK',
     true
    ),
    (
     'user22',
     '$2a$10$Q.mIu9QQP/mK1ik2SiNvv.dvmmvGsAIsgE17KXx6XKsuMawTlvRDK',
     true
    ),
    (
     'user31',
     '$2a$10$Q.mIu9QQP/mK1ik2SiNvv.dvmmvGsAIsgE17KXx6XKsuMawTlvRDK',
     true
    ),
    (
     'user32',
     '$2a$10$Q.mIu9QQP/mK1ik2SiNvv.dvmmvGsAIsgE17KXx6XKsuMawTlvRDK',
     true
    ),
    (
     'user41',
     '$2a$10$Q.mIu9QQP/mK1ik2SiNvv.dvmmvGsAIsgE17KXx6XKsuMawTlvRDK',
     true
    ),
    (
     'user42',
     '$2a$10$Q.mIu9QQP/mK1ik2SiNvv.dvmmvGsAIsgE17KXx6XKsuMawTlvRDK',
     true
    )
on conflict do nothing;

insert into authorities(username, authority)
values
    ('user1', 'ROLE_ADMIN'),
    ('user21', 'ROLE_POSTS_VIEWER'),
    ('user22', 'ROLE_POSTS_EDITOR'),
    ('user31', 'ROLE_USERS_VIEWER'),
    ('user32', 'ROLE_USERS_EDITOR'),
    ('user41', 'ROLE_ALBUMS_VIEWER'),
    ('user42', 'ROLE_ALBUMS_EDITOR')
on conflict do nothing;
