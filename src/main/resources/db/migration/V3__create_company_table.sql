CREATE TABLE company(
    id UUID PRIMARY KEY,
    company_name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE ,
    password TEXT NOT NULL
);