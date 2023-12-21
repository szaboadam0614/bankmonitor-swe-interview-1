#!/bin/bash

# Variables
H2_DATABASE_PATH="mem:myDb"
H2_JAR_URL="https://repo1.maven.org/maven2/com/h2database/h2/2.1.214/h2-2.1.214.jar"
POSTGRES_DB="posgres"
POSTGRES_USER="posgres"
POSTGRES_PASSWORD="posgres"
POSTGRES_HOST="localhost"
POSTGRES_PORT="5432"

export_h2_data() {
    echo "Exporting data from H2..."
    # It is just for testing, the jar should be already exist in the container.
    curl -o h2-2.1.214.jar "$H2_JAR_URL"
    # Add command to export data from H2
    java -cp h2-2.1.214.jar org.h2.tools.Script -url jdbc:h2:mem:myDb -user postgres -script backup.sql
}

import_data_to_postgres() {
    echo "Importing data to PostgreSQL..."
    # Add command to import data to PostgreSQL
    psql -h $POSTGRES_HOST -U $POSTGRES_USER -d $POSTGRES_DB -f backup.sql
}

# Main migration process
main() {
    export_h2_data
    import_data_to_postgres
    echo "Migration completed."
#    should cleanup
}

# Run the main function
main
