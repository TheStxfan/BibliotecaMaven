create schema biblioteca_scolastica;
use biblioteca_scolastica;

drop table Utenti;
drop table Studenti;
drop table Docenti;
drop table Libri;
drop table Prestiti;

create table Utenti (
	id_utente			int auto_increment primary key,
    nome				varchar(50),
    cognome				varchar(50),
    email				varchar(100),
    tipo_utente			enum('STUDENTE', 'DOCENTE')
);

create table Studenti (
	id_utente			int,
    classe				varchar(10),
	primary key (id_utente),
    foreign key (id_utente) references Utenti(id_utente)
		on delete restrict
);

create table Docenti (
	id_utente			int,
    materia				varchar(50),
	primary key (id_utente),
    foreign key (id_utente) references Utenti(id_utente)
		on delete restrict
);

create table Libri (
	id_libro			int auto_increment primary key,
    titolo				varchar(100),
    autore				varchar(100),
    anno_pubblicazione	int,
    disponibile			bool default true
);

create table Prestiti (
	id_prestito			int auto_increment primary key,
    id_utente			int,
    id_libro			int,
    data_prestito		date,
    data_restituzione	date,
    stato				enum('ATTIVO', 'RESTITUITO'),
    foreign key (id_utente) references Utenti(id_utente)
		on delete restrict,
	foreign key (id_libro) references Libri(id_libro)
		on delete restrict
	);