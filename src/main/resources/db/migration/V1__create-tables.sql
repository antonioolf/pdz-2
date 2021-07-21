CREATE TABLE plan (
	id UUID PRIMARY KEY,
	plan_name VARCHAR (50),
	value money NOT NULL,
	active boolean default true
);

CREATE TABLE subscription (
	id UUID PRIMARY KEY,
    id_customer UUID,
    id_plan UUID,
    renewal_days integer,
    active boolean,
    start_subscription date,
    end_subscription date,
    next_renewal_date date
);