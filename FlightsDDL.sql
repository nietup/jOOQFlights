-------------------------------------------------------------------------------
CREATE DATABASE "Flights"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
-------------------------------------------------------------------------------
CREATE SEQUENCE public."City_city_id_seq";

ALTER SEQUENCE public."City_city_id_seq"
    OWNER TO postgres;
-------------------------------------------------------------------------------
CREATE SEQUENCE public."Passenger_passenger_id_seq";

ALTER SEQUENCE public."Passenger_passenger_id_seq"
    OWNER TO postgres;
-------------------------------------------------------------------------------
CREATE SEQUENCE public."Aircraft_aircraft_id_seq";

ALTER SEQUENCE public."Aircraft_aircraft_id_seq"
    OWNER TO postgres;
-------------------------------------------------------------------------------
CREATE TABLE public."City"
(
    city_id integer NOT NULL DEFAULT nextval('"City_city_id_seq"'::regclass),
    name character varying(200) COLLATE pg_catalog."default" NOT NULL,
    time_zone integer,
    CONSTRAINT "City_pkey" PRIMARY KEY (city_id),
    CONSTRAINT "City_name_key" UNIQUE (name)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."City"
    OWNER to postgres;
-------------------------------------------------------------------------------
CREATE TABLE public."Aircraft"
(
    aircraft_id integer NOT NULL DEFAULT nextval('"Aircraft_aircraft_id_seq"'::regclass),
    model character varying(200) COLLATE pg_catalog."default",
    company character varying(200) COLLATE pg_catalog."default",
    seats integer NOT NULL,
    CONSTRAINT "Aircraft_pkey" PRIMARY KEY (aircraft_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."Aircraft"
    OWNER to postgres;
-------------------------------------------------------------------------------
CREATE TABLE public."Airport"
(
    iata character varying(3) COLLATE pg_catalog."default" NOT NULL,
    city_id integer NOT NULL,
    name character varying(200) COLLATE pg_catalog."default",
    CONSTRAINT "Airport_pkey" PRIMARY KEY (iata),
    CONSTRAINT "Airport_city_id_fkey" FOREIGN KEY (city_id)
        REFERENCES public."City" (city_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."Airport"
    OWNER to postgres;
-------------------------------------------------------------------------------
CREATE TABLE public."Flight"
(
    flight_no character varying(10) COLLATE pg_catalog."default" NOT NULL,
    source_iata character varying(3) COLLATE pg_catalog."default" NOT NULL,
    destination_iata character varying(3) COLLATE pg_catalog."default" NOT NULL,
    aircraft_id integer NOT NULL,
    start_time timestamp with time zone NOT NULL,
    landing_time timestamp with time zone NOT NULL,
    CONSTRAINT "Flight_pkey" PRIMARY KEY (flight_no),
    CONSTRAINT "Flight_aircraft_id_fkey" FOREIGN KEY (aircraft_id)
        REFERENCES public."Aircraft" (aircraft_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT "Flight_destination_iata_fkey" FOREIGN KEY (destination_iata)
        REFERENCES public."Airport" (iata) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT "Flight_source_iata_fkey" FOREIGN KEY (source_iata)
        REFERENCES public."Airport" (iata) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."Flight"
    OWNER to postgres;
-------------------------------------------------------------------------------
CREATE TABLE public."Passenger"
(
    passenger_id integer NOT NULL DEFAULT nextval('"Passenger_passenger_id_seq"'::regclass),
    flight_no character varying(10) COLLATE pg_catalog."default" NOT NULL,
    first_name character varying(200) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(200) COLLATE pg_catalog."default" NOT NULL,
    passport_no character varying(20) COLLATE pg_catalog."default" NOT NULL,
    street character varying(200) COLLATE pg_catalog."default",
    city character varying(200) COLLATE pg_catalog."default",
    country_code character varying(3) COLLATE pg_catalog."default",
    phone character varying(20) COLLATE pg_catalog."default",
    sub character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Passenger_pkey" PRIMARY KEY (passenger_id),
    CONSTRAINT "Passenger_passport_no_key" UNIQUE (passport_no)
,
    CONSTRAINT "Passenger_flight_no_fkey" FOREIGN KEY (flight_no)
        REFERENCES public."Flight" (flight_no) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."Passenger"
    OWNER to postgres;
-------------------------------------------------------------------------------
insert into 
	public."City"
	(city_id, name, time_zone) 
values
	(1, 'Warsaw', 2),
	(2, 'Vatican', 0),
	(3, 'NYC', -7),
	(4, 'Beijing', 8),
	(5, 'Dubai', 3),
	(6, 'Helsinki', 2),
	(7, 'Tunis', 1),
	(8, 'London', 0);
-------------------------------------------------------------------------------
insert into
	public."Airport"
	(iata, city_id, name)
values
	('WAW', 1, 'Chopin International'),
	('WMI', 1, 'Warsaw Modlin'),
	('JFK', 3, 'JFK International'),
	('PEK', 4, 'Beijing Capital International'),
	('DXB', 5, 'Dubai International'),
	('HEL', 6, 'Helsinki-Vantaa'),
	('TUN', 7, 'Tunis-Carthage'),
	('LHR', 8, 'London Heathrow');
-------------------------------------------------------------------------------
insert into 
	public."Aircraft"
	(aircraft_id, model, company, seats)
values
	(1, 'Boeing 737 max-8', 'Finnair', 2),
	(2, 'Airbus', 'Lufthansa', 3),
	(3, 'F-16', 'LOT', 3),
	(4, 'Concord', 'WizzAir', 2),
	(5, 'Air Force One', 'USA', 1);
-------------------------------------------------------------------------------
insert into
 	public."Flight"
 	(flight_no, source_iata, destination_iata, aircraft_id, start_time, landing_time)
values
	('PL1234', 'WAW', 'DXB', 3, '2019-09-19 15:00:00+02', '2019-09-19 18:00:00+03'),
	('US4312', 'JFK', 'PEK', 5, '2019-09-19 12:00:00-07', '2019-09-20 18:00:00+08'),
	('LF9997', 'HEL', 'TUN', 4, '2019-09-19 15:00:00+02', '2019-09-19 15:00:00+00'),
	('PL4321', 'WMI', 'LHR', 1, '2019-09-19 18:00:00+02', '2019-09-19 18:00:00+00');
-------------------------------------------------------------------------------
