PGDMP  3    &                |            proje    14.13    16.4 U    V           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            W           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            X           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            Y           1262    26444    proje    DATABASE     z   CREATE DATABASE proje WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Turkish_T�rkiye.1254';
    DROP DATABASE proje;
                efe    false            Z           0    0    DATABASE proje    ACL     W   GRANT CONNECT ON DATABASE proje TO doctor;
GRANT CONNECT ON DATABASE proje TO patient;
                   efe    false    3417            [           0    0    proje    DATABASE PROPERTIES     ,   ALTER DATABASE proje CONNECTION LIMIT = 10;
                     efe    false                        2615    2200    public    SCHEMA     2   -- *not* creating schema, since initdb creates it
 2   -- *not* dropping schema, since initdb creates it
                postgres    false            \           0    0    SCHEMA public    ACL     Q   REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;
                   postgres    false    4            �            1255    34631    check_email_format()    FUNCTION     �  CREATE FUNCTION public.check_email_format() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- E-posta adresinin @ işareti içerip içermediğini ve .com ile bitip bitmediğini kontrol et
    IF NEW.email NOT LIKE '%@%.com' THEN
        RAISE EXCEPTION 'Email must contain "@" and end with ".com"';
    END IF;
    
    -- Yeni e-posta adresini geçerli kabul et
    RETURN NEW;
END;
$$;
 +   DROP FUNCTION public.check_email_format();
       public          postgres    false    4            �            1255    34633    check_max_appointments()    FUNCTION     �  CREATE FUNCTION public.check_max_appointments() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF (SELECT COUNT(*) 
        FROM public.appointments 
        WHERE status = 'planned' 
          AND patient_id = NEW.patient_id) >= 10 THEN
        RAISE EXCEPTION '--- Bir hasta toplamda en fazla 10 randevu alabilir! ---';
    END IF;
	
    IF (SELECT COUNT(*) 
        FROM public.appointments 
        WHERE status = 'planned' 
          AND patient_id = NEW.patient_id 
          AND doctor_id = NEW.doctor_id) >= 1 THEN
        RAISE EXCEPTION '--- Bir hasta aynı doktor için yalnızca 1 randevu alabilir! ---';
    END IF;

    RETURN NEW;
END;
$$;
 /   DROP FUNCTION public.check_max_appointments();
       public          postgres    false    4            �            1255    34629    format_phone_number()    FUNCTION     s  CREATE FUNCTION public.format_phone_number() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
   IF NEW.phone IS NOT NULL THEN
    -- Telefon numarasının başındaki 0'ı sil
    NEW.phone := CASE
                    WHEN NEW.phone LIKE '0%' THEN SUBSTRING(NEW.phone FROM 2)
                    ELSE NEW.phone
                 END;

    -- Telefon numarasının 10 karakter uzunluğunda olup olmadığını kontrol et
    IF LENGTH(NEW.phone) != 10 THEN
        RAISE EXCEPTION 'Phone number must be exactly 10 characters';
    END IF;
END IF;

    -- Yeni telefon numarasını geçerli kabul et
    RETURN NEW;
END;
$$;
 ,   DROP FUNCTION public.format_phone_number();
       public          postgres    false    4            �            1255    34628 1   get_doctor_available_times(integer[], date, date)    FUNCTION     b  CREATE FUNCTION public.get_doctor_available_times(doctor_ids integer[], start_date date DEFAULT (CURRENT_DATE + '1 day'::interval), end_date date DEFAULT (CURRENT_DATE + '8 days'::interval)) RETURNS TABLE(doctor_id integer, available_time timestamp without time zone, doctor_name text)
    LANGUAGE plpgsql
    AS $$
DECLARE
    temp_doctor_id integer;
    temp_available_time timestamp;
    temp_date date;
BEGIN
    -- Eğer start_date yarından önceyse, yarına ayarla
    IF start_date < current_date + interval '1 day' THEN
        start_date := current_date + interval '1 day';
    END IF;

    -- Her doktor için döngü
    FOR temp_doctor_id IN SELECT unnest(doctor_ids)
    LOOP
        -- Her gün için döngü
        FOR temp_date IN
            SELECT generate_series(start_date, end_date, '1 day'::interval)::date
        LOOP
            FOR temp_available_time IN
                SELECT generate_series(
                    temp_date + interval '9 hours',  -- O günün 09:00'ı
                    temp_date + interval '15 hours', -- O günün 15:00'ı
                    '15 minutes'::interval           
                ) AS available_times
            LOOP
                -- Randevu olan saatleri eleyelim
                IF NOT EXISTS (
                    SELECT 1
                    FROM appointments a
                    WHERE a.doctor_id = temp_doctor_id
                    AND a.appointment_date = temp_date 
					AND a.status = 'planned' 
                    AND a.appointment_time = (temp_available_time)::time  
                ) THEN
                    -- users tablosundan doktor adı alınıyor
                    SELECT u.name
                    INTO doctor_name
                    FROM doctors d
                    JOIN users u ON d.user_id = u.user_id
                    WHERE d.doctor_id = temp_doctor_id;

                    -- Müsait zamanı döndür
                    doctor_id := temp_doctor_id;
                    available_time := temp_available_time;
                    RETURN NEXT;
                END IF;
            END LOOP;
        END LOOP;
    END LOOP;
    RETURN;
END;
$$;
 g   DROP FUNCTION public.get_doctor_available_times(doctor_ids integer[], start_date date, end_date date);
       public          postgres    false    4            �            1255    34635 !   update_past_appointments_status() 	   PROCEDURE     �   CREATE PROCEDURE public.update_past_appointments_status()
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE public.appointments
    SET status = 'completed'
    WHERE status = 'planned'
      AND appointment_date < CURRENT_DATE;
END;
$$;
 9   DROP PROCEDURE public.update_past_appointments_status();
       public          postgres    false    4            �            1259    34604    analysis    TABLE     �   CREATE TABLE public.analysis (
    analysis_id integer NOT NULL,
    doctor_id integer NOT NULL,
    patient_id integer NOT NULL,
    name character varying(50) NOT NULL,
    date date DEFAULT CURRENT_DATE,
    result text
);
    DROP TABLE public.analysis;
       public         heap    postgres    false    4            ]           0    0    TABLE analysis    ACL     ]   GRANT ALL ON TABLE public.analysis TO doctor;
GRANT ALL ON TABLE public.analysis TO patient;
          public          postgres    false    224            �            1259    34603    analysis_analysis_id_seq    SEQUENCE     �   CREATE SEQUENCE public.analysis_analysis_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.analysis_analysis_id_seq;
       public          postgres    false    4    224            ^           0    0    analysis_analysis_id_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.analysis_analysis_id_seq OWNED BY public.analysis.analysis_id;
          public          postgres    false    223            �            1259    26488    appointments    TABLE     +  CREATE TABLE public.appointments (
    appointment_id integer NOT NULL,
    doctor_id integer NOT NULL,
    patient_id integer NOT NULL,
    appointment_date date NOT NULL,
    appointment_time time without time zone NOT NULL,
    status character varying(20) DEFAULT 'planned'::character varying,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT appointments_status_check CHECK (((status)::text = ANY ((ARRAY['planned'::character varying, 'completed'::character varying, 'cancelled'::character varying])::text[])))
);
     DROP TABLE public.appointments;
       public         heap    postgres    false    4            _           0    0    TABLE appointments    ACL     e   GRANT ALL ON TABLE public.appointments TO doctor;
GRANT ALL ON TABLE public.appointments TO patient;
          public          postgres    false    216            �            1259    26487    appointments_appointment_id_seq    SEQUENCE     �   CREATE SEQUENCE public.appointments_appointment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.appointments_appointment_id_seq;
       public          postgres    false    4    216            `           0    0    appointments_appointment_id_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.appointments_appointment_id_seq OWNED BY public.appointments.appointment_id;
          public          postgres    false    215            �            1259    26457    doctors    TABLE     �   CREATE TABLE public.doctors (
    doctor_id integer NOT NULL,
    user_id integer NOT NULL,
    specialization character varying(100),
    phone character varying(15),
    gender character varying(10) DEFAULT 'Male'::character varying
);
    DROP TABLE public.doctors;
       public         heap    postgres    false    4            a           0    0    TABLE doctors    ACL     [   GRANT ALL ON TABLE public.doctors TO doctor;
GRANT ALL ON TABLE public.doctors TO patient;
          public          postgres    false    212            �            1259    26471    patients    TABLE       CREATE TABLE public.patients (
    patient_id integer NOT NULL,
    user_id integer NOT NULL,
    date_of_birth date,
    gender character varying(10),
    address text,
    phone character varying(15),
    bloodtype character varying(10),
    CONSTRAINT patients_bloodtype_check CHECK (((bloodtype)::text = ANY ((ARRAY['A-'::character varying, 'A+'::character varying, 'B-'::character varying, 'B+'::character varying, 'AB+'::character varying, 'AB-'::character varying, '0-'::character varying, '0+'::character varying])::text[])))
);
    DROP TABLE public.patients;
       public         heap    postgres    false    4            b           0    0    TABLE patients    ACL     ]   GRANT ALL ON TABLE public.patients TO doctor;
GRANT ALL ON TABLE public.patients TO patient;
          public          postgres    false    214            �            1259    26446    users    TABLE       CREATE TABLE public.users (
    user_id integer NOT NULL,
    name character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    password character varying(255) NOT NULL,
    role character varying(20) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    tc_number character varying(11) NOT NULL,
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['doctor'::character varying, 'patient'::character varying, 'admin'::character varying])::text[])))
);
    DROP TABLE public.users;
       public         heap    postgres    false    4            c           0    0    TABLE users    ACL     W   GRANT ALL ON TABLE public.users TO doctor;
GRANT ALL ON TABLE public.users TO patient;
          public          postgres    false    210            �            1259    26593    doctorappointments    VIEW     �  CREATE VIEW public.doctorappointments AS
 SELECT u.name AS doctor_name,
    pu.name AS patient_name,
    a.appointment_date,
    a.appointment_time,
    a.status,
    d.doctor_id,
    a.appointment_id
   FROM ((((public.doctors d
     JOIN public.appointments a ON ((d.doctor_id = a.doctor_id)))
     JOIN public.users u ON ((d.user_id = u.user_id)))
     JOIN public.patients p ON ((a.patient_id = p.patient_id)))
     JOIN public.users pu ON ((p.user_id = pu.user_id)));
 %   DROP VIEW public.doctorappointments;
       public          postgres    false    216    216    216    216    216    216    214    214    212    212    210    210    4            d           0    0    TABLE doctorappointments    ACL     �   GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.doctorappointments TO doctor;
GRANT SELECT,INSERT,UPDATE ON TABLE public.doctorappointments TO patient;
          public          postgres    false    219            �            1259    26585    doctorprofile    VIEW       CREATE VIEW public.doctorprofile AS
 SELECT u.name,
    u.email,
    u.password,
    d.specialization,
    d.phone,
    u.created_at,
    u.user_id,
    u.tc_number,
    d.gender
   FROM (public.doctors d
     JOIN public.users u ON ((d.user_id = u.user_id)));
     DROP VIEW public.doctorprofile;
       public          postgres    false    210    212    212    212    212    210    210    210    210    210    4            e           0    0    TABLE doctorprofile    ACL     �   GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.doctorprofile TO doctor;
GRANT SELECT,INSERT,UPDATE ON TABLE public.doctorprofile TO patient;
          public          postgres    false    217            �            1259    26456    doctors_doctor_id_seq    SEQUENCE     �   CREATE SEQUENCE public.doctors_doctor_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.doctors_doctor_id_seq;
       public          postgres    false    212    4            f           0    0    doctors_doctor_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.doctors_doctor_id_seq OWNED BY public.doctors.doctor_id;
          public          postgres    false    211            �            1259    26598    patientappointments    VIEW     �  CREATE VIEW public.patientappointments AS
 SELECT u.name AS doctor_name,
    pu.name AS patient_name,
    a.appointment_date,
    a.appointment_time,
    a.status,
    d.specialization,
    p.patient_id,
    a.appointment_id
   FROM ((((public.doctors d
     JOIN public.appointments a ON ((d.doctor_id = a.doctor_id)))
     JOIN public.users u ON ((d.user_id = u.user_id)))
     JOIN public.patients p ON ((a.patient_id = p.patient_id)))
     JOIN public.users pu ON ((p.user_id = pu.user_id)));
 &   DROP VIEW public.patientappointments;
       public          postgres    false    216    210    210    212    212    212    214    214    216    216    216    216    216    4            g           0    0    TABLE patientappointments    ACL     �   GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.patientappointments TO doctor;
GRANT SELECT,INSERT,UPDATE ON TABLE public.patientappointments TO patient;
          public          postgres    false    220            �            1259    26589    patientprofile    VIEW       CREATE VIEW public.patientprofile AS
 SELECT u.name,
    u.email,
    u.password,
    p.phone,
    p.gender,
    p.bloodtype,
    p.address,
    p.date_of_birth,
    p.user_id,
    u.tc_number
   FROM (public.patients p
     JOIN public.users u ON ((p.user_id = u.user_id)));
 !   DROP VIEW public.patientprofile;
       public          postgres    false    214    210    210    210    210    210    214    214    214    214    214    4            h           0    0    TABLE patientprofile    ACL     �   GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.patientprofile TO doctor;
GRANT SELECT,INSERT,UPDATE ON TABLE public.patientprofile TO patient;
          public          postgres    false    218            �            1259    26470    patients_patient_id_seq    SEQUENCE     �   CREATE SEQUENCE public.patients_patient_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.patients_patient_id_seq;
       public          postgres    false    214    4            i           0    0    patients_patient_id_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.patients_patient_id_seq OWNED BY public.patients.patient_id;
          public          postgres    false    213            �            1259    34555    prescriptions    TABLE       CREATE TABLE public.prescriptions (
    prescription_id integer NOT NULL,
    doctor_id integer NOT NULL,
    patient_id integer NOT NULL,
    medication_name text NOT NULL,
    dosage text NOT NULL,
    prescribed_date date DEFAULT CURRENT_DATE NOT NULL
);
 !   DROP TABLE public.prescriptions;
       public         heap    postgres    false    4            j           0    0    TABLE prescriptions    ACL     g   GRANT ALL ON TABLE public.prescriptions TO doctor;
GRANT ALL ON TABLE public.prescriptions TO patient;
          public          postgres    false    222            �            1259    34554 !   prescriptions_prescription_id_seq    SEQUENCE     �   CREATE SEQUENCE public.prescriptions_prescription_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 8   DROP SEQUENCE public.prescriptions_prescription_id_seq;
       public          postgres    false    4    222            k           0    0 !   prescriptions_prescription_id_seq    SEQUENCE OWNED BY     g   ALTER SEQUENCE public.prescriptions_prescription_id_seq OWNED BY public.prescriptions.prescription_id;
          public          postgres    false    221            �            1259    26445    users_user_id_seq    SEQUENCE     �   CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.users_user_id_seq;
       public          postgres    false    4    210            l           0    0    users_user_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;
          public          postgres    false    209            �           2604    34607    analysis analysis_id    DEFAULT     |   ALTER TABLE ONLY public.analysis ALTER COLUMN analysis_id SET DEFAULT nextval('public.analysis_analysis_id_seq'::regclass);
 C   ALTER TABLE public.analysis ALTER COLUMN analysis_id DROP DEFAULT;
       public          postgres    false    223    224    224            �           2604    26491    appointments appointment_id    DEFAULT     �   ALTER TABLE ONLY public.appointments ALTER COLUMN appointment_id SET DEFAULT nextval('public.appointments_appointment_id_seq'::regclass);
 J   ALTER TABLE public.appointments ALTER COLUMN appointment_id DROP DEFAULT;
       public          postgres    false    216    215    216            �           2604    26460    doctors doctor_id    DEFAULT     v   ALTER TABLE ONLY public.doctors ALTER COLUMN doctor_id SET DEFAULT nextval('public.doctors_doctor_id_seq'::regclass);
 @   ALTER TABLE public.doctors ALTER COLUMN doctor_id DROP DEFAULT;
       public          postgres    false    211    212    212            �           2604    26474    patients patient_id    DEFAULT     z   ALTER TABLE ONLY public.patients ALTER COLUMN patient_id SET DEFAULT nextval('public.patients_patient_id_seq'::regclass);
 B   ALTER TABLE public.patients ALTER COLUMN patient_id DROP DEFAULT;
       public          postgres    false    214    213    214            �           2604    34558    prescriptions prescription_id    DEFAULT     �   ALTER TABLE ONLY public.prescriptions ALTER COLUMN prescription_id SET DEFAULT nextval('public.prescriptions_prescription_id_seq'::regclass);
 L   ALTER TABLE public.prescriptions ALTER COLUMN prescription_id DROP DEFAULT;
       public          postgres    false    222    221    222            �           2604    26449    users user_id    DEFAULT     n   ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);
 <   ALTER TABLE public.users ALTER COLUMN user_id DROP DEFAULT;
       public          postgres    false    209    210    210            S          0    34604    analysis 
   TABLE DATA           Z   COPY public.analysis (analysis_id, doctor_id, patient_id, name, date, result) FROM stdin;
    public          postgres    false    224   �|       O          0    26488    appointments 
   TABLE DATA           �   COPY public.appointments (appointment_id, doctor_id, patient_id, appointment_date, appointment_time, status, created_at) FROM stdin;
    public          postgres    false    216   �}       K          0    26457    doctors 
   TABLE DATA           T   COPY public.doctors (doctor_id, user_id, specialization, phone, gender) FROM stdin;
    public          postgres    false    212   ɀ       M          0    26471    patients 
   TABLE DATA           i   COPY public.patients (patient_id, user_id, date_of_birth, gender, address, phone, bloodtype) FROM stdin;
    public          postgres    false    214   W�       Q          0    34555    prescriptions 
   TABLE DATA           y   COPY public.prescriptions (prescription_id, doctor_id, patient_id, medication_name, dosage, prescribed_date) FROM stdin;
    public          postgres    false    222   �       I          0    26446    users 
   TABLE DATA           \   COPY public.users (user_id, name, email, password, role, created_at, tc_number) FROM stdin;
    public          postgres    false    210   k�       m           0    0    analysis_analysis_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.analysis_analysis_id_seq', 60, true);
          public          postgres    false    223            n           0    0    appointments_appointment_id_seq    SEQUENCE SET     O   SELECT pg_catalog.setval('public.appointments_appointment_id_seq', 278, true);
          public          postgres    false    215            o           0    0    doctors_doctor_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.doctors_doctor_id_seq', 6, true);
          public          postgres    false    211            p           0    0    patients_patient_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.patients_patient_id_seq', 7, true);
          public          postgres    false    213            q           0    0 !   prescriptions_prescription_id_seq    SEQUENCE SET     P   SELECT pg_catalog.setval('public.prescriptions_prescription_id_seq', 77, true);
          public          postgres    false    221            r           0    0    users_user_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.users_user_id_seq', 15, true);
          public          postgres    false    209            �           2606    34612    analysis analysis_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.analysis
    ADD CONSTRAINT analysis_pkey PRIMARY KEY (analysis_id);
 @   ALTER TABLE ONLY public.analysis DROP CONSTRAINT analysis_pkey;
       public            postgres    false    224            �           2606    26496    appointments appointments_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT appointments_pkey PRIMARY KEY (appointment_id);
 H   ALTER TABLE ONLY public.appointments DROP CONSTRAINT appointments_pkey;
       public            postgres    false    216            �           2606    26462    doctors doctors_pkey 
   CONSTRAINT     Y   ALTER TABLE ONLY public.doctors
    ADD CONSTRAINT doctors_pkey PRIMARY KEY (doctor_id);
 >   ALTER TABLE ONLY public.doctors DROP CONSTRAINT doctors_pkey;
       public            postgres    false    212            �           2606    26464    doctors doctors_user_id_key 
   CONSTRAINT     Y   ALTER TABLE ONLY public.doctors
    ADD CONSTRAINT doctors_user_id_key UNIQUE (user_id);
 E   ALTER TABLE ONLY public.doctors DROP CONSTRAINT doctors_user_id_key;
       public            postgres    false    212            �           2606    26479    patients patients_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.patients
    ADD CONSTRAINT patients_pkey PRIMARY KEY (patient_id);
 @   ALTER TABLE ONLY public.patients DROP CONSTRAINT patients_pkey;
       public            postgres    false    214            �           2606    26481    patients patients_user_id_key 
   CONSTRAINT     [   ALTER TABLE ONLY public.patients
    ADD CONSTRAINT patients_user_id_key UNIQUE (user_id);
 G   ALTER TABLE ONLY public.patients DROP CONSTRAINT patients_user_id_key;
       public            postgres    false    214            �           2606    34563     prescriptions prescriptions_pkey 
   CONSTRAINT     k   ALTER TABLE ONLY public.prescriptions
    ADD CONSTRAINT prescriptions_pkey PRIMARY KEY (prescription_id);
 J   ALTER TABLE ONLY public.prescriptions DROP CONSTRAINT prescriptions_pkey;
       public            postgres    false    222            �           2606    26455    users users_email_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);
 ?   ALTER TABLE ONLY public.users DROP CONSTRAINT users_email_key;
       public            postgres    false    210            �           2606    26453    users users_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    210            �           2606    34580    users users_tc_number_unique 
   CONSTRAINT     \   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_tc_number_unique UNIQUE (tc_number);
 F   ALTER TABLE ONLY public.users DROP CONSTRAINT users_tc_number_unique;
       public            postgres    false    210            �           2620    34634 +   appointments check_patient_max_appointments    TRIGGER     �   CREATE TRIGGER check_patient_max_appointments BEFORE INSERT ON public.appointments FOR EACH ROW EXECUTE FUNCTION public.check_max_appointments();
 D   DROP TRIGGER check_patient_max_appointments ON public.appointments;
       public          postgres    false    239    216            �           2620    34639 #   doctors format_phone_number_trigger    TRIGGER     �   CREATE TRIGGER format_phone_number_trigger BEFORE INSERT OR UPDATE ON public.doctors FOR EACH ROW EXECUTE FUNCTION public.format_phone_number();
 <   DROP TRIGGER format_phone_number_trigger ON public.doctors;
       public          postgres    false    212    227            �           2620    34640 $   patients format_phone_number_trigger    TRIGGER     �   CREATE TRIGGER format_phone_number_trigger BEFORE INSERT OR UPDATE ON public.patients FOR EACH ROW EXECUTE FUNCTION public.format_phone_number();
 =   DROP TRIGGER format_phone_number_trigger ON public.patients;
       public          postgres    false    227    214            �           2620    34632    users validate_email_format    TRIGGER     �   CREATE TRIGGER validate_email_format BEFORE INSERT OR UPDATE ON public.users FOR EACH ROW EXECUTE FUNCTION public.check_email_format();
 4   DROP TRIGGER validate_email_format ON public.users;
       public          postgres    false    210    225            �           2606    26497 (   appointments appointments_doctor_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT appointments_doctor_id_fkey FOREIGN KEY (doctor_id) REFERENCES public.doctors(doctor_id) ON DELETE CASCADE;
 R   ALTER TABLE ONLY public.appointments DROP CONSTRAINT appointments_doctor_id_fkey;
       public          postgres    false    216    3232    212            �           2606    26502 )   appointments appointments_patient_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT appointments_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patients(patient_id) ON DELETE CASCADE;
 S   ALTER TABLE ONLY public.appointments DROP CONSTRAINT appointments_patient_id_fkey;
       public          postgres    false    214    3236    216            �           2606    34613    analysis doctor_id    FK CONSTRAINT     |   ALTER TABLE ONLY public.analysis
    ADD CONSTRAINT doctor_id FOREIGN KEY (doctor_id) REFERENCES public.doctors(doctor_id);
 <   ALTER TABLE ONLY public.analysis DROP CONSTRAINT doctor_id;
       public          postgres    false    3232    212    224            �           2606    26465    doctors doctors_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.doctors
    ADD CONSTRAINT doctors_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;
 F   ALTER TABLE ONLY public.doctors DROP CONSTRAINT doctors_user_id_fkey;
       public          postgres    false    212    3228    210            �           2606    34618    analysis patient_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.analysis
    ADD CONSTRAINT patient_id FOREIGN KEY (patient_id) REFERENCES public.patients(patient_id);
 =   ALTER TABLE ONLY public.analysis DROP CONSTRAINT patient_id;
       public          postgres    false    3236    214    224            �           2606    26482    patients patients_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.patients
    ADD CONSTRAINT patients_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;
 H   ALTER TABLE ONLY public.patients DROP CONSTRAINT patients_user_id_fkey;
       public          postgres    false    214    3228    210            �           2606    34564 *   prescriptions prescriptions_doctor_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.prescriptions
    ADD CONSTRAINT prescriptions_doctor_id_fkey FOREIGN KEY (doctor_id) REFERENCES public.doctors(doctor_id) ON DELETE CASCADE;
 T   ALTER TABLE ONLY public.prescriptions DROP CONSTRAINT prescriptions_doctor_id_fkey;
       public          postgres    false    3232    222    212            �           2606    34569 +   prescriptions prescriptions_patient_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.prescriptions
    ADD CONSTRAINT prescriptions_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patients(patient_id) ON DELETE CASCADE;
 U   ALTER TABLE ONLY public.prescriptions DROP CONSTRAINT prescriptions_patient_id_fkey;
       public          postgres    false    214    222    3236            S   =  x���=n�@���Sp���]��YJ+R�4[ 0H`Y�ER&e.A�|��a��y�>��E��B)AKmS�S����E-�'���h���6��U���p���1U*a2�=ܿC�M�ppN��c[��е��
�S�$ὼ�I�[���Я`��8��a)=J�T�|�N�1)��Sھ�~u��kElƱHdD��|yvs(%�_J�6#86�Ҍ�F�Rf�GIdt)e�\T��;IF�ٟ��pz��P�S��L��">�ɮ��}t��¥��"ڭ"3�����5�oEd�%�$4�Ep>�~�!� �P      O   �  x��VK��0\�)r(I���,o�*�]&�s�c���d�؉��j}L�4����4!��`����s����:�p�d�o$�v��6ۆ���z��5��A�6�x�7�����P8����b�l�DGG�������2>&E&���Xn�,VM�M!+)�	x�� �2�v�(h6�S�b���,���k�,֚��JYH�BGY��Ap�������QH��T�СlC�ӑ�V��4ؓ��iR��B��?�Ʊ��*a���r��p� ~2k6@����\�)]��IC��'���m�b��7#I7X����gJ���T���G����G{>�f�(؅�l�x$<����-�8�H
�7T`[�Iv���
pT��iI�1k9�i3]�H�DQ��䀬8�;����dP����2
�{�3���,��m�n��w�&��ѹ��<XU��i��[�%<![q�ڻc����L	�E�S�\�C�^�ҁ�����p���!�%�<��W�^�2��i��g+q�j�W�����DR���m�C�S�V�;^�6�^�������շ�t}k�� ��/��������f3���)�X����Իj-pw5W�j��4����*���YF��&�c����{���5o�7�fL��j?���f�\g��<?˪��AV�Ϸ��,ӷ�k��6�_	      K   ~   x�3�4��N,Jɬ������4564426153�t-�N��2�4�HM�L,)�B$-��R�l��2��tI���ɬLI�%-`�f����E%�@#@�iC��	����mEˍ����jy� �^-)      M   �   x�Eα�0����*�Н'v�HT�@c)(���2�P���^5���euاayMH�ʏ5��~CjaD��c��x�<�S���}��!L-�)�`X��/���V�lK�C�Zx������5��K���Bu�	��ߎ)Wڶ�:qC�-�e2�      Q   S  x�u�AN�0E�3��A��'�R���X 6�5(��(j�©8㻋$USy�?���-�)��]��MO�Cθ"�.3��A��>�qG��Yr��ñ9tT�X�"�㬛�!��l�EA���2�h�&�,�����a����Y�v��Q�YJ|�z�yu�R	=���L�L�b�(SZ�~�q`kY�*�4�[�_e��5�+��{؇�YaΚ�Ц��]laь�BɉM=�1�cX��`&�������t3U��ޅ6nI&R�7����
{����O�j.�x�)Ջ\�J����\@+�i��I�6��ViU�6"9���4W��疽?-<��,(^n��"@��      I   J  x���1N�0���S��b�i�NPPņby4.��8U�"�pFz��l���.�BU���e�!�5�]U��={�,��bӲ �g��"�%�Z�D d���H���C�8A?$�������0Y�gHS�}nߑ�˴��ql��K�WΨ%ݽ�\h�Y:7�	���'Y�taK��z�o��.�.�n_U��6�5��1�!u��������e��1+�uY��>����AC��F��0��=���]�Q�QH�y�jz���l }bN�������L���}k~��zs;IH�zF���<��ݼ�V�F��(+     