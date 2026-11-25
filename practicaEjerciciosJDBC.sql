use guille;
create table Dep (
	NumDep smallint unsigned not null primary key,
    Nombre varchar(20) not null,
    Ala varchar(10) not null
);

create table Persona (
	DNI char(9)not null primary key,
    Nombre varchar(40)not null,
    Apellido varchar(60)not null,
    Edad tinyint unsigned not null,
    NumDep smallint unsigned not null,
    FOREIGN KEY (NumDep) REFERENCES Dep(NumDep)
);

