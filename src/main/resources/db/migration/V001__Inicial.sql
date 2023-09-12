create sequence remedios_seq start with 1 increment by 50;
create sequence sintomas_seq start with 1 increment by 50;

    create table remedios (
       id bigint not null,
        atualizado timestamp(6) not null,
        criado timestamp(6),
        descricao TEXT not null,
        nome varchar(255) not null,
        posologia TEXT not null,
        quantidade integer not null,
        usuario uuid not null,
        validade date not null,
        primary key (id)
    );

    create table remedios_sintomas (
       remedio_id bigint not null,
        sintoma_id bigint not null,
        primary key (remedio_id, sintoma_id)
    );

    create table sintomas (
       id bigint not null,
        atualizado timestamp(6) not null,
        criado timestamp(6),
        descricao varchar(255) not null,
        usuario uuid not null,
        primary key (id)
    );

    alter table if exists remedios_sintomas
       add constraint FKsskajf542kba5xxhgp5xdq105
       foreign key (sintoma_id)
       references sintomas;

    alter table if exists remedios_sintomas
       add constraint FKjfpobqc0n9whe10wymarkj2hp
       foreign key (remedio_id)
       references remedios;
