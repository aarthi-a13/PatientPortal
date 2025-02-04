create table patients
(
    id                serial
        primary key,
    ssn               varchar(11)
        unique,
    pid               varchar(9) not null
        unique,
    full_name         varchar(255),
    date_of_birth     date,
    gender            varchar(10),
    address           text,
    contact_number    varchar(255),
    emergency_contact varchar(255),
    medical_history   text,
    primary_physician varchar(255)
);
alter table patients
    owner to tsdbadmin;

INSERT INTO public.patients (id, ssn, pid, full_name, date_of_birth, gender, address, contact_number, emergency_contact, medical_history, primary_physician) VALUES (3, '5659', 'PID955530', 'Aarthi Alagarsamy', '1997-04-13', 'Female', '240 Mercer St, New York, NY, 10012', '631-565-1844', 'Steve Smith, 631-568-9752, steve@gmail.com', '', '');
