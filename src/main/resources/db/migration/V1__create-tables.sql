CREATE TABLE plan (
	id serial PRIMARY KEY,
	plan_name VARCHAR (50),
	value money NOT NULL,
	active boolean default true
);

CREATE TABLE subscription (
	id serial PRIMARY KEY,
    id_customer integer,
    id_plan integer,
    renewal_days integer,
    active boolean,
    start_subscription date,
    end_subscription date,
    next_renewal_date date
);