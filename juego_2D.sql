
create database if not exists juego_2D;
use juego_2D;

-- Crear la tabla de tipos de personajes
create table tipos_personaje(
    tipo_id int auto_increment primary key,
    categoria varchar(50) not null, 
    salto int not null,
    bonus int not null
);

-- Crear la tabla de personajes
create table personajes(
    id_personaje int auto_increment primary key,
    nombre varchar(100) not null,
    tipo_id int, 
    constraint fk_personaje_tipo_id foreign key (tipo_id) references tipos_personaje(tipo_id)
);

-- Crear la tabla de puntajes, sin la referencia a la tabla partidas
CREATE TABLE puntajes(
    id_puntaje INT AUTO_INCREMENT PRIMARY KEY, 
    id_personaje INT,  -- Columna para el id_personaje
    por_moneda INT NOT NULL,
    bonus_adicional INT NOT NULL, 
    estrella INT NOT NULL, 
    total INT NOT NULL,
    FOREIGN KEY (id_personaje) REFERENCES personajes(id_personaje) 
);


select * from puntajes;
select * from personajes;

INSERT INTO tipos_personaje (categoria, salto, bonus) VALUES
('Principiante', 10, 5),
('Escalador', 15, 10);









