create table consumos (
    id bigserial not null,
    remedio_id bigint not null,
    criado timestamp without time zone,
    usuario uuid not null,
    primary key (id)
);

create table remedios (
    id bigserial not null,
    nome varchar(255) not null,
    descricao TEXT not null,
    posologia TEXT not null,
    contra_indicacao TEXT NOT NULL,
    quantidade integer not null,
    dose integer not null,
    validade date not null,
    estoque_baixo integer not null,
    criado timestamp without time zone,
    atualizado timestamp without time zone,
    usuario uuid not null,
    primary key (id)
);

create table remedios_sintomas (
    remedio_id bigint not null,
    sintoma_id bigint not null,
    primary key (remedio_id, sintoma_id)
);

create table sintomas (
    id bigserial not null,
    descricao varchar(255) not null,
    criado timestamp without time zone,
    atualizado timestamp without time zone,
    usuario uuid not null,
    primary key (id)
);

alter table if exists consumos
   add constraint FKgdfy64ghwgd764gfdgheu3tda
   foreign key (remedio_id)
   references remedios;

alter table if exists remedios_sintomas
   add constraint FKsskajf542kba5xxhgp5xdq105
   foreign key (sintoma_id)
   references sintomas;

alter table if exists remedios_sintomas
   add constraint FKjfpobqc0n9whe10wymarkj2hp
   foreign key (remedio_id)
   references remedios;
