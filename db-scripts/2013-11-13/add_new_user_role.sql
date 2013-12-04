 -- Add new user role for stat department.
USE COMMON;
INSERT INTO COMMON.ROLES (roleId, name, permissions, status) VALUES('STAT', 'Statistic Department', 0x2000000000010000, 1);