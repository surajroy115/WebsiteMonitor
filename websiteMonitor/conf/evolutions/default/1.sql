# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table content_requirement (
  id                        bigint not null,
  required_text             varchar(255),
  website_id                bigint,
  constraint pk_content_requirement primary key (id))
;

create table website (
  id                        bigint not null,
  url                       varchar(255),
  constraint pk_website primary key (id))
;

create sequence content_requirement_seq;

create sequence website_seq;

alter table content_requirement add constraint fk_content_requirement_website_1 foreign key (website_id) references website (id) on delete restrict on update restrict;
create index ix_content_requirement_website_1 on content_requirement (website_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists content_requirement;

drop table if exists website;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists content_requirement_seq;

drop sequence if exists website_seq;

