CREATE SCHEMA IF NOT EXISTS aero_database;

CREATE TABLE IF NOT EXISTS aero_database.form_ntu (
    id int generated by default as identity primary key,
    name varchar(255),
    file_name varchar(255)
);

CREATE TABLE IF NOT EXISTS aero_database.material_info (
    id int generated by default as identity primary key,
    name varchar(255) not null unique,
    density float not null
);

CREATE TABLE IF NOT EXISTS aero_database.characteristics_ntu (
    id int generated by default as identity primary key,
    name varchar(255) unique,
    form_id int references aero_database.form_ntu(id),
    radius float not null,
    length float,
    thickness float not null,
    material_id int references aero_database.material_info(id)
);

CREATE TABLE IF NOT EXISTS aero_database.cubesat_size (
    id int generated by default as identity primary key,
    name varchar(255) unique,
    length float,
    width float,
    height float,
    x_mass float,
    y_mass float,
    z_mass float,
    mass float
);

CREATE TABLE aero_database.aerodynamic_characteristics(
    id int generated by default as identity primary key,
    alfa float,
    cubesat_size_id int references aero_database.cubesat_size(id),
    size_ntu_id int references aero_database.characteristics_ntu(id),
    force_x float,
    moment_x float,
    coefficient_x float,
    force_y float,
    moment_y float,
    coefficient_y float,
    velocity_head float,
    date_of_calculation timestamp,
    density float,
    speed float,
    min_speed float
)




