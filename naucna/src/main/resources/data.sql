delete from naucna_oblast;
insert into naucna_oblast (id, name) values (1, 'mat');
insert into naucna_oblast (id, name) values (2, 'fiz');
insert into naucna_oblast (id, name) values (3, 'biol');
delete from user; 
insert into user(id,ime,prezime,email,grad,drzava,titula,recenzent,username,lozinka,uloga,verifikovan) values (
155,'Admin','Adminic','upp.rudan@gmail.com','NS','SRB','dr',false,'admin','$2a$10$.xqfFJKIOGxEzUz0f2ZIYOx7roR8kv8647OcxhqWsVhs6PwbdK4Mm','admin',true);
insert into user(id,ime,prezime,email,grad,drzava,titula,recenzent,username,lozinka,uloga,verifikovan) values (
123,'Rec1','Recic','rec1@gmail.com','NS','SRB','dr',true,'rec1','$2a$10$.xqfFJKIOGxEzUz0f2ZIYOx7roR8kv8647OcxhqWsVhs6PwbdK4Mm','REC',true);

insert into user(id,ime,prezime,email,grad,drzava,titula,recenzent,username,lozinka,uloga,verifikovan) values (
125,'Rec2','Recic','rec2@gmail.com','NS','SRB','dr',true,'rec2','0e138a7406b6583942db7e3fea19a349e9ac607bf008a0cfdc30c5f675d56b5d','REC',true);

insert into user(id,ime,prezime,email,grad,drzava,titula,recenzent,username,lozinka,uloga,verifikovan) values (
1919,'Urednik1','Recic','rudananastasijaa@gmail.com','NS','SRB','dr',false,'miso','$2a$10$eBGUHIMgZK3ncFhw7CrSTuCnh.fHNMpVBmS.cL5hJU.7hvMfIIUBm','urednik',true);
insert into user(id,ime,prezime,email,grad,drzava,titula,recenzent,username,lozinka,uloga,verifikovan) values (
1278,'Urednik2','Recic','urednik2@gmail.com','NS','SRB','dr',false,'pera','$2a$10$eBGUHIMgZK3ncFhw7CrSTuCnh.fHNMpVBmS.cL5hJU.7hvMfIIUBm','urednik',true);


delete from role;
insert into role values (1, 'ROLE_USER');
insert into role values (2, 'ROLE_ADMIN');
insert into role values (3, 'ROLE_UREDNIK');
insert into role values (4, 'ROLE_AUTOR');
insert into role values (5, 'ROLE_RECENZENT');


delete from privilege;
insert into privilege values (1, 'POTVRDI_RECENZENT');
insert into privilege values (2, 'KREIRANJE_CASOPISA');

delete from roles_privileges;
insert into roles_privileges values (2, 1);
insert into roles_privileges values (3, 2);

delete from user_roles;
insert into user_roles values (155, 2);
insert into user_roles values (123, 5);

insert into user_roles values (125, 5);
insert into user_roles values (1919, 3);

insert into user_roles values (1278, 3);