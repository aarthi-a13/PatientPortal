create table insurance
(
    patient_pid         varchar(9)  not null
        constraint fk_insurance_patient
            references patients (pid)
            on delete cascade,
    policyholder_name   varchar(255),
    policy_number       varchar(50) not null
        primary key,
    insurance_provider  varchar(255),
    group_number        varchar(50),
    coverage_start_date date,
    coverage_end_date   date,
    insured_address     text,
    provider_contact    varchar(255),
    plan_type           varchar(50)
);

alter table insurance
    owner to tsdbadmin;

INSERT INTO public.insurance (patient_pid, policyholder_name, policy_number, insurance_provider, group_number, coverage_start_date, coverage_end_date, insured_address, provider_contact, plan_type) VALUES ('PID955530', 'Aarthi Alagarsamy', 'UHI93493GJN', 'United Health Insurance', '84NKD7', '2022-02-16', '2025-03-19', '240 Mercer St, New York, NY, 10012', 'United Healthcare, Attn: Customer Service, P.O. Box 20002, Nashville, TN 37202', 'Silver');
