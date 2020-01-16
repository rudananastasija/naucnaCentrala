delete from naucna_oblast;
insert into naucna_oblast (id, name) values (1, 'mat');
insert into naucna_oblast (id, name) values (2, 'fiz');
insert into naucna_oblast (id, name) values (3, 'biol');
delete from user;
insert into user(id,ime,prezime,email,grad,drzava,titula,recenzent,username,lozinka,uloga,verifikovan) values (
1,'Admin','Adminic','admin@gmail.com','NS','SRB','dr',false,'admin','0e138a7406b6583942db7e3fea19a349e9ac607bf008a0cfdc30c5f675d56b5d','admin',true);
insert into user(id,ime,prezime,email,grad,drzava,titula,recenzent,username,lozinka,uloga,verifikovan) values (
123,'Rec1','Recic','rec1@gmail.com','NS','SRB','dr',true,'rec1','0e138a7406b6583942db7e3fea19a349e9ac607bf008a0cfdc30c5f675d56b5d','REC',true);

insert into user(id,ime,prezime,email,grad,drzava,titula,recenzent,username,lozinka,uloga,verifikovan) values (
125,'Rec2','Recic','rec2@gmail.com','NS','SRB','dr',true,'rec2','0e138a7406b6583942db7e3fea19a349e9ac607bf008a0cfdc30c5f675d56b5d','REC',true);

insert into user(id,ime,prezime,email,grad,drzava,titula,recenzent,username,lozinka,uloga,verifikovan) values (
1919,'Urednik1','Recic','urednik1@gmail.com','NS','SRB','dr',false,'miso','0e138a7406b6583942db7e3fea19a349e9ac607bf008a0cfdc30c5f675d56b5d','urednik',true);
insert into user(id,ime,prezime,email,grad,drzava,titula,recenzent,username,lozinka,uloga,verifikovan) values (
1278,'Urednik2','Recic','urednik2@gmail.com','NS','SRB','dr',false,'pera','0e138a7406b6583942db7e3fea19a349e9ac607bf008a0cfdc30c5f675d56b5d','urednik',true);