CREATE TABLE EMPLOYEE_PASSWORD_RESET
(
    ID          BIGSERIAL,
    EMPLOYEE_ID BIGINT      NOT NULL,
    CODE        TEXT        NOT NULL,
    STATUS      VARCHAR(50) NOT NULL,
    CREATED_AT  TIMESTAMP   NOT NULL,
    UPDATED_AT  TIMESTAMP   NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT FK_EMPLOYEE_PASSWORD_RESET_EMPLOYEE FOREIGN KEY (EMPLOYEE_ID) REFERENCES EMPLOYEE (ID)
);

CREATE INDEX UNIQUE_EMPLOYEE_PASSWORD_RESET_EMPLOYEE_ID ON EMPLOYEE_PASSWORD_RESET (EMPLOYEE_ID) WHERE STATUS = 'PENDING';

CREATE TABLE CUSTOMER_PASSWORD_RESET
(
    ID              BIGSERIAL,
    CUSTOMER_ID     BIGINT      NOT NULL,
    CODE            TEXT        NOT NULL,
    STATUS          VARCHAR(50) NOT NULL,
    CREATED_AT      TIMESTAMP   NOT NULL,
    UPDATED_AT      TIMESTAMP   NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT FK_CUSTOMER_PASSWORD_RESET_CUSTOMER FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER (ID)
);

CREATE INDEX UNIQUE_CUSTOMER_PASSWORD_RESET_CUSTOMER_ID ON CUSTOMER_PASSWORD_RESET (CUSTOMER_ID) WHERE STATUS = 'PENDING';