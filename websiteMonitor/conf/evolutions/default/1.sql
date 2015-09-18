# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table website (
  id                        bigint not null,
  url                       varchar(255),
  constraint pk_website primary key (id))
;

create sequence website_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists website;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists website_seq;

