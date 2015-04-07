#!/bin/bash

echo "host all all 0.0.0.0/0 trust" >> /var/lib/postgresql/data/pg_hba.conf

echo "******CREATING eecs405 DATABASE******"
gosu postgres postgres --single <<- EOSQL
   CREATE DATABASE eecs405;
   CREATE USER eecs405;
   GRANT ALL PRIVILEGES ON DATABASE eecs405 to eecs405;
EOSQL
echo ""
echo "******eecs405 DATABASE CREATED******"

echo "******Creating imdbNames Table******"
gosu postgres postgres --single eecs405 <<- EOSQL
     CREATE TABLE imdbNames (\
            id BIGSERIAL NOT NULL PRIMARY KEY,\
            string VARCHAR(500)\
     );

     

EOSQL
echo ""
echo "********imdbNames Table Created********"

echo "**************SETTING PERMISSIONS**********"
gosu postgres postgres --single eecs405 <<- EOSQL

GRANT ALL on imdbNames to eecs405;
GRANT ALL on imdbNames_id_seq to eecs405;

EOSQL
echo ""
echo "***********FINISHED SETTING PERMISSIONS********"
